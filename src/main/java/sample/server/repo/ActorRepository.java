package sample.server.controller;

import org.springframework.data.repository.CrudRepository;

import sample.server.application.Actor;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ActorRepository extends CrudRepository<Actor, Integer> {

}