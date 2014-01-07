package com.adaptavant.useractivity.list;

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

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.adaptavant.timezone.services.MCacheService;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@SuppressWarnings("unchecked")
public class DataListProvider {
	Logger logger=Logger.getLogger("DataListProvider");
	JSONArray array=new JSONArray();
	
/*
 * This method will get country list from database and store it in memcache. 
 * 
 * It will be called only if it is not available in memcache.
 */
	public void getCountryList(int limit,String cursorString){
		String keyString="CountryList";
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();	
		Query query = pm.newQuery(TimezoneJDO.class);
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
				System.out.println(results);
				if (!results.isEmpty()) {
					Collection<String> country=new TreeSet<String>();
					for (TimezoneJDO tj: results) {
						country.add(tj.getCountry());
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
						Collection<String> data=(TreeSet<String>) MCacheService.get(keyString);
						array=new JSONArray();
						array.addAll(data);
						logger.log(Level.INFO,"Updating Memcache with country List");
						MCacheService.set("getCountryList", array);
						MCacheService.remove(keyString);
					} 
				}
			}
			finally {
			  query.closeAll();
			}
		}
	/*
	 * This method will return JSON Array of State and also store it in memcache after taking Country name from user.
	 * 
	 * It will be called only if state list is not available in memcache.
	 */
	
	public JSONArray getStateList(String country, int limit, String cursorString){
		String keyString="getStateList1"+country;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();	
		Query query = pm.newQuery(TimezoneJDO.class , "country == '"+country+"'");
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
		  System.out.println(query.execute());
		  if (!results.isEmpty()) {
			  Collection<String> state=new TreeSet<String>();
			  for (TimezoneJDO tj: results) {
					state.add(tj.getState());
				}
				if(MCacheService.get(keyString)!=null){
					state.addAll((TreeSet<String>) MCacheService.get(keyString));
				}
				System.out.println(state);
				MCacheService.set(keyString, state);
				if(results.size()==limit){
					Queue queue = QueueFactory.getQueue("subscription-queue");
					queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("cursorString", cursorString).param("country", country).param("list", "state"));
				}
				else {
					Collection<String> data=(TreeSet<String>) MCacheService.get(keyString);
					array=new JSONArray();
					array.addAll(data);
					logger.log(Level.INFO,"Updating Memcache with state List");
					MCacheService.set("getStateList"+country, array);
					MCacheService.remove(keyString);
				}
		  }	
		} 
		finally {
		  query.closeAll();
		}
		return array;		
	}
	
	/*
	 * This method will return JSON Array of city list after taking Country name and State name from user.
	 * 
	 * It will be called only if city list is not available in memcache.
	 */

	public JSONArray getCityList(String country, String state){
		String keyString="getCityList"+country+"and"+state;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();	
		if(MCacheService.get(keyString)!=null){
			System.out.println("inside memcache");
			return (JSONArray) MCacheService.get(keyString);
		}
		else{
		Query query = pm.newQuery(TimezoneJDO.class, "country == '"+country+"'&& state == '"+state+"'");
		query.setResult ("distinct city");
		 logger.info(query.toString());
		try {
		  Collection<?> results = (Collection<?>)query.execute (); 
		  logger.info(query.execute().toString());
		  if (!results.isEmpty()) {
			  Collection<String> city=new TreeSet<String>();
			  city.addAll((Collection<? extends String>) results);
			  array=new JSONArray();
			  array.addAll(city);
			  MCacheService.set(keyString, array);
			  return array;
			  
		  } else {
		    return null;
		  }
		} finally {
		  query.closeAll();
		}
		}
		
	}
}
