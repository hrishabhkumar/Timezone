package com.adaptavant.timezone.services.uploaddata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import javax.jdo.PersistenceManager;
import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class UploadData {
	public boolean uploadTime(){
		StringTokenizer token = null;
		BufferedReader br = null;
		String line=null;
		String str="db.txt";
		PersistenceManager pm1 = PMF.getPMF().getPersistenceManager();
		TimezoneJDO time;
		Collection<TimezoneJDO> timeobj=new ArrayList<TimezoneJDO>();
			try{
				br=new BufferedReader(new FileReader(str));
				System.out.println("Here is the count of words in file: ");
				
				while((line=br.readLine())!=null){	
					
					System.out.println(line);
					token=new StringTokenizer(line, "\t");
					time=new TimezoneJDO();
				while(token.hasMoreTokens()){
					String country=token.nextToken().replace("\"", "");
					System.out.println(token.countTokens());
					String state=token.nextToken().replace("\"", "");;
					String city=token.nextToken().replace("\"", "");;
					String latitude=token.nextToken().replace("\"", "");;
					String longitude=token.nextToken().replace("\"", "");;
					@SuppressWarnings("unused")
					String countryCode=token.nextToken().replace("\"", "");;
					String timeZone=token.nextToken().replace("\"", "");;
					String rawOffset=token.nextToken().replace("\"", "");;
					String dstOffset=token.nextToken().replace("\"", "");;
					
					time.setCountry(country);
					time.setCity(city);
					time.setDstOffset(dstOffset);
					time.setRawOffset(rawOffset);
					time.setState(state);
					time.setTimeZoneId(timeZone);
					time.setTimeZoneName(timeZone.substring(timeZone.lastIndexOf('/')+1));
					time.setLatitude(latitude);
					time.setLongitude(longitude);					
				}
				timeobj.add(time);
				}
				br.close();
				pm1.makePersistentAll(timeobj);
				pm1.close();
				Queue queue=QueueFactory.getDefaultQueue();
				queue.add(TaskOptions.Builder.withUrl("/countrylist").param("limit", "1000"));
				return true;
			}
		catch (Exception e) {
			return false;
		}
	}
}
