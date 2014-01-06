/**
 * 
 */
package com.adaptavant.adminactivity;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.json.simple.JSONObject;

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.customer.CustomerJDO;
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.adaptavant.timezone.services.MCacheService;
import com.adaptavant.useractivity.list.DataListProvider;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author Hrishabh.Kumar
 *	This Class is will used to change or delete the data of TimeZoneJDO.
 */
public class UpdateData {
	/**
	 * This method will used to update TimeZone data.
	 * Whole TimeZone data will retrieved as a jsonObject.
	 */
	Logger logger=Logger.getLogger(UpdateData.class.getName());
	
	public String changeData(JSONObject oldTimeZoneData, JSONObject newtimezonedata){
		String timeZoneID=newtimezonedata.get("timeZoneID").toString();
		String timeZoneName=newtimezonedata.get("timeZoneName").toString();
		String country=newtimezonedata.get("country").toString();
		String state=newtimezonedata.get("state").toString();
		String city=newtimezonedata.get("city").toString();
		long rawOffset=Long.parseLong(newtimezonedata.get("rawOffset").toString());
		int dstOffset=Integer.parseInt(newtimezonedata.get("dstOffset").toString());
		String longitude=newtimezonedata.get("longitude").toString();
		String latitude=newtimezonedata.get("latitude").toString();
		
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		String keyString=KeyFactory.createKeyString(TimezoneJDO.class.getSimpleName(), city+state+country);
		Query query = pm.newQuery(TimezoneJDO.class,
                " city == '"+oldTimeZoneData.get("city").toString()+"' && state == '" +
                oldTimeZoneData.get("state").toString()+"' "
                + "&& country == '"+oldTimeZoneData.get("country").toString()+"'");
		
		query.setRange(0,1);
		logger.info(query.toString());
		try {
		  @SuppressWarnings("unchecked")
		  List<TimezoneJDO> results = (List<TimezoneJDO>) query.execute();
		  TimezoneJDO timeZoneJDO=new TimezoneJDO();
		  timeZoneJDO.setCity(city);
		  timeZoneJDO.setCountry(country);
		  timeZoneJDO.setDstOffset(dstOffset);
		  timeZoneJDO.setLatitude(latitude);
		  timeZoneJDO.setLongitude(longitude);
		  timeZoneJDO.setRawOffset(rawOffset);
		  timeZoneJDO.setState(state);
		  timeZoneJDO.setTimeZoneId(timeZoneID);
		  timeZoneJDO.setTimeZoneName(timeZoneName);
		  System.out.println(results);
		  if (!results.isEmpty()) {
			  StringBuilder sb=new StringBuilder();
			  String str;
			  
				  str="{\"city\":\""+timeZoneJDO.getCity()+"\" ,\n"
				  		+ "\"state\":\""+timeZoneJDO.getState()+"\" , \n"
						  +"\"country\":\""+timeZoneJDO.getCountry()+"\" , \n"+
						  "\"longitude\":\""+timeZoneJDO.getLongitude()+"\" , \n"+
						  "\"latitude\":\""+timeZoneJDO.getLatitude()+"\" , \n"+
						  "\"timeZoneID\":\""+timeZoneJDO.getTimeZoneId()+"\" , \n"+
						  "\"timeZoneName\":\""+timeZoneJDO.getTimeZoneName()+"\" , \n"+
						  "\"rawOffset\":\""+timeZoneJDO.getRawOffset()+"\" , \n"+
						  "\"dstOffset\":\""+timeZoneJDO.getDstOffset()+"\" , \n"+
						  "}"; 
			 str="{\"data\": ["+str+"]}";
			 MCacheService.set(keyString, str);
			 if(!oldTimeZoneData.get("country").toString().equals(country)){
				 DataListProvider dld=new DataListProvider();
				 dld.getCountryList(1000, null);
			 }
			 if(!oldTimeZoneData.get("state").toString().equals(state)){
				MCacheService.remove("getStateList"+oldTimeZoneData.get("country").toString());			 
			 }
			 if(!oldTimeZoneData.get("city").toString().equals(city)){
				 MCacheService.remove("getCityList"+oldTimeZoneData.get("country").toString()+"and"+oldTimeZoneData.get("state").toString());
				 MCacheService.remove("getTimeZoneDataOf"+oldTimeZoneData.get("city").toString()+oldTimeZoneData.get("state").toString()+oldTimeZoneData.get("country").toString());
			 }
			 return str;
		  } 
		  else {
		    return "{\"data\": [\"data not found.\"]}";
		  }
		} finally {
		  query.closeAll();
		}
	}
}
