package com.adaptavant.controller;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adaptavant.timezone.Timezone;
import com.adaptavant.timezone.services.MCacheService;
import com.adaptavant.timezone.services.uploaddata.UploadData;
import com.adaptavant.useractivity.list.DataListProvider;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;


@Controller

public class TimezoneController {
	Logger logger=Logger.getLogger("TimezoneController");
	@RequestMapping(value="/timezone", method=RequestMethod.GET)
	public String timezone(){
		
		return "timezone";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/timezone", method=RequestMethod.POST)
	public @ResponseBody String timezoneAction( @RequestBody String timezonejson) throws ParseException{
		try{
		JSONParser parser=new JSONParser();
		JSONObject jsonObject=(JSONObject) parser.parse(timezonejson);
		String key=jsonObject.get("key").toString();
		JSONObject place=(JSONObject) jsonObject.get("place");
		String city;
		String state;
		String country;
		if(key!=null){
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
				String timezonedata=timezone.getTimezoneData(key, city, state, country);
				logger.info("timezone data:  "+timezonedata);
				tomezonejson=(JSONObject) parser.parse(timezonedata);
				tomezonejson.put("status", "success");
				return tomezonejson.toJSONString();
			}
			catch(Exception e){
				logger.log(Level.WARNING,"Error in data");
				tomezonejson.put("data", "data is not in proper format");
				tomezonejson.put("status", "failed");
				return tomezonejson.toJSONString();
		}
		}else{
			JSONObject tomezonejson=new JSONObject();
			tomezonejson.put("data", "Key is not available");
			tomezonejson.put("status", "failed");
			return tomezonejson.toJSONString();
		}
		} catch (Exception e1) {
			logger.log(Level.WARNING,"Error in Parsing");
			JSONObject tomezonejson=new JSONObject();
			tomezonejson.put("data", "data is not in proper format");
			tomezonejson.put("status", "failed");
			return tomezonejson.toJSONString();
		}
		
	}
	
	@RequestMapping("/uploaddata")
	public void uploadTime(HttpServletRequest req, HttpServletResponse resp){
		UploadData uploadData=new UploadData();
		uploadData.uploadTime();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getList")
	public @ResponseBody String getList(@RequestBody String json){
		JSONParser parser=new JSONParser();
		JSONObject jsonObject;
		try {
			logger.info(json);
			jsonObject=(JSONObject) parser.parse(json);
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
						queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "country").taskName("getCountry1-"));
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
				jsonObject=(JSONObject) parser.parse(timezoneAction(jsonObject.toJSONString()));
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
	DataListProvider dlp=new DataListProvider();
	@RequestMapping("/list")
	public void getlist(HttpServletRequest req, HttpServletResponse resp){
		int limit=Integer.parseInt(req.getParameter("limit"));
		String cursorString=req.getParameter("cursorString");
		
		if(req.getParameter("list").equals("country")){
			dlp.getCountryList(limit, cursorString);
		}
		else if(req.getParameter("list").equals("state")){
			dlp.getStateList(req.getParameter("country"), limit, cursorString);
		}
	}
}
