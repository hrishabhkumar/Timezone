package com.adaptavant.timezone.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.TimezoneJDO;
import com.adaptavant.utilities.MCacheService;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@SuppressWarnings("unchecked")
public class DataListProvider {
	Logger logger=Logger.getLogger("DataListProvider");
	/**
	 * 
	 * @param limit
	 * @param cursorString
	 * This method will get country list from database and store it in memcache.
	 * It will be called only if it is not available in memcache.
	 */
	public void getCountryList(int limit,String cursorString){
		String keyString="CountryList";
		PersistenceManager persistenceManager = PMF.getPMF().getPersistenceManager();	
		Query query = persistenceManager.newQuery(TimezoneJDO.class);
		query.setRange(0, limit);			
		logger.log(Level.INFO,query.toString());
		if(cursorString!=null){
			Cursor cursor=Cursor.fromWebSafeString(cursorString);
			Map<String,Object> extnMap=new HashMap<String, Object>();
			extnMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			query.setExtensions(extnMap);
		}
			try {
				List<TimezoneJDO> results = (List<TimezoneJDO>)query.execute ();
				Cursor cursor = JDOCursorHelper.getCursor(results);
				cursorString=cursor.toWebSafeString();
				if (!results.isEmpty()) {
					Collection<String> country=new TreeSet<String>();
					for (TimezoneJDO timeZone: results) {
						country.add(timeZone.getCountry()+","+timeZone.getCountryCode());
					}
					if(MCacheService.get(keyString)!=null){
					country.addAll((TreeSet<String>) MCacheService.get(keyString));
					}
					MCacheService.set(keyString, country);
					if(results.size()==limit){
						Queue queue = QueueFactory.getQueue("subscription-queue");
						queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("cursorString", cursorString).param("list", "country"));
					}
					else {
						Collection<String> countrySet=(TreeSet<String>) MCacheService.get(keyString);
						JSONArray countryJsonArray=new JSONArray();
						for(String countryNameandCode: countrySet){
							JSONObject countryData=new JSONObject();
							countryData.put("country", countryNameandCode.substring(0, countryNameandCode.indexOf(",")));
							countryData.put("countryCode", countryNameandCode.substring(countryNameandCode.indexOf(",")+1));
							countryData.put("label", countryNameandCode.substring(countryNameandCode.indexOf(",")+1)+" "+countryNameandCode.substring(0, countryNameandCode.indexOf(",")));
							countryJsonArray.add(countryData);
						}
						logger.log(Level.INFO,"Updating Memcache with country List");
						MCacheService.set("getCountryList", countryJsonArray);
						MCacheService.remove(keyString);
					} 
				}
			}
			finally {
			  query.closeAll();
			}
		}
	/**
	 * 
	 * @param country
	 * @param limit
	 * @param cursorString
	 * @return JSONArray of state list
	 * This method will return JSON Array of State and also store it in memcache after taking Country name from user.
	 * It will be called only if state list is not available in memcache.
	 */
	public JSONArray getStateList(String country, int limit, String cursorString)
	{
		JSONArray stateJsonArray=null;
		String keyString="getStateList1"+country;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();	
		Query query = pm.newQuery(TimezoneJDO.class , "country == '"+country+"'");
		query.setRange(0, limit);
		logger.log(Level.INFO,query.toString());
		if(cursorString!=null)
		{
			Cursor cursor=Cursor.fromWebSafeString(cursorString);
			Map<String,Object> extnMap=new HashMap<String, Object>();
			extnMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			query.setExtensions(extnMap);
		}
		try
		{
			List<TimezoneJDO> results = (List<TimezoneJDO>)query.execute ();
			Cursor cursor = JDOCursorHelper.getCursor(results);
			cursorString=cursor.toWebSafeString();
		  if (!results.isEmpty()) 
		  {
			  Collection<String> state=new TreeSet<String>();
			  for (TimezoneJDO timezoneJDO: results) 
			  {
				state.add(timezoneJDO.getState());
			  }
			  if(MCacheService.get(keyString)!=null)
			  {
				state.addAll((TreeSet<String>) MCacheService.get(keyString));
			  }
			  MCacheService.set(keyString, state);
			  if(results.size()==limit)
			  {
				getStateList(country, limit, cursor.toWebSafeString());
				
			  }
			  else{
				  Collection<String> stateSet=(TreeSet<String>) MCacheService.get(keyString);
				  stateJsonArray=new JSONArray();
				  stateJsonArray.addAll(stateSet);
				  logger.log(Level.INFO,"Updating Memcache with state List");
				  MCacheService.set("getStateList"+country, stateJsonArray);
				  MCacheService.remove(keyString);
			  }
			  return stateJsonArray;
			  
		  }
		  else
		  {
			  Collection<String> stateSet=(TreeSet<String>) MCacheService.get(keyString);
			  stateJsonArray=new JSONArray();
			  stateJsonArray.addAll(stateSet);
			  logger.log(Level.INFO,"Updating Memcache with state List");
			  MCacheService.set("getStateList"+country, stateJsonArray);
			  MCacheService.remove(keyString);
			  return stateJsonArray; 
		  }
		}
		catch(Exception e){
			logger.warning(e.getMessage());
			return stateJsonArray;
		}
		finally {
		  query.closeAll();
		}
	}
	/**
	 * 
	 * @param country
	 * @param state
	 * @return JSONArray of city list
	 *  This method will return JSON Array of city list after taking Country name and State name from user.
	 * It will be called only if city list is not available in memcache.
	 */
	public JSONArray getCityList(String country, String state)
	{
		JSONArray cityJsonArray=null;
		String keyString="getCityList"+country+"and"+state;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();	
		if(MCacheService.get(keyString)!=null)
		{
			logger.info("Method:getCityList=> inside memcache");
			return (JSONArray) MCacheService.get(keyString);
		}
		else
		{
		Query query = pm.newQuery(TimezoneJDO.class, "country == '"+country+"'&& state == '"+state+"'");
		query.setResult ("distinct city");
		 logger.info(query.toString());
		try 
		{
		  Collection<?> results = (Collection<?>)query.execute (); 
		  logger.info(query.execute().toString());
		  if (!results.isEmpty()) 
		  {
			  Collection<String> city=new TreeSet<String>();
			  city.addAll((Collection<? extends String>) results);
			  cityJsonArray=new JSONArray();
			  cityJsonArray.addAll(city);
			  MCacheService.set(keyString, cityJsonArray);
			  return cityJsonArray;
			  
		  } 
		  else 
		  {
		    return cityJsonArray;
		  }
		}
		finally 
		{
		  query.closeAll();
		}
		}
	}
}
