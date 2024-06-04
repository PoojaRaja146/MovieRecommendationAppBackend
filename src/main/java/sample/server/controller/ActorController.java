package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import sample.server.application.Actor;

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
    public @ResponseBody String addNewActor(@RequestParam String name, @RequestParam String gender) {
        Actor actor = new Actor();
        actor.setName(name);
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
    public @ResponseBody String updateActor(@PathVariable("id") Integer id, @RequestBody Actor updatedActor) {
        return actorRepository.findById(id)
                .map(actor -> {
                    actor.setName(updatedActor.getName());
                    actor.setGender(updatedActor.getGender());
                    actorRepository.save(actor);
                    return "Updated actor with id " + id;
                })
                .orElseGet(() -> {
                    // Optionally add a new actor if not found
                    updatedActor.setId(id);
                    actorRepository.save(updatedActor);
                    return "Created new actor with id " + id;
                });
    }
}
