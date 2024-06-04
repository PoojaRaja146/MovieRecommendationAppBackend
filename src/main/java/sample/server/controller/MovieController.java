package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import sample.server.application.Movie;

@OpenAPIDefinition(
        info = @Info(
                title = "Movie Service",
                description = "A simple Web Service to manage movies.",
                version = "1.0"
        )
)
@RestController
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Operation(summary = "Status message", description = "Returns a status message.")
    @GetMapping(path="/movies", produces = "text/plain")
    public String status() {
        return "Movie service is up and running";
    }

    @PostMapping(path="/movies/add")
    public @ResponseBody String addNewMovie(@RequestParam String name, @RequestParam String language) {
        Movie movie = new Movie();
        movie.setName(name);
        movie.setLanguage(language);
        movieRepository.save(movie);
        return "Saved";
    }

    @GetMapping(path="/movies/all")
    public @ResponseBody Iterable<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @DeleteMapping(path="/movies/delete/{id}")
    public @ResponseBody String deleteMovie(@PathVariable("id") Integer id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return "Deleted movie with id " + id;
        } else {
            return "Movie not found with id " + id;
        }
    }

    @PutMapping(path="/movies/update/{id}")
    public @ResponseBody String updateMovie(@PathVariable("id") Integer id, @RequestBody Movie updatedMovie) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setName(updatedMovie.getName());
                    movie.setLanguage(updatedMovie.getLanguage());
                    movieRepository.save(movie);
                    return "Updated movie with id " + id;
                })
                .orElseGet(() -> {
                    // Optionally add a new movie if not found
                    updatedMovie.setId(id);
                    movieRepository.save(updatedMovie);
                    return "Created new movie with id " + id;
                });
    }
}
