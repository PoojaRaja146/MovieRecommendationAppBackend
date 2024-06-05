package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import sample.server.application.Director;
import sample.server.application.User;

import java.util.Optional;

@OpenAPIDefinition(
        info = @Info(
                title = "Director Service",
                description = "A simple Web Service to manage directors.",
                version = "1.0"
        )
)
@RestController
public class DirectorController {

    @Autowired
    private DirectorRepository directorRepository;

    @Operation(summary = "Status message", description = "Returns a status message.")
    @GetMapping(path="/directors", produces = "text/plain")
    public String status() {
        return "Director service is up and running";
    }

    @PostMapping(path="/directors/add")
    public @ResponseBody String addNewDirector(@RequestParam String name, @RequestParam String gender) {
        Director director = new Director();
        director.setName(name);
        director.setGender(gender);
        directorRepository.save(director);
        return "Saved";
    }

    @GetMapping(path="/directors/all")
    public @ResponseBody Iterable<Director> getAllDirectors() {
        return directorRepository.findAll();
    }

    @DeleteMapping(path="/directors/delete/{id}")
    public @ResponseBody String deleteDirector(@PathVariable("id") Integer id) {
        if (directorRepository.existsById(id)) {
            directorRepository.deleteById(id);
            return "Deleted director with id " + id;
        } else {
            return "Director not found with id " + id;
        }
    }

    @PutMapping(path="/directors/update/{id}")
    public @ResponseBody String updateDirector(
            @PathVariable("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("gender") String gender
    ) {
        return directorRepository.findById(id)
                .map(director -> {
                    director.setName(name);
                    director.setGender(gender);
                    directorRepository.save(director);
                    directorRepository.save(director);
                    return "Updated user with id " + id;
                })
                .orElseGet(() -> {
                    // Optionally add a new user if not found
                    Director updatedDirector = new Director();
                    updatedDirector.setId(id);
                    updatedDirector.setName(name);
                    updatedDirector.setGender(gender);
                    directorRepository.save(updatedDirector);
                    return "Created new user with id " + id;
                });
    }

    @GetMapping("/directors/{id}")
    public ResponseEntity<String> getUserById(@PathVariable("id") Integer id) {
        Optional<Director> directorOptional = directorRepository.findById(id);
        if (directorOptional.isPresent()) {
            Director director = directorOptional.get();
            String directorDetails = "ID: " + director.getId() + ", Name: " + director.getName() + ", gender: " + director.getGender();
            return ResponseEntity.ok(directorDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id " + id);
        }
    }
}
