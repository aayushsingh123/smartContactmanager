package com.smartt.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.razorpay.*;
import com.smartt.dao.ContactRepository;
import com.smartt.dao.UserRepository;
import com.smartt.entities.Contact;
import com.smartt.entities.User;
import com.smartt.helper.Message;

import ch.qos.logback.core.net.server.Client;

@Controller
@RequestMapping("/user") // esi case mein hme do chij likhna padega access ke liye ex
							// -localhost:8080/user/idex
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;

	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) { // yeah method har baar chalega model attribute ke
																	// jariye or sirf yaha likhenge toh model ke jariye
																	// sab ho jaayega .. niche

		String userName = principal.getName();
		System.out.println("USERNAME" + userName);
		// get the user using username(Email)

		User user = userRepository.getUserByUserName(userName);
		System.out.println("USER" + user);

		model.addAttribute("user", user); // model.addAttribute yeah bhejne kaa kaam karta hai jaise ki yeah
											// user_dashboard.html mein bhej raha hai

	}
	// dashboard home

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) { // principal se unique id nikaal sakte hai

		model.addAttribute("title", "UserDashboard");

		/*
		 * String userName = principal.getName(); System.out.println("USERNAME"
		 * +userName); //get the user using username(Email)
		 * 
		 * User user = userRepository.getUserByUserName(userName);
		 * System.out.println("USER" +user);
		 * 
		 * model.addAttribute("user", user); //model.addAttribute yeah bhejne kaa kaam
		 * karta hai jaise ki yeah user_dashboard.html mein bhej raha hai
		 */ return "normal/user_dashboard";
	}

	// open add from Handler

	@GetMapping("/add-contact") // requestmapping bhi use kar sakte ho
	public String openAddContactForm(Model model) {

		model.addAttribute("tittle", "Add Contact");// model.addAttribute yeah path par dikhega naam Add Contact
		model.addAttribute("contatc", new Contact());
		return "normal/add_contact_form";

	}

	// processing add contact form

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) { // Contact se naam match karega kyuki contact mein
														// name,secondName,work,email yeah sab hai toh yeah
														// add_contact_form se match karega
		// Httpsession mein message store kar sakte hai
		try {

			String name = principal.getName();

			User user = this.userRepository.getUserByUserName(name);

			// processing and uploading file
			if (file.isEmpty()) {

				// if the file is empty then try our message
				System.out.println("file is empty, please upload new one");
				contact.setImage("contact.png");

			} else {
				// file the file to folder and update the name to contact
				contact.setImage(file.getOriginalFilename());// return karega original file
				File saveFile = new ClassPathResource("static/img").getFile();// ClassPathResource yeah path ke liye
																				// hota hai jo tumhe path dalna hai woh
																				// daalo

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is uploaded");
			}

			user.getContacts().add(contact);
			contact.setUser(user);

			this.userRepository.save(user);

			System.out.println("DATA " + contact);

			System.out.println("Added to dataBase");
			// message success
			session.setAttribute("message", new Message("Your contact is added !! add more..", "success"));

		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
			e.printStackTrace();

			// error message
			session.setAttribute("message", new Message("Something went wrong !! Try again.. ", "danger"));
		}
		return "normal/add_contact_form";

	}

	// show contacts handler pagination
	// per page =5[n]
	// current page=0[page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "show user contatcs");

		// contact ki list bhejna hai
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		Pageable pageable = PageRequest.of(page, 5);// PageRequest.of(page, 5); yeah page and size yeah extends se aaya
													// hai public class PageRequest extends AbstractPageRequest {

		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);// eis user se "id"
																									// nikaal sakte hai
		model.addAttribute("contacts", contacts);// esme list of contacts hai
		model.addAttribute("currentpage", page);
		model.addAttribute("totalpages", contacts.getTotalPages());
		/*
		 * public String showContacts(Model model,Principal principal) {//This interface
		 * represents the abstract notion of a principal, which can be used to represent
		 * any entity, such as an individual, a corporation, and a login id.
		 * model.addAttribute("title", "show user contatcs"); String userName =
		 * principal.getName();//db This interface represents the abstract notion of a
		 * principal, which can be used to represent any entity, such as an individual,
		 * a corporation, and a login id. User user=
		 * this.userRepository.getUserByUserName(userName);//userName pass kar rahe hai
		 * toh simpli user mil jaayega List<Contact> contacts = user.getContacts();//
		 * user.getContacts se puri list mil jaayega
		 */

		return "normal/show_contacts";

	}

	// showing particular cpntact details means jab
	// http://localhost:8080/user/show-contacts/4 esme email ko click kare toh ek
	// email ka details aaye

	@RequestMapping("/{cId}/contact") // {cId} yeah path variable hai
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {// cId ke
																											// jariye
																											// contact
																											// details
																											// fetch kar
																											// lenge

		System.out.println("CID " + cId);// jo aa raha hai vo cId aa raha hai

		Optional<Contact> contactoptional = this.contactRepository.findById(cId);
		Contact contact = contactoptional.get();

		// data secure ke liye hai year user sirf apna hi id dekh sake
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);

		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}

		return "normal/contact_detail";

	}

	// delete contact handler
	
	@GetMapping("/delete/{cid}")

	public String deleteContact(@PathVariable("cid") Integer cId, HttpSession session, Principal principal) {

	    try {
	        // Contact ko fetch karna
	        Contact contact = this.contactRepository.findById(cId).orElse(null);
            // Agar contact nahi milta, toh error message set karna aur redirect karna

	        if (contact == null) {
	            // Contact agar nahi milta toh message set karna
	            session.setAttribute("message", new Message("Contact not found", "danger"));
	            return "redirect:/user/show_contacts/0";
	        }

	        //current  User ko fetch karna or  // Current logged-in user ko find karna
	        User user = this.userRepository.getUserByUserName(principal.getName());
	        if (user != null) {
	        	// User ke contacts list se contact ko remove karna
	            user.getContacts().remove(contact);
	            this.userRepository.save(user);// // Updated user object ko database mein save karna
	            System.out.println("DELETED");

	            // Success message set karna
	            session.setAttribute("message", new Message("Contact deleted successfully", "success"));
	        } else {
	            // Agar user nahi milta toh message set karna
	            session.setAttribute("message", new Message("User not found", "danger"));
	        }

	    } catch (Exception e) {
	        // Error ko log karna aur message set karna
	        e.printStackTrace();
	        session.setAttribute("message", new Message("An error occurred while deleting contact", "danger"));
	    }

	    // Redirect path sahi hai ki nahi check karna
	   
	    return "redirect:/user/show-contacts/0";
	}




