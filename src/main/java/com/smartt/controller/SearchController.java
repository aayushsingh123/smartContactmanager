/*package com.smartt.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.smartt.dao.ContactRepository;
import com.smartt.dao.UserRepository;
import com.smartt.entities.Contact;
import com.smartt.entities.User;

@RestController
public class SearchController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal) {
        // Print query for debugging
        System.out.println("Search query: " + query);

        // Get the current user
        User user = userRepository.getUserByUserName(principal.getName());
        if (user == null) {
            // Handle case where user is not found
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Fetch contacts based on the search query and user
        List<Contact> contacts = contactRepository.findByNameContainingAndUser(query, user);
        if (contacts.isEmpty()) {
            // Handle case where no contacts are found
            return new ResponseEntity<>("No contacts found", HttpStatus.NOT_FOUND);
        }

        // Return the list of contacts with HTTP OK status
        return ResponseEntity.ok(contacts);
    }
}
*/
package com.smartt.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartt.dao.ContactRepository;
import com.smartt.dao.UserRepository;
import com.smartt.entities.Contact;
import com.smartt.entities.User;

@RestController
public class SearchController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	//search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query ,Principal  principal )   //current user ko nikaalne ke liye Principal ka use karte hai
	{
		System.out.println(query);
		User user = this.userRepository.getUserByUserName(principal.getName());//yeaha se aayega niche wala user
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);  //user-- current user ka username pass kar raha hai toh uske liye hmne user ek banaya just esi ke upper pehle yeah line likhenge phir upper wala

	return ResponseEntity.ok(contacts);
	}
	
}










