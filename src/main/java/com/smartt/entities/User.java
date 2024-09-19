package com.smartt.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
@Table(name="USER")
public class User {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)  //autogenrate hota rahega
	private int id;
	@NotBlank(message="Name field is required!!")
	@Size(min=2, max=20 ,message="min 2 and max 20 characters are allowed !!")
	private String name;
	
	@Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid name format")
	private String email;
	@NotBlank(message="Password field is required!")
	@Pattern(regexp = "^(?=.*\\d)(?=.*[^a-zA-Z0-9\\s]).*|^[a-zA-Z]{3,}$", message = "Password should contain at least one number and one special character, or be at least 3 letters long")
	private String password;

	private String role;
	private boolean enabled;
	private String imageUrl;
	
	@Column(length=500)
	private String about;
	
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true) //jab user nikaal rahe ho or usi samau contats bhi fetch kare or jab jarurat ho tab LAZY use karo
//	orphanRemoval matlab jab  child (contact.java) entity unlink ho jaayega apne parent(User.java) entity se use case mein user ko remove kar diya jaayega
	private List<Contact> contacts=new ArrayList<>();
	

	public User() {
		super();
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String getAbout() {
		return about;
	}


	public void setAbout(String about) {
		this.about = about;
	}


	public List<Contact> getContacts() {
		return contacts;
	}


	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	/*
	 * @Override public String toString() { return "User [id=" + id + ", name=" +
	 * name + ", email=" + email + ", password=" + password + ", role=" + role +
	 * ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about +
	 * ", contacts=" + contacts + "]"; }
	 */
	
	
	

}