//open update from handler

	@PostMapping("/update-contact/{cid}") // postmapping ki jagah Getmapping bhi use kar sakte hai and postmapping
											// secure bhi hai kyuki agar hm hit karte hai toh log path se bhi id changes
											// kar dete hai lekin post mein aapko button ke jariye hi kar sakte hai or
											// aap aise bhi add kar sakte hai manually agar aap button lagay=te hai toh
											// aapko sirf button se hi kar sakte hai manaually nhi
	public String updateForm(@PathVariable("cid") Integer cid, Model model) {
		// Model mein title attribute add kar rahe hain jo UI pe dikhayi dega.

		model.addAttribute("title", "update contatct"); // tittle--key and update contatct--form hai//
		// contactRepository se ID ke basis par contact object find kar rahe hain.
		Contact contact = this.contactRepository.findById(cid).get();
		// Model mein contact attribute add kar rahe hain jisse UI pe contact details
		// dikhayi denge.
		model.addAttribute("contact", contact);
		return "normal/update_form";

	}

	// update Contact handler

	@RequestMapping(value = "/process-update", method = RequestMethod.POST) // @RequestMapping postmapping ka
																			// alternative hai. hm postmapping use kar
																			// sakte hai. postmapping chote mein ho
																			// jaata hai kyuki usme sirf yeah add krna
																			// hota hai
																			// @PostMapping("/update-contact/{cid}") and
																			// requestmapping mein alag se method and
																			// value add karna hota hai ji mein add kiya
																			// hai process-update ke liye
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Model model, HttpSession session, Principal principal) { // contact yeah sab @ModelAttribute Contact ke
																		// contact mein store hoga and jo update karne
																		// ke baad new image daloge to new image
																		// MultipartFile ke file mein store hoga
		// contact,file,model,session yeaha se data hmko mil raha hai and jaa raha hai
		try {

			// old contact details
			Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();

			if (!file.isEmpty()) {
				// filework
				// rewrite

				// delete old photo

				File deleteFile = new ClassPathResource("static/img").getFile();// ClassPathResource yeah path ke liye
																				// hota hai jo tumhe path dalna hai woh
																				// daalo
				File file1 = new File(deleteFile, oldContactDetail.getImage());// yeaha new file1 object banaye hai
																				// jisme deleteFile pass kar rahe hai or
																				// oldContactDetail se image le rahe hai
																				// delete ke liyye
				file1.delete();

				/*
				 * //ese old photo delete nhi ho raha hai esliye ek new file1 object banakar
				 * deleteFile ko pass kiye hai File deleteFile= new
				 * ClassPathResource("static/img").getFile(); deleteFile.delete();
				 */

				// update new photo

				File saveFile = new ClassPathResource("static/img").getFile();// ClassPathResource yeah path ke liye
																				// hota hai jo tumhe path dalna hai woh
																				// daalo

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());

			} else {

				contact.setImage(oldContactDetail.getImage());// contact-- new hai toh oldcontactdetails se image get
																// karke conatct mein daal diya

			}

			User user = this.userRepository.getUserByUserName(principal.getName());// contact mein user ko set kar diye
																					// niche code mein
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Your contact is updated...", "success"));// new Message yeah
																									// new message kaa
																									// object hai

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("CONTACT NAME " + contact.getName());
		System.out.println("CONTACT NAME " + contact.getcId());
		return "redirect:/user/" + contact.getcId() + "/contact";// http://localhost:8080/user/81/contact---- user is
																	// user 18 is id and contact is contact

	}
	
	//your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model)

	{
		model.addAttribute("title", "this is your profile page");//yeah UI par title bhjne ke liye hai
		return "normal/profile";
	}
	
	//setting handle ke liye
	
	@GetMapping("/settings")
	public String openSettings() {
		
		return "normal/settings";
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, 
	                             @RequestParam("newPassword") String newPassword, 
	                             Principal principal, 
	                             HttpSession session) {
	    
	    // Old aur new password ko print karo
	    System.out.println("OLD PASSWORD " + oldPassword);
	    System.out.println("NEW PASSWORD " + newPassword);
	    
	    // Current logged-in user ka username fetch karo
	    String userName = principal.getName();
	    // Username ke basis pe user ko database se fetch karo
	    User currentUser = this.userRepository.getUserByUserName(userName);
	    System.out.println(currentUser.getPassword());
	    
	    // Check karo agar old password match karta hai
	    if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
	        // Naya password set karo aur save karo
	        currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
	        this.userRepository.save(currentUser);
	        // Success message session mein set karo
	        session.setAttribute("message", new Message("Your password is successfully changed...", "alert-success"));
	    } else {
	        // Agar password match nahi karta, error message set karo
	        session.setAttribute("message", new Message("Your old password is wrong!!", "alert-danger"));
	        return "redirect:/user/settings";
	    }

	    // User ko index page pe redirect karo
	    return "redirect:/user/index";
	}
	
	
	
	//creating order for payment
	
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data) throws Exception {//yeah RazorpayException bydefault aaya hai jab jey and value add kiya hai tab aaya hai leking hm saare exception ke liye sirf Exception 
		
		System.out.println(data);
		int amt = Integer.parseInt(data.get("amount").toString());//pehle data ko string mein nikaala hai hai phir string ko Integer mein changes kiya hai phor jo amount hai woh Integer mein aa jaayega
		
		RazorpayClient razorpayClient = new RazorpayClient("rzp_test_pZYglnNLZmN15r", "Kxm5hmz3s4Z5u88gfev40VL7");
		JSONObject options = new JSONObject();
		options.put("amount",amt*100);
		options.put("currency","INR");
		options.put("receipt","txn_12345");
		
		//creating new order
		
		Order order = razorpayClient.orders.create(options);
		System.out.println(order);		
		
		//if you want you can save this to your database..
		
	//	this.userRepository.getUserByUserName(Order);
		
		return order.toString();
	}
	
	
	
	
}