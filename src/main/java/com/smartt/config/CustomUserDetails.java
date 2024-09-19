package com.smartt.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smartt.entities.User;

public class CustomUserDetails implements UserDetails {
	
	private User user; //pehle yeah banayenge and phir constructor using fields karenge
	private SimpleGrantedAuthority simpleGrantedAuthority;
	
	public CustomUserDetails(User user) { // and phir CustomUserDetails se ek object bana rahe hai through constructor user pass kar rahe hai or woh user upper chala jaayega or user se hm password, username ,jo jo niche diya hai woh sab nikaal sakte hai
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { //user ko kya kya autority ho sakta hai
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());
		return List.of(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getEmail();
	}
	@Override
	public boolean isAccountNonExpired() {
	
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	
		return true;
	}

	@Override
	public boolean isEnabled() {
	
		return true;
	}

}



