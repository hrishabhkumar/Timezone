package com.adaptavant.useractivity.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.json.simple.JSONArray;

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.adaptavant.timezone.services.MCacheService;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;


public class DataListProvider {
	JSONArray array=new JSONArray();
	@SuppressWarnings("unchecked")
	public JSONArray getCountryList(){
		String keyString="getCountryList";
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();	
		if(MCacheService.get(keyString)!=null){
			System.out.println("inside memcache");
			return (JSONArray) MCacheService.get(keyString);
		}
		else{
			Query query = pm.newQuery(TimezoneJDO.class);
			query.setRange(0, 1000);			
			System.out.println(query.toString());
			
			try {
			  @SuppressWarnings("unchecked")
			List<TimezoneJDO> results = (List<TimezoneJDO>)query.execute ();
//			  Cursor cursor = JDOCursorHelper.getCursor(results);
//			  String cursorString=cursor.toWebSafeString();
			  System.out.println(query.execute());
			  if (!results.isEmpty()) {
				  Collection<String> country=new TreeSet<String>();
				  for (TimezoneJDO tj: results) {
				      country.add(tj.getCountry());
				  }	
				  
				  array=new JSONArray();
				  array.addAll(country);
				  MCacheService.set(keyString, array);
				  System.out.println(array.toJSONString());
				  return array;
				  
			  } 
			  else {
			    return null;
			  }
			} 
			finally {
			  query.closeAll();
			}
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getStateList(String country){
		String keyString="getStateList"+country;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();	
		if(MCacheService.get(keyString)!=null){
			System.out.println("inside memcache");
			return (JSONArray) MCacheService.get(keyString);
		}
		else{
		Query query = pm.newQuery(TimezoneJDO.class, "country == '"+country+"'");
	
		query.setResult ("distinct state");
	
		System.out.println(query.toString());
		try {
		  Collection<?> results = (Collection<?>)query.execute (); 
		  System.out.println(query.execute());
		  if (!results.isEmpty()) {
			 
			  Collection<String> state=new TreeSet<String>();
			  state.addAll((Collection<? extends String>) results);
			  
			  array=new JSONArray();
			  array.addAll(state);
				MCacheService.set(keyString, array);
			  return array;
			  
		  } else {
			  
		    return null;
		  }
		} finally {
		  query.closeAll();
		}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getCityList(String country, String state){
		String keyString="getCityList"+country+"and"+state;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();	
		if(MCacheService.get(keyString)!=null){
			System.out.println("inside memcache");
			return (JSONArray) MCacheService.get(keyString);
		}
		else{
		Query query = pm.newQuery(TimezoneJDO.class, "country == '"+country+"'&& state == '"+state+"'");
	
		query.setResult ("distinct city");
	
		System.out.println(query.toString());
		try {
		  Collection<?> results = (Collection<?>)query.execute (); 
		  System.out.println(query.execute());
		  if (!results.isEmpty()) {
			  Collection<String> city=new TreeSet<String>();
			  
			  city.addAll((Collection<? extends String>) results);
			  
			  array=new JSONArray();
			  array.addAll(city);
				MCacheService.set(keyString, array);
			  return array;
			  
		  } else {
		    return null;
		  }
		} finally {
		  query.closeAll();
		}
		}
		
	}
}
