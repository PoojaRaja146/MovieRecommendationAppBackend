package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import sample.server.application.User;

import java.util.Optional;

@OpenAPIDefinition(
	    info = @Info(
	        title = "Sample-Application",
	        description = "A simple Web Service to demonstrate Rest-Services.",
	        version = "1.0"
	    )
	)
@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	//	Call with: http://localhost:port/context
	@Operation(summary = "Status message", description = "Returns a status message.")
	@GetMapping(path="/", produces = "text/plain")
	public String status() {
		return "The server is up an running";
	}
	
	@PostMapping(path="/users/add") // Map ONLY POST Requests
	  public @ResponseBody String addNewUser (@RequestParam String firstName, @RequestParam String lastName,
	         @RequestParam String email, @RequestParam String phoneNo) {
	    // @ResponseBody means the returned String is the response, not a view name
	    // @RequestParam means it is a parameter from the GET or POST request

	    User n = new User();
	    n.setFirstName(firstName);
		n.setLastName(lastName);
	    n.setEmail(email);
		n.setPhoneNo(phoneNo);
	    userRepository.save(n);
	    return "Saved";
	  }
	
	@GetMapping(path="/users/all")
	  public @ResponseBody Iterable<User> getAllUsers() {
	    // This returns a JSON or XML with the users
	    return userRepository.findAll();
	  }
	
	@DeleteMapping(path="users/delete/{id}")
	public @ResponseBody String deleteUser(@PathVariable("id") Integer id) {
	    if (userRepository.existsById(id)) {
	        userRepository.deleteById(id);
	        return "Deleted user with id " + id;
	    } else {
	        return "User not found with id " + id;
	    }
	}

	@PutMapping(path="/users/update/{id}")
	public @ResponseBody String updateUser(
			@PathVariable("id") Integer id,
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("email") String email,
			@RequestParam("phoneNo") String phoneNo
	) {
		return userRepository.findById(id)
				.map(user -> {
					user.setFirstName(firstName);
					user.setLastName(lastName);
					user.setEmail(email);
					user.setPhoneNo(phoneNo);
					userRepository.save(user);
					return "Updated user with id " + id;
				})
				.orElseGet(() -> {
					// Optionally add a new user if not found
					User updatedUser = new User();
					updatedUser.setId(id);
					updatedUser.setFirstName(firstName);
					updatedUser.setLastName(lastName);
					updatedUser.setEmail(email);
					updatedUser.setPhoneNo(phoneNo);
					userRepository.save(updatedUser);
					return "Created new user with id " + id;
				});
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<String> getUserById(@PathVariable("id") Integer id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			String userDetails = "ID: " + user.getId() + ", First Name: " + user.getFirstName()+ ", Last Name: " + user.getLastName() + ", Email: " + user.getEmail()+ ", PhoneNo: " + user.getPhoneNo();
			return ResponseEntity.ok(userDetails);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
		}
	}

}
