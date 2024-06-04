package sample.server.controller;

import org.springframework.data.repository.CrudRepository;

import sample.server.application.Movie;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface MovieRepository extends CrudRepository<Movie, Integer> {
}
