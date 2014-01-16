/**
 * 
 */
package com.adaptavant.useractivity;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpSession;

import com.adaptavant.jdo.CustomerJDO;
import com.adaptavant.jdo.PMF;
import com.adaptavant.utilities.TextEncription;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author Hrishabh.Kumar
 *
 */
public class Register {
	HttpSession session;
	PersistenceManager pm = PMF.getPMF().getPersistenceManager();
	CustomerJDO customerJDO=new CustomerJDO();
	/**
	 * 
	 * @param session
	 * @param email
	 * @param password
	 * @return Key
	 * This method is used for user registration.
	 */
	public String addUser(HttpSession session, String email, String password) {
		try {
			String keyString = KeyFactory.createKeyString(CustomerJDO.class.getSimpleName(), email);
			customerJDO.setKey(keyString);
			customerJDO.setEmail(email);
			
			customerJDO.setPassword(TextEncription.sha1(password));
			customerJDO.setLastLogin(new Date());
			pm.makePersistent(customerJDO);
			pm.close();
			session.setAttribute("userid", customerJDO.getEmail());
			session.setAttribute("key", keyString);
			session.setAttribute("request", customerJDO.getRequests());
			session.setAttribute("lastLogin", customerJDO.getLastLogin());
			return keyString;
			
		}
		 catch (Exception e) {
			return null;
		}
		
	   
	}
	public boolean alreadyRegisterd(String email) {
		try{
			String keyString = KeyFactory.createKeyString(CustomerJDO.class.getSimpleName(), email);
			pm.getObjectById(CustomerJDO.class, keyString);
			return true;
		}
		catch(Exception e){
			return false;
		}
		
		
	}
}
