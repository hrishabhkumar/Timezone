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
	public JSONObject getTimezoneData(String key, String city, String state, String country, String countryCode, String zipCode, String latitude, String longitude){
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		System.out.println(key);
		CustomerJDO customer= pm.getObjectById(CustomerJDO.class, key);
		String keyString="getTimeZoneDataOf"+city+state+country+countryCode+latitude+longitude+zipCode;
		if(MCacheService.get(keyString)!=null){
			int requests=customer.getRequests();
			requests++;
			customer.setRequests(requests);
			return (JSONObject) MCacheService.get(keyString);
		}
		else{
			Query query=null;
			if(city!=null&&state!=null&&country!=null){
				query = pm.newQuery(TimezoneJDO.class,
		                " city == '"+city+"' && state == '" +
						state+"' && country == '"+country+"'");
				query.setRange(0,1);
				System.out.println(query.toString());
			}
			else if(city==null&&state!=null&&country!=null){
				query = pm.newQuery(TimezoneJDO.class,
		                	"state == '" +state+"' && country == '"+country+"'");
				query.setRange(0,1);
				System.out.println(query.toString());
			}
			else if(city!=null&&state!=null){
				query = pm.newQuery(TimezoneJDO.class,
	                	"state == '" +state+"' && city == '"+city+"'");
				query.setRange(0,1);
				System.out.println(query.toString());
			}
			else if(state!=null&&countryCode!=null){
				query = pm.newQuery(TimezoneJDO.class,
	                	"state == '" +state+"' && countryCode == '"+countryCode+"'");
				query.setRange(0,1);
				System.out.println(query.toString());
			}
			else if(zipCode!=null){
				query = pm.newQuery(TimezoneJDO.class,
	                	"zipCode == '" +zipCode+"'");
				query.setRange(0,1);
				System.out.println(query.toString());
			}
			else if(longitude!=null&&latitude!=null){
				query = pm.newQuery(TimezoneJDO.class,
	                	"longitude == '" +longitude+"' && latitude == '"+latitude+"'");
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
					  if(city!=null||zipCode!=null||(longitude!=null&&latitude!=null)){
						  object.put("city", timezonejdo.getCity());
						  object.put("zipCode", timezonejdo.getZipCode());
					  }
					  object.put("country", timezonejdo.getCountry());
					  object.put("countryCode", timezonejdo.getCountryCode());
					  object.put("state", timezonejdo.getState());
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


