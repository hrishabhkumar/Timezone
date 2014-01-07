/**
 * 
 */
package com.adaptavant.timezone;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.customer.CustomerJDO;
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.adaptavant.timezone.services.MCacheService;

/**
 * @author Hrishabh.Kumar
 *
 */
public class Timezone {
	/*
	 * This method will return Timezone data as JSON String.
	 * User Key, State name, Country Name and City Name are Mandatory.
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getTimezoneData(String key, String city, String state, String country){
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		System.out.println(key);
		CustomerJDO customer= pm.getObjectById(CustomerJDO.class, key);
		String keyString="getTimeZoneDataOf"+city+state+country;
		if(MCacheService.get(keyString)!=null){
			int requests=customer.getRequests();
			requests++;
			customer.setRequests(requests);
			return (JSONObject) MCacheService.get(keyString);
		}
		else{
			Query query=null;
			if(city!=null){
				query = pm.newQuery(TimezoneJDO.class,
		                " city == '"+city+"' && state == '" +
						state+"' && country == '"+country+"'");
				query.setRange(0,1);
				System.out.println(query.toString());
			}
			else{
				query = pm.newQuery(TimezoneJDO.class,
		                	"state == '" +state+"' && country == '"+country+"'");
				query.setRange(0,1);
				System.out.println(query.toString());
			}
			
			try {
			List<TimezoneJDO> results = (List<TimezoneJDO>) query.execute();
			  System.out.println(results);
			  if (!results.isEmpty()) {
				  JSONArray array=new JSONArray();
				  for (TimezoneJDO timezonejdo : results) {
					  JSONObject object=new JSONObject();
					  if(city!=null){
						  object.put("city", timezonejdo.getCity());
					  }
					  object.put("state", timezonejdo.getState());
					  object.put("country", timezonejdo.getCountry());
					  object.put("longitude", timezonejdo.getLongitude());
					  object.put("latitude", timezonejdo.getLatitude());
					  object.put("timeZoneID", timezonejdo.getTimeZoneId());
					  object.put("timeZoneName", timezonejdo.getTimeZoneName());
					  object.put("rawOffset", timezonejdo.getRawOffset());
					  object.put("dstOffset", timezonejdo.getDstOffset());
					  array.add(object);
				  }
				  JSONObject object=new JSONObject();
				  object.put("data",array);
				  object.put("status", "success");
				  int requests=customer.getRequests();
					requests++;
					customer.setRequests(requests);
					MCacheService.set(keyString, object);
				  return object;
				  
			  } else {
				  JSONObject object=new JSONObject();
				  object.put("data",null);
				  object.put("status", "error");
			    return null;
			  }
			} finally {
			  query.closeAll();
			}
		}
	}
}


