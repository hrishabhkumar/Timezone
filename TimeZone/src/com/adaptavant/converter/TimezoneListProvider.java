package com.adaptavant.converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
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
	public String getTimezoneIDList(int limit, String cursorString) {
		logger.info("inside timezone id list "+limit);
		Query query=null;
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		logger.info("inside timezone id list "+limit);
		String keyString="getTimeZoneID";
		
		if(MCacheService.containsKey(keyString)){
			return (String) MCacheService.get(keyString);
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
				Collection<String> timezoneName=new TreeSet<String>();
				for (TimezoneJDO tj: results) {
					timezoneName.add(tj.getTimeZoneId());
				}
				System.out.println(timezoneName);
				if(MCacheService.get("getTimeZoneID1")!=null){
					timezoneName.addAll((TreeSet<String>) MCacheService.get("getTimeZoneID1"));
				}
				MCacheService.set("getTimeZoneID1", timezoneName);
				if(results.size()==limit){
					Queue queue = QueueFactory.getQueue("subscription-queue");
					queue.add(TaskOptions.Builder.withUrl("/list").param("limit", "1000").param("cursorString", cursorString).param("list", "timezoneID"));
					return null;
				}
				else{
					Collection<String> data=(TreeSet<String>) MCacheService.get("getTimeZoneID1");
					JSONArray array=new JSONArray();
					array.addAll(data);
					JSONObject obj=new JSONObject();
					obj.put("list", array);
					logger.log(Level.INFO,"Updating Memcache with timezone List");
					MCacheService.set(keyString, obj.toJSONString());
					MCacheService.remove("getTimeZoneID1");
					return array.toJSONString();
				}
			}
			else{
				Collection<String> data=(TreeSet<String>) MCacheService.get("getTimeZoneID1");
				JSONArray array=new JSONArray();
				array.addAll(data);
				JSONObject obj=new JSONObject();
				obj.put("list", array);
				logger.log(Level.INFO,"Updating Memcache with timezone List");
				MCacheService.set(keyString, obj.toJSONString());
				MCacheService.remove("getTimeZoneID1");
				return obj.toJSONString();
			}
		}finally{
			query.closeAll();
		}
		}
		
	}

}
