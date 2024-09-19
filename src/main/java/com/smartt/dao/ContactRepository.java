package com.smartt.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartt.entities.Contact;
import com.smartt.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	//pagination
	
	@Query("from Contact as c where c.user.id =:userId")//c.user.id  c(contact.java) ke andar user(user.java) main gaye and uske baad user(user.java) se id mein
	public Page<Contact> findContactsByUser(@Param("userId") int userId,Pageable pePageable);
	//Pageable--current-page and contact per page-5
	/*//search
	public List<Contact> findByNameContainingAndUser(String name,User user);
}*/
	
	
	//search
	public List<Contact> findByNameContainingAndUser(String name,User user); ;  /*//findByNamContainingAndUser ka matlab agar aap search kar rahe hai apna naam ayush singh to singh naam se jitne bhi name hone woh dikhne lagenge list mein
*/
	
}
