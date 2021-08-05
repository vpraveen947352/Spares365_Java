package com.stigentech.spares365.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.stigentech.spares365.model.User;
import com.stigentech.spares365.repository.Spares365Repository;


@Service
@Transactional
public class Spares365Service
{
	@Autowired
	private Spares365Repository repo;
	
	public User saveUser(User user)
	{
		return repo.save(user);
	}
	
	public User findUserByEmail(String email)
	{
		return repo.findByEmail(email);
	}
	
	public User findUserByEmailAndPassword(String email, String password)
	{
		return repo.findByEmailAndPassword(email, password);
	}
	
	public String updateResetPasswordToken(String token, String email)
	{
        User user = repo.findByEmail(email);
        if (user != null) {
        	user.setResetPasswordToken(token);
        	repo.save(user);
        	return "Updated Reset Password Token";
        } else {
            return "Could not find any user with the email " + email;
        }
    }
	
	public User getByResetPasswordToken(String token) {
        return repo.findByResetPasswordToken(token);
    }
     
    public void updatePassword(User user, String newPassword) {
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newPassword); //encodedPassword
         
        user.setResetPasswordToken(null);
        repo.save(user);
    }
}
