package sample.server.application;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sample.server.application.Movie;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class MovieMetaInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Summary cannot be blank")
    private String summary;

    @NotBlank(message = "Rating cannot be blank")
    @Pattern(regexp = "^(G|PG|PG-13|R|NC-17)$", message = "Invalid rating format")
    private String rating;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    // Constructors, getters, and setters
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
