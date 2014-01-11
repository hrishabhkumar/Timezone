/**
 * 
 */
package com.adaptavant.timezone.services;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Hrishabh.Kumar
 *
 */
public class SendEmail {
	
	
	static Properties props=new Properties();
	static Session session = Session.getInstance(props, null);
	/**
	 * 
	 * @param recipientEmail
	 * @param tempPass
	 * this method will used to send mail with recovery URL.
	 */
	public static void email(String recipientEmail, String tempPass){
			
		try {  
			MimeMessage message = new MimeMessage(session);  
			message.setFrom(new InternetAddress("hrishabh.kumar@a-cti.com", "Timezone-Adaptavant"));
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipientEmail));  
			message.setSubject("recovery Password from Tomezone-Adaptavant");  
			String str="Click Here";
			String msg="<h1>Hello "+recipientEmail+"</h1><br/>"+"<h2>You have requested to reset your password at Timezone-Adaptavsant.</h2><br/>"+
					"Your Login Details: <br/><br/>"+"<b>User Name:</b> "+ recipientEmail +"\n\n"+"<br/><a href='timezone-adaptavant.appspot.com/recover?userid="+recipientEmail+"&tpass="+tempPass+"'>"+str+"</a> to reset your password <br/><br/>";
			message.setContent(msg,"text/html; charset=utf-8");
			//send message  
			Transport.send(message);  
			  
			System.out.println("message sent successfully");  
			   
		} 
		catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
