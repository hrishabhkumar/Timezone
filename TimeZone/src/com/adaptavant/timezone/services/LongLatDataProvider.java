/**
 * 
 */
package com.adaptavant.timezone.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

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
public class LongLatDataProvider 
{
	Logger logger=Logger.getLogger(LongLatDataProvider.class.getName());
	/**
	 * 
	 * @param limit
	 * @param cursorString
	 * @return longitude and Latitude list along with zipCode
	 */
	@SuppressWarnings("unchecked")
  	public Map<String, String> getlongitudeList(int limit, String cursorString) 
  	{
		Map<String, String> longAndLatMap=null;
  		logger.info("inside timezone id list "+limit);
  		Query query=null;
  		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
  		logger.info("inside getLongAndtLat "+limit);
  		String keyString="getLongAndtLat1";
  		if(MCacheService.containsKey("getLongAndtLat"))
  		{
  			return (Map<String, String>) MCacheService.get("getLongAndtLat");
  		}
  		else
  		{
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
	  				longAndLatMap=new HashMap<String, String>();
	  				for (TimezoneJDO tj: results) 
	  				{
	  					String longAndLatList=tj.getLatitude()+"s"+tj.getLongitude();
	  					longAndLatMap.put(tj.getZipCode(), longAndLatList);
	  				}
	  				
	  				if(MCacheService.containsKey(keyString))
	  				{
	  					longAndLatMap.putAll( (Map<String, String>) MCacheService.get(keyString));
	  				}
	  				MCacheService.set(keyString, longAndLatMap);
	  				if(results.size()==limit)
	  				{
	  					Queue queue = QueueFactory.getQueue("subscription-queue");
	  					queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("cursorString", cursorString).param("list", "longAndLat"));
	  					
	  				}
	  				else
	  				{
	  					longAndLatMap= (Map<String, String>) MCacheService.get(keyString);
	  					logger.log(Level.INFO,"Updating Memcache with LongAndtLat List2");
	  					MCacheService.set("getLongAndtLat", longAndLatMap );
	  					MCacheService.remove(keyString);
	  				}
	  				return longAndLatMap;
	  			}
	  			else
	  			{
	  				return longAndLatMap;
	  			}
	  			
  			}
  			catch(Exception e)
  			{
  				return longAndLatMap;
  			}
  			
  			finally
  			{
  				query.closeAll();
  			}
  		}
  	}
  	/**
  	 * 
  	 * @param latpoint
  	 * @param longpoint
  	 * @return zipcode along with distance(in KM)
  	 */
  	public Map<String, String> getNearestZip(double latpoint, double longpoint)
  	{
  		Map<String, String> nearestZip=null;
		Map<String, String> longAndLatMap= getlongitudeList(1000, null);
  		Map<Double, String> distanceWithZip=new TreeMap<Double, String>();
  		logger.info("returned from getlongitudeList "+longAndLatMap.size());
  		Set<String> keySet= longAndLatMap.keySet();
  		for (String zipCode: keySet) 
  		{
  			String langString=longAndLatMap.get(zipCode);
  			double latitude=Double.parseDouble(langString.substring(0, langString.indexOf('s')));
  			double longitude=Double.parseDouble(langString.substring(langString.indexOf('s')+1, langString.length()));
  		if((latitude>=(latpoint-50/111.045)&&latitude<=(latpoint+50/111.045))&&(longitude>=(longpoint - (50.0 / (111.045 * Math.cos(Math.toRadians(latpoint)))))&&longitude<=(longpoint + (50.0 / (111.045 * Math.cos(Math.toRadians(latpoint))))))){
	  		double distance=Math.round(6378.10 * Math.acos(Math.cos(Math.toRadians(latpoint)) 
		                 * Math.cos(Math.toRadians(latitude)) 
		                 * Math.cos(Math.toRadians(longpoint) - Math.toRadians(longitude)) 
		                 + Math.sin(Math.toRadians(latpoint))
		                 * Math.sin(Math.toRadians(latitude)))*100.0)/100.0;
	  		
	  		distanceWithZip.put(distance, zipCode);
	  		
  		}
  		}
  		logger.info("size of result"+distanceWithZip.size());
  		
  		if(distanceWithZip.size()!=0){
	  		Double nearestDistance=distanceWithZip.keySet().iterator().next();
	  		String zip=distanceWithZip.get(nearestDistance);
	  		nearestZip=new HashMap<String, String>();
	  		nearestZip.put("zip", zip);
	  		nearestZip.put("distance", nearestDistance.toString());
	  		return nearestZip;
  		}
  		else{
  			return nearestZip;
  		}
  	}

}
