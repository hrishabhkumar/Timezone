package com.adaptavant.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
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

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.adaptavant.timezone.Timezone;
import com.adaptavant.useractivity.list.DataListProvider;

@Controller

public class TimezoneController {
	Logger logger=Logger.getLogger("TimezoneController");
	@RequestMapping(value="/timezone", method=RequestMethod.GET)
	public String timezone(){
		
		return "timezone";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/timezone", method=RequestMethod.POST)
	public @ResponseBody String registerAction(HttpServletRequest req, @RequestBody String timezonejson) throws ParseException{
		try{
			
		JSONParser parser=new JSONParser();
		JSONObject jsonObject=(JSONObject) parser.parse(timezonejson);
		String key=jsonObject.get("key").toString();
		JSONObject place=(JSONObject) jsonObject.get("place");
		String city;
		String state;
		String country;
		try{
			city=place.get("city").toString();
			System.out.println(city);
		}
		catch(Exception e){
			city=null;
		}
		try{
			state=place.get("state").toString();
			System.out.println(state);
		}
		catch(Exception e){
			state=null;
		}
		try{
			country=place.get("country").toString();
			System.out.println(country);
		}
		catch(Exception e){
			country=null;
		}
		JSONObject tomezonejson=new JSONObject();
		try{
			Timezone timezone=new Timezone();
			String timezonedata=timezone.getTimezoneData(key, city, state, country);
			logger.info("timezone data:  "+timezonedata);
			tomezonejson=(JSONObject) parser.parse(timezonedata);
			return tomezonejson.toJSONString();
		}
		catch(Exception e){
			e.printStackTrace();
			tomezonejson.put("data", "data is not in proper format");
			return tomezonejson.toJSONString();
		}
		} catch (ParseException e1) {
			JSONObject tomezonejson=new JSONObject();
			tomezonejson.put("data", "data is not in proper format");
			return tomezonejson.toJSONString();
		}
		
	}
	
	@RequestMapping("/upload")
	public void uploadTime(HttpServletRequest req, HttpServletResponse resp){
		StringTokenizer token = null;
		BufferedReader br = null;
		String line=null;
		String str="db.txt";
		
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Query query = pm.newQuery(TimezoneJDO.class);
		query.deletePersistentAll();
			try{
				br=new BufferedReader(new FileReader(str));
				System.out.println("Here is the count of words in file: ");
				
				while((line=br.readLine())!=null){	
				System.out.println(line);
				token=new StringTokenizer(line, "\t");
				TimezoneJDO time=new TimezoneJDO();
				while(token.hasMoreTokens()){
				
					String country=token.nextToken().replace("\"", "");
					System.out.println(token.countTokens());
					String state=token.nextToken().replace("\"", "");;
					String city=token.nextToken().replace("\"", "");;
					String latitude=token.nextToken().replace("\"", "");;
					String longitude=token.nextToken().replace("\"", "");;
					@SuppressWarnings("unused")
					String countryCode=token.nextToken().replace("\"", "");;
					String timeZone=token.nextToken().replace("\"", "");;
					String rawOffset=token.nextToken().replace("\"", "");;
					String dstOffset=token.nextToken().replace("\"", "");;
					
					time.setCountry(country);
					time.setCity(city);
					time.setDstOffset(dstOffset);
					time.setRawOffset(rawOffset);
					time.setState(state);
					time.setTimeZoneId(timeZone);
					time.setTimeZoneName(timeZone.substring(timeZone.lastIndexOf('/')+1));
					time.setLatitude(latitude);
					time.setLongitude(longitude);
					
					
					
					pm.makePersistent(time);
					
					
				}
				
				
				}
				pm.close();
				resp.setContentType("text/plain");
				resp.getWriter().println("Hello, world");
			}
		catch(FileNotFoundException f){
			f.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Words ===> Count");
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getList")
	public @ResponseBody String getList(@RequestBody String json){
		JSONParser parser=new JSONParser();
		try {
			logger.info(json);
			JSONObject jsonObject=(JSONObject) parser.parse(json);
			DataListProvider dlp=new DataListProvider();
			if(jsonObject.get("required").toString().equals("country")){
				System.out.println("inside country");
				JSONArray str=dlp.getCountryList();
				jsonObject=new JSONObject();
				jsonObject.put("list", str);
				System.out.println(jsonObject.get("list").toString());
				return jsonObject.toJSONString();
			}
			else if(jsonObject.get("required").toString().equals("state")){
				System.out.println("inside state");
				String country=(String) jsonObject.get("country");
				JSONArray state=dlp.getStateList(country);
				logger.info(state.toJSONString());
				jsonObject=new JSONObject();
				jsonObject.put("list", state);
				System.out.println(jsonObject.toJSONString());
				return jsonObject.toJSONString();
			}
			else if(jsonObject.get("required").toString().equals("city")){
				System.out.println("inside City");
				String country=(String) jsonObject.get("country");
				String state=(String) jsonObject.get("state");
				JSONArray city=dlp.getCityList(country, state);
				logger.info(city.toJSONString());
				jsonObject=new JSONObject();
				jsonObject.put("list", city);
				System.out.println(jsonObject.toJSONString());
				return jsonObject.toJSONString();
			}
			else{
				JSONObject timezonejson=new JSONObject();
				timezonejson.put("data", "data is not in proper format");
				return timezonejson.toJSONString();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			JSONObject timezonejson=new JSONObject();
			timezonejson.put("data", "data is not in proper format");
			return timezonejson.toJSONString();
		}
		
		
	}
}
