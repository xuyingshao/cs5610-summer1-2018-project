package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import server.models.Restaurant;
import server.repositories.RestaurantRepository;

@RestController
@CrossOrigin(origins = "*")
public class RestaurantService {
  @Autowired
  RestaurantRepository restaurantRepository;

  @GetMapping("/api/restaurant")
  List<Restaurant> findAllRestaurants() {
    return (List<Restaurant>) restaurantRepository.findAll();
  }

  @GetMapping("/api/restaurant/{yelpId}")
  public Restaurant findRestaurantByYelpId(@PathVariable("yelpId") String yelpId,
                                           HttpServletResponse response) {

//    List<Restaurant> restaurants = (List<Restaurant>) restaurantRepository.findAll();
//
//    for (Restaurant restaurant : restaurants) {
//      if (restaurant.getYelpId().equals(yelpId)) {
//        return restaurant;
//      }
//    }
    Optional<Restaurant> data = restaurantRepository.findRestaurantByYelpId(yelpId);
    if (data.isPresent()) {
      return data.get();
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }
}
