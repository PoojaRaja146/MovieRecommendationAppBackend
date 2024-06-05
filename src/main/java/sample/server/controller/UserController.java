package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
	  public @ResponseBody String addNewUser (@RequestParam String name
	      , @RequestParam String email) {
	    // @ResponseBody means the returned String is the response, not a view name
	    // @RequestParam means it is a parameter from the GET or POST request

	    User n = new User();
	    n.setName(name);
	    n.setEmail(email);
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
			@RequestParam("name") String name,
			@RequestParam("email") String email
	) {
		return userRepository.findById(id)
				.map(user -> {
					user.setName(name);
					user.setEmail(email);
					userRepository.save(user);
					return "Updated user with id " + id;
				})
				.orElseGet(() -> {
					// Optionally add a new user if not found
					User updatedUser = new User();
					updatedUser.setId(id);
					updatedUser.setName(name);
					updatedUser.setEmail(email);
					userRepository.save(updatedUser);
					return "Created new user with id " + id;
				});
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<String> getUserById(@PathVariable("id") Integer id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			String userDetails = "ID: " + user.getId() + ", Name: " + user.getName() + ", Email: " + user.getEmail();
			return ResponseEntity.ok(userDetails);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
		}
	}

}
