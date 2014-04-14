package models;

import java.util.Date;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class Comment {
	String senderName;
	String senderContent;
	
	public Comment(String senderName, String senderContent){
		this.senderName = senderName;
		this.senderContent = senderContent;
	}
}
