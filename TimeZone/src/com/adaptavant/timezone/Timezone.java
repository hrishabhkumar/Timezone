/**
 * 
 */
package com.adaptavant.timezone;

import java.util.List;
import java.util.Map;

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
	/**
	 * 
	 * @param key
	 * @param city
	 * @param state
	 * @param country
	 * @param countryCode
	 * @param zipCode
	 * @param latitude
	 * @param longitude
	 * @return Timezone data as JSON String.
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getTimezoneData(String key, String city, String state, String country, String countryCode, String zipCode, String latitude, String longitude){
		String distance=null;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		System.out.println(key);
		CustomerJDO customer= pm.getObjectById(CustomerJDO.class, key);
		String keyString="getTimeZoneDataOf"+"city="+city+"state="+state+"country="+country+"countrycode="+countryCode+"latitude="+latitude+"longitude="+longitude+"zipcode="+zipCode;
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
			//Query for city,state and Country.
			else if(city==null&&state!=null&&country!=null){
				query = pm.newQuery(TimezoneJDO.class,
		                	"state == '" +state+"' && country == '"+country+"'");
				query.setRange(0,1);
				System.out.println(query.toString());
			}
			//Query for city and state
			else if(city!=null&&state!=null){
				query = pm.newQuery(TimezoneJDO.class,
	                	"state == '" +state+"' && city == '"+city+"'");
				query.setRange(0,1);
				System.out.println(query.toString());
			}
			//Query for state and Country code
			else if(state!=null&&countryCode!=null){
				query = pm.newQuery(TimezoneJDO.class,
	                	"state == '" +state+"' && countryCode == '"+countryCode+"'");
				query.setRange(0,1);
				System.out.println(query.toString());
			}
			//Query for Zip code
			else if(zipCode!=null){
				query = pm.newQuery(TimezoneJDO.class,
	                	"zipCode == '" +zipCode+"'");
				query.setRange(0,1);
				System.out.println(query.toString());
			}
			//Query for longitude and latitude.
			else if(longitude!=null&&latitude!=null){
				double latpoint=Double.parseDouble(latitude);
				double longpoint=Double.parseDouble(longitude);
				LongLatDataProvider longLatDataProvide=new LongLatDataProvider();
				Map<String, String> vistorZip=longLatDataProvide.getNearestZip(latpoint, longpoint);
				if(vistorZip!=null){
					distance=vistorZip.get("distance")+"K.M";
					String zip=vistorZip.get("zip");
					query = pm.newQuery(TimezoneJDO.class,
		                	"zipCode == '" +zip+"'");
					query.setRange(0,1);
					
				}
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
				  if(longitude!=null&&latitude!=null){
					  object.put("distance", distance);
				  }
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


