package controllers;

import models.User;
import play.mvc.Controller;

public class Account extends Controller {
	
	public static void showPage() {
        renderTemplate("login.html");
    }	
	public static void signup(String name, String age, String mail, String account, String password){
//    	System.out.println("name : " + name + "/age : " + age + "/mail : " + mail + "/account : " + account + "/password : " + password);
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
    		showPage();
    	}
    }
    public static void loginSuccessful(){
    	String userSession = session.get("account");
    	if(userSession!=null){
    		User user = User.find("account", userSession).first();
        	String userName = user.getName();
    		renderTemplate("timeline.html", userSession, userName);
    	}else{
    		showPage();
    	}
    }
    public static void logout(){
    	session.remove("account");
//    	System.out.println("##logout remove session : " + session.get("account"));
    	showPage();
    }

}
