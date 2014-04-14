package controllers;

import java.util.ArrayList;
import java.util.List;
import models.User;
import play.mvc.Controller;

public class MyUser extends Controller {
	
	public static void user(){
		renderTemplate("user.html");
	}
	
	public static void create(String name, String age, String mail, String account, String password){
//		System.out.println(name + "/" + age + "/" + mail + "/" + account + "/" + password);
		if( name==null || age==null || mail==null || account==null || password==null ){ //javascript傳過來的值就是沒寫也不會是null
			renderTemplate("create.html");
			return;
		}
		new User(name, Integer.parseInt(age), mail, account, password).save(); //NumberFormatException
		User user = User.find("account", account).first(); //NullPointerException
		String result = "error";
		if(user!=null){
			result = "ok";
			ArrayList al = new ArrayList();
			al.add(user.getIdAsStr()); //傳id
			al.add(result);            //傳ok或error
			renderJSON(al);
		}else {
			renderText(result);
		}
	}
	public static void update(String id){
		if(id==null){
			List<User> userList = User.findAll();
			renderTemplate("update.html", userList);
			return;
		}
		User user = User.findById(id);
		renderTemplate("upForm.html", user);
	}
	public static void upForm(String id, String name, String age, String mail, String account, String password){
//		System.out.println(id + "/" + name + "/" + age + "/" + mail + "/" + account + "/" + password);
		User user = User.findById(id);
		user.setName(name);
		user.setAge(Integer.parseInt(age));
		user.setEmail(mail);
		user.setAccount(account);
		user.setPassword(password);
		user.save();
		update(null);
	}
	public static void delete(String id){
		if(id==null){
			List<User> userList = User.findAll();
			renderTemplate("delete.html", userList);
			return;
		}
		String result = "error";
		User.findById(id).delete();
		if(User.findById(id)==null){
			result = "ok";
		}
		renderText(result);
	}
	public static void list(){
		renderTemplate("list.html");
	}
	public static void getList(){
		List<User> list = User.findAll();
		renderJSON(list);
	}
	
}
