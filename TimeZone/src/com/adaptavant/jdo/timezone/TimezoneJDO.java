/**
 * 
 */
package com.adaptavant.jdo.timezone;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * @author Hrishabh.Kumar
 *
 */
@PersistenceCapable
public class TimezoneJDO {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String timeZoneId;
	
	@Persistent
	private String timeZoneName;
	
	@Persistent
	private String rawOffset;
	
	@Persistent
	private String dstOffset;
	
	@Persistent
	private String state;
	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String city;
	
	@Persistent
	@Index
	private String country;
	
	@Persistent
	private String longitude;
	
	@Persistent
	private String latitude;
	
	
	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}


	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}
	


	/**
	 * @return the timeZoneId
	 */
	public String getTimeZoneId() {
		return timeZoneId;
	}


	/**
	 * @param timeZoneId the timeZoneId to set
	 */
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}


	/**
	 * @return the timeZoneName
	 */
	public String getTimeZoneName() {
		return timeZoneName;
	}


	/**
	 * @param timeZoneName the timeZoneName to set
	 */
	public void setTimeZoneName(String timeZoneName) {
		this.timeZoneName = timeZoneName;
	}


	/**
	 * @return the rawOffset
	 */
	public String getRawOffset() {
		return rawOffset;
	}


	/**
	 * @param rawOffset2 the rawOffset to set
	 */
	public void setRawOffset(String rawOffset2) {
		this.rawOffset = rawOffset2;
	}


	/**
	 * @return the dstOffset
	 */
	public String getDstOffset() {
		return dstOffset;
	}


	/**
	 * @param dstOffset2 the dstOffset to set
	 */
	public void setDstOffset(String dstOffset2) {
		this.dstOffset = dstOffset2;
	}


	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}


	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}


	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}


	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}


	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}


	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}


	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}


	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}


	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}


	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}


}
