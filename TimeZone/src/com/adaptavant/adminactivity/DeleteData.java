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
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.adaptavant.timezone.services.MCacheService;

/**
 * @author Hrishabh.Kumar
 *
 */
public class DeleteData {
	Logger logger=Logger.getLogger(DeleteData.class.getName());
	
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
		  if (!results.isEmpty()){
			TimezoneJDO timeZoneJDO=results.get(0);
			pm.deletePersistent(timeZoneJDO);
			MCacheService.remove("getTimeZoneDataOf"+city+state+country);
			MCacheService.remove("getTimeZoneDataOf"+city+state+country);
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
