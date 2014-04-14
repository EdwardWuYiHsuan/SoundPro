package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Comment;
import models.Music;
import models.User;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Application extends Controller {

    public static void index() {
        render();
    }
    public static void signup(String name, String age, String mail, String account, String password){
    	System.out.println("name : " + name + "age : " + age +mail + account + password);
    	if(name==null||age==null||account==null||password==null){
    		render("signup.html");  //這邊可以連結到loginpage
    	}else{
			if(User.find("account", account).first()!=null){
				renderText("your input account already in use!!");
			}else if(User.find("password", password).first()!=null){
				renderText("your input password already in use!!");
			}else{
				int a = Integer.parseInt(age);
				User user = new User(name, a, mail, account, password);
	    		user.save();
	    		renderText("ok");
			}
    		
		}
    }
    public static void login(String account, String password){
//    	System.out.println("##account : " + account + "/password : " + password);
    	if( account!=null && password!=null ){
    		User user = User.find("account", account).first(); //連帳號都輸入錯誤, 就會沒找到user就會null
			try{
	    		if(user.getPassword().equals(password)){
	    			session.put("account", account);
	    			renderJSON(true);
	    		}else{
	    			renderText("your account and password incorrect");
	    		}
			}catch(NullPointerException e){
				renderText("your account and password incorrect");
			}
    	}else{
    		index();
    	}
    }
    public static void loginpage(){
    	String userSession = session.get("account");
    	if(userSession!=null){
    		User user = User.find("account", userSession).first();
        	String userName = user.getName();
    		renderTemplate("loginpage.html", userSession, userName);
    	}else{
    		index();
    	}
    }
    public static void logout(){
    	session.remove("account");
//    	System.out.println("##logout remove session : " + session.get("account"));
    	index();
    }
    public static void parseJsonSav(String json, int num, String year, String place, String act){
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
    public static void showTagFriends(String videoId){  //做tag的顯示
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
    	renderTemplate("showTagFriends.html", userList, res, musicTagFriField); //taginFriend
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
    public static void cancelTagFriend(String videoId, String tagFriId){
    	Music music = Music.find("v_id", videoId).first();
    	boolean b = false;
    	b = music.taggedFriends.remove(User.findById(tagFriId));
    	music.save();
    	renderJSON(b);
    }
    public static void commentPage(String videoId){
    	Music music = Music.find("v_id", videoId).first();
    	List<Comment> commentList = music.message;
    	renderTemplate("commentPage.html", commentList); //只有屬性message這筆陣列為一筆資料
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