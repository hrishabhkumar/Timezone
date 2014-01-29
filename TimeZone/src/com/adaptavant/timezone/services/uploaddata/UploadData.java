package com.adaptavant.timezone.services.uploaddata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.apache.commons.lang3.text.WordUtils;

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.TimezoneJDO;
import com.adaptavant.utilities.MCacheService;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class UploadData {
	Logger logger=Logger.getLogger(UploadData.class.getName());
	public void uploadTime()
	{
		StringTokenizer token = null;
		BufferedReader br = null;
		String line=null;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		TimezoneJDO timezoneJDO=null;;
		Collection<TimezoneJDO> timezoneDataList=new ArrayList<TimezoneJDO>();
		try
		{
			br=new BufferedReader(new FileReader("db.csv"));
			while((line=br.readLine())!=null)
			{	
				token=new StringTokenizer(line, "\t");
				timezoneJDO=new TimezoneJDO();
			while(token.hasMoreTokens())
			{
				String country=WordUtils.capitalizeFully(token.nextToken().replace("\"", ""));
				country=Normalizer
				           .normalize(country, Normalizer.Form.NFD)
				           .replaceAll("[^\\p{ASCII}]", "");
				System.out.println(token.countTokens());
				String state=WordUtils.capitalizeFully(token.nextToken().replace("\"", ""));
				state=Normalizer
		           .normalize(state, Normalizer.Form.NFD)
		           .replaceAll("[^\\p{ASCII}]", "");
				String city=WordUtils.capitalizeFully(token.nextToken().replace("\"", ""));
				city=Normalizer
		           .normalize(city, Normalizer.Form.NFD)
		           .replaceAll("[^\\p{ASCII}]", "");
				String latitude=WordUtils.capitalizeFully(token.nextToken().replace("\"", ""));
				String longitude=WordUtils.capitalizeFully(token.nextToken().replace("\"", ""));
				
				String countryCode=token.nextToken().replace("\"", "").toUpperCase();
				countryCode=Normalizer
		           .normalize(countryCode, Normalizer.Form.NFD)
		           .replaceAll("[^\\p{ASCII}]", "");
				String timeZone=WordUtils.capitalizeFully(token.nextToken().replace("\"", ""));
				String rawOffsetstr=WordUtils.capitalizeFully(token.nextToken().replace("\"", ""));
				
				System.out.println(rawOffsetstr);
				int sign=rawOffsetstr.substring(0,1).toCharArray()[0];
				int hours=Integer.parseInt(rawOffsetstr.substring(1, rawOffsetstr.indexOf(":")))*1000*60*60;
				int min=Integer.parseInt(rawOffsetstr.substring(rawOffsetstr.indexOf(':')+1, rawOffsetstr.length()))*60*1000;
				long rawOffset;
				if(sign=='-')
				{
					rawOffset=-(hours+min);   
				}
				else
				{
					rawOffset=+(hours+min);  
				}
				String dstOffsetstr=token.nextToken().replace("\"", "");
				int sign1=dstOffsetstr.substring(0,1).toCharArray()[0];
				int hours1=Integer.parseInt(dstOffsetstr.substring(1, dstOffsetstr.indexOf(":")))*1000*60*60;
				int min1=Integer.parseInt(dstOffsetstr.substring(dstOffsetstr.indexOf(':')+1, dstOffsetstr.length()))*60*1000;
				int dstOffset;
				if(sign1=='-')
				{
					dstOffset=-(hours1+min1);   
				}
				else
				{
					dstOffset=+(hours+min);  
				}
				String zipCode=token.nextToken().replace("\"", "");
				
				timezoneJDO.setCountry(country);
				timezoneJDO.setCity(city);
				timezoneJDO.setDstOffset(dstOffset);
				timezoneJDO.setRawOffset(rawOffset);
				timezoneJDO.setState(state);
				timezoneJDO.setTimeZoneId(timeZone);
				timezoneJDO.setTimeZoneName(timeZone.substring(timeZone.lastIndexOf('/')+1));
				timezoneJDO.setLatitude(latitude);
				timezoneJDO.setLongitude(longitude);
				timezoneJDO.setCountryCode(countryCode);
				timezoneJDO.setZipCode(zipCode);
			}
			timezoneDataList.add(timezoneJDO);
			}
			br.close();
			pm.makePersistentAll(timezoneDataList);
			pm.close();
			MCacheService.removeAll();
			Queue queue = QueueFactory.getQueue("subscription-queue");
			queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "country"));
			queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "converterData"));
			queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "longAndLat"));
			queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("list", "cityData"));
		}
		catch (Exception e) 
		{
			logger.warning(e.getMessage());
		}
	}
}
