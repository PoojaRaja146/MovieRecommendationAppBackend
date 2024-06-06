package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.server.application.UserWatchHistory;
import sample.server.application.User;
import sample.server.application.Movie;

import java.util.Optional;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "User Watch History Service",
                description = "A simple Web Service to manage user watch history.",
                version = "1.0"
        )
)
@RestController
public class UserWatchHistoryController {

    @Autowired
    private UserWatchHistoryRepository userWatchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Operation(summary = "Status message", description = "Returns a status message.")
    @GetMapping(path="/watch-history", produces = "text/plain")
    public String status() {
        return "User Watch History service is up and running";
    }

    @PostMapping(path="/watch-history/add")
    public ResponseEntity<String> addNewUserWatchHistory(@RequestParam Integer rating,
                                                         @RequestParam Integer userId,
                                                         @RequestParam Integer movieId) {
        // Fetch the related entities using repositories
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie with ID " + movieId + " does not exist."));

        // Create the UserWatchHistory object and set its properties
        UserWatchHistory watchHistory = new UserWatchHistory();
        watchHistory.setRating(rating);
        watchHistory.setUser(user);
        watchHistory.setMovie(movie);

        // Save the UserWatchHistory object
        userWatchHistoryRepository.save(watchHistory);

        return ResponseEntity.ok("Saved");
    }

    @GetMapping(path="/watch-history/all")
    public ResponseEntity<String> getAllUserWatchHistory() {
        Iterable<UserWatchHistory> allWatchHistories = userWatchHistoryRepository.findAll();
        StringBuilder response = new StringBuilder();

        for (UserWatchHistory watchHistory : allWatchHistories) {
            response.append("ID: ").append(watchHistory.getId())
                    .append(", Rating: ").append(watchHistory.getRating());

            if (watchHistory.getUser() != null) {
                response.append(", User: ").append(watchHistory.getUser().getName());
            } else {
                response.append(", User: N/A");
            }

            if (watchHistory.getMovie() != null) {
                response.append(", Movie: ").append(watchHistory.getMovie().getName());
            } else {
                response.append(", Movie: N/A");
            }

            response.append("\n");
        }

        if (response.length() > 0) {
            return ResponseEntity.ok(response.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user watch histories found");
        }
    }

    @DeleteMapping(path="/watch-history/delete/{id}")
    public ResponseEntity<String> deleteUserWatchHistory(@PathVariable("id") Integer id) {
        if (userWatchHistoryRepository.existsById(id)) {
            userWatchHistoryRepository.deleteById(id);
            return ResponseEntity.ok("Deleted user watch history with id " + id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User watch history not found with id " + id);
        }
    }

    @GetMapping("/watch-history/{id}")
    public ResponseEntity<String> getUserWatchHistoryById(@PathVariable("id") Integer id) {
        Optional<UserWatchHistory> watchHistoryOptional = userWatchHistoryRepository.findById(id);
        if (watchHistoryOptional.isPresent()) {
            UserWatchHistory watchHistory = watchHistoryOptional.get();
            String watchHistoryDetails = "ID: " + watchHistory.getId() + ", Rating: " + watchHistory.getRating();

            // Add user details
            if (watchHistory.getUser() != null) {
                watchHistoryDetails += ", User: " + watchHistory.getUser().getName();
            } else {
                watchHistoryDetails += ", User: N/A";
            }

            // Add movie details
            if (watchHistory.getMovie() != null) {
                watchHistoryDetails += ", Movie: " + watchHistory.getMovie().getName();
            } else {
                watchHistoryDetails += ", Movie: N/A";
            }

            return ResponseEntity.ok(watchHistoryDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User watch history not found with id " + id);
        }
    }

    @PutMapping(path="/watch-history/update/{id}")
    public ResponseEntity<String> updateUserWatchHistory(@PathVariable("id") Integer id,
                                                         @RequestParam Integer rating,
                                                         @RequestParam Integer userId,
                                                         @RequestParam Integer movieId) {
        // Fetch the watch history from the database
        Optional<UserWatchHistory> optionalWatchHistory = userWatchHistoryRepository.findById(id);
        if (optionalWatchHistory.isPresent()) {
            UserWatchHistory watchHistory = optionalWatchHistory.get();

            // Fetch the related entities using repositories
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));

            Movie movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new IllegalArgumentException("Movie with ID " + movieId + " does not exist."));

            // Update the watch history object with new values
            watchHistory.setRating(rating);
            watchHistory.setUser(user);
            watchHistory.setMovie(movie);

            // Save the updated watch history object
            userWatchHistoryRepository.save(watchHistory);

            return ResponseEntity.ok("Updated user watch history with id " + id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User watch history not found with id " + id);
        }
    }
}
