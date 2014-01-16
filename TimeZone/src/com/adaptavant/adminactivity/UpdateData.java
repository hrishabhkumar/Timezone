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
import com.adaptavant.jdo.TimezoneJDO;
import com.adaptavant.timezone.services.DataListProvider;
import com.adaptavant.utilities.MCacheService;

/**
 * @author Hrishabh.Kumar
 *	This Class is will used to change or delete the data of TimeZoneJDO.
 */
public class UpdateData {
	Logger logger=Logger.getLogger(UpdateData.class.getName());
	/**
	 * 
	 * @param oldTimeZoneData
	 * @param newtimezonedata
	 * @return status
	 *  This method will used to update TimeZone data.
	 */
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
		Query query = pm.newQuery(TimezoneJDO.class,
                " city == '"+oldTimeZoneData.get("city").toString()+"' && state == '" +
                oldTimeZoneData.get("state").toString()+"' "
                + "&& country == '"+oldTimeZoneData.get("country").toString()+"'");
		
		query.setRange(0,1);
		logger.info(query.toString());
		try {
		  @SuppressWarnings("unchecked")
		  List<TimezoneJDO> results = (List<TimezoneJDO>) query.execute();
		  
		  if (!results.isEmpty()){
			TimezoneJDO timeZoneJDO=results.get(0);
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
			MCacheService.removeAll();
			DataListProvider dld=new DataListProvider();
			dld.getCountryList(1000, null);
			 return "success";
		 }
		 else{
			 return "failed";
		  }
		} 
		catch(Exception e){
		  return "failed";
		}
			
		finally {
		  query.closeAll();
		}
	}
}
