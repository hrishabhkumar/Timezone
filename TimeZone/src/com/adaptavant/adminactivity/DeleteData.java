/**
 * 
 */
package com.adaptavant.adminactivity;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.json.simple.JSONObject;

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.TimezoneJDO;
import com.adaptavant.utilities.MCacheService;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * @author Hrishabh.Kumar
 *
 */
public class DeleteData {
	Logger logger=Logger.getLogger(DeleteData.class.getName());
	/**
	 * @param TimeZoneData
	 * @return status
	 * this method will be used to delete timezone data
	 */
	public String deleteData(JSONObject TimeZoneData){
		String country=TimeZoneData.get("country").toString();
		String state=TimeZoneData.get("state").toString();
		String city=TimeZoneData.get("city").toString();
		
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Query query = pm.newQuery(TimezoneJDO.class," city == '"+city+"' && state == '" +state+"' "
                										+ "&& country == '"+country+"'");
		query.setRange(0,1);
		logger.info(query.toString());
		try {
		  @SuppressWarnings("unchecked")
		  List<TimezoneJDO> results = (List<TimezoneJDO>) query.execute();
		  if (!results.isEmpty())
		  {
			TimezoneJDO timeZoneJDO=results.get(0);
			pm.deletePersistent(timeZoneJDO);
			MCacheService.removeAll();
			Queue queue = QueueFactory.getQueue("subscription-queue");
			queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "country"));
			queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "longAndLat"));
			queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "cityData"));
			return "success";
		 }
		 else{
			 return "failed";
		  }
		} 
		catch(Exception e){
		  return "failed";
		}
			
		finally {
		  query.closeAll();
		}
	}
}
