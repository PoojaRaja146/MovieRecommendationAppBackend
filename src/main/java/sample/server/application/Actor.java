package sample.server.application;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Service;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Entity
public class Actor {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String firstName;

    private String lastName;

    @NotBlank(message = "Gender is mandatory")
    @Pattern(regexp = "^(M|F|other)$", message = "Gender must be M, F, or other")
    private String gender;

    @OneToMany(mappedBy = "actor")
    @JsonBackReference
    private List<Movie> movies;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
