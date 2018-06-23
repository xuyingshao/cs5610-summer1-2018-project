package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import server.models.Dish;
import server.models.Restaurant;
import server.repositories.DishRepository;
import server.repositories.RestaurantRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class DishService {
  @Autowired
  DishRepository dishRepository;
  @Autowired
  RestaurantRepository restaurantRepository;

  @GetMapping("/api/restaurant/{RID}/dish")
  public List<Dish> findAllDishesForRestaurant(@PathVariable("RID") int restaurantId,
                                               HttpServletResponse response) {
    Optional<Restaurant> data = restaurantRepository.findById(restaurantId);
    if (data.isPresent()) {
      Restaurant restaurant = data.get();
      return restaurant.getDishes();
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }


//  @PostMapping("/api/restaurant/{RID}/dishes")
//  public void saveAllDishesForRestaurant(@PathVariable("RID") int restaurantId,
//                                         HttpServletResponse response,
//                                         @RequestBody List<Dish> menu) {
//    List<Integer> toDelete = new ArrayList<Integer>();
//    int id = 0;
//
//    Optional<Restaurant> restaurantData = restaurantRepository.findById(restaurantId);
//    if (restaurantData.isPresent()) {
//      Restaurant restaurant = restaurantData.get();
//      List<Dish> oldMenu = restaurant.getDishes();
//
//      System.out.println("new menu: ");
//      for (int i = 0; i < menu.size(); i++) {
//        System.out.println(menu.get(i).getName());
//      }
//      System.out.println();
//
//
//      System.out.println("old menu: ");
//      for (int i = 0; i < oldMenu.size(); i++) {
//        System.out.println(oldMenu.get(i).getName());
//      }
//      System.out.println();
//
//
//      for (Dish dish : menu) {
//        Optional<Dish> dishData = dishRepository.findById(dish.getId());
//        if (!dishData.isPresent()) {    // add
//          dish.setRestaurant(restaurant);
//          dishRepository.save(dish);
//        } else {              // update
//          Dish oldDish = dishData.get();
//          if (!oldDish.equals(dish)) {
//            oldDish.setName(dish.getName());
//            oldDish.setPrice(dish.getPrice());
//            oldDish.setPosition(dish.getPosition());
//            dishRepository.save(oldDish);
//          }
//        }
//      }
//
//
//      for (Dish oldDish : oldMenu) {
//        boolean found = false;
//        for (Dish newDish : menu) {
//          if (oldDish.getId() == newDish.getId()) {
//            found = true;
//          }
//        }
//        if (!found) {
//          System.out.println("dish to delete: ");
//          System.out.println(oldDish.getId());
//
//          dishRepository.delete(oldDish);
////          dishRepository.deleteById(5);
//          id = oldDish.getId();
//          toDelete.add(oldDish.getId());
//        }
//      }
//    }
//  }


  @DeleteMapping("/api/restaurant/{restaurantId}/dish/{dishId}")
  public void deleteDishForRestaurant(@PathVariable("dishId") int dishId) {
    dishRepository.deleteById(dishId);
  }

  @PostMapping("/api/restaurant/{restaurantId}/dish")
  public Dish createDishForRestaurant(@PathVariable("restaurantId") int restaurantId,
                                      @RequestBody Dish dish,
                                      HttpServletResponse response) {

    Optional<Restaurant> data = restaurantRepository.findById(restaurantId);

    if (data.isPresent()) {
      Restaurant restaurant = data.get();
      dish.setRestaurant(restaurant);
      return dishRepository.save(dish);
    }

    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }

  @PutMapping("/api/restaurant/{restaurantId}/dish/{dishId}")
  public Dish updateDishForRestaurant(@PathVariable("dishId") int dishId,
                         @RequestBody Dish newDish,
                         HttpServletResponse response) {
    Optional<Dish> data = dishRepository.findById(dishId);
    if (data.isPresent()) {
      Dish dish = data.get();
      dish.setName(newDish.getName());
      dish.setPrice(newDish.getPrice());
      return dishRepository.save(dish);
    }

    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }
}
