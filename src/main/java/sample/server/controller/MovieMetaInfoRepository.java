package sample.server.controller;

import org.springframework.data.repository.CrudRepository;
import sample.server.application.MovieMetaInfo;

public interface MovieMetaInfoRepository extends CrudRepository<MovieMetaInfo, Integer> {
}
