package server.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import server.models.BaseUser;
import server.models.Restaurant;

public interface RestaurantRepository extends CrudRepository <Restaurant, Integer> {
  @Query("SELECT r FROM Restaurant r WHERE r.yelpId=:yelpId")
  Optional<Restaurant> findRestaurantByYelpId(
          @Param("yelpId") String yelpId
  );
}
