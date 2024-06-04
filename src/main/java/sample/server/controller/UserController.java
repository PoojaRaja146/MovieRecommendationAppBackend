package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import sample.server.application.User;

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
	
	@PostMapping(path="/add") // Map ONLY POST Requests
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
	
	@GetMapping(path="/all")
	  public @ResponseBody Iterable<User> getAllUsers() {
	    // This returns a JSON or XML with the users
	    return userRepository.findAll();
	  }
	
	@DeleteMapping(path="/delete/{id}")
	public @ResponseBody String deleteUser(@PathVariable("id") Integer id) {
	    if (userRepository.existsById(id)) {
	        userRepository.deleteById(id);
	        return "Deleted user with id " + id;
	    } else {
	        return "User not found with id " + id;
	    }
	}
	
	@PutMapping(path="/update/{id}")
	public @ResponseBody String updateUser(@PathVariable("id") Integer id, @RequestBody User updatedUser) {
	    return userRepository.findById(id)
	        .map(user -> {
	            user.setName(updatedUser.getName());
	            user.setEmail(updatedUser.getEmail());
	            userRepository.save(user);
	            return "Updated user with id " + id;
	        })
	        .orElseGet(() -> {
	            // Optionally add a new user if not found
	            updatedUser.setId(id);
	            userRepository.save(updatedUser);
	            return "Created new user with id " + id;
	        });
	}
	
}
