/**
 * 
 */
package com.adaptavant.useractivity.register;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpSession;

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.customer.CustomerJDO;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author Hrishabh.Kumar
 *
 */
public class Register {
	HttpSession session;
	PersistenceManager pm = PMF.getPMF().getPersistenceManager();
	public String addUser(HttpSession session, String email, String password) {
		try {
			String keyString = KeyFactory.createKeyString(CustomerJDO.class.getSimpleName(), email);
			CustomerJDO customer=new CustomerJDO();
			customer.setKey(keyString);
			customer.setEmail(email);
			customer.setPassword(password);
			customer.setRequests(1);
			pm.makePersistent(customer);
			pm.close();
			session.setAttribute("userid", customer.getEmail());
			session.setAttribute("key", keyString);
			session.setAttribute("request", customer.getRequests());
			return keyString;
			
		}
		 catch (Exception e) {
			return null;
		}
		
	   
	}
}
