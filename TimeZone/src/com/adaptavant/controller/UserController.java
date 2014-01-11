package com.adaptavant.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.adaptavant.adminactivity.DeleteData;
import com.adaptavant.adminactivity.UpdateData;
import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.customer.CustomerJDO;
import com.adaptavant.timezone.services.SendEmail;
import com.adaptavant.timezone.services.TextEncription;
import com.adaptavant.useractivity.login.Login;
import com.adaptavant.useractivity.register.Register;
import com.google.appengine.api.datastore.KeyFactory;

@Controller
public class UserController {
	JSONParser parser=new JSONParser();
	JSONObject jsonObject=null;
	Logger logger=Logger.getLogger(UserController.class.getName());
	/**
	 * This is mapping for home and index URL.
	 */
	@RequestMapping(value={"/", "/home"})
	public String indexURL(HttpServletRequest req, HttpServletResponse resp){
		return "home";
	}
	/**
	 * This is mapping for admin URL.
	 */
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public String adminURL(){
		return "admin";
	}
	/**
	 * @param timezoneData 
	 * @return status of admin Action(update & delete)
	 * This is mapping for admin action.(update and deletion)
	 */
	@RequestMapping(value="/admin", method=RequestMethod.POST)
	public @ResponseBody String adminAction(@RequestBody String timezoneData){
		logger.info("inside admin action.");
		try {
			JSONObject data=(JSONObject) parser.parse(timezoneData);
			if(data.get("opreq").toString().equals("update")){
				logger.info("inside update.");
				JSONObject oldTimezoneData=(JSONObject) data.get("oldTimezoneData");
				JSONObject newTimezoneData=(JSONObject) data.get("newTimezoneData");
				UpdateData updateData=new UpdateData();
				updateData.changeData(oldTimezoneData, newTimezoneData);
			}
			else if(data.get("opreq").toString().equals("delete")){
				logger.info("inside delete.");
				JSONObject TimezoneData=(JSONObject) data.get("timezoneData");
				DeleteData deleteData=new DeleteData();
				deleteData.deleteData(TimezoneData);
			}
		} catch (Exception e) {
			logger.severe("exception in admin Action.");
			return null;
		}
		return "admin";
	}
	/**
	 * 
	 * @param req
	 * @param resp
	 * @return login page
	 */
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String loginURL(HttpServletRequest req, HttpServletResponse resp){
		return "login";
	}
	/**
	 * 
	 * @param req
	 * @param resp
	 * @return register page
	 */
			
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerURL(HttpServletRequest req, HttpServletResponse resp){
		return "register";
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return login page
	 */
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logoutURL(HttpServletRequest request, HttpServletResponse response){
		request.getSession().invalidate();
		return "login";
	}
	
	/**
	 * 
	 * @param req
	 * @param loginData
	 * @return status of login with key
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/login", method=RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public @ResponseBody String loginAction(HttpServletRequest req, @RequestBody String loginData){
		System.out.println(loginData);
		try{
			JSONObject jsonObject=(JSONObject) parser.parse(loginData);
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
				jsonObject.put("error", "Your are not registered. Please register to use timezone");
			}
			System.out.println(jsonObject.toJSONString());
			return jsonObject.toJSONString();
		}
		catch(Exception e){
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("status", "failed");
			jsonObject.put("key", null);
			jsonObject.put("error", "Your data is not in correct format");
			return jsonObject.toJSONString();
		}
	}
	/**
	 * 
	 * @param req
	 * @param userData
	 * @return status of registration with key
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public @ResponseBody String registerAction(HttpServletRequest req, @RequestBody String userData){
		try{
		JSONObject jsonObject=(JSONObject) parser.parse(userData);
		Register register=new Register();
		if(jsonObject.get("userid").toString().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[-A-Za-z0-9]+(\\.[A-Za-z]{1,})+$")&&jsonObject.get("password").toString().length()>=6){
			if(!register.alreadyRegisterd(jsonObject.get("userid").toString())){
				String key=register.addUser(req.getSession(), jsonObject.get("userid").toString(), jsonObject.get("password").toString());
				if(key!=null){
					jsonObject=new JSONObject();
					jsonObject.put("status", "success");
					jsonObject.put("key", key);
					return jsonObject.toJSONString();
				}
				else{
					jsonObject=new JSONObject();
					jsonObject.put("status", "failed");
					jsonObject.put("key", null);
					jsonObject.put("error", "Key should not be null");
					return jsonObject.toJSONString();
				}
			}
			else{
				jsonObject=new JSONObject();
				jsonObject.put("status", "failed");
				jsonObject.put("key", null);
				jsonObject.put("error", "User ID is already registered");
				return jsonObject.toJSONString();
			}
		}
		else{
			jsonObject=new JSONObject();
			jsonObject.put("status", "failed");
			jsonObject.put("key", null);
			jsonObject.put("error", "User ID must be email, and Password must have atleast 6 character");
			return jsonObject.toJSONString();
		}
		}
		catch(Exception e){
			jsonObject=new JSONObject();
			jsonObject.put("status", "failed");
			jsonObject.put("key", null);
			jsonObject.put("error", "Your data is not in correct format");
			return jsonObject.toJSONString();
		}
	}
	/**
	 * 
	 * @return recovery page
	 */
	@RequestMapping(value="/recover/initiate", method=RequestMethod.GET)
	public String recoverIntiate(){
		return "recovery";
	}
	/**
	 * 
	 * @param req(user email)
	 * @param resp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/recover/initiate", method=RequestMethod.POST)
	public @ResponseBody String recoverMail(HttpServletRequest req,@RequestBody String email){
		try{
			PersistenceManager pm = PMF.getPMF().getPersistenceManager();
			JSONObject obj=(JSONObject) parser.parse(email);
			String userid=obj.get("userid").toString();
			System.out.println(userid);
			String key=KeyFactory.createKeyString(CustomerJDO.class.getSimpleName(), userid);
			CustomerJDO customerObj=pm.getObjectById(CustomerJDO.class, key);
			if(!customerObj.isForgot()){
				SendEmail.email(userid, customerObj.getPassword());
				customerObj.setForgot(true);
				JSONObject response=new JSONObject();
				response.put("msg", "Email sent successfully .");
				response.put("status", "success");
				return response.toJSONString();
			}
			else{
				JSONObject response=new JSONObject();
				response.put("msg", "email already sent. Please check your email");
				response.put("status", "failed");
				return response.toJSONString();
			}
		}
		catch(Exception e){
			JSONObject response=new JSONObject();
			response.put("msg", "your email is not yet registered");
			response.put("status", "failed");
			return response.toJSONString();
		}
	}
	/**
	 * 
	 * @param req
	 * @param resp
	 * @param model
	 * @return changePassword page if passed else login page with error message.
	 */
	@RequestMapping(value="/recover", params={"userid", "tpass"}, method=RequestMethod.GET)
	public ModelAndView recoverPass(HttpServletRequest req, HttpServletResponse resp, ModelMap model){
		try{
			PersistenceManager pm = PMF.getPMF().getPersistenceManager();
			String key=KeyFactory.createKeyString(CustomerJDO.class.getSimpleName(), req.getParameter("userid"));
			CustomerJDO customerObj=pm.getObjectById(CustomerJDO.class, key);
			String tpass=req.getParameter("tpass");
			if(customerObj.getEmail().equalsIgnoreCase(req.getParameter("userid"))&&customerObj.getPassword().equals(tpass)){
				req.getSession().setAttribute("recover", "ok");
				req.getSession().setAttribute("userid", req.getParameter("userid"));
				return new ModelAndView("changePassword", "userid", req.getParameter("userid"));
			}
			else{
				return new ModelAndView("login", "recoverfail", "your creditial are wrong. Please try again");
			}
		}
		catch(Exception e){
			return new ModelAndView("login", "recoverfail", "your creditial are wrong. Please try again");

		}
	}
	/**
	 * 
	 * @param req(user email)
	 * @param resp
	 * @return user found/notFound
	 */
	@RequestMapping(value="/recover/searchuser",  method=RequestMethod.POST)
	public @ResponseBody String searchUser(HttpServletRequest req, HttpServletResponse resp){
		System.out.println(req.getParameter("userid"));
		try{
			PersistenceManager pm = PMF.getPMF().getPersistenceManager();
			System.out.println(KeyFactory.createKeyString(CustomerJDO.class.getSimpleName(), req.getParameter("userid")));
			
			String key=KeyFactory.createKeyString(CustomerJDO.class.getSimpleName(), req.getParameter("userid"));
			pm.getObjectById(CustomerJDO.class, key);
			return "found";
		}
		catch(Exception e){
			return "notFound";
		}
	}
	@RequestMapping(value="/passwordchange",  method=RequestMethod.GET)
	public void changePasswordURL(HttpServletRequest req, HttpServletResponse resp){
		try {
			resp.sendRedirect("/login");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param req
	 * @param resp
	 * @return Model and view after changing password
	 */
	@RequestMapping(value="/passwordchange",  method=RequestMethod.POST)
	public ModelAndView changePassword(HttpServletRequest req, HttpServletResponse resp){
		try{
			String pass=TextEncription.sha1(req.getParameter("pass"));
			String userid=(String) req.getSession().getAttribute("userid");
			logger.info("User Id: "+userid+" and Password: "+pass);
			PersistenceManager pm = PMF.getPMF().getPersistenceManager();
			String key=KeyFactory.createKeyString(CustomerJDO.class.getSimpleName(), userid);
			CustomerJDO customerObj=pm.getObjectById(CustomerJDO.class, key);
			if(customerObj.isForgot()){
				if(!customerObj.getPassword().equals(pass)){
					customerObj.setPassword(pass);
					customerObj.setForgot(false);
					req.getSession().invalidate();
					return new ModelAndView("login","recovermsg", "Password changed success fully. Please login..");
				}
				else{
					return new ModelAndView("changePassword","changefail", "You are using your old password");
				}
			}
			else{
				
				return new ModelAndView("login","recoverfail", "You haven't yet applied for forget password. Or you have already changed it.");
			}
		}
		catch(Exception e){
			req.getSession().invalidate();
			return new ModelAndView("login","recoverfail", "sorry!! recovery failed. Please try again. ");
		}
		
	}
}
