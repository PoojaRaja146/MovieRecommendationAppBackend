package sample.server.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sample.server.application.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@OpenAPIDefinition(
        info = @Info(
                title = "Movie Service",
                description = "A simple Web Service to manage movies.",
                version = "1.0"
        )
)
@RestController
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieMetaInfoRepository movieMetaInfoRepository;

    @Operation(summary = "Status message", description = "Returns a status message.")
    @GetMapping(path="/movies", produces = "text/plain")
    public String status() {
        return "Movie service is up and running";
    }

    @Transactional
    @PostMapping(path = "/movies/add")
    public ResponseEntity<String> addNewMovie(@RequestParam String name, @RequestParam String language,
                                              @RequestParam Integer directorId, @RequestParam Integer actorId,
                                              @RequestParam Integer genreId, @RequestParam String summary,
                                              @RequestParam String rating) {

        try {
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
            movieRepository.save(movie);

            // Create the meta info object and set its properties
            MovieMetaInfo metaInfo = new MovieMetaInfo();
            metaInfo.setSummary(summary);
            metaInfo.setRating(rating);
            metaInfo.setMovie(movie);

            movieMetaInfoRepository.save(metaInfo);

            // Log and return success response
            logger.info("Movie added successfully: {}", movie);
            return ResponseEntity.ok("Saved");

        } catch (Exception e) {
            // Handle general exceptions
            logger.error("Error adding movie: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding movie");
        }
    }

    @PostMapping(path = "/movies/add-without-transaction")
    public ResponseEntity<String> addNewMovieWithoutTransaction(@RequestParam String name, @RequestParam String language,
                                                                @RequestParam Integer directorId, @RequestParam Integer actorId,
                                                                @RequestParam Integer genreId, @RequestParam String summary,
                                                                @RequestParam String rating) {
        try {
            // Fetch the related entities using repositories
            Director director = directorRepository.findById(directorId)
                    .orElseThrow(() -> new IllegalArgumentException("Director with ID " + directorId + " does not exist."));

            Actor actor = actorRepository.findById(actorId)
                    .orElseThrow(() -> new IllegalArgumentException("Actor with ID " + actorId + " does not exist."));

            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new IllegalArgumentException("Genre with ID " + genreId + " does not exist."));

            // Create the movie object and save it
            Movie movie = new Movie();
            movie.setName(name);
            movie.setLanguage(language);
            movie.setDirector(director);
            movie.setActor(actor);
            movie.setGenre(genre);
            movieRepository.save(movie);

            // Create the meta info object and save it
            MovieMetaInfo metaInfo = new MovieMetaInfo();
            metaInfo.setSummary(summary);
            metaInfo.setRating(rating);
            metaInfo.setMovie(movie);
            movieMetaInfoRepository.save(metaInfo);

            // Log and return success response
            logger.info("Movie added successfully: {}", movie);
            return ResponseEntity.ok("Saved");

        } catch (Exception e) {
            // Handle general exceptions
            logger.error("Error adding movie: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding movie");
        }
    }

    @GetMapping(path="/movies/all")
    @Transactional // Ensure lazy loading works outside of transactional boundaries
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> allMovies = (List<Movie>) movieRepository.findAll();
        if (!allMovies.isEmpty()) {
            for (Movie movie : allMovies) {
                // Force loading of metaInfo if it's lazily initialized
                movie.getMetaInfo();
            }
            logger.info("Movies found: {}", allMovies);
            return ResponseEntity.ok(allMovies);
        } else {
            logger.info("No movies found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Transactional
    @DeleteMapping(path = "movies/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable("id") Integer id) {
        try {
            if (movieRepository.existsById(id)) {
                movieRepository.deleteById(id);
                logger.info("Deleted movie with id {}", id);
                return ResponseEntity.ok("Deleted movie with id " + id);
            } else {
                logger.info("Movie not found with id {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found with id " + id);
            }
        } catch (Exception e) {
            logger.error("Error deleting movie with id {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting movie");
        }
    }

    @GetMapping("/movies/{id}")
    @Transactional
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") Integer id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            movie.getMetaInfo(); // Force loading of metaInfo
            logger.info("Movie found with id {}: {}", id, movie);
            return ResponseEntity.ok(movie);
        } else {
            logger.info("Movie not found with id {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Transactional
    @PutMapping(path="/movies/update/{id}")
    public ResponseEntity<String> updateMovie(
            @PathVariable("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("language") String language,
            @RequestParam("directorId") Integer directorId,
            @RequestParam("actorId") Integer actorId,
            @RequestParam("genreId") Integer genreId,
            @RequestParam("summary") String summary,
            @RequestParam("rating") String rating
    ) {
        try {
            Optional<Movie> optionalMovie = movieRepository.findById(id);
            if (optionalMovie.isPresent()) {
                Movie movie = optionalMovie.get();

                Director director = directorRepository.findById(directorId)
                        .orElseThrow(() -> new IllegalArgumentException("Director with ID " + directorId + " does not exist."));

                Actor actor = actorRepository.findById(actorId)
                        .orElseThrow(() -> new IllegalArgumentException("Actor with ID " + actorId + " does not exist."));

                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new IllegalArgumentException("Genre with ID " + genreId + " does not exist."));

                movie.setName(name);
                movie.setLanguage(language);
                movie.setDirector(director);
                movie.setActor(actor);
                movie.setGenre(genre);

                // Update the meta info
                MovieMetaInfo metaInfo = movie.getMetaInfo();
                if (metaInfo != null) {
                    metaInfo.setSummary(summary);
                    metaInfo.setRating(rating);
                    movieMetaInfoRepository.save(metaInfo);
                } else {
                    metaInfo = new MovieMetaInfo();
                    metaInfo.setSummary(summary);
                    metaInfo.setRating(rating);
                    metaInfo.setMovie(movie);
                    movie.setMetaInfo(metaInfo);
                    movieMetaInfoRepository.save(metaInfo);
                }

                movieRepository.save(movie);
                logger.info("Updated movie with id {}", id);
                return ResponseEntity.ok("Updated movie with id " + id);
            } else {
                logger.info("Movie not found with id {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found with id " + id);
            }
        } catch (Exception e) {
            logger.error("Error updating movie with id {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating movie");
        }
    }

    @GetMapping("/genres/{genreId}/movies")
    public ResponseEntity<String> getMoviesByGenreId(@PathVariable("genreId") Integer genreId) {
        List<Movie> movies = movieRepository.findByGenre_Id(genreId);
        if (movies.isEmpty()) {
            logger.info("No movies found for genre with id {}", genreId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found for genre with id " + genreId);
        } else {
            String movieDetails = movies.stream()
                    .map(movie -> {
                        String details = "ID: " + movie.getId() + ", Name: " + movie.getName() + ", Language: " + movie.getLanguage();

                        if (movie.getDirector() != null) {
                            details += ", Director: " + movie.getDirector().getName();
                        } else {
                            details += ", Director: N/A";
                        }

                        if (movie.getActor() != null) {
                            details += ", Actor: " + movie.getActor().getName();
                        } else {
                            details += ", Actor: N/A";
                        }

                        if (movie.getGenre() != null) {
                            details += ", Genre: " + movie.getGenre().getName();
                        } else {
                            details += ", Genre: N/A";
                        }

                        return details;
                    })
                    .collect(Collectors.joining("\n"));

            logger.info("Movies found for genre with id {}: {}", genreId, movieDetails);
            return ResponseEntity.ok(movieDetails);
        }
    }

    @GetMapping("/directors/{directorId}/movies")
    public ResponseEntity<String> getMoviesByDirectorId(@PathVariable("directorId") Integer directorId) {
        List<Movie> movies = movieRepository.findByDirector_Id(directorId);
        if (movies.isEmpty()) {
            logger.info("No movies found for director with id {}", directorId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found for director with id " + directorId);
        } else {
            String movieDetails = movies.stream()
                    .map(movie -> {
                        String details = "ID: " + movie.getId() + ", Name: " + movie.getName() + ", Language: " + movie.getLanguage();

                        if (movie.getDirector() != null) {
                            details += ", Director: " + movie.getDirector().getName();
                        } else {
                            details += ", Director: N/A";
                        }

                        if (movie.getActor() != null) {
                            details += ", Actor: " + movie.getActor().getName();
                        } else {
                            details += ", Actor: N/A";
                        }

                        if (movie.getGenre() != null) {
                            details += ", Genre: " + movie.getGenre().getName();
                        } else {
                            details += ", Genre: N/A";
                        }

                        return details;
                    })
                    .collect(Collectors.joining("\n"));

            logger.info("Movies found for director with id {}: {}", directorId, movieDetails);
            return ResponseEntity.ok(movieDetails);
        }
    }

    @GetMapping("/actors/{actorId}/movies")
    public ResponseEntity<String> getMoviesByActorId(@PathVariable("actorId") Integer actorId) {
        List<Movie> movies = movieRepository.findByActor_Id(actorId);
        if (movies.isEmpty()) {
            logger.info("No movies found for actor with id {}", actorId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found for actor with id " + actorId);
        } else {
            String movieDetails = movies.stream()
                    .map(movie -> {
                        String details = "ID: " + movie.getId() + ", Name: " + movie.getName() + ", Language: " + movie.getLanguage();

                        if (movie.getDirector() != null) {
                            details += ", Director: " + movie.getDirector().getName();
                        } else {
                            details += ", Director: N/A";
                        }

                        if (movie.getActor() != null) {
                            details += ", Actor: " + movie.getActor().getName();
                        } else {
                            details += ", Actor: N/A";
                        }

                        if (movie.getGenre() != null) {
                            details += ", Genre: " + movie.getGenre().getName();
                        } else {
                            details += ", Genre: N/A";
                        }

                        return details;
                    })
                    .collect(Collectors.joining("\n"));

            logger.info("Movies found for actor with id {}: {}", actorId, movieDetails);
            return ResponseEntity.ok(movieDetails);
        }
    }
}
