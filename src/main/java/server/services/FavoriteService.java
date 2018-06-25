package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import server.models.BaseUser;
import server.models.Customer;
import server.models.Favorite;
import server.models.Restaurant;
import server.repositories.CustomerRepository;
import server.repositories.FavoriteRepository;
import server.repositories.RestaurantRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true")
public class FavoriteService {
  @Autowired
  FavoriteRepository favoriteRepository;
  @Autowired
  RestaurantRepository restaurantRepository;
  @Autowired
  CustomerRepository customerRepository;

  @PostMapping("/api/favorite/{restaurantId}")
  public void customerLikesRestaurant(@PathVariable("restaurantId") int restaurantId,
                                      HttpServletRequest request, HttpServletResponse response) {
    // find restaurant by id
    Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);

    // retrieve user by session
    HttpSession session = request.getSession(false);
    BaseUser currentUser = null;
    Optional<Customer> customerOptional = null;
    if (session == null) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    } else {
      currentUser = (BaseUser) session.getAttribute("user");
      customerOptional = customerRepository.findById(currentUser.getId());
    }

    if (restaurantOptional.isPresent() && customerOptional.isPresent()) {
      Restaurant restaurant = restaurantOptional.get();
      Customer customer = customerOptional.get();

      List<Favorite> favHistory = (List<Favorite>) favoriteRepository.findAll();
      for (Favorite favorite : favHistory) {
        if (favorite.getCustomer().getId() == customer.getId()
                && favorite.getRestaurant().getId() == restaurant.getId()) {
          return;
        }
      }

      Favorite fav = new Favorite();
      fav.setRestaurant(restaurant);
      fav.setCustomer(customer);
      favoriteRepository.save(fav);
      return;
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }


  @DeleteMapping("/api/favorite/{restaurantId}")
  public void customerUnlikesRestaurant(@PathVariable("restaurantId") int restaurantId,
                                        HttpServletRequest request, HttpServletResponse response) {
    // find restaurant by id
    Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);

    // retrieve user by session
    HttpSession session = request.getSession(false);
    BaseUser currentUser = null;
    Optional<Customer> customerOptional = null;
    if (session == null) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    } else {
      currentUser = (BaseUser) session.getAttribute("user");
      customerOptional = customerRepository.findById(currentUser.getId());
    }

    if (restaurantOptional.isPresent() && customerOptional.isPresent()) {
      Restaurant restaurant = restaurantOptional.get();
      Customer customer = customerOptional.get();

      List<Favorite> favHistory = (List<Favorite>) favoriteRepository.findAll();
      for (Favorite favorite : favHistory) {
        if (favorite.getCustomer().getId() == customer.getId()
                && favorite.getRestaurant().getId() == restaurant.getId()) {
          favoriteRepository.deleteById(favorite.getId());
        }
      }
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }


  @GetMapping("/api/favorite/{restaurantId}")
  public Favorite findFavorite(@PathVariable("restaurantId") int restaurantId,
                               HttpServletRequest request, HttpServletResponse response) {
    // find restaurant by id
    Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);

    // retrieve user by session
    HttpSession session = request.getSession(false);
    BaseUser currentUser = null;
    Optional<Customer> customerOptional = null;
    if (session == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return null;
    } else {
      currentUser = (BaseUser) session.getAttribute("user");
      customerOptional = customerRepository.findById(currentUser.getId());
    }

    if (restaurantOptional.isPresent() && customerOptional.isPresent()) {
      Restaurant restaurant = restaurantOptional.get();
      Customer customer = customerOptional.get();

      List<Favorite> favHistory = (List<Favorite>) favoriteRepository.findAll();

      for (Favorite favorite : favHistory) {
        if (favorite.getCustomer().getId() == customer.getId()
                && favorite.getRestaurant().getId() == restaurant.getId()) {
          return favorite;
        }
      }
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }


  @GetMapping("/api/favorite/user/{userId}")
  public List<Restaurant> findFavoritesForUser(@PathVariable("userId") int userId) {
    List<Restaurant> restaurants = new ArrayList<Restaurant>();
    List<Favorite> favorites = (List<Favorite>) favoriteRepository.findAll();

    for (Favorite fav : favorites) {
      if (fav.getCustomer().getId() == userId) {
        restaurants.add(fav.getRestaurant());
      }
    }
    return restaurants;
  }

  @GetMapping("/api/favorite/restaurant/{restaurantId}")
  public List<Customer> findFollowersForRestaurant(@PathVariable("restaurantId") int restaurantId) {
    List<Customer> followers = new ArrayList<Customer>();
    List<Favorite> favorites = (List<Favorite>) favoriteRepository.findAll();

    for (Favorite fav : favorites) {
      if (fav.getRestaurant().getId() == restaurantId) {
        followers.add(fav.getCustomer());
      }
    }
    return followers;
  }
}
