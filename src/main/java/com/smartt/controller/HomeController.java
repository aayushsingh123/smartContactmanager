package com.smartt.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartt.dao.UserRepository;
import com.smartt.entities.User;
import com.smartt.helper.Message;



@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; //@Bean public BCryptPasswordEncoder passwordEncoder bean wala yeha use hua
	
	@Autowired
	private UserRepository userRepository;
	private User user;
	
	    @RequestMapping("/")
        public String home(Model model) {
	    	
	    	model.addAttribute("title" 	,"home-smart conatct manager"); //yeah path ke time likha rahega means UI mein likha rahega
		return "home";
	}
	
	    @RequestMapping("/about")
        public String about(Model model) {
	    	
	    	model.addAttribute("title" 	,"About-smart conatct manager"); //yeah path ke time likha rahega means UI mein likha rahega
		return "about";
	}
	    @RequestMapping("/signup")
        public String signup(Model model) {
	    	
	    	model.addAttribute("title" 	,"Register-smart conatct manager"); //yeah path ke time likha rahega means UI mein likha rahega
		    model.addAttribute("user",new User());////"user" ke naam se mapping kar raha hu and new User ko send kar dunga.esse kya hoga blank field chala jaayega
	    	return "signup";

}
	    
	
	    
		/*
		 * @Valid--@Valid ka Mukhya Kaam: Automatic Validation:
		 * 
		 * @Valid annotation ko form handling methods ke parameters mein use kiya jata
		 * hai, jo Spring ko batata hai ki us object par validation rules apply karni
		 * hain. Validation Rules Define Karna:
		 * 
		 * Validation rules ko define karne ke liye aap Java Bean Validation API
		 * annotations (jaise @NotNull, @Size, @Email, etc.) ko model class ke fields
		 * par lagate hain. BindingResult ke Sath Kaam Karna:
		 * 
		 * @Valid annotation ke saath, BindingResult object ko use karke aap validation
		 * errors ko capture aur handle kar sakte hain.
		 */
	  //Handling for registration user
	    
	    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
		public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,// //User.java bean class hai jo entities mein hai and user ka jo field match karega aapke form se or object se woh sab aa jaayga . jaise name, email,password, 
				@RequestParam(value="agreement",defaultValue="false") boolean agreement, Model model ,//User user yeah model hai menas pojo hai
				HttpSession session) {
	    try {
			if(!agreement) {
				System.out.println("you have not agreed the terms and condition");
				throw new Exception("you have not agreed the terms and condition");
			}
			
			if(result1.hasErrors()) {
				System.out.println("ERROR"+result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
		    user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement "+agreement);
			System.out.println("User" +user);
			
			User result = this.userRepository.save(user);
			model.addAttribute("user", new User()); //blank user jaayega
			
			
			session.setAttribute("message", new Message("Successfully Registered!!" , "alert-success"));
	  	  return "signup";
			
			
		}
	          catch(Exception e) {
	        	  e.printStackTrace();
	        	  model.addAttribute("user" ,user);
	        	  session.setAttribute("message", new Message("Something went wrong!!" +e.getMessage(), "alert-danger"));
	        	  return "signup";  
	          }

	  		
	  		
	}
		
          @GetMapping("/signin")
         public String customLogin(Model model) {
        	 
        	  
        	  model.addAttribute("title", "Login Page");
        	 return "login";
          }
          
}





