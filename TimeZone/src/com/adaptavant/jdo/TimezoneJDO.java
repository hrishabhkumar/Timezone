/**
 * 
 */
package com.adaptavant.jdo;

import javax.jdo.annotations.IdGeneratorStrategy;
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
	private String city;
	
	@Persistent
	private String cityCode;
	
	@Persistent
	private String state;
	
	@Persistent
	private String stateCode;

	@Persistent
	private String country;

	@Persistent
	private String countryCode;
	
	@Persistent
	private String zipCode;
	
	@Persistent
	private String longitude;
	
	@Persistent
	private String latitude;
	
	@Persistent
	private String timeZoneId;
	
	@Persistent
	private String timeZoneName;
	
	@Persistent
	private long rawOffset;
	
	@Persistent
	private int dstOffset;
	
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
	public long getRawOffset() {
		return rawOffset;
	}


	/**
	 * @param rawOffset the rawOffset to set
	 */
	public void setRawOffset(long rawOffset) {
		this.rawOffset = rawOffset;
	}


	/**
	 * @return the dstOffset
	 */
	public int getDstOffset() {
		return dstOffset;
	}


	/**
	 * @param dstOffset the dstOffset to set
	 */
	public void setDstOffset(int dstOffset) {
		this.dstOffset = dstOffset;
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


	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}


	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}


	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	/**
	 * @return the cityCode
	 */
	public String getCityCode() {
		return cityCode;
	}


	/**
	 * @param cityCode the cityCode to set
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}


	/**
	 * @return the stateCode
	 */
	public String getStateCode() {
		return stateCode;
	}


	/**
	 * @param stateCode the stateCode to set
	 */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

}
