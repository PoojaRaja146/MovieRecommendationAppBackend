package sample.server.application;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "directorId", referencedColumnName = "id")
    @JsonManagedReference
    private Director director;

    @ManyToOne
    @JoinColumn(name = "actorId", referencedColumnName = "id")
    @JsonManagedReference
    private Actor actor;

    @ManyToOne
    @JoinColumn(name = "genreId", referencedColumnName = "id")
    @JsonManagedReference
    private Genre genre;

    @OneToOne(mappedBy = "movie", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private MovieMetaInfo metaInfo;

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

    public MovieMetaInfo getMetaInfo() {
        return this.metaInfo;
    }

    public void setMetaInfo(MovieMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }
}
