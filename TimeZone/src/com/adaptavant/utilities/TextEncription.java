/**
 * 
 */
package com.adaptavant.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Hrishabh.Kumar
 *
 */
public class TextEncription {
	/**
	 * 
	 * @param input
	 * @return encrypted text
	 * this method is used to encrypt text using SHA1 algorithm.
	 */
	public static String sha1(String input){
        MessageDigest mDigest;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    
	} catch (NoSuchAlgorithmException e) {
		return null;
	}
		
	}
}
