package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Music;
import models.User;
import play.mvc.Controller;

public class Tag extends Controller {

	public static void list(String videoId){  //做tag的顯示
    	List<User> userList = User.findAll();
    	Music music = Music.find("v_id", videoId).first();
    	List<User> musicTagFriField = music.taggedFriends; //現在可以存null了不會有Exception(本來就可以存的, 所以移到外面用)
    	String[] res;
    	try{  
    		//System.out.println("重要的一行, 早點讓他進入catch裡, 是不是空的 : " + musicTagFriField.isEmpty());
    		for(User musicEle:musicTagFriField){ 
    			userList.remove(musicEle);               //為什麼我去override User的equals方法不行(幹, 可能是不同物件字串對music)
    		}
    		res = new String[]{videoId, ""};
    	}catch(NullPointerException e){
    		System.out.println("showTagFriends function catch exception");
    		res=new String[]{videoId, "selcet list can tag friends!!"};
    	}
    	renderTemplate("fragments/showTagFriends.html", userList, res, musicTagFriField); //taginFriend
    }
    public static void tagFriend(String videoId, String nameId){ //做tag的儲存
    	Music music = Music.find("v_id", videoId).first();
    	User user = User.findById(nameId); //要存入在taggedFriends集合裡的是一個User物件, 而傳過來的是user id, 建立物件存入
    	boolean b = false;
    	try{
    		b = music.taggedFriends.add(user);
    	}catch(NullPointerException e){ //當還沒有taggedFriends欄位時, 會是null
    		System.out.println("tagFriend function catch exception");
    		music.taggedFriends = new ArrayList<User>();
    		b = music.taggedFriends.add(user);
    	}
    	music.save();
    	renderJSON(b);
    }
    public static void remove(String videoId, String tagFriId){
    	Music music = Music.find("v_id", videoId).first();
    	boolean b = false;
    	b = music.taggedFriends.remove(User.findById(tagFriId));
    	music.save();
    	renderJSON(b);
    }
}
