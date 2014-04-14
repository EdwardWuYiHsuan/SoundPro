package models;

import play.modules.morphia.*;

import com.google.code.morphia.annotations.*;

@Entity
public class User extends Model {
	
	String name;
	int age;
	String email;
	String account;
	String password;
	
	public User(String name, int age, String email, String account, String password){
		this.name = name;
		this.age = age;
		this.email = email;
		this.account = account;
		this.password = password;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setAge(int age){
		this.age = age;
	}
	public int getAge(){
		return age;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public String getEmail(){
		return email;
	}
	public void setAccount(String account){
		this.account = account;
	}
	public String getAccount(){
		return account;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getPassword(){
		return password;
	}


}
