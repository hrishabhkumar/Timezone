/**
 * 
 */
package com.adaptavant.useractivity;

import java.util.Date;
import java.util.logging.Logger;

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
public class Login {
	Logger logger=Logger.getLogger(Login.class.getName());
	String keyString=null;
	/**
	 * 
	 * @param session
	 * @param email
	 * @param password
	 * @return userKey
	 * this method is used for user login and create their session.
	 */
	public String login(HttpSession session, String email, String password)
	{
		
		if(session.getAttribute("key")==null)
		{
			logger.info("new Seesion");
			if(email==""||password==""||email==null||password==null)
			{
				logger.info("null value");
				return keyString;
			}
			else {
				PersistenceManager pm = PMF.getPMF().getPersistenceManager();
				try{
					keyString=KeyFactory.createKeyString(CustomerJDO.class.getSimpleName(), email);
					logger.info("Got Key: "+keyString);
					CustomerJDO customer= pm.getObjectById(CustomerJDO.class, keyString);
					if(email.equals(customer.getEmail())&& TextEncription.sha1(password).equals(customer.getPassword())){
						logger.info("value for req  "+email);
						Date lastLogin=customer.getLastLogin();
						Date currentTime=new Date();
						customer.setLastLogin(currentTime);
						session.setAttribute("userid", customer.getEmail());
						session.setAttribute("key", keyString);
						session.setAttribute("lastLogin", lastLogin);
						logger.info("Key String: "+keyString);
						return keyString;
					}
					else{
						keyString=null;
						logger.info("Wrong credentilas");
						return keyString;
					}
				}
				catch(Exception e){
					logger.info("Exception Occurred");
					return keyString;
				}
			}
		}
		else{
			logger.info("old Seesion");
			logger.info(session.getAttribute("userid").toString());
			return session.getAttribute("key").toString();		
		}
	}
}
