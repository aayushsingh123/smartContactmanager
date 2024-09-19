package com.smartt.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartt.dao.UserRepository;
import com.smartt.entities.User;
import com.smartt.service.EmailService;

@Controller
public class ForgotController {
	Random random = new Random(1000); //1000 se lekar 99999999 ke bitch kaa number milega
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	//email id form handler
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@RequestMapping("/forgot")
	public String openEmailForm() {
		
		
		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam ("email") String email , HttpSession session) {
		
		System.out.println("EMAIL "+email);
		//generating OTP 4 digit
		
		int otp = random.nextInt(99999999);
		System.out.println("OTP "+otp);
		//write code for send otp to email
		
		String subject= "OTP from Ayush incox";
		String message=" "
				+"<div style='border:1px solid #e2e2e2; padding:20px'>"
				+"<h1>"
				+"OTP is"
				+"<b>"+otp
				+"</n>"
				+"</h1> "
				+"</div>";
		String to="email";
		boolean flag = this.emailService.sendEmail(subject, message, to);
		
		if(flag) {
			
			session.setAttribute("myotp", otp);//login hoone ke baad otp ko session mein store kar rahe hai kuch der le liye
			session.setAttribute("email", email);
			return "verify_otp";
		}
		else {
			
		 session.setAttribute("message", "check your email id");
		 return "forgot_email_form";
		}
		
	}

	
	//verify otp
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session 	)//Integer otp--yeah woh otp jo user ne dala hai
{
		
	     int myOtp=(int)session.getAttribute("myotp");//int mein value lenge eisliye typecaste kiye hai and eis Integer otp ko hmko myOtp se match karna hai
		String email=(String)session.getAttribute("email");
		if(myOtp==otp)
		{
			
			//password changes
			//email se user ko fetch kar rahe hai
			User user = this.userRepository.getUserByUserName(email);
			if(user==null) {
				
				//send error message
				 session.setAttribute("message", "User does not exist this your email id");
				 return "forgot_email_form";
			}
			else {
				//send changes password form
				
			}
			return "password_change_form";
		}
		else
		{
			
			session.setAttribute("message", "you have eneterd wrong OTP!!");
			return "verify_otp";
		

}}
		
	@PostMapping("/change-password")
	public String changesPassword(@RequestParam("newpassword") String newpassword ,HttpSession session) 
	{
		String email=(String)session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
		this.userRepository.save(user);
		return "redirect:/signin?changes=password changed successfully";
			
			
		}
		
}

