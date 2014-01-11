/**
 * 
 */
package com.adaptavant.timezone.services;

/**
 * @author Hrishabh.Kumar
 *
 */
import java.util.Collections;
import java.util.Map;
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
	/**
	 * 
	 * @return cache Instance
	 */
	private static Cache getCacheInstance(){
		if(_defaultInstance==null)
			_defaultInstance = getCacheInstance(Collections.emptyMap());
		return _defaultInstance;
	}
	/**
	 * 
	 * @param config(Map)
	 * @return Cache instance
	 */
	private static Cache getCacheInstance(Map<Object, Object> config){
		try {
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			return cacheFactory.createCache(config);
		} catch (CacheException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			return null;
		}
	}
	/**
	 * 
	 * @param key
	 * @param obj
	 * @return true/false
	 * this method is used to put a key-value pair in memCache
	 */
	public static boolean set(String key,Object obj) {
		Cache cache= getCacheInstance();
		if(cache==null)
			return false;
		cache.put(key, obj);
		return true;

	}
	/**
	 * 
	 * @param key
	 * @return value from memCache
	 * this method is used to get value from memCache.
	 */
	public static Object get(String key) {
		Cache cache= getCacheInstance();
		if(cache==null)
			return null;
		return cache.get(key);
	}
	/**
	 * 
	 * @param key
	 * this method is used to remove a key-value pair from memCache.
	 */
	public static void remove(String key){
		Cache cache= getCacheInstance();
		if(cache==null)
			return;
		cache.remove(key);
	}
	/**
	 * 
	 * @param key
	 * @return true/false
	 * this method is used to check whether key available in memCache or not.
	 */
	public static boolean containsKey(String key){
		Cache cache= getCacheInstance();
		if(cache==null)
			return false;
		return cache.containsKey(key);
	}
	/**
	 * 
	 * this method is to clear memCache.
	 */
	public static void removeAll(){
		Cache cache= getCacheInstance();
		cache.clear();
		System.out.println("cache removed");
		return ;
	}

}

