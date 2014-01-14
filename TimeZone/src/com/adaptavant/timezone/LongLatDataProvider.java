/**
 * 
 */
package com.adaptavant.timezone;

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
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.adaptavant.timezone.services.MCacheService;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

/**
 * @author Hrishabh.Kumar
 *
 */
public class LongLatDataProvider {
	Logger logger=Logger.getLogger(LongLatDataProvider.class.getName());
	/**
	 * 
	 * @param limit
	 * @param cursorString
	 * @return longitude and Latitude list along with zipCode
	 */
	@SuppressWarnings("unchecked")
  	public Map<String, String> getlongitudeList(int limit, String cursorString) {
  		logger.info("inside timezone id list "+limit);
  		Query query=null;
  		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
  		logger.info("inside getLongAndtLat "+limit);
  		String keyString="getLongAndtLat1";
  		if(MCacheService.containsKey("getLongAndtLat")){
  			return (Map<String, String>) MCacheService.get("getLongAndtLat");
  		}
  		else{
  			try{
  			query=pm.newQuery(TimezoneJDO.class);
  			query.setRange(0, limit);
  			logger.info(query.toString());
  			if(cursorString!=null){
  				Cursor cursor=Cursor.fromWebSafeString(cursorString);
  				Map<String,Object> extnMap=new HashMap<String, Object>();
  				extnMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
  				query.setExtensions(extnMap);
  			}
  			List<TimezoneJDO> results = (List<TimezoneJDO>)query.execute ();
  			Cursor cursor = JDOCursorHelper.getCursor(results);
  			cursorString=cursor.toWebSafeString();
  			if (!results.isEmpty()) {
  				Map<String, String> longAndLat=new HashMap<String, String>();
  				for (TimezoneJDO tj: results) {
  					String longAndLatList=tj.getLatitude()+"s"+tj.getLongitude();
  					longAndLat.put(tj.getZipCode(), longAndLatList);
  					}
  				
  				if(MCacheService.containsKey(keyString)){
  					longAndLat.putAll( (Map<String, String>) MCacheService.get(keyString));
  				}
  				MCacheService.set(keyString, longAndLat);
  				if(results.size()==limit){
  					Queue queue = QueueFactory.getQueue("subscription-queue");
  					queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("cursorString", cursorString).param("list", "longAndLat"));
  					
  				}
  				else{
  					Map<String, String> data= (Map<String, String>) MCacheService.get(keyString);
  					logger.log(Level.INFO,"Updating Memcache with LongAndtLat List2");
  					System.out.println(data);
  					MCacheService.set("getLongAndtLat", data );
  					MCacheService.remove(keyString);
  					return data;
  				}
  				
  			}
  			return null;
  		}finally{
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
  	public Map<String, String> getNearestZip(double latpoint, double longpoint){
		Map<String, String> data= getlongitudeList(1000, null);
  		Map<Double, String> distanceWithZip=new TreeMap<Double, String>();
  		System.out.println("returned from getlongitudeList "+data.size());
  		Set<String> keySet= data.keySet();
  		for (String data1: keySet) {
  			String langString=data.get(data1);
  			double latitude=Double.parseDouble(langString.substring(0, langString.indexOf('s')));
  			double longitude=Double.parseDouble(langString.substring(langString.indexOf('s')+1, langString.length()));
//  			if((latitude>=(latpoint-50/111.045)&&latitude<=(latpoint+50/111.045))&&(longitude>=(longpoint - (50.0 / (111.045 * Math.cos(Math.toRadians(latpoint)))))&&longitude<=(longpoint + (50.0 / (111.045 * Math.cos(Math.toRadians(latpoint))))))){
	  			double distance=Math.round(6378.10 * Math.acos(Math.cos(Math.toRadians(latpoint)) 
		                 * Math.cos(Math.toRadians(latitude)) 
		                 * Math.cos(Math.toRadians(longpoint) - Math.toRadians(longitude)) 
		                 + Math.sin(Math.toRadians(latpoint))
		                 * Math.sin(Math.toRadians(latitude)))*100.0)/100.0;
	  			distanceWithZip.put(distance, data1);
//  			}
  		}
  		System.out.println("size of result"+distanceWithZip.size());
  		if(distanceWithZip.size()!=0){
	  		Double nearestDistance=distanceWithZip.keySet().iterator().next();
	  		String zip=distanceWithZip.get(nearestDistance);
	  		Map<String, String> result=new HashMap<String, String>();
	  		result.put("zip", zip);
	  		result.put("distance", nearestDistance.toString());
	  		return result;
  		}
  		else{
  			return null;
  		}
  	}

}
