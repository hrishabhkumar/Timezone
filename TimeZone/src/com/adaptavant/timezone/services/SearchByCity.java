/**
 * 
 */
package com.adaptavant.timezone.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

/**
 * @author Hrishabh.Kumar
 *
 */
public class SearchByCity {
	Logger logger=Logger.getLogger(SearchByCity.class.getName());
	
	@SuppressWarnings("unchecked")
	public JSONArray getCityJson(int limit, String cursorString, String keyString) 
  	{
		JSONArray cityDataArray=null;
		JSONObject cityDataJson=null;
  		logger.info("inside getCityList "+keyString);
  		Query query=null;
  		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
  		logger.info("inside getCityList "+limit);
  			try
  			{
	  			query=pm.newQuery(TimezoneJDO.class);
	  			query.setRange(0, limit);
	  			logger.info(query.toString());
	  			if(cursorString!=null)
	  			{
	  				Cursor cursor=Cursor.fromWebSafeString(cursorString);
	  				Map<String,Object> extnMap=new HashMap<String, Object>();
	  				extnMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
	  				query.setExtensions(extnMap);
	  			}
	  			List<TimezoneJDO> results = (List<TimezoneJDO>)query.execute ();
	  			Cursor cursor = JDOCursorHelper.getCursor(results);
	  			cursorString=cursor.toWebSafeString();
	  			if (!results.isEmpty()) 
	  			{
	  				cityDataArray=new JSONArray();
	  				for (TimezoneJDO timeZoneJDO: results) 
	  				{
	  					cityDataJson=new JSONObject();
	  					cityDataJson.put("label", timeZoneJDO.getCity()+" "+timeZoneJDO.getState()+" "+timeZoneJDO.getCountry());
	  					cityDataJson.put("city", timeZoneJDO.getCity());
	  					cityDataJson.put("state", timeZoneJDO.getState());
	  					cityDataJson.put("country", timeZoneJDO.getCountry());
	  					cityDataJson.put("zipCode", timeZoneJDO.getZipCode());
	  					cityDataJson.put("countryCode", timeZoneJDO.getCountryCode());
	  					cityDataArray.add(cityDataJson);
	  				}
	  				
	  				if(MCacheService.containsKey(keyString))
	  				{
	  					JSONArray memcacheData= (JSONArray) MCacheService.get(keyString);
	  					if(memcacheData.size()<=5000)
	  					{
	  						cityDataArray.addAll(memcacheData);
	  					}
	  					else
	  					{
	  						int i=Integer.parseInt(keyString.substring(keyString.length()-1));
	  						i++;
	  						keyString=keyString.substring(0,keyString.length()-1)+i;
	  					}
	  					
	  				}
	  				MCacheService.set(keyString, cityDataArray);
	  				if(results.size()==limit)
	  				{
	  					Queue queue = QueueFactory.getQueue("subscription-queue");
	  					queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("cursorString", cursorString).param("list", "cityData").param("keyString", keyString));
	  					
	  				}
	  				System.out.println(keyString);
	  				return cityDataArray;
	  			}
	  			else
	  			{
	  				return cityDataArray;
	  			}
	  			
  			}
  			catch(Exception e)
  			{
  				logger.warning("exception occured in getCityJson: "+e.getMessage());
  				return cityDataArray;
  			}
  			finally
  			{
  				query.closeAll();
  			}
  		
  	}
}
