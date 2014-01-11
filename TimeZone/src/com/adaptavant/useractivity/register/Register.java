/**
 * 
 */
package com.adaptavant.useractivity.register;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpSession;

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.customer.CustomerJDO;
import com.adaptavant.timezone.services.TextEncription;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author Hrishabh.Kumar
 *
 */
public class Register {
	HttpSession session;
	PersistenceManager pm = PMF.getPMF().getPersistenceManager();
	CustomerJDO customer=new CustomerJDO();
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
			customer.setKey(keyString);
			customer.setEmail(email);
			
			customer.setPassword(TextEncription.sha1(password));
			customer.setLastLogin(new Date());
			pm.makePersistent(customer);
			pm.close();
			session.setAttribute("userid", customer.getEmail());
			session.setAttribute("key", keyString);
			session.setAttribute("request", customer.getRequests());
			session.setAttribute("lastLogin", customer.getLastLogin());
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
