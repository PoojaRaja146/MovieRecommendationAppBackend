package sample.server.application;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sample.server.controller.ActorRepository;
import sample.server.controller.DirectorRepository;
import sample.server.controller.GenreRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "directorId", referencedColumnName = "id")
    @JsonManagedReference
    private Director director;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "actorId", referencedColumnName = "id")
    @JsonManagedReference
    private Actor actor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "genreId", referencedColumnName = "id")
    @JsonManagedReference
    private Genre genre;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<UserWatchHistory> watchHistory;

    private String name;

    private String language;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<UserWatchHistory> getWatchHistory() {
        return watchHistory;
    }

    public void setWatchHistory(List<UserWatchHistory> watchHistory) {
        this.watchHistory = watchHistory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public Integer getDirectorId() {
        return director != null ? director.getId() : null;
    }

    public Integer getActorId() {
        return actor != null ? actor.getId() : null;
    }

    public Integer getGenreId() {
        return genre != null ? genre.getId() : null;
    }

}
