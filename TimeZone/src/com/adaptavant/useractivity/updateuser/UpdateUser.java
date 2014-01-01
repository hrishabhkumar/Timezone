/**
 * 
 */
package com.adaptavant.useractivity.updateuser;

import javax.jdo.PersistenceManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.customer.CustomerJDO;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;



/**
 * @author Hrishabh.Kumar
 *
 */
public class UpdateUser {
	public String updateUser(String json){
		try{
			JSONObject obj;
			JSONParser parser=new JSONParser();
			obj=(JSONObject) parser.parse(json);
			CustomerJDO customer=new CustomerJDO();
			PersistenceManager pm = PMF.getPMF().getPersistenceManager();
			Key key = KeyFactory.createKey(CustomerJDO.class.getSimpleName(), obj.get("email").toString());
			customer= pm.getObjectById(CustomerJDO.class,key );
			customer.setEmail(obj.get("email").toString());
			customer.setPassword(obj.get("password").toString());
			pm.makePersistent(customer);
			return "success";
		}
		catch(Exception e){
			System.out.println("in update catch" +e.toString());
			return "error";
		}
		
	}
}
