package com.adaptavant.timezone.converter;

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
import com.adaptavant.jdo.TimezoneJDO;
import com.adaptavant.utilities.MCacheService;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.datanucleus.query.JDOCursorHelper;


public class TimezoneListProvider {
	Logger logger=Logger.getLogger(TimezoneListProvider.class.getName());
	@SuppressWarnings("unchecked")
	public JSONObject getTimezoneConvertorData(int limit, String cursorString) {
		logger.info("inside timezone converter list "+limit);
		Query query=null;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		logger.info("inside timezone converter list "+limit);
		String keyString="tempKeyString";
		String tempKeyString="tempKeyString1";
		if(MCacheService.containsKey(keyString)){
			
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
				for (TimezoneJDO timezoneJDO: results) {
					timezoneName.put(timezoneJDO.getTimeZoneId(), timezoneJDO.getRawOffset());
					timezoneName.put(timezoneJDO.getState(), timezoneJDO.getRawOffset());
					timezoneName.put(timezoneJDO.getCountry(), timezoneJDO.getRawOffset());
				}
				if(MCacheService.containsKey(tempKeyString)){
					timezoneName.putAll((Map<? extends String, ? extends Long>) MCacheService.get(tempKeyString));
				}
				MCacheService.set(tempKeyString, timezoneName);
				if(results.size()==limit){
					Queue queue = QueueFactory.getQueue("subscription-queue");
					queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("cursorString", cursorString).param("list", "converterData"));
					return null;
				}
				else{
					Map<String, Long> timezoneDataMap=(Map<String, Long>) MCacheService.get(tempKeyString);
					JSONArray timezoneDataArray=new JSONArray();
					JSONObject timezoneDataJson;
					Set<String> keys=timezoneDataMap.keySet();
					for(String key: keys){
						timezoneDataJson=new JSONObject();
						timezoneDataJson.put("value", timezoneDataMap.get(key));
						timezoneDataJson.put("label", key);
						timezoneDataArray.add(timezoneDataJson);
					}
					logger.log(Level.INFO,"Updating Memcache with timezone List");
					timezoneDataJson=new JSONObject();
					timezoneDataJson.put("list", timezoneDataArray);
					MCacheService.set(keyString, timezoneDataJson);
					logger.log(Level.INFO,"Updating Memcache with timezone converter List");
					MCacheService.remove(tempKeyString);
					return timezoneDataJson;
				}
			}
			else{
				Map<String, Long> timezoneDataMap=(Map<String, Long>) MCacheService.get(tempKeyString);
				JSONArray timezoneDataArray=new JSONArray();
				JSONObject timezoneDataJson;
				Set<String> keys=timezoneDataMap.keySet();
				for(String key: keys){
					timezoneDataJson=new JSONObject();
					timezoneDataJson.put("value", timezoneDataMap.get(key));
					timezoneDataJson.put("label", key);
					timezoneDataArray.add(timezoneDataJson);
				}
				logger.log(Level.INFO,"Updating Memcache with timezone convertor List");
				timezoneDataJson=new JSONObject();
				timezoneDataJson.put("list", timezoneDataArray);
				MCacheService.set(keyString, timezoneDataJson);
				MCacheService.remove(tempKeyString);
				return timezoneDataJson;
			}
		}finally{
			query.closeAll();
		}
		}
		}	
	
	  	
}
