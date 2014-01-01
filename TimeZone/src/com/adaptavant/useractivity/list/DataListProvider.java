package com.adaptavant.useractivity.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.adaptavant.timezone.services.MCacheService;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.datanucleus.query.JDOCursorHelper;


public class DataListProvider {
	Queue queue = QueueFactory.getDefaultQueue();
	public String getCountryList(){
		String keyString="getCountryList";
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();	
		if(MCacheService.get(keyString)!=null){
			System.out.println("inside memcache");
			return (String) MCacheService.get(keyString);
		}
		else{
			Query query = pm.newQuery(TimezoneJDO.class);
			query.setRange(0, 1000);
			Cursor cursor = JDOCursorHelper.getCursor((List<TimezoneJDO>)query.execute());
			query.setResult ("distinct country");
			
			System.out.println(query.toString());
			String cursorString;
			try {
			  List<?> results = (List<?>)query.execute ();
			  cursorString=cursor.toWebSafeString();
			  System.out.println(query.execute());
			  if (!results.isEmpty()) {
				  Iterator<?> iter = results.iterator();
				  StringBuilder sb=new StringBuilder();
				  String str;
				  while (iter.hasNext()) {
				      String country = (String) iter.next();
				      country=country.replace('\"', ' ');
				      sb=sb.append("{\"c_name\":\""+country+"\"},");
				  }	
				  GetCursor gc=new GetCursor();
				  str =gc.getCountryList(cursorString, 1000);
				 
					  sb=sb.append(str);
					  System.out.println(str);
				  
				  str="{\"list\":["+sb.toString()+"]}";			  
				  MCacheService.set(keyString, str);
				  return str;
				  
			  } 
			  else {
			    return "{\"list\": [\"data not found.\"]}";
			  }
			} 
			finally {
			  query.closeAll();
			}
		}
		
	}
	
	public String getStateList(String country){
		String keyString="getStateList"+country;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();	
		if(MCacheService.get(keyString)!=null){
			System.out.println("inside memcache");
			return (String) MCacheService.get(keyString);
		}
		else{
		Query query = pm.newQuery(TimezoneJDO.class, "country == '"+country+"'");
	
		query.setResult ("distinct state");
	
		System.out.println(query.toString());
		try {
		  Collection<?> results = (Collection<?>)query.execute (); 
		  System.out.println(query.execute());
		  if (!results.isEmpty()) {
			  Iterator<?> iter = results.iterator();
			 
			  StringBuilder sb=new StringBuilder();
			  String str;
			  while (iter.hasNext()) {
			      String state = (String) iter.next();
			      state=state.replace('\"', ' ');
			    	 sb=sb.append("{\"st_name\":\""+state+"\"},");
			  }
			  str="{\"list\":["+sb.toString()+"]}";
			  
				MCacheService.set(keyString, str);
			  return str;
			  
		  } else {
		    return "{\"list\": [\"data not found.\"]}";
		  }
		} finally {
		  query.closeAll();
		}
		}
		
	}
	
	public String getCityList(String country, String state){
		String keyString="getCityList"+country+"and"+state;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();	
		if(MCacheService.get(keyString)!=null){
			System.out.println("inside memcache");
			return (String) MCacheService.get(keyString);
		}
		else{
		Query query = pm.newQuery(TimezoneJDO.class, "country == '"+country+"'&& state == '"+state+"'");
	
		query.setResult ("distinct city");
	
		System.out.println(query.toString());
		try {
		  Collection<?> results = (Collection<?>)query.execute (); 
		  System.out.println(query.execute());
		  if (!results.isEmpty()) {
			  Iterator<?> iter = results.iterator();
			 
			  StringBuilder sb=new StringBuilder();
			  String str;
			  while (iter.hasNext()) {
			      String city = (String) iter.next();
			      city=city.replace('\"', ' ');
			    	 sb=sb.append("{\"ct_name\":\""+city+"\"},");
			  }
			  str="{\"list\":["+sb.toString()+"]}";
			  
				MCacheService.set(keyString, str);
			  return str;
			  
		  } else {
		    return "{\"list\": [\"data not found.\"]}";
		  }
		} finally {
		  query.closeAll();
		}
		}
		
	}
}
