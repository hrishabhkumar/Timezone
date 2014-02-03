/**
 * 
 */
package com.adaptavant.timezone.services;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.adaptavant.jdo.CustomerJDO;
import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.TimezoneJDO;
import com.adaptavant.utilities.MCacheService;

/**
 * @author Hrishabh.Kumar
 *
 */
public class Timezone 
{
	Logger logger=Logger.getLogger(Timezone.class.getName());
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
	public JSONObject getTimezoneData(String key, String city, String state,String stateCode, String country, String countryCode, String zipCode, String latitude, String longitude)
	{
		JSONObject reponseJson=null;
		String distance=null;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		logger.info("Key:" +key);
		CustomerJDO customer= pm.getObjectById(CustomerJDO.class, key);
		String keyString="getTimeZoneDataOf"+"city="+city+"state="+state+"country="+country+"countrycode="+countryCode+"latitude="+latitude+"longitude="+longitude+"zipcode="+zipCode;
		if(MCacheService.get(keyString)!=null)
		{
			int requests=customer.getRequests();
			requests++;
			customer.setRequests(requests);
			return (JSONObject) MCacheService.get(keyString);
		}
		else
		{
			Query query=null;
			if(city!=null&&state!=null&&country!=null)
			{
				query = pm.newQuery(TimezoneJDO.class,
		                " city == '"+city+"' && state == '" +
						state+"' && country == '"+country+"'");
				query.setRange(0,1);
				logger.info(query.toString());
			}
			//Query for city,state and Country.
			else if(city==null&&state!=null&&country!=null)
			{
				query = pm.newQuery(TimezoneJDO.class,
		                	"state == '" +state+"' && country == '"+country+"'");
				query.setRange(0,1);
				logger.info(query.toString());
			}
			//Query for city and state
			else if(city!=null&&state!=null)
			{
				query = pm.newQuery(TimezoneJDO.class,
	                	"state == '" +state+"' && city == '"+city+"'");
				query.setRange(0,1);
				logger.info(query.toString());
			}
			//Query for state and Country code
			else if(stateCode!=null&&countryCode!=null)
			{
				query = pm.newQuery(TimezoneJDO.class,
	                	"stateCode == '" +stateCode+"' && countryCode == '"+countryCode+"'");
				query.setRange(0,1);
				logger.info(query.toString());
			}
			//Query for Zip code
			else if(zipCode!=null)
			{
				query = pm.newQuery(TimezoneJDO.class,
	                	"zipCode == '" +zipCode+"'");
				query.setRange(0,1);
				logger.info(query.toString());
			}
			//Query for longitude and latitude.
			else if(longitude!=null&&latitude!=null)
			{
				double latpoint=Double.parseDouble(latitude);
				double longpoint=Double.parseDouble(longitude);
				LongLatDataProvider longLatDataProvide=new LongLatDataProvider();
				Map<String, String> vistorZip=longLatDataProvide.getNearestZip(latpoint, longpoint);
				if(vistorZip!=null)
				{
					distance=vistorZip.get("distance")+"K.M";
					String zip=vistorZip.get("zip");
					query = pm.newQuery(TimezoneJDO.class,
		                	"zipCode == '" +zip+"'");
					query.setRange(0,1);
					
				}
				logger.info(query.toString());
			}
			
			
			try 
			{
				List<TimezoneJDO> results = (List<TimezoneJDO>) query.execute();
				if (!results.isEmpty()) 
				{
					JSONArray placeArray=new JSONArray();
					for (TimezoneJDO timezonejdo : results)
					{
						JSONObject timezoneDataJson=new JSONObject();
						if(city!=null||zipCode!=null||(longitude!=null&&latitude!=null))
						{
							timezoneDataJson.put("city", timezonejdo.getCity());
							timezoneDataJson.put("zipCode", timezonejdo.getZipCode());
						}
						timezoneDataJson.put("country", timezonejdo.getCountry());
						timezoneDataJson.put("countryCode", timezonejdo.getCountryCode());
						timezoneDataJson.put("state", timezonejdo.getState());
						timezoneDataJson.put("stateCode", timezonejdo.getStateCode());
						timezoneDataJson.put("longitude", timezonejdo.getLongitude());
						timezoneDataJson.put("latitude", timezonejdo.getLatitude());
						timezoneDataJson.put("timeZoneID", timezonejdo.getTimeZoneId());
						timezoneDataJson.put("timeZoneName", timezonejdo.getTimeZoneName());
						timezoneDataJson.put("rawOffset", timezonejdo.getRawOffset());
						timezoneDataJson.put("dstOffset", timezonejdo.getDstOffset());
						placeArray.add(timezoneDataJson);
				  }
				  reponseJson=new JSONObject();
				  reponseJson.put("data",placeArray);
				  if(longitude!=null&&latitude!=null)
				  {
					  reponseJson.put("distance", distance);
				  }
				  reponseJson.put("status", "success");
				  int requests=customer.getRequests();
					requests++;
					customer.setRequests(requests);
					MCacheService.set(keyString, reponseJson);
				  return reponseJson;
				  
				} 
				else 
				{
					reponseJson=new JSONObject();
					reponseJson.put("data",null);
					reponseJson.put("status", "notFound");
					return reponseJson;
			  }
			} 
			finally 
			{
			  query.closeAll();
			}
		}
	}
}


