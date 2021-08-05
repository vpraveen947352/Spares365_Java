package com.stigentech.spares365.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "RegistrationDB")
public class User
{
	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	@Column(name = "id")
//	private int id;
	@Column(name = "email")
	private String email;
	@Column(name = "firstName")
	private String firstName;
	@Column(name = "lastName")
	private String lastName;
	@Column(name = "phone")
	private long phone;
	@Column(name = "password")
	private String password;
	@Column(name = "date_registered")
	private String datetime;
	@Column(name = "reset_password_token")
    private String resetPasswordToken;
	
	public User()
	{
		
	}
	
	public User(String email, String firstName, String lastName, long phone, String password, String datetime, String resetPasswordToken) {
		super();
		//this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.password = password;
		this.datetime = datetime;
		this.resetPasswordToken = resetPasswordToken;
	}
	
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getDatetime() {
		return datetime;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}


		
	}


