package server.repositories;

import org.springframework.data.repository.CrudRepository;

import server.models.Restaurant;

public interface RestaurantRepository extends CrudRepository<Restaurant, Integer> {

}
