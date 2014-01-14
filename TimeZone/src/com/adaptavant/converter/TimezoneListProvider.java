package com.adaptavant.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.adaptavant.timezone.services.MCacheService;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.datanucleus.query.JDOCursorHelper;


public class TimezoneListProvider {
	Logger logger=Logger.getLogger(TimezoneListProvider.class.getName());
	@SuppressWarnings("unchecked")
	public JSONObject getTimezoneIDList(int limit, String cursorString) {
		logger.info("inside timezone id list "+limit);
		Query query=null;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		logger.info("inside timezone id list "+limit);
		String keyString="getTimeZoneID";
		
		if(MCacheService.containsKey(keyString)){
			System.out.println(MCacheService.get(keyString));
			return (JSONObject) MCacheService.get(keyString);
		}
		else{
			try{
			query=pm.newQuery(TimezoneJDO.class);
			query.setRange(0, limit);
			logger.info(query.toString());
			if(cursorString!=null){
				Cursor cursor=Cursor.fromWebSafeString(cursorString);
				Map<String,Object> extnMap=new HashMap<String, Object>();
				extnMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extnMap);
			}
			List<TimezoneJDO> results = (List<TimezoneJDO>)query.execute ();
			Cursor cursor = JDOCursorHelper.getCursor(results);
			cursorString=cursor.toWebSafeString();
			if (!results.isEmpty()) {
				Map<String, Long> timezoneName=new TreeMap<String, Long>();
				for (TimezoneJDO tj: results) {
					timezoneName.put(tj.getTimeZoneId(), tj.getRawOffset());
				}
				System.out.println(timezoneName);
				if(MCacheService.get("getTimeZoneID1")!=null){
					timezoneName.putAll((Map<? extends String, ? extends Long>) MCacheService.get("getTimeZoneID1"));
				}
				MCacheService.set("getTimeZoneID1", timezoneName);
				if(results.size()==limit){
					Queue queue = QueueFactory.getQueue("subscription-queue");
					queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("cursorString", cursorString).param("list", "timezoneID"));
					return null;
				}
				else{
					Map<String, Long> data=(Map<String, Long>) MCacheService.get("getTimeZoneID1");
					JSONArray array=new JSONArray();
					JSONObject obj;
					Set<String> keys=data.keySet();
					for(String key: keys){
						obj=new JSONObject();
						obj.put("desc", key);
						obj.put("value", data.get(key));
						obj.put("label", key.substring(key.indexOf("/")+1, key.length()));
						array.add(obj);
					}
					logger.log(Level.INFO,"Updating Memcache with timezone List");
					obj=new JSONObject();
					obj.put("list", array);
					System.out.println(obj.toJSONString());
					MCacheService.set(keyString, obj);
					MCacheService.remove("getTimeZoneID1");
					return obj;
				}
			}
			else{
				Map<String, Long> data=(Map<String, Long>) MCacheService.get("getTimeZoneID1");
				JSONArray array=new JSONArray();
				JSONObject obj;
				Set<String> keys=data.keySet();
				for(String key: keys){
					obj=new JSONObject();
					obj.put("desc", key);
					obj.put("value", data.get(key));
					obj.put("label", key.substring(key.indexOf("/")+1, key.length()));
					array.add(obj);
				}
				logger.log(Level.INFO,"Updating Memcache with timezone List");
				obj=new JSONObject();
				obj.put("list", array);
				MCacheService.set(keyString, obj);
				MCacheService.remove("getTimeZoneID1");
				return obj;
			}
		}finally{
			query.closeAll();
		}
		}
		}	
	
	  	
}
