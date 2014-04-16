package models;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;

@Entity
//@Indexes({@Index("-year")})
public class Music extends Model {
	@Id
	public String videoId;
	public String title;
	public String description;
	//@Indexed(value=IndexDirection.ASC, name="year") 
	public Date year;
	public String place;
	public String userId;
	
	@Embedded 
	public List<User> taggedFriends; 
	
	@Embedded
	public List<Comment> comments;
	
	public Music(String videoId, String title, String description, Date year, String place, String userId){
		this.videoId = videoId;
		this.title = title;
		this.description = description;
		this.year = year;
		this.place = place;
		this.userId = userId;
		this.taggedFriends = new ArrayList<User>();
		this.comments = new ArrayList<Comment>();
	} 
	public void setv_id(String videoId){
		this.videoId = videoId;
	}
	public String getv_id(){
		return videoId;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return title;
	}
	public void setDes(String description){
		this.description = description;
	}
	public String getDes(){
		return description;
	}
	public void setYear(Date year){
		this.year = year;
	}
	public Date getYear(){
		return year;
	}
	public void setPlace(String place){
		this.place = place;
	}
	public String getPlace(){
		return place;
	}
	public void setUserId(String id){
		this.userId = id;
	}
	public String getUserId(){
		return userId;
	}
	
}

