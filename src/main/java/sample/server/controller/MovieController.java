package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.server.application.Actor;
import sample.server.application.Director;
import sample.server.application.Genre;
import sample.server.application.Movie;

import java.util.Optional;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

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

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Operation(summary = "Status message", description = "Returns a status message.")
    @GetMapping(path="/movies", produces = "text/plain")
    public String status() {
        return "Movie service is up and running";
    }

    @PostMapping(path="/movies/add")
    public ResponseEntity<String> addNewMovie(@RequestParam String name, @RequestParam String language,
                                              @RequestParam Integer directorId, @RequestParam Integer actorId,
                                              @RequestParam Integer genreId) {
        // Fetch the related entities using repositories
        Director director = directorRepository.findById(directorId)
                .orElseThrow(() -> new IllegalArgumentException("Director with ID " + directorId + " does not exist."));

        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new IllegalArgumentException("Actor with ID " + actorId + " does not exist."));

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Genre with ID " + genreId + " does not exist."));

        // Create the movie object and set its properties
        Movie movie = new Movie();
        movie.setName(name);
        movie.setLanguage(language);
        movie.setDirector(director);
        movie.setActor(actor);
        movie.setGenre(genre);

        // Save the movie object
        movieRepository.save(movie);

        return ResponseEntity.ok("Saved");
    }

    @GetMapping(path="/movies/all")
    public ResponseEntity<String> getAllMovies() {
        Iterable<Movie> allMovies = movieRepository.findAll();
        StringBuilder response = new StringBuilder();

        for (Movie movie : allMovies) {
            response.append("ID: ").append(movie.getId())
                    .append(", Name: ").append(movie.getName())
                    .append(", Language: ").append(movie.getLanguage());

            if (movie.getDirector() != null) {
                response.append(", Director: ").append(movie.getDirector().getName());
            } else {
                response.append(", Director: N/A");
            }

            if (movie.getActor() != null) {
                response.append(", Actor: ").append(movie.getActor().getName());
            } else {
                response.append(", Actor: N/A");
            }

            if (movie.getGenre() != null) {
                response.append(", Genre: ").append(movie.getGenre().getName());
            } else {
                response.append(", Genre: N/A");
            }

            response.append("\n");
        }

        if (response.length() > 0) {
            return ResponseEntity.ok(response.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found");
        }
    }

    @DeleteMapping(path="/movies/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable("id") Integer id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return ResponseEntity.ok("Deleted movie with id " + id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found with id " + id);
        }
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<String> getMovieById(@PathVariable("id") Integer id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            String movieDetails = "ID: " + movie.getId() + ", Name: " + movie.getName() + ", Language: " + movie.getLanguage();

            // Add director details
            if (movie.getDirector() != null) {
                movieDetails += ", Director: " + movie.getDirector().getName();
            } else {
                movieDetails += ", Director: N/A";
            }

            // Add actor details
            if (movie.getActor() != null) {
                movieDetails += ", Actor: " + movie.getActor().getName();
            } else {
                movieDetails += ", Actor: N/A";
            }

            // Add genre details
            if (movie.getGenre() != null) {
                movieDetails += ", Genre: " + movie.getGenre().getName();
            } else {
                movieDetails += ", Genre: N/A";
            }

            return ResponseEntity.ok(movieDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found with id " + id);
        }
    }

    @PutMapping(path="/movies/update/{id}")
    public ResponseEntity<String> updateMovie(
            @PathVariable("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("language") String language,
            @RequestParam("directorId") Integer directorId,
            @RequestParam("actorId") Integer actorId,
            @RequestParam("genreId") Integer genreId
    ) {
        // Fetch the movie from the database
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();

            // Fetch the related entities using repositories
            Director director = directorRepository.findById(directorId)
                    .orElseThrow(() -> new IllegalArgumentException("Director with ID " + directorId + " does not exist."));

            Actor actor = actorRepository.findById(actorId)
                    .orElseThrow(() -> new IllegalArgumentException("Actor with ID " + actorId + " does not exist."));

            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new IllegalArgumentException("Genre with ID " + genreId + " does not exist."));

            // Update the movie object with new values
            movie.setName(name);
            movie.setLanguage(language);
            movie.setDirector(director);
            movie.setActor(actor);
            movie.setGenre(genre);

            // Save the updated movie object
            movieRepository.save(movie);

            return ResponseEntity.ok("Updated movie with id " + id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found with id " + id);
        }
    }
}
