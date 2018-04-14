package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.Setter;

@Entity(name="users")
public class User {
	//프라이머리 키 보통 long으로 한다.
	@Id //프라이머리 키
	@GeneratedValue
	@Setter
	private Long no;
	
	@Setter
	private String id;
	
	@Setter
	private String pw;
	
	@Setter
	private String name;
	
	public User() {}
	
	public User(String id, String pw, String name) {
		this.id = id;
		this.pw = pw;
		this.name = name;
	}
	
	public void update(User updatedUser) {
		this.id = updatedUser.id;
		this.pw = updatedUser.pw;
		this.name = updatedUser.name;
	}
	
	public boolean matchPw(String newPw) {
		if(newPw == null) {
			return false;
		}
		
		return newPw.equals(pw);
	}
}
