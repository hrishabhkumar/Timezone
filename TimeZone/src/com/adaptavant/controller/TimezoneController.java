package com.adaptavant.controller;

import java.util.Calendar;
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
import com.adaptavant.timezone.converter.TimezoneListProvider;
import com.adaptavant.timezone.services.DataListProvider;
import com.adaptavant.timezone.services.LongLatDataProvider;
import com.adaptavant.timezone.services.SearchByCity;
import com.adaptavant.timezone.services.Timezone;
import com.adaptavant.timezone.services.uploaddata.UploadData;
import com.adaptavant.utilities.MCacheService;


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
			return timezoneAction(req, requestJson.toJSONString());
		}
		else
		{
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
	@RequestMapping(value="/timezone",params={"countryCode", "state"}, method=RequestMethod.GET)
	public @ResponseBody String countryCodeState(HttpServletRequest req, HttpServletResponse resp)
	{
		String key=(String) req.getSession().getAttribute("key");
		if(key!=null)
		{
			String countryCode=req.getParameter("countryCode").toUpperCase();
			String state=WordUtils.capitalizeFully(req.getParameter("state"));
			JSONObject placeJson=new JSONObject();
			placeJson.put("state", state);
			placeJson.put("countryCode", countryCode);
			JSONObject requestJson=new JSONObject();
			requestJson.put("place", placeJson);
			requestJson.put("key", key);
			return timezoneAction(req, requestJson.toJSONString());
		}
		else
		{
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
			return timezoneAction(req, requestJson.toJSONString());
		}
		else
		{
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
			return timezoneAction(req, requestJson.toJSONString());
		}
		else
		{
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
	public @ResponseBody String timezoneAction( HttpServletRequest req,@RequestBody String timezonejson) 
	{
		JSONObject timezoneJsonObject=null;
		try
		{
			logger.info(timezonejson);
			timezoneJsonObject=(JSONObject) jsonParser.parse(timezonejson);
			String key=null;
			String city=null;
			String state=null;
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
					tomezonejson=timezone.getTimezoneData(key, city, state, country, countryCode, zipCode, latitude, longitude);
					logger.info("timezone data:  "+tomezonejson.toJSONString());
					return tomezonejson.toJSONString();
				}
				catch(Exception e)
				{
					logger.warning("Error in data");
					logger.warning(e.getMessage());
					tomezonejson.put("data", "data is not in proper format");
					tomezonejson.put("status", "failed");
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
			return timezoneJson.toJSONString();
		}
		
	}
	/**
	 * 
	 * @param req
	 * @param resp
	 * this is temporary url.
	 */
	@RequestMapping("/uploaddata")
	public void uploadTime(HttpServletRequest req, HttpServletResponse resp)
	{
		UploadData uploadData=new UploadData();
		uploadData.uploadTime();
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
	public @ResponseBody String getList(HttpServletRequest req, @RequestBody String listRequired)
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
					responseJson=new JSONObject();
					responseJson.put("list", stateListJsonArray);
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
				String state=(String) listRequiredJson.get("state");
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
					responseJson=(JSONObject) jsonParser.parse(timezoneAction(req,listRequiredJson.toJSONString()));
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
		TimezoneListProvider listprovider=null;
		LongLatDataProvider longLatDataProvider=null;
		int limit=Integer.parseInt(req.getParameter("limit"));
		String cursorString=req.getParameter("cursorString");
		
		
		if(req.getParameter("list").equals("country"))
		{
			 dataListProvider=new DataListProvider();
			dataListProvider.getCountryList(limit, cursorString);
		}
		else if(req.getParameter("list").equals("converterData"))
		{
			listprovider=new TimezoneListProvider();
			listprovider.getTimezoneConvertorData(limit, cursorString);
		}
		else if(req.getParameter("list").equals("longAndLat"))
		{
			longLatDataProvider=new LongLatDataProvider();
			longLatDataProvider.getlongitudeList(limit, cursorString);
		}
		else if(req.getParameter("list").equals("cityData")){
			SearchByCity searchByCity=new SearchByCity();
			searchByCity.getCityJson(limit, cursorString, req.getParameter("keyString"));
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
	@RequestMapping(value="/converter",method=RequestMethod.POST )
	public @ResponseBody String converter(@RequestBody String reqired)
	{
		logger.info(reqired);
		try 
		{
			JSONObject requirment=(JSONObject) jsonParser.parse(reqired);
			if(requirment.get("required").toString().equals("timezonenames"))
			{
				
				TimezoneListProvider timezoneListProvider=new TimezoneListProvider();
				responseJson = timezoneListProvider.getTimezoneConvertorData(1000, null);
				if(responseJson!=null)
				{
					return responseJson.toJSONString();
				}
				else
				{
					return null;
				}
			}
			else
			{
				logger.info("required field not matched");
				return null;
			}
		} 
		catch (ParseException e) 
		{
			logger.info(e.getMessage());
			return null;
		}
		
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
	public @ResponseBody String getTimezoneByCity(@RequestBody String searchData)
	{
		JSONArray responseArray=new JSONArray();
		try 
		{
			JSONObject termJSon=(JSONObject) jsonParser.parse(searchData);
			if(MCacheService.containsKey("getCityData1"))
			{
				logger.info("inside memcache of getTimezoneByCity");
				JSONArray memcacheData=new JSONArray();
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
				logger.info(termJSon.get("term").toString());
				for(Object cityData: memcacheData)
				{
					JSONObject data=(JSONObject) cityData;
					if(data.get("label").toString().toLowerCase().contains(termJSon.get("term").toString().toLowerCase()))
					{
						responseArray.add(data);
					}
					else
					{
						continue;
					}
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
	
	@RequestMapping(value="/dailyMemcacheClearance")
	public void dailyMemcacheClearance(){
		MCacheService.removeAll();
		DataListProvider dataListProvider=new DataListProvider();;
		TimezoneListProvider listprovider=new TimezoneListProvider();
		LongLatDataProvider longLatDataProvider=new LongLatDataProvider();
		SearchByCity searchByCity=new SearchByCity();
		int limit=1000;
		String cursorString=null;
		
		dataListProvider.getCountryList(limit, cursorString);
		listprovider.getTimezoneConvertorData(limit, cursorString);
		longLatDataProvider.getlongitudeList(limit, cursorString);
		searchByCity.getCityJson(limit, cursorString, "getCityData1");
	}
}
