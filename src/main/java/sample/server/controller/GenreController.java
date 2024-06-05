package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.server.application.Genre;

import java.util.Optional;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Genre Service",
                description = "A simple Web Service to manage genres.",
                version = "1.0"
        )
)
@RestController
public class GenreController {

    @Autowired
    private GenreRepository genreRepository;

    @Operation(summary = "Status message", description = "Returns a status message.")
    @GetMapping(path="/genres", produces = "text/plain")
    public String status() {
        return "Genre service is up and running";
    }

    @PostMapping(path="/genres/add")
    public @ResponseBody String addNewGenre(@RequestParam String name) {
        Genre genre = new Genre();
        genre.setName(name);
        genreRepository.save(genre);
        return "Saved";
    }

    @GetMapping(path="/genres/all")
    public @ResponseBody Iterable<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @DeleteMapping(path="/genres/delete/{id}")
    public @ResponseBody String deleteGenre(@PathVariable("id") Integer id) {
        if (genreRepository.existsById(id)) {
            genreRepository.deleteById(id);
            return "Deleted genre with id " + id;
        } else {
            return "Genre not found with id " + id;
        }
    }

    @PutMapping(path="/genres/update/{id}")
    public @ResponseBody String updateGenre(
            @PathVariable("id") Integer id,
            @RequestParam("name") String name
    ) {
        return genreRepository.findById(id)
                .map(genre -> {
                    genre.setName(name);
                    genreRepository.save(genre);
                    return "Updated genre with id " + id;
                })
                .orElseGet(() -> {
                    // Optionally add a new genre if not found
                    Genre updatedGenre = new Genre();
                    updatedGenre.setId(id);
                    updatedGenre.setName(name);
                    genreRepository.save(updatedGenre);
                    return "Created new genre with id " + id;
                });
    }

    @GetMapping("/genres/{id}")
    public ResponseEntity<String> getGenreById(@PathVariable("id") Integer id) {
        Optional<Genre> genreOptional = genreRepository.findById(id);
        if (genreOptional.isPresent()) {
            Genre genre = genreOptional.get();
            String genreDetails = "ID: " + genre.getId() + ", Name: " + genre.getName();
            return ResponseEntity.ok(genreDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Genre not found with id " + id);
        }
    }
}
