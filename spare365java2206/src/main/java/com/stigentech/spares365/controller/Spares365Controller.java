package com.stigentech.spares365.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stigentech.spares365.model.Product;
import com.stigentech.spares365.model.User;
import com.stigentech.spares365.service.MailService;
import com.stigentech.spares365.service.ProductService;
import com.stigentech.spares365.service.Spares365Service;
import com.sun.mail.imap.Utility;

import net.bytebuddy.utility.*;
import net.bytebuddy.utility.RandomString;

@RestController
@CrossOrigin(origins="*") //"http://spares365.s3-website.us-east-2.amazonaws.com"
public class Spares365Controller
{
	@Autowired
	private Spares365Service service;

	@Autowired
	private ProductService productService;

	@Autowired
	private JavaMailSender mailSender;

	@PostMapping("/registeruser")
	public LinkedHashMap<String, Object> registerUser(@RequestBody User user) throws Exception
	{
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

		String tempEmail = user.getEmail();
		if(tempEmail != null && !"".equals(tempEmail))
		{
			User userObj = service.findUserByEmail(tempEmail);
			if(userObj != null)
			{
				//throw new Exception("User with "+tempEmail+" already exists.");
				response.put("Status", "0");
				response.put("Message", "User with "+tempEmail+" already exists.");
				return response;
			}
		}
		User userObj = user;
		//Date Time
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

		String formattedDate = myDateObj.format(myFormatObj);

		userObj.setDatetime(formattedDate);
		userObj = service.saveUser(userObj);

		if(userObj!= null)
		{
			response.put("Status", "1");
			response.put("Message", "User registered successfully.");
		}

		return response;
	}

	@PostMapping("/login")
	public LinkedHashMap<String, Object> loginUser(@RequestBody User user) throws Exception
	{
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

		String tempEmail = user.getEmail();
		String tempPass = user.getPassword();

		User userObj = null;
		if(tempEmail!=null && tempPass!=null)
		{
			userObj = service.findUserByEmailAndPassword(tempEmail, tempPass);
		}

		if(userObj == null)
		{
			//throw new Exception("Wrong username or password");
			response.put("Status", "0");
			response.put("Message", "Wrong username or password.");
			return response;
		}

		response.put("Status", "1");
		response.put("Message", "Login Successful.");

		return response;
	}

	@Autowired
	private MailService mailService;

	@PostMapping("/validate")
	public String validateOTP(@RequestBody User user) throws Exception
	{	
		int otp = 0;
		if(service.findUserByEmail(user.getEmail()) == null)
		{
			Random rand = new Random();
			otp = rand.nextInt(999999 - 100000) + 100000;
			mailService.send(user.getEmail(), "spares365.stigentech@gmail.com", "OTP", String.valueOf(otp));
		}
		else
		{
			return "Email already exists.";
		}

		return String.valueOf(otp);
	}

	@PostMapping(value = "/addproduct")
	public LinkedHashMap<String, Object> addProduct(@RequestBody Product product)
	{
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

		Product prod = productService.saveProduct(product);
		if(prod!=null)
		{
			response.put("Status", "1");
			response.put("Message", "Product Added Successfully.");
		}
		else
		{
			response.clear();
			response.put("Status", "0");
			response.put("Message", "Failed to add Product.");
			return response;
		}
		return response;
	}

	@GetMapping("/findallproducts")
	public LinkedHashMap<String, Object> findAllProducts()
	{
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

		List<Product> retrievedProducts = productService.findAllProducts();

		response.put("Status", "1");
		response.put("Message", "Retrieved all products successfully.");
		response.put("ResultSet", retrievedProducts);

		return response;
	}

	@GetMapping("/products/{id}")
	public LinkedHashMap<String, Object> findProductById(@PathVariable("id") int id)
	{
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

		Product prod = productService.findById(id);

		if(prod!=null)
		{
			response.put("Status", "1");
			response.put("Message", "Retrieved Product with id "+ id + ".");
			response.put("ResultSet", prod);
		}
		else
		{
			response.put("Status", "0");
			response.put("Message", "Unable to retrieve Product with id "+ id + ".");
			return response;
		}

		return response;
	}

	@GetMapping("/product/{name}")
	public LinkedHashMap<String, Object> findProductByName(@PathVariable("name") String name)
	{
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

		Product prod = productService.findByName(name); 

		if(prod!=null)
		{
			response.put("Status", "1");
			response.put("Message", "Retrieved Product with name "+ name + ".");
			response.put("ResultSet", prod);
		}
		else
		{
			response.put("Status", "0");
			response.put("Message", "Unable to retrieve Product with name "+ name + ".");
			return response;
		}

		return response;
	}

	@GetMapping("/")
	public LinkedHashMap<String, Object> getIndex()
	{
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		//		response.put("Status", "1");
		//		response.put("Message", "200 Ok");

		return response;
	}

	@GetMapping("/forgot_password")
	public String showForgotPasswordForm() {
		return "forgot_password_form";
	}


