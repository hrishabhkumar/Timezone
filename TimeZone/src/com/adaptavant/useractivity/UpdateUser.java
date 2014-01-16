/**
 * 
 */
package com.adaptavant.useractivity;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.json.simple.JSONObject;
import com.adaptavant.jdo.CustomerJDO;
import com.adaptavant.jdo.PMF;
/**
 * @author Hrishabh.Kumar
 *
 */
public class UpdateUser 
{
	Logger logger=Logger.getLogger(UpdateUser.class.getName());
	public String updateUser(JSONObject userDetailJSON)
	{
		try
		{
			PersistenceManager pm = PMF.getPMF().getPersistenceManager();
			String keyString=userDetailJSON.get("key").toString();
			CustomerJDO customerJDO= pm.getObjectById(CustomerJDO.class, keyString);
			customerJDO.setEmail(userDetailJSON.get("email").toString());
			customerJDO.setPassword(userDetailJSON.get("password").toString());
			pm.makePersistent(customerJDO);
			logger.info("User data Updated");
			return "success";
		}
		catch(Exception e)
		{
			logger.warning(e.getMessage());
			return "error";
		}
		
	}
}
