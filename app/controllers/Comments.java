package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Comment;
import models.Music;
import play.mvc.Controller;

public class Comments extends Controller {

	public static void commentPage(String videoId){
    	Music music = Music.find("v_id", videoId).first();
    	List<Comment> commentList = music.message;
    	renderTemplate("fragments/comments.html", commentList); //只有屬性message這筆陣列為一筆資料
    }
    public static void commentSave(String id, String name, String content){
    	Music music = Music.find("v_id", id).first();
    	Comment com = new Comment(name, content);
    	if(music.message == null) {
    		music.message = new ArrayList<Comment>();
    	}
    	music.message.add(com);
    	music.save();
    	renderText(name+" say:<br>"+content);
	}
}
