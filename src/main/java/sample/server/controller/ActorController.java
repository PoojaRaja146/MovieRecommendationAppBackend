package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.server.application.Actor;

import java.util.Optional;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Actor Service",
                description = "A simple Web Service to manage actors.",
                version = "1.0"
        )
)
@RestController
public class ActorController {

    @Autowired
    private ActorRepository actorRepository;

    @Operation(summary = "Status message", description = "Returns a status message.")
    @GetMapping(path="/actors", produces = "text/plain")
    public String status() {
        return "Actor service is up and running";
    }

    @PostMapping(path="/actors/add")
    public @ResponseBody String addNewActor(@RequestParam String firstName,@RequestParam String lastName,  @RequestParam String gender) {
        Actor actor = new Actor();
        actor.setFirstName(firstName);
        actor.setLastName(lastName);
        actor.setGender(gender);
        actorRepository.save(actor);
        return "Saved";
    }

    @GetMapping(path="/actors/all")
    public @ResponseBody Iterable<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    @DeleteMapping(path="/actors/delete/{id}")
    public @ResponseBody String deleteActor(@PathVariable("id") Integer id) {
        if (actorRepository.existsById(id)) {
            actorRepository.deleteById(id);
            return "Deleted actor with id " + id;
        } else {
            return "Actor not found with id " + id;
        }
    }

    @PutMapping(path="/actors/update/{id}")
    public @ResponseBody String updateActor(
            @PathVariable("id") Integer id,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("gender") String gender
    ) {
        return actorRepository.findById(id)
                .map(actor -> {
                    actor.setFirstName(firstName);
                    actor.setLastName(lastName);
                    actor.setGender(gender);
                    actorRepository.save(actor);
                    return "Updated actor with id " + id;
                })
                .orElseGet(() -> {
                    // Optionally add a new actor if not found
                    Actor updatedActor = new Actor();
                    updatedActor.setId(id);
                    updatedActor.setFirstName(firstName);
                    updatedActor.setLastName(lastName);
                    updatedActor.setGender(gender);
                    actorRepository.save(updatedActor);
                    return "Created new actor with id " + id;
                });
    }

    @GetMapping("/actors/{id}")
    public ResponseEntity<String> getActorById(@PathVariable("id") Integer id) {
        Optional<Actor> actorOptional = actorRepository.findById(id);
        if (actorOptional.isPresent()) {
            Actor actor = actorOptional.get();
            String actorDetails = "ID: " + actor.getId() + ", First Name: " + actor.getFirstName() + ", Last Name: " + actor.getLastName() + ", Gender: " + actor.getGender();
            return ResponseEntity.ok(actorDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actor not found with id " + id);
        }
    }
}
