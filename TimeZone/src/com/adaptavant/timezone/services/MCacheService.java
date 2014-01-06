/**
 * 
 */
package com.adaptavant.timezone.services;

/**
 * @author Hrishabh.Kumar
 *
 */
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

public class MCacheService {

	private static final Logger logger = Logger.getLogger(MCacheService.class.getName());
	private static Cache _defaultInstance;

	private MCacheService (){}

	private static Cache getCacheInstance(){
		if(_defaultInstance==null)
			_defaultInstance = getCacheInstance(Collections.emptyMap());
		return _defaultInstance;
	}

	private static Cache getCacheInstance(Map<Object, Object> config){
		try {
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			return cacheFactory.createCache(config);
		} catch (CacheException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			return null;
		}
	}
	public static boolean set(String key,Object obj) {
		Cache cache= getCacheInstance();
		if(cache==null)
			return false;
		cache.put(key, obj);
		return true;

	}

	public static Object get(String key) {
		Cache cache= getCacheInstance();
		if(cache==null)
			return null;
		return cache.get(key);
	}
	public static void remove(String key){
		Cache cache= getCacheInstance();
		if(cache==null)
			return;
		cache.remove(key);
	}

	public static boolean containsKey(String key){
		Cache cache= getCacheInstance();
		if(cache==null)
			return false;
		return cache.containsKey(key);
	}
	
	public static boolean removeAll(){
		Cache cache= getCacheInstance();
		cache.remove(cache);
		System.out.println("cache removed");
		return true;
	}

}

