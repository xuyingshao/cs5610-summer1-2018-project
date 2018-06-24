package server.repositories;

import org.springframework.data.repository.CrudRepository;

import server.models.Review;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
}
