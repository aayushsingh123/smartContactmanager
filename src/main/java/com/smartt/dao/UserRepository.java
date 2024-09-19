package com.smartt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartt.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query("Select u from User u where u.email= :email")
	public User getUserByUserName(@Param ("email")String email);  //getUserByUserName ko call karenge or parameter pass kiya hai email woh email uppar query wale email se match karna chaiye. and uss query mein user ko select kar rahe hai toh uss user mein email bhi hai
	

}