	@PostMapping("/forgot_password")
	public LinkedHashMap<String, Object> processForgotPassword(@RequestBody User user) throws UnsupportedEncodingException, MessagingException {
		String email = user.getEmail();
		System.out.println("email : " + email);

		String token = RandomString.make(30);
		System.out.println("token : " + token);

		//String siteURL = request.getRequestURI().toString();
		//siteURL = siteURL.replace(request.getServletPath(), "");

		String userExistsOrNot = service.updateResetPasswordToken(token, email);
		System.out.println("userExistsOrNot : "+ userExistsOrNot);
		//String siteURL = "http://spares365.s3-website.us-east-2.amazonaws.com/"; //"http://localhost:4200/"
		//String resetPasswordLink = siteURL + "/reset_password?token=" + token;
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		if(userExistsOrNot.equals("Updated Reset Password Token")){
			String siteURL = "http://localhost:4200/";
			String resetPasswordLink = siteURL + "account/reset_password?token=" + token;
			System.out.println("###### resetPasswordLink : "+ resetPasswordLink);
			sendEmail(email, resetPasswordLink);

			response.put("Status", "1");
			response.put("Message", userExistsOrNot);

			//model.addAttribute("message", "We have sent a reset password link to your email. Please check.");	         
			//	    } catch (Exception e) {
			//	        model.addAttribute("error", ex.getMessage());
			//	    }
		} else {

			response.put("Status", "0");
			response.put("Message", userExistsOrNot);
		}

		return response;
	}

	public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException
	{
		MimeMessage message = mailSender.createMimeMessage();              
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("spares365.stigentech@gmail.com", "Spares365 Support");
		helper.setTo(recipientEmail);

		String subject = "Here's the link to reset your password";

		String content = "<p>Hello,</p>"
				+ "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>"
				+ "<p><a href=\"" + link + "\">Change my password</a></p>"
				+ "<br>"
				+ "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
	}


	@GetMapping("/reset_password")
	public String showResetPasswordForm(@RequestParam(value = "token") String token, Model model) {
		User user = service.getByResetPasswordToken(token);
		model.addAttribute("token", token);

		if (user == null) {
			model.addAttribute("message", "Invalid Token");
			return "message";
		}

		return "reset_password_form";
	}

	@PostMapping("/reset_password")
	public LinkedHashMap<String,String> processResetPassword(@RequestBody User user)
	{
		LinkedHashMap<String,String> response = new LinkedHashMap<String,String>();

		String token = user.getResetPasswordToken();

		String password = user.getPassword();
		//	    String conformPass = user.getPassword();
		user.setPassword(password);

		User tempuser = service.getByResetPasswordToken(token);
		//    user.save(user);
		//	    tempuser.delete(tempuser);
		service.updatePassword(tempuser, password);
		response.put("title", "Reset your password");

		response.put("message", "You have successfully changed your password.");

		return response;
	}

	@PostMapping("/contact/{emailid}/{message}/{Name}/{Subject}")
	public LinkedHashMap<String,Object> userQuery(@PathVariable String emailid, @PathVariable String message,
			@PathVariable String Name, @PathVariable String Subject) throws Exception{

		LinkedHashMap<String,Object> response = new LinkedHashMap<String,Object>();
		String finalMessage = "Hello my name is "+Name +" My email: "+ emailid+" "+"my Subject is "+Subject+" "+"I have the following query "+message;
		mailService.send("spares365.stigentech@gmail.com", "spares365.stigentech@gmail.com", "user query ", finalMessage);
		response.put("Status ", 1);
		response.put("Message ", "Your Query Submitted Successfully");
		return response;
	}

	
/*
 * @GetMapping("/wish") public String wisher() {
 * 
 * return "Hello Ayodhya"; }
 */

	
	  // Added By Ayodhya
	  
	 
	  @GetMapping("/place_Order")
		public String processPlaceOrder(@RequestParam("Email") String Email) throws UnsupportedEncodingException, MessagingException {
			User email = service.findUserByEmail(Email);
//					"prince.francis9154@gmail.com";
			System.out.println(email);
			
		
	sendPlaceOrderMail(Email);

	return "Mail Sent Successfully";
	}

public void sendPlaceOrderMail(String recipientEmail) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
helper.setFrom("spares365.stigentech@gmail.com", "Spares365 Support");
		helper.setTo(recipientEmail);

		String subject = "ORDER PLACED";

		String content = "Your order successfully placed";
		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
	}
}




/*
 * // Added By Ayodhya
 * 
 * @GetMapping("/place_Order") public String processPlaceOrder() throws
 * UnsupportedEncodingException, MessagingException { String email =
 * "uber.ayodhya@gmail.com"; System.out.println(email); String token =
 * RandomString.make(30);
 * 
 * // String siteURL = request.getRequestURI().toString(); // siteURL =
 * siteURL.replace(request.getServletPath(), "");
 * 
 * // try { service.updateResetPasswordToken(token, email); // String
 * resetPasswordLink = siteURL + "/reset_password?token=" + token; String
 * resetPasswordLink = "http://localhost:4200" +
 * "/account/reset_password?token=" + token; sendPlaceOrderMail(email); //
 * model.addAttribute("message", "We have sent a reset password link to your //
 * email. Please check."); // } catch (Exception ex) { //
 * model.addAttribute("error", ex.getMessage()); // }
 * 
 * return "Mail Sent Successfully"; }
 * 
 * public void sendPlaceOrderMail(String recipientEmail) throws
 * MessagingException, UnsupportedEncodingException { MimeMessage message =
 * mailSender.createMimeMessage(); MimeMessageHelper helper = new
 * MimeMessageHelper(message);
 * 
 * helper.setFrom("spares365.stigentech@gmail.com", "Spares365 Support");
 * helper.setTo(recipientEmail);
 * 
 * String subject = "Here's the link to reset your password";
 * 
 * String content = "Text from Spare365"; helper.setSubject(subject);
 * 
 * helper.setText(content, true);
 * 
 * mailSender.send(message); } }
 */