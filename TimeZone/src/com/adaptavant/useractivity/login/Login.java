/**
 * 
 */
package com.adaptavant.useractivity.login;

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
public class Login {
	
	/**
	 * 
	 * @param session
	 * @param email
	 * @param password
	 * @return userKey
	 * this method is used for user login and create their session.
	 */
	public String login(HttpSession session, String email, String password){	
		if(session.getAttribute("key")==null){
			System.out.println("new Seesion");
			if(email==""||password==""||email==null||password==null){
				System.out.println("null value");
				return null;
			}
			else {
				PersistenceManager pm = PMF.getPMF().getPersistenceManager();
				try{
					String keyString=KeyFactory.createKeyString(CustomerJDO.class.getSimpleName(), email);
					System.out.println("Got Key: "+keyString);
					CustomerJDO customer= pm.getObjectById(CustomerJDO.class, keyString);
					
					if(email.equals(customer.getEmail())&& TextEncription.sha1(password).equals(customer.getPassword())){
						System.out.println("value for req  "+email);
						Date lastLogin=customer.getLastLogin();
						Date currentTime=new Date();
						customer.setLastLogin(currentTime);
						session.setAttribute("userid", customer.getEmail());
						session.setAttribute("key", keyString);
						session.setAttribute("lastLogin", lastLogin);
						System.out.println("Key String: "+keyString);
						return keyString;
					}
					else{
						System.out.println("Wrong credentilas");
						return null;
					}
				}
				catch(Exception e){
					System.out.println("Exception Occurred");
					return null;
				}
			}
		}
		else{
			System.out.println("old Seesion");
			System.out.println(session.getAttribute("userid"));
			return session.getAttribute("key").toString();		
		}
	}
}
