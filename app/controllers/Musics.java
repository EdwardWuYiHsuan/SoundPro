package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.Music;
import models.User;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;

public class Musics extends Controller {
	
	public static void add(String videoId, String title, String description, String year, String place) {
		//做日期的轉換
        year = year + "/01/01"; 
        DateFormat df = new SimpleDateFormat("yyyy/mm/DD");
        Date date = new Date();
        try{
        	date = df.parse(year);
        }catch(Exception e){
        	System.out.println("exception message :" + e.getMessage());
        }
        String userAccount = session.get("account");
        User user = User.find("account", userAccount).first(); //存在音樂裡的user id
        String result = "save error";
        if(Music.find("videoId", videoId).first()==null){
//        	System.out.println("=>check before save : "+v_id+"&"+title+"&"+description+"&"+date+"&"+place+"&"+user.getIdAsStr()+"@@@");
        	Music music = new Music(videoId, title, description, date, place, user.getIdAsStr()); //line49~52是使用@embedded的關鍵,沒元素資料庫就沒有embedded欄位
        	music.save();
        	result = "ok"; 
        }else{
        	result = "already have the same video!!";
        }
        renderText(result);
	}
	
	public static void getlist(int strNum, int endNum) {
//    	System.out.println("strNum:"+ strNum + "/endNum:" + endNum); 
		String userAccount = session.get("account");
    	User user = User.find("account", userAccount).first();
    	String userId = user.getIdAsStr();
    	MorphiaQuery q = Music.q();
    	q.filter("userId", userId).order("-year, place"); //-為遞減(大到小)
    	List<Music> musicListByUserid = q.asList();
//    	List<Music> musicListByUserid = Music.q().order("-year").findBy("userId", userId).asList(); //省的寫法
    	if(endNum <= musicListByUserid.size()){
    		musicListByUserid = musicListByUserid.subList(strNum, endNum);  //0~3跟substr一樣
    		renderJSON(musicListByUserid);
    	}else if(strNum <= musicListByUserid.size()){
    		endNum = musicListByUserid.size();
    		musicListByUserid = musicListByUserid.subList(strNum, endNum); 
    		renderJSON(musicListByUserid);
    	}else{
    		renderJSON(false);
    	}
    }
}
