package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import sample.server.application.Director;

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
    public @ResponseBody String updateDirector(@PathVariable("id") Integer id, @RequestBody Director updatedDirector) {
        return directorRepository.findById(id)
                .map(director -> {
                    director.setName(updatedDirector.getName());
                    director.setGender(updatedDirector.getGender());
                    directorRepository.save(director);
                    return "Updated director with id " + id;
                })
                .orElseGet(() -> {
                    // Optionally add a new director if not found
                    updatedDirector.setId(id);
                    directorRepository.save(updatedDirector);
                    return "Created new director with id " + id;
                });
    }
}
