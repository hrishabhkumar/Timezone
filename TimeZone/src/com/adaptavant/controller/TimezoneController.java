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

import com.adaptavant.converter.TimezoneListProvider;
import com.adaptavant.timezone.LongLatDataProvider;
import com.adaptavant.timezone.Timezone;
import com.adaptavant.timezone.services.MCacheService;
import com.adaptavant.timezone.services.uploaddata.UploadData;
import com.adaptavant.useractivity.list.DataListProvider;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;


@Controller
@SuppressWarnings("unchecked")
public class TimezoneController {
	Logger logger=Logger.getLogger("TimezoneController");
	JSONParser parser=new JSONParser();
	/**
	 * 
	 * @param req(country,state, city)
	 * @param resp
	 * @return timezone data
	 * this is rest client url will used to find time zone data using 
	 * country name, state name and/or city name
	 */
	@RequestMapping(value="/timezone",params={"country", "state"}, method=RequestMethod.GET)
	public @ResponseBody String countryStateCity(HttpServletRequest req, HttpServletResponse resp){
		String key=(String) req.getSession().getAttribute("key");
		if(key!=null){
		String country=WordUtils.capitalizeFully(req.getParameter("country"));
		String state=WordUtils.capitalizeFully(req.getParameter("state"));
		String city=WordUtils.capitalizeFully(req.getParameter("city"));
		JSONObject obj=new JSONObject();
		obj.put("city", city);
		obj.put("state", state);
		obj.put("country", country);
		JSONObject obj1=new JSONObject();
		obj1.put("place", obj);
		obj1.put("key", key);
		return timezoneAction(req, obj1.toJSONString());
		}
		else{
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
	public @ResponseBody String countryCodeState(HttpServletRequest req, HttpServletResponse resp){
		String key=(String) req.getSession().getAttribute("key");
		if(key!=null){
		String countryCode=req.getParameter("countryCode").toUpperCase();
		String state=WordUtils.capitalizeFully(req.getParameter("state"));
		JSONObject obj=new JSONObject();
		obj.put("state", state);
		obj.put("countryCode", countryCode);
		JSONObject obj1=new JSONObject();
		obj1.put("place", obj);
		obj1.put("key", key);
		return timezoneAction(req, obj1.toJSONString());
		}
		else{
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
	public @ResponseBody String zipCode(HttpServletRequest req, HttpServletResponse resp){
		String key=(String) req.getSession().getAttribute("key");
		if(key!=null){
		String zipCode=WordUtils.capitalizeFully(req.getParameter("zipcode"));
		JSONObject obj=new JSONObject();
		obj.put("zipCode", zipCode);
		JSONObject obj1=new JSONObject();
		obj1.put("place", obj);
		obj1.put("key", key);
		return timezoneAction(req, obj1.toJSONString());
		}
		else{
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
	public @ResponseBody String longitudeLatitude(HttpServletRequest req, HttpServletResponse resp) throws ParseException{
		String key=(String) req.getSession().getAttribute("key");
		if(key!=null){
			String latitude=req.getParameter("latitude");
			String longitude=req.getParameter("longitude");
			JSONObject obj=new JSONObject();
			obj.put("longitude", longitude);
			obj.put("latitude", latitude);
			JSONObject obj1=new JSONObject();
			obj1.put("place", obj);
			obj1.put("key", key);
			return timezoneAction(req, obj1.toJSONString());
		}
		else{
			return "please login..";
		}
	}
	/**
	 * 
	 * @return timezone page
	 */
	@RequestMapping(value="/timezone", method=RequestMethod.GET)
	public String timezoneURL(){
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
	public @ResponseBody String timezoneAction( HttpServletRequest req,@RequestBody String timezonejson) {
		try{
			logger.info(timezonejson);
			JSONObject jsonObject=(JSONObject) parser.parse(timezonejson);
			String key;
			String city;
			String state;
			String country;
			String countryCode;
			String zipCode;
			String longitude;
			String latitude;			
			try{
				key=jsonObject.get("key").toString();
			}
			catch(Exception e){
				JSONObject tomezonejson=new JSONObject();
				tomezonejson.put("data", "Key is not available");
				tomezonejson.put("status", "failed");
				return tomezonejson.toJSONString();
			}
			JSONObject place=(JSONObject) jsonObject.get("place");
//			if(req.getSession().getAttribute("key").equals(key)){
				try{
					countryCode=place.get("countryCode").toString();
					System.out.println(countryCode);
				}
				catch(Exception e){
					logger.warning("Country Code is null");
					countryCode=null;
				}
				try{
					zipCode=place.get("zipCode").toString();
					System.out.println(zipCode);
				}
				catch(Exception e){
					logger.warning("Zip Code is null");
					zipCode=null;
				}
				try{
					longitude=place.get("longitude").toString();
					System.out.println(longitude);
				}
				catch(Exception e){
					logger.warning("Longitude is null");
					longitude=null;
				}
				try{
					latitude=place.get("latitude").toString();
					System.out.println(latitude);
				}
				catch(Exception e){
					logger.warning("Lattitude is null");
					latitude=null;
				}
				try{
					city=place.get("city").toString();
					System.out.println(city);
				}
				catch(Exception e){
					logger.warning("City name is null");
					city=null;
				}
				try{
					state=place.get("state").toString();
					System.out.println(state);
				}
				catch(Exception e){
					logger.log(Level.WARNING,"State name is null");
					state=null;
				}
				try{
					country=place.get("country").toString();
					System.out.println(country);
				}
				catch(Exception e){
					logger.log(Level.WARNING,"Country name is null");
					country=null;
				}
				JSONObject tomezonejson=new JSONObject();
				try{
					Timezone timezone=new Timezone();
					tomezonejson=timezone.getTimezoneData(key, city, state, country, countryCode, zipCode, latitude, longitude);
					logger.info("timezone data:  "+tomezonejson.toJSONString());
					return tomezonejson.toJSONString();
				}
				catch(Exception e){
					logger.log(Level.WARNING,"Error in data");
					tomezonejson.put("data", "data is not in proper format");
					tomezonejson.put("status", "failed");
					return tomezonejson.toJSONString();
//			}
//			}else{
//				JSONObject tomezonejson=new JSONObject();
//				tomezonejson.put("data", "Key is not available");
//				tomezonejson.put("status", "failed");
//				return tomezonejson.toJSONString();
		}
		} catch (Exception e1) {
			logger.log(Level.WARNING,"Error in Parsing");
			JSONObject tomezonejson=new JSONObject();
			tomezonejson.put("data", "data is not in proper format");
			tomezonejson.put("status", "failed");
			return tomezonejson.toJSONString();
		}
		
	}
	/**
	 * 
	 * @param req
	 * @param resp
	 * this is temporary url.
	 */
	@RequestMapping("/uploaddata")
	public void uploadTime(HttpServletRequest req, HttpServletResponse resp){
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
	public @ResponseBody String getList(HttpServletRequest req, @RequestBody String listRequired){
		JSONParser parser=new JSONParser();
		JSONObject jsonObject;
		try {
			logger.info(listRequired);
			jsonObject=(JSONObject) parser.parse(listRequired);
			DataListProvider dlp=new DataListProvider();
			if(jsonObject.get("required").toString().equals("country")){
				logger.info("inside country");
				if(MCacheService.containsKey("getCountryList")){
					logger.log(Level.INFO,"inside memcache of Country");
					JSONArray str=(JSONArray) MCacheService.get("getCountryList");
					jsonObject=new JSONObject();
					jsonObject.put("list", str);
					jsonObject.put("status", "success");
					return jsonObject.toJSONString();
				}
				else{
					if(!MCacheService.containsKey("CountryList")){					
						Queue queue = QueueFactory.getQueue("subscription-queue");
						queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "country").taskName("getCountryList_2"));
					}
					jsonObject.put("status", "wait");
					return jsonObject.toJSONString();
				}
			}
			else if(jsonObject.get("required").toString().equals("state")){
				System.out.println("inside state");
				String country=(String) jsonObject.get("country");
				if(MCacheService.containsKey("getStateList"+country)){
					logger.log(Level.INFO,"inside memcache of State");
					JSONArray str=(JSONArray) MCacheService.get("getStateList"+country);
					jsonObject=new JSONObject();
					jsonObject.put("list", str);
					jsonObject.put("status", "success");
					return jsonObject.toJSONString();
				}
				else{
					logger.log(Level.INFO,jsonObject.toJSONString());
					JSONArray state=dlp.getStateList(country, 1000, null);
					jsonObject=new JSONObject();
					jsonObject.put("list", state);
					if(state!=null){
						jsonObject.put("status", "success");
					}
					else{
						jsonObject.put("status", "wait");
					}
					return jsonObject.toJSONString();
				}
				
			}
			else if(jsonObject.get("required").toString().equals("city")){
				System.out.println("inside City");
				String country=(String) jsonObject.get("country");
				String state=(String) jsonObject.get("state");
				JSONArray city=dlp.getCityList(country, state);
				logger.log(Level.INFO,city.toJSONString());
				jsonObject=new JSONObject();
				jsonObject.put("list", city);
				jsonObject.put("status", "success");
				System.out.println(jsonObject.toJSONString());
				return jsonObject.toJSONString();
			}
			else if(jsonObject.get("required").toString().equals("timezoneData")){
				jsonObject =(JSONObject) jsonObject.get("data");
				jsonObject=(JSONObject) parser.parse(timezoneAction(req,jsonObject.toJSONString()));
				Calendar cal=Calendar.getInstance();
				jsonObject.put("currentTime", cal.getTimeInMillis());
				jsonObject.put("status", "success");
				return jsonObject.toJSONString();
			}
			else{
				jsonObject=new JSONObject();
				jsonObject.put("status", "error");
				return jsonObject.toJSONString();
			}
		} catch (ParseException e) {
			logger.log(Level.WARNING, "parsing Exception in Request body");
			jsonObject=new JSONObject();
			jsonObject.put("status", "error");
			return jsonObject.toJSONString();
		}
	}
	/**
	 * 
	 * @param req
	 * @param resp
	 * this URL is used to retrieve large state list or country list via queue. 
	 */
	@RequestMapping("/list")
	public void getlist(HttpServletRequest req, HttpServletResponse resp){
		DataListProvider dlp=new DataListProvider();
		
		int limit=Integer.parseInt(req.getParameter("limit"));
		String cursorString=req.getParameter("cursorString");
		if(req.getParameter("list").equals("country")){
			dlp.getCountryList(limit, cursorString);
		}
		else if(req.getParameter("list").equals("state")){
			dlp.getStateList(req.getParameter("country"), limit, cursorString);
		}
		else if(req.getParameter("list").equals("timezoneID")){
			TimezoneListProvider listprovider=new TimezoneListProvider();
			listprovider.getTimezoneIDList(limit, cursorString);
		}
		else if(req.getParameter("list").equals("longAndLat")){
			LongLatDataProvider longLatDataProvider=new LongLatDataProvider();
			longLatDataProvider.getlongitudeList(limit, cursorString);
		}
		
	}
	
	@RequestMapping(value="/converter",method=RequestMethod.GET )
	public String converterURL(){
		return "timezoneConverter";
	}
	@RequestMapping(value="/converter",method=RequestMethod.POST )
	public @ResponseBody String converter(@RequestBody String reqired){
		logger.info(reqired);
		try {
			JSONObject requirment=(JSONObject) parser.parse(reqired);
			if(requirment.get("required").toString().equals("timezonenames")){
				TimezoneListProvider timezoneListProvider=new TimezoneListProvider();
				JSONObject obj= timezoneListProvider.getTimezoneIDList(1000, null);
				return obj.toJSONString();				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
