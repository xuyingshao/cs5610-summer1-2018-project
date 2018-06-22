package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @GetMapping("/api/restaurant/restaurateur/{yelpId}")
  Boolean findRestaurateurForRestaurantBool(@PathVariable("yelpId") String yelpId) {
    Optional<Restaurant> data = restaurantRepository.findRestaurantByYelpId(yelpId);
    return data.isPresent() && data.get().getRestaurateur() != null;
  }

  @PostMapping("/api/restaurant")
  Restaurant createRestaurant(@RequestBody Restaurant restaurant, HttpServletResponse response) {
    Optional<Restaurant> data = restaurantRepository.findRestaurantByYelpId(restaurant.getYelpId());
    if (data.isPresent()) {
      response.setStatus(HttpServletResponse.SC_CONFLICT);
      return null;
    } else {
      return restaurantRepository.save(restaurant);
    }
  }

  @GetMapping("/api/restaurant/yelp/{yelpId}")
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

  @GetMapping("/api/restaurant/{restaurantId}")
  public Restaurant findRestaurantById(@PathVariable("restaurantId") int restaurantId,
                                       HttpServletResponse response) {
    Optional<Restaurant> data = restaurantRepository.findById(restaurantId);
    if (data.isPresent()) {
      return data.get();
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }
}
