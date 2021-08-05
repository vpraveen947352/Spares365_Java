package com.stigentech.spares365.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stigentech.spares365.model.User;

@Repository
public interface Spares365Repository extends JpaRepository<User, String>
{
	public User findByEmail(String email);
	public User findByEmailAndPassword(String email, String password);    
	public User findByResetPasswordToken(String resetPasswordToken);
	//public User findByEmailAndOtp(String email, Integer otp);
}
