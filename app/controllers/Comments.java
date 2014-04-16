package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Comment;
import models.Music;
import play.mvc.Controller;

public class Comments extends Controller {

	public static void showPage(String videoId){
    	Music music = Music.find("videoId", videoId).first();
    	List<Comment> commentList = music.comments;
    	renderTemplate("fragments/comments.html", commentList); //只有屬性message這筆陣列為一筆資料
    }
    public static void add(String videoId, String name, String content){
    	Music music = Music.find("videoId", videoId).first();
    	Comment com = new Comment(name, content);
    	if(music.comments == null) {
    		music.comments = new ArrayList<Comment>();
    	}
    	music.comments.add(com);
    	music.save();
    	renderText(name+" say:<br>"+content);
	}
}
