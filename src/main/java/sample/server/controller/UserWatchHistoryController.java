package sample.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import sample.server.application.UserWatchHistory;

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

    @Operation(summary = "Status message", description = "Returns a status message.")
    @GetMapping(path="/watch-history", produces = "text/plain")
    public String status() {
        return "User Watch History service is up and running";
    }

    @PostMapping(path="/watch-history/add")
    public @ResponseBody String addNewUserWatchHistory(@RequestParam Integer rating) {
        UserWatchHistory watchHistory = new UserWatchHistory();
        watchHistory.setRating(rating);
        userWatchHistoryRepository.save(watchHistory);
        return "Saved";
    }

    @GetMapping(path="/watch-history/all")
    public @ResponseBody Iterable<UserWatchHistory> getAllUserWatchHistory() {
        return userWatchHistoryRepository.findAll();
    }

    @DeleteMapping(path="/watch-history/delete/{id}")
    public @ResponseBody String deleteUserWatchHistory(@PathVariable("id") Integer id) {
        if (userWatchHistoryRepository.existsById(id)) {
            userWatchHistoryRepository.deleteById(id);
            return "Deleted user watch history with id " + id;
        } else {
            return "User watch history not found with id " + id;
        }
    }

    @PutMapping(path="/watch-history/update/{id}")
    public @ResponseBody String updateUserWatchHistory(@PathVariable("id") Integer id, @RequestBody UserWatchHistory updatedWatchHistory) {
        return userWatchHistoryRepository.findById(id)
                .map(watchHistory -> {
                    watchHistory.setRating(updatedWatchHistory.getRating());
                    userWatchHistoryRepository.save(watchHistory);
                    return "Updated user watch history with id " + id;
                })
                .orElseGet(() -> {
                    // Optionally add a new user watch history if not found
                    updatedWatchHistory.setId(id);
                    userWatchHistoryRepository.save(updatedWatchHistory);
                    return "Created new user watch history with id " + id;
                });
    }
}
