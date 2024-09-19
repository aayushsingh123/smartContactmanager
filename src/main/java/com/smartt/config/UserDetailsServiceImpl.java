package com.smartt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartt.dao.UserRepository;
import com.smartt.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //unimplemented karne par aaya hai
		//fetching from database
		
		
		User user = userRepository.getUserByUserName(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("Could not found user");
		}
			CustomUserDetails customUserDetails= new CustomUserDetails(user);
	
		return customUserDetails;//return mein UserDetails ko return karna tha toh hmne customUserDetails ek object bana hai and phir customUserDetails ko return kar diye
	}
	
	
	}


