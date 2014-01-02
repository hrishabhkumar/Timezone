
package com.adaptavant.useractivity.list;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.adaptavant.jdo.PMF;
import com.adaptavant.jdo.timezone.TimezoneJDO;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

public class GetCursor {
	StringBuilder sb=new StringBuilder();
	String str;
	public String getCountryList(String cursorString, int range){
		PersistenceManager pm = PMF.getPMF().getPersistenceManager();
		Query query = pm.newQuery(TimezoneJDO.class);
		query.setRange(0, range);
		@SuppressWarnings("unchecked")
		Cursor cursor1=JDOCursorHelper.getCursor((List<TimezoneJDO>)query.execute());
		
		if(cursorString!=null){
			Cursor cursor=Cursor.fromWebSafeString(cursorString);
			Map<String,Object> extnMap=new HashMap<String, Object>();
			extnMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			query.setExtensions(extnMap);
		}
		
		System.out.println(query.toString());
		try {
			List<?> results = (List<?>)query.execute ();
			System.out.println(query.execute());
			if (!results.isEmpty()) {
				Iterator<?> iter = results.iterator();
				
				while (iter.hasNext()) {
					String country = (String) iter.next();
					country=country.replace('\"', ' ');
					sb=sb.append("{\"c_name\":\""+country+"\"},");
				}
				
				cursorString=cursor1.toWebSafeString();
				
				while(cursorString!=null){
					getCountryList(cursorString, 1000);
				}
				str=sb.toString();
				return str;			  
			
				
			} 
			else {
				cursorString=null;
				return null;
			}
		}finally{
			query.closeAll();
			pm.close();
		}
	}
}