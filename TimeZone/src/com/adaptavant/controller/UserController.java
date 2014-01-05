package com.adaptavant.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adaptavant.useractivity.login.Login;
import com.adaptavant.useractivity.register.Register;

@Controller
public class UserController {
	JSONParser parser=new JSONParser();
	JSONObject jsonObject=null;
	//
	@RequestMapping(value="/")
	public String indexURL(HttpServletRequest req, HttpServletResponse resp){
		return "home";
	}
	@RequestMapping(value="/home")
	public String homeURL(HttpServletRequest req, HttpServletResponse resp){
		return "home";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String loginURL(HttpServletRequest req, HttpServletResponse resp){
		return "login";
	}
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerURL(HttpServletRequest req, HttpServletResponse resp){
		return "register";
	}
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logoutURL(HttpServletRequest request, HttpServletResponse response){
		request.getSession().invalidate();
		response.setHeader("Cache-Control","no-cache");
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragma","no-cache");
		response.setDateHeader ("Expires", 0);
		return "login";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/login", method=RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public @ResponseBody String loginAction(HttpServletRequest req, @RequestBody String json) throws ParseException{
		System.out.println(json);
		JSONObject jsonObject=(JSONObject) parser.parse(json);
		Login login=new Login();
		String key=login.login(req.getSession(), jsonObject.get("userid").toString(), jsonObject.get("password").toString());
		jsonObject=new JSONObject();
		if(key!=null){
			jsonObject.put("status", "success");
			jsonObject.put("key", key);
		}
		else{
			jsonObject.put("status", "failed");
			jsonObject.put("key", null);
		}
		System.out.println(jsonObject.toJSONString());
		return jsonObject.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public @ResponseBody String registerAction(HttpServletRequest req, @RequestBody String json) throws ParseException{
		JSONObject jsonObject=(JSONObject) parser.parse(json);
		Register register=new Register();
		String key=register.addUser(req.getSession(), jsonObject.get("userid").toString(), jsonObject.get("password").toString());
		jsonObject=new JSONObject();
		if(key!=null){
			jsonObject.put("status", "success");
			jsonObject.put("key", key);
		}
		else{
			jsonObject.put("status", "failed");
			jsonObject.put("key", null);
		}
		
		return jsonObject.toJSONString();
	}

}
