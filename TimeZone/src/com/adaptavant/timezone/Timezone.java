/**
 * 
 */
package com.adaptavant.timezone;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.customer.CustomerJDO;
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.adaptavant.timezone.services.MCacheService;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author Hrishabh.Kumar
 *
 */
public class Timezone {
	
	public String getTimezoneData(String key, String city, String state, String country){
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		System.out.println(key);
		CustomerJDO customer= pm.getObjectById(CustomerJDO.class, key);
		String keyString=KeyFactory.createKeyString(TimezoneJDO.class.getSimpleName(), city+state+country);
		if(MCacheService.get(keyString)!=null){
			int requests=customer.getRequests();
			requests++;
			customer.setRequests(requests);
			return (String) MCacheService.get(keyString);
		}
		else{
		Query query = pm.newQuery(TimezoneJDO.class,
                " city == '"+city+"' && state == '" +
				state+"' && country == '"+country+"'");
		System.out.println(query.toString());
		try {
		  @SuppressWarnings("unchecked")
		List<TimezoneJDO> results = (List<TimezoneJDO>) query.execute();
		  System.out.println(results);
		  if (!results.isEmpty()) {
			  StringBuilder sb=new StringBuilder();
			  String str;
			  for (TimezoneJDO timezonejdo : results) {
				  str="{\"city\":\""+timezonejdo.getCity()+"\" ,\n\"state\":\""+timezonejdo.getState()+"\" , \n"
						  +"\"country\":\""+timezonejdo.getCountry()+"\" , \n"+
						  "\"longitude\":\""+timezonejdo.getLongitude()+"\" , \n"+
						  "\"latitude\":\""+timezonejdo.getLatitude()+"\" , \n"+
						  "\"timeZoneID\":\""+timezonejdo.getTimeZoneId()+"\" , \n"+
						  "\"timeZoneName\":\""+timezonejdo.getTimeZoneName()+"\" , \n"+
						  "\"rawOffset\":\""+timezonejdo.getRawOffset()+"\" , \n"+
						  "\"dstOffset\":\""+timezonejdo.getDstOffset()+"\" , \n"+
						  "},\n"; 
			    	 sb=sb.append(str+",");
			  }
			  str="{\"data\": ["+sb.toString()+"]}";
			  int requests=customer.getRequests();
				requests++;
				customer.setRequests(requests);
			  
				MCacheService.set(keyString, str);
			  return str;
			  
		  } else {
		    return "{\"data\": [\"data not found.\"]}";
		  }
		} finally {
		  query.closeAll();
		}
		}
	}
}


