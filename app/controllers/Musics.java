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
	
	public static void add(String json, int num, String year, String place, String act){
    	// obect.items[0].snippet.title
    	JsonElement jelement = new JsonParser().parse(json);
        JsonObject  jobject = jelement.getAsJsonObject();        //找到最上層的object
        JsonArray jarray = jobject.getAsJsonArray("items");     //因為items是一個陣列,需要JosnArray
    	jobject = jarray.get(num).getAsJsonObject();            //找到items這個物件, 編號為num的元素
        //============找id==================
        JsonObject idobject = jobject.getAsJsonObject("id");
        String v_id = idobject.get("videoId").toString().replaceAll("\"", "");
        System.out.println("tarce v_id parse json have : " + v_id); //測試是否有雙引號(是的), 因為parse json會有雙引號, 所以連同雙引號也存入mongodb
    	//============找title, description================
    	JsonObject snippet = jobject.getAsJsonObject("snippet");
        String title = snippet.get("title").toString();
        String description = snippet.get("description").toString();
        //做日期的轉換
        year = year + "/01/01"; 
        DateFormat df = new SimpleDateFormat("yyyy/mm/DD");
        Date d = new Date();
        try{
        	d = df.parse(year);
        }catch(Exception e){
        	System.out.println("exception message :" + e.getMessage());
        }
        User user = User.find("account", act).first(); //存在音樂裡的user id
//        System.out.println("=>check before save : "+v_id+"&"+title+"&"+description+"&"+d+"&"+place+"&"+user.getIdAsStr()+"@@@");
        String result = "save error";
        if(Music.find("v_id", v_id).first()==null){
        	Music music = new Music(v_id, title, description, d, place, user.getIdAsStr()); //line49~52是使用@embedded的關鍵,沒元素資料庫就沒有embedded欄位
        	music.save();
        	result = "ok"; 
        }else{
        	result = "already have the same video!!";
        }
        renderText(result);
    }
	public static void getlist(String userAccount, int strNum, int endNum){
//    	System.out.println("userAccount: " + userAccount + "/strNum:"+ strNum + "/endNum:" + endNum); 
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
