package com.revature.Tapestry.beans;

import javax.persistence.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "USER_TABLE")
public class User {
	private Integer userID;
	private String username;
	private String email;
	private String password;
	public User () {}
	public User( String username, String email, String role, String password) {
		super();
		this.username = username;
		this.email = email;
		this.role = role;
		this.encryptPassword(password);
	}
	
	public User(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = encryptPassword(password);
	}
	private String role;
	
	@Id
	@GeneratedValue
	@Column
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	@Column
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String encryptPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}
	@Column
	private String getPassword()
	{
		return password;
	}
	
	private void setPassword(String password){
		this.password=password;
	}
	
	public void nullPassword()
	{
		this.password=null;
	}
	
	public boolean isCorrectPassword(String passwordToCheck) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(passwordToCheck, this.getPassword());
	}
}
