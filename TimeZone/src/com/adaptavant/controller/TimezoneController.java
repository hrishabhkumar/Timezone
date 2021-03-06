package com.adaptavant.controller;

import java.util.Calendar;
import java.util.Collection;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.text.WordUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adaptavant.timezone.services.DataListProvider;
import com.adaptavant.timezone.services.LongLatDataProvider;
import com.adaptavant.timezone.services.SearchByCity;
import com.adaptavant.timezone.services.Timezone;
import com.adaptavant.timezone.services.uploaddata.UploadDataTimeZoneData;
import com.adaptavant.utilities.MCacheService;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;


@Controller
@SuppressWarnings("unchecked")
public class TimezoneController 
{
	Logger logger=Logger.getLogger("TimezoneController");
	JSONParser jsonParser=new JSONParser();
	JSONObject responseJson=null;
	
	/**
	 * 
	 * @param req(country,state, city)
	 * @param resp
	 * @return timezone data
	 * this is rest client url will used to find time zone data using 
	 * country name, state name and/or city name
	 */
	@RequestMapping(value="/timezone",params={"country", "state"}, method=RequestMethod.GET)
	public @ResponseBody String countryStateCityRequest(HttpServletRequest req, HttpServletResponse resp)
	{
		String key=(String) req.getSession().getAttribute("key");
		if(key!=null){
			String country=WordUtils.capitalizeFully(req.getParameter("country"));
			String state=WordUtils.capitalizeFully(req.getParameter("state"));
			String city=WordUtils.capitalizeFully(req.getParameter("city"));
			JSONObject placeJson=new JSONObject();
			placeJson.put("city", city);
			placeJson.put("state", state);
			placeJson.put("country", country);
			JSONObject requestJson=new JSONObject();
			requestJson.put("place", placeJson);
			requestJson.put("key", key);
			return timezoneAction(req, resp, requestJson.toJSONString());
		}
		else
		{
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "please login..";
		}
	}
	/**
	 * 
	 * @param req(countryCode, state name)
	 * @param resp
	 * @return timezone data
	 * this is rest client URL will used to find time zone data using 
	 * country code and state name
	 */
	@RequestMapping(value="/timezone",params={"countryCode", "stateCode"}, method=RequestMethod.GET)
	public @ResponseBody String countryCodeState(HttpServletRequest req, HttpServletResponse resp)
	{
		String key=(String) req.getSession().getAttribute("key");
		if(key!=null)
		{
			String countryCode=req.getParameter("countryCode").toUpperCase();
			String stateCode=req.getParameter("stateCode").toUpperCase();
			JSONObject placeJson=new JSONObject();
			placeJson.put("stateCode", stateCode);
			placeJson.put("countryCode", countryCode);
			JSONObject requestJson=new JSONObject();
			requestJson.put("place", placeJson);
			requestJson.put("key", key);
			return timezoneAction(req, resp, requestJson.toJSONString());
		}
		else
		{
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "please login..";
		}
	}
	/**
	 * 
	 * @param req(zipcode)
	 * @param resp
	 * @return timezone data
	 * this is rest client URL will used to find time zone data using only zip code of city
	 */
	@RequestMapping(value="/timezone",params={"zipcode"}, method=RequestMethod.GET)
	public @ResponseBody String zipCode(HttpServletRequest req, HttpServletResponse resp)
	{
		String key=(String) req.getSession().getAttribute("key");
		if(key!=null)
		{
			String zipCode=WordUtils.capitalizeFully(req.getParameter("zipcode"));
			JSONObject placeJson=new JSONObject();
			placeJson.put("zipCode", zipCode);
			JSONObject requestJson=new JSONObject();
			requestJson.put("place", placeJson);
			requestJson.put("key", key);
			return timezoneAction(req, resp, requestJson.toJSONString());
		}
		else
		{
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "please login..";
		}
	}
	/**
	 * 
	 * @param req(longitude, latitude)
	 * @param resp
	 * @return timezone data
	 * this is rest client URL will used to find time zone data using only longitude and latitude
	 * @throws ParseException 
	 */
	@RequestMapping(value="/timezone",params={"longitude", "latitude"}, method=RequestMethod.GET)
	public @ResponseBody String longitudeLatitude(HttpServletRequest req, HttpServletResponse resp)
	{
		String key=(String) req.getSession().getAttribute("key");
		if(key!=null)
		{
			String latitude=req.getParameter("latitude");
			String longitude=req.getParameter("longitude");
			JSONObject placeJson=new JSONObject();
			placeJson.put("longitude", longitude);
			placeJson.put("latitude", latitude);
			JSONObject requestJson=new JSONObject();
			requestJson.put("place", placeJson);
			requestJson.put("key", key);
			return timezoneAction(req, resp, requestJson.toJSONString());
		}
		else
		{
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "please login..";
		}
	}
	/**
	 * 
	 * @return timezone page
	 */
	@RequestMapping(value="/timezone", method=RequestMethod.GET)
	public String timezoneURL()
	{
		return "timezone";
	}
	/**
	 * 
	 * @param req
	 * @param timezonejson(key,(city,state,country)or(countryCode, state)or(zipCode)or(longitude,latitude))
	 * @return timezonedata
	 * This method will retrieve timezonejson data and give response as timezone data in json format.
	 */
	@RequestMapping(value="/timezone", method=RequestMethod.POST)
	public @ResponseBody String timezoneAction( HttpServletRequest req,HttpServletResponse resp,@RequestBody String timezonejson) 
	{
		JSONObject timezoneJsonObject=null;
		try
		{
			logger.info(timezonejson);
			timezoneJsonObject=(JSONObject) jsonParser.parse(timezonejson);
			String key=null;
			String city=null;
			String state=null;
			String stateCode=null;
			String country=null;
			String countryCode=null;
			String zipCode=null;
			String longitude=null;
			String latitude=null;			
			try
			{
				key=timezoneJsonObject.get("key").toString();
			}
			catch(Exception e)
			{
				timezoneJsonObject=new JSONObject();
				timezoneJsonObject.put("data", "Key is not available");
				timezoneJsonObject.put("status", "failed");
				return timezoneJsonObject.toJSONString();
			}
			JSONObject place=(JSONObject) timezoneJsonObject.get("place");
//			if(req.getSession().getAttribute("key").equals(key)){
				try
				{
					countryCode=place.get("countryCode").toString();
					logger.info("Country code="+countryCode);
				}
				catch(Exception e)
				{
					logger.warning("Country Code is null");
					countryCode=null;
				}
				try
				{
					zipCode=place.get("zipCode").toString();
					logger.info("zipCode="+zipCode);
				}
				catch(Exception e)
				{
					logger.warning("Zip Code is null");
					zipCode=null;
				}
				try
				{
					longitude=place.get("longitude").toString();
					logger.info("longitude="+longitude);
				}
				catch(Exception e)
				{
					logger.warning("Longitude is null");
					longitude=null;
				}
				try
				{
					latitude=place.get("latitude").toString();
					logger.info("latitude="+latitude);
				}
				catch(Exception e){
					logger.warning("Lattitude is null");
					latitude=null;
				}
				try{
					city=place.get("city").toString();
					logger.info("city="+city);
				}
				catch(Exception e)
				{
					logger.warning("City name is null");
					city=null;
				}
				try
				{
					state=place.get("state").toString();
					logger.info("state="+state);
				}
				catch(Exception e)
				{
					logger.warning("State name is null");
					state=null;
				}
				try
				{
					stateCode=place.get("stateCode").toString();
					logger.info("stateCode="+stateCode);
				}
				catch(Exception e)
				{
					logger.warning("State code name is null");
					stateCode=null;
				}
				try
				{
					country=place.get("country").toString();
					logger.info("country="+country);
				}
				catch(Exception e)
				{
					logger.warning("Country name is null");
					country=null;
				}
				JSONObject tomezonejson=new JSONObject();
				try
				{
					Timezone timezone=new Timezone();
					tomezonejson=timezone.getTimezoneData(key,city, state,stateCode, country, countryCode, zipCode, latitude, longitude);
					logger.info("timezone data:  "+tomezonejson.toJSONString());
					if(tomezonejson.get("status").toString().equals("success"))
					{
						return tomezonejson.toJSONString();
					}
					else
					{
						resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
						return tomezonejson.toJSONString();
						
					}
				}
				catch(Exception e)
				{
					logger.warning("Error in data");
					logger.warning(e.getMessage());
					tomezonejson.put("data", "data is not in proper format");
					tomezonejson.put("status", "failed");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return tomezonejson.toJSONString();
				}
//			}
//			else
//			{
//				JSONObject tomezonejson=new JSONObject();
//				tomezonejson.put("data", "Key is not available");
//				tomezonejson.put("status", "failed");
//				return tomezonejson.toJSONString();
//			}
		} 
		catch (Exception e) 
		{
			logger.warning("Error in Parsing");
			logger.warning(e.getMessage());
			JSONObject timezoneJson=new JSONObject();
			timezoneJson.put("data", "data is not in proper format");
			String format="{\"key\": \"userKey\",\"place\":{\"country\":\"countryName\",\"state\":\"stateName\",\"city\":\"cityName\"}||{\"country\":\"countryName\",\"state\":\"stateName\"}||{\"countryCode\":\"countryCode\",\"state\":\"stateName\"}||{\"zipCode\":\"ZipCode\"}||{\"longitude\":\"longitude\",\"latitude\":\"latitude\"}}";
			timezoneJson.put("format", format);
			timezoneJson.put("status", "failed");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return timezoneJson.toJSONString();
		}
		
	}
	/**
	 * 
	 * @param req
	 * @param resp
	 * this is temporary url.
	 */
	@RequestMapping("/uploadtimezonedata")
	public String uploadTimezoneData(HttpServletRequest req, HttpServletResponse resp)
	{
		Queue queue = QueueFactory.getQueue("subscription-queue");
		queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "uploadtimezonedata"));
		return "login";	
	}
	
	/**
	 * 
	 * @param req
	 * @param listRequired
	 * @return countryList
	 * @return stateList(according to country)
	 * @return cityList(according to state and country)
	 * @return timezone data with current server time
	 * 
	 */
	@RequestMapping(value="/getList")
	public @ResponseBody String getPlaceList(HttpServletRequest req, @RequestBody String listRequired)
	{
		JSONObject listRequiredJson=null;
		try 
		{
			logger.info(listRequired);
			listRequiredJson=(JSONObject) jsonParser.parse(listRequired);
			DataListProvider dataListProvider=new DataListProvider();
			if(listRequiredJson.get("required").toString().equals("country"))
			{
				logger.info("inside country");
				if(MCacheService.containsKey("getCountryList"))
				{
					logger.log(Level.INFO,"inside memcache of Country");
					JSONArray countryListJsonArray=(JSONArray) MCacheService.get("getCountryList");
					responseJson=new JSONObject();
					responseJson.put("list", countryListJsonArray);
					responseJson.put("status", "success");
					return responseJson.toJSONString();
				}
				else if(MCacheService.containsKey("CountryList"))
				{
					Collection<String> countrySet=(TreeSet<String>) MCacheService.get("CountryList");
					JSONArray countryJsonArray=new JSONArray();
					for(String countryNameandCode: countrySet)
					{
						JSONObject countryData=new JSONObject();
						countryData.put("country", countryNameandCode.substring(0, countryNameandCode.indexOf("&")));
						countryData.put("countryCode", countryNameandCode.substring(countryNameandCode.indexOf("&")+1));
						countryData.put("label", countryNameandCode.substring(countryNameandCode.indexOf("&")+1)+" "+countryNameandCode.substring(0, countryNameandCode.indexOf("&")));
						countryJsonArray.add(countryData);
					}
					responseJson=new JSONObject();
					responseJson.put("list", countryJsonArray);
					responseJson.put("status", "success");
					return responseJson.toJSONString();
				}
				else
				{
					if(!MCacheService.containsKey("CountryList"))
					{					
						dataListProvider.getCountryList(1000,null);
					}
					responseJson=new JSONObject();
					responseJson.put("status", "wait");
					return responseJson.toJSONString();
				}
			}
			else if(listRequiredJson.get("required").toString().equals("state"))
			{
				logger.info("inside state");
				String country=(String) listRequiredJson.get("country");
				if(MCacheService.containsKey("getStateList"+country))
				{
					logger.info("inside memcache of State");
					JSONArray stateListJsonArray=(JSONArray) MCacheService.get("getStateList"+country);
					System.out.println(stateListJsonArray);
					responseJson=new JSONObject();
					responseJson.put("list", stateListJsonArray);
					responseJson.put("status", "success");
					return responseJson.toJSONString();
				}
				else if(MCacheService.containsKey("getStateList1"+country))
				{
					Collection<String> stateSet=(TreeSet<String>) MCacheService.get("getStateList1"+country);
					JSONArray stateJsonArray=new JSONArray();
					stateJsonArray.addAll(stateSet);
					responseJson=new JSONObject();
					responseJson.put("list", stateJsonArray);
					responseJson.put("status", "success");
					return responseJson.toJSONString();
				}
				else
				{
					logger.info(listRequiredJson.toJSONString());
					JSONArray stateListJsonArray=dataListProvider.getStateList(country, 1000, null);
					responseJson=new JSONObject();
					responseJson.put("list", stateListJsonArray);
					if(stateListJsonArray!=null)
					{
						responseJson.put("status", "success");
					}
					else
					{
						responseJson.put("status", "wait");
					}
					return responseJson.toJSONString();
				}
				
			}
			else if(listRequiredJson.get("required").toString().equals("city"))
			{
				logger.info("inside City");
				String country=(String) listRequiredJson.get("country");
				String state=listRequiredJson.get("state").toString();
				System.out.println(state);
				JSONArray city=dataListProvider.getCityList(country, state);
				logger.log(Level.INFO,city.toJSONString());
				responseJson=new JSONObject();
				responseJson.put("list", city);
				responseJson.put("status", "success");
				logger.info("Json response in city"+responseJson.toJSONString());
				return responseJson.toJSONString();
			}
			else if(listRequiredJson.get("required").toString().equals("timezoneData"))
			{
				try{
					listRequiredJson =(JSONObject) listRequiredJson.get("data");
					responseJson=(JSONObject) jsonParser.parse(timezoneAction(req,null, listRequiredJson.toJSONString()));
					Calendar cal=Calendar.getInstance();
					responseJson.put("currentTime", cal.getTimeInMillis());
					return responseJson.toJSONString();
				}
				catch(Exception e){
					logger.info(e.getMessage());
					responseJson=new JSONObject();
					responseJson.put("status", "failed");
					return responseJson.toJSONString();
				}
			}
			else
			{
				responseJson=new JSONObject();
				responseJson.put("status", "error");
				return responseJson.toJSONString();
			}
		} 
		catch (ParseException e) 
		{
			logger.warning("parsing Exception in Request body: "+e.getMessage());
			responseJson=new JSONObject();
			responseJson.put("status", "error");
			return responseJson.toJSONString();
		}
	}
	/**
	 * 
	 * @param req
	 * @param resp
	 * this URL is used to retrieve large state list or country list via queue. 
	 */
	@RequestMapping("/list")
	public void getlist(HttpServletRequest req, HttpServletResponse resp)
	{
		DataListProvider dataListProvider=null;
		LongLatDataProvider longLatDataProvider=null;
		int limit=Integer.parseInt(req.getParameter("limit"));
		String cursorString=req.getParameter("cursorString");
		if(req.getParameter("list").equals("country"))
		{
			 dataListProvider=new DataListProvider();
			dataListProvider.getCountryList(limit, cursorString);
		}
		else if(req.getParameter("list").equals("state"))
		{
			 dataListProvider=new DataListProvider();
			 dataListProvider.getStateList(req.getParameter("country"), limit, cursorString);
		}
		else if(req.getParameter("list").equals("longAndLat"))
		{
			longLatDataProvider=new LongLatDataProvider();
			longLatDataProvider.getlongitudeList(limit, cursorString, req.getParameter("keyString"));
		}
		else if(req.getParameter("list").equals("cityData")){
			SearchByCity searchByCity=new SearchByCity();
			searchByCity.getCityJson(limit, cursorString, req.getParameter("keyString"));
		}
		else if(req.getParameter("list").equals("uploadtimezonedata")){
			UploadDataTimeZoneData uploadData=new UploadDataTimeZoneData();
			uploadData.uploadTimeZoneData();
		}
	}
	/**
	 * 
	 * @return data for converter.
	 */
	@RequestMapping(value="/converter",method=RequestMethod.GET )
	public String converterURL()
	{
		return "timezoneConverter";
	}
	
	/**
	 * 
	 * @return current server time.
	 */
	@RequestMapping(value="/getUTCTime")
	public @ResponseBody String getUTCTime()
	{
		Calendar cal=Calendar.getInstance();
		System.out.println(((Long)cal.getTimeInMillis()).toString());
		return ((Long)cal.getTimeInMillis()).toString();
	}
	/**
	 * 
	 * @param searchData
	 * @return
	 */
	@RequestMapping(value="/timezonebycity" ,method=RequestMethod.POST)
	public @ResponseBody String getTimezoneByCity(HttpServletResponse resp,@RequestBody String searchData)
	{
		JSONArray responseArray=new JSONArray();
		try 
		{
			JSONObject termJSon=(JSONObject) jsonParser.parse(searchData);
			logger.info(termJSon.get("term").toString());
			String searchKey=termJSon.get("term").toString().replace("\\", "");
			JSONArray memcacheData=new JSONArray();
			if(MCacheService.containsKey("getCityData1"))
			{
				logger.info("inside memcache of getTimezoneByCity");
				for(int i=1;i<=10;i++)
				{
					if(MCacheService.containsKey("getCityData"+i))
					{
						memcacheData.addAll((JSONArray)MCacheService.get("getCityData"+i));
					}
					else
					{
						break;
					}
				}
			for(int i=0;i<memcacheData.size();i++)
			{
				JSONObject cityData=(JSONObject) memcacheData.get(i);
				if(cityData.get("label").toString().toLowerCase().contains(searchKey.toLowerCase()))
				{
					responseArray.add(cityData);
					if(responseArray.size()>=500)
					{
						break;
					}
				}
			}
				if(responseArray.size()==0)
				{
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
				
				return responseArray.toJSONString();
			}
			else
			{
				logger.info("inside else of getTimezoneByCity data not available in membache");
				SearchByCity searchByCity=new SearchByCity();
				String cityData=searchByCity.getCityJson(1000, null, "getCityData1").toJSONString();
				return cityData;
			}
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			return responseArray.toJSONString();
		}
	}
	
	@RequestMapping(value="/monthlyMemcacheClearance")
	public @ResponseBody String monthlyMemcacheClearance(){
		MCacheService.removeAll();
		Queue queue = QueueFactory.getQueue("subscription-queue");
		queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "country"));
		queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "converterData"));
		queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "longAndLat"));
		queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "cityData"));
		return "success";
	}
}
