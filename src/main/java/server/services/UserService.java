package server.services;

import com.fasterxml.jackson.databind.ser.Serializers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.persistence.GeneratedValue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import server.models.BaseUser;
import server.models.Customer;
import server.models.Deliverer;
import server.models.Dish;
import server.models.Favorite;
import server.models.Restaurant;
import server.models.Restaurateur;
import server.models.Review;
import server.repositories.BaseUserRepository;
import server.repositories.CustomerRepository;
import server.repositories.DelivererRepository;
import server.repositories.FavoriteRepository;
import server.repositories.RestaurantRepository;
import server.repositories.RestaurateurRepository;
import server.repositories.ReviewRepository;

@RestController
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@CrossOrigin(origins = "https://cs5610-project-client.herokuapp.com", allowCredentials = "true")
public class UserService {
  @Autowired
  BaseUserRepository baseUserRepository;
  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  DelivererRepository delivererRepository;
  @Autowired
  RestaurateurRepository restaurateurRepository;
  @Autowired
  RestaurantRepository restaurantRepository;
  @Autowired
  FavoriteRepository favoriteRepository;
  @Autowired
  ReviewRepository reviewRepository;

  @GetMapping("/api/profile/customer/{userId}/account")
  public Customer findCustomerById(@PathVariable("userId") int userId,
                                   HttpServletRequest request) {
    HttpSession session = request.getSession(false);

    Optional<Customer> data = customerRepository.findById(userId);
    if (data.isPresent()) {
      return data.get();
    }
    return null;
  }

  @GetMapping("/api/profile/restaurateur/{userId}/account")
  public Restaurateur findRestaurateurById(@PathVariable("userId") int userId,
                                           HttpServletRequest request) {
    HttpSession session = request.getSession(false);

    Optional<Restaurateur> data = restaurateurRepository.findById(userId);
    if (data.isPresent()) {
      return data.get();
    }
    return null;
  }

  @GetMapping("/api/profile/deliverer/{userId}/account")
  public Deliverer findDelivererById(@PathVariable("userId") int userId,
                                     HttpServletRequest request) {
    HttpSession session = request.getSession(false);

    Optional<Deliverer> data = delivererRepository.findById(userId);
    if (data.isPresent()) {
      return data.get();
    }
    return null;
  }

  @PutMapping("/api/login/customer")
  @ResponseBody
  public Customer customerLogin(@RequestBody BaseUser user,
                                HttpServletResponse response, HttpServletRequest request) {
    String username = user.getUsername();
    String password = user.getPassword();

    HttpSession session = request.getSession();

    if (username.equals("admin") && username.equals(password)) {
      Optional<BaseUser> data = baseUserRepository.findUserByCredentials(username, password);
      if (data.isPresent()) {
        BaseUser admin = data.get();
        session.setAttribute("user", admin);
        return new Customer(admin);
      }
    }

    Optional<Customer> data = customerRepository.findCustomerByCredentials(username, password);
    if (data.isPresent()) {
      Customer customer = data.get();
      session.setAttribute("user", (BaseUser) customer);
      return customer;
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }


  @PutMapping("/api/login/deliverer")
  @ResponseBody
  public Deliverer delivererLogin(@RequestBody BaseUser user, HttpServletResponse response,
                                  HttpServletRequest request) {
    String username = user.getUsername();
    String password = user.getPassword();

    HttpSession session = request.getSession();

    if (username.equals("admin") && username.equals(password)) {
      Optional<BaseUser> data = baseUserRepository.findUserByCredentials(username, password);
      if (data.isPresent()) {
        BaseUser admin = data.get();
        session.setAttribute("user", admin);
        return new Deliverer(admin);
      }
    }

    Optional<Deliverer> data = delivererRepository.findDelivererByCredentials(username, password);
    if (data.isPresent()) {
      Deliverer deliverer = data.get();
      session.setAttribute("user", (BaseUser) deliverer);
      return deliverer;
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }


  @PutMapping("/api/login/restaurateur")
  @ResponseBody
  public Restaurateur restaurateurLogin(@RequestBody BaseUser user, HttpServletResponse response,
                                        HttpServletRequest request) {
    String username = user.getUsername();
    String password = user.getPassword();

    HttpSession session = request.getSession();

    if (username.equals("admin") && username.equals(password)) {
      Optional<BaseUser> data = baseUserRepository.findUserByCredentials(username, password);
      if (data.isPresent()) {
        BaseUser admin = data.get();
        session.setAttribute("user", admin);
        return new Restaurateur(admin);
      }
    }

    Optional<Restaurateur> data = restaurateurRepository.findRestaurateurByCredentials(username, password);
    if (data.isPresent()) {
      Restaurateur restaurateur = data.get();
      session.setAttribute("user", (BaseUser) restaurateur);
      return restaurateur;
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }

  @PostMapping("/api/register/customer")
  @ResponseBody
  public Customer customerRegister(@RequestBody Customer customer,
                                   HttpServletResponse response,
                                   HttpServletRequest request) {
    String username = customer.getUsername();

    HttpSession session = request.getSession();

    Optional<BaseUser> data = baseUserRepository.findUserByUsername(username);
    if (!data.isPresent()) {
      session.setAttribute("user", (BaseUser) customer);
      return customerRepository.save(customer);
    }
    response.setStatus(HttpServletResponse.SC_CONFLICT);
    return null;
  }

  @PostMapping("/api/register/deliverer")
  @ResponseBody
  public Deliverer delivererRegister(@RequestBody Deliverer deliverer,
                                     HttpServletResponse response,
                                     HttpServletRequest request) {
    String username = deliverer.getUsername();

    HttpSession session = request.getSession();

    Optional<BaseUser> data = baseUserRepository.findUserByUsername(username);
    if (!data.isPresent()) {
      session.setAttribute("user", (BaseUser) deliverer);
      return delivererRepository.save(deliverer);
    }
    response.setStatus(HttpServletResponse.SC_CONFLICT);
    return null;
  }

  @PostMapping("/api/register/restaurateur/{restaurantId}")
  public Restaurateur restaurateurRegister(
          @RequestBody Restaurateur restaurateur, @PathVariable("restaurantId") int restaurantId,
          HttpServletResponse response,
          HttpServletRequest request) {
    String username = restaurateur.getUsername();

    HttpSession session = request.getSession();

    Optional<BaseUser> userData = baseUserRepository.findUserByUsername(username);
    if (!userData.isPresent()) {
      session.setAttribute("user", (BaseUser) restaurateur);
      Optional<Restaurant> data = restaurantRepository.findById(restaurantId);
      if (data.isPresent()) {
        Restaurant restaurant = data.get();
        restaurateur.setRestaurant(restaurant);
        Restaurateur res = restaurateurRepository.save(restaurateur);
        restaurant.setRestaurateur(res);
        return restaurateurRepository.save(restaurateur);
      }
      response.setStatus(HttpServletResponse.SC_CONFLICT);
      return null;
    }
    response.setStatus(HttpServletResponse.SC_CONFLICT);
    return null;
  }

  @PutMapping("/api/profile/customer/{userId}/account")
  public Customer updateCustomerProfileAccount(@PathVariable("userId") int userId,
                                               @RequestBody Customer customer) {
    Optional<Customer> data = customerRepository.findById(userId);
    if (data.isPresent()) {
      Customer res = data.get();
      if (!customer.getPassword().equals(res.getPassword())) {
        res.setPassword(customer.getPassword());
      }
      if (!customer.getFirstName().equals(res.getFirstName())) {
        res.setFirstName(customer.getFirstName());
      }
      if (!customer.getLastName().equals(res.getLastName())) {
        res.setLastName(customer.getLastName());
      }
      if (!customer.getPhone().equals(res.getPhone())) {
        res.setPhone(customer.getPhone());
      }
      if (!customer.getEmail().equals(res.getEmail())) {
        res.setEmail(customer.getEmail());
      }
      if (!customer.getAddress().equals(res.getAddress())) {
        res.setAddress(customer.getAddress());
      }
      return customerRepository.save(res);
    }
    return null;
  }

  @PutMapping("/api/profile/restaurateur/{userId}/account")
  public Restaurateur updateRestaurateurProfileAccount(@PathVariable("userId") int userId,
                                                       @RequestBody Restaurateur restaurateur) {
    Optional<Restaurateur> data = restaurateurRepository.findById(userId);
    if (data.isPresent()) {
      Restaurateur res = data.get();
      if (!restaurateur.getPassword().equals(res.getPassword())) {
        res.setPassword(restaurateur.getPassword());
      }
      if (!restaurateur.getFirstName().equals(res.getFirstName())) {
        res.setFirstName(restaurateur.getFirstName());
      }
      if (!restaurateur.getLastName().equals(res.getLastName())) {
        res.setLastName(restaurateur.getLastName());
      }
      if (!restaurateur.getPhone().equals(res.getPhone())) {
        res.setPhone(restaurateur.getPhone());
      }
      if (!restaurateur.getEmail().equals(res.getEmail())) {
        res.setEmail(restaurateur.getEmail());
      }
      return restaurateurRepository.save(res);
    }
    return null;
  }


  @PutMapping("/api/profile/deliverer/{userId}/account")
  public Deliverer updateDelivererProfileAccount(@PathVariable("userId") int userId,
                                                 @RequestBody Deliverer deliverer) {
    Optional<Deliverer> data = delivererRepository.findById(userId);
    if (data.isPresent()) {
      Deliverer res = data.get();
      if (!deliverer.getPassword().equals(res.getPassword())) {
        res.setPassword(deliverer.getPassword());
      }
      if (!deliverer.getFirstName().equals(res.getFirstName())) {
        res.setFirstName(deliverer.getFirstName());
      }
      if (!deliverer.getLastName().equals(res.getLastName())) {
        res.setLastName(deliverer.getLastName());
      }
      if (!deliverer.getPhone().equals(res.getPhone())) {
        res.setPhone(deliverer.getPhone());
      }
      if (!deliverer.getEmail().equals(res.getEmail())) {
        res.setEmail(deliverer.getEmail());
      }
      return delivererRepository.save(res);
    }
    return null;
  }

  @GetMapping("/api/order/{restaurateurId}")
  public List<Dish> findDishesForRestaurateur(@PathVariable("RID") int restaurateurId,
                                              HttpServletResponse response) {
    Optional<Restaurateur> data = restaurateurRepository.findById(restaurateurId);
    if (data.isPresent()) {
      Restaurant restaurant = data.get().getRestaurant();
      return restaurant.getDishes();
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }

  @PostMapping("/api/logout")
  public void logout(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
  }

  @GetMapping("/api/user/restaurant/{restaurantId}")
  public Restaurateur findOwnerOfRestaurant(@PathVariable("restaurantId") int restaurantId,
                                            HttpServletResponse response) {
    Optional<Restaurant> data = restaurantRepository.findById(restaurantId);

    if (data.isPresent()) {
      Restaurant restaurant = data.get();
      return restaurant.getRestaurateur();
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }

  @GetMapping("/api/session/user")
  public BaseUser findCurrentCustomer(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(false);
//    if (session != null) {
//      return (BaseUser) session.getAttribute("user");
//    }
    if (session != null && (BaseUser)session.getAttribute("user") != null) {
      return (BaseUser) session.getAttribute("user");
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }

  @GetMapping("/api/user/customer")
  public List<Customer> findAllCustomers() {
    return (List<Customer>) customerRepository.findAll();
  }

  @GetMapping("/api/user/restaurateur")
  public List<Restaurateur> findAllRestaurateurs() {
    return (List<Restaurateur>) restaurateurRepository.findAll();
  }

  @GetMapping("/api/user/deliverer")
  public List<Deliverer> findAllDeliverers() {
    return (List<Deliverer>) delivererRepository.findAll();
  }

  @DeleteMapping("/api/user/customer/{userId}")
  public void deleteCustomer(@PathVariable("userId") int userId) {
    Optional<Customer> customerOptional = customerRepository.findById(userId);
    if (customerOptional.isPresent()) {
      Customer customer = customerOptional.get();
      // remove related favorites
      List<Favorite> favorites = (List<Favorite>) favoriteRepository.findAll();
      for (Favorite fav : favorites) {
        if (fav.getCustomer().getId() == userId) {
          favoriteRepository.deleteById(fav.getId());
        }
      }
      // remove related reviews
      List<Review> reviews = (List<Review>) reviewRepository.findAll();
      for (Review review : reviews) {
        if (review.getCustomer().getId() == userId) {
          reviewRepository.deleteById(review.getId());
        }
      }
    }
    customerRepository.deleteById(userId);
  }

  @DeleteMapping("/api/user/restaurateur/{userId}")
  public void deleteRestaurateur(@PathVariable("userId") int userId) {
    Optional<Restaurateur> restaurateurOptional = restaurateurRepository.findById(userId);
    if (restaurateurOptional.isPresent()) {
      Restaurateur restaurateur = restaurateurOptional.get();
      Restaurant restaurant = restaurateur.getRestaurant();
      // remove related favorites
      List<Favorite> favorites = (List<Favorite>) favoriteRepository.findAll();
      for (Favorite fav : favorites) {
        if (fav.getRestaurant().getId() == restaurant.getId()) {
          favoriteRepository.deleteById(fav.getId());
        }
      }
      restaurantRepository.deleteById(restaurant.getId());
    }
    restaurateurRepository.deleteById(userId);
  }

  @DeleteMapping("/api/user/deliverer/{userId}")
  public void deleteDeliverer(@PathVariable("userId") int userId) {
    Optional<Deliverer> delivererOptional = delivererRepository.findById(userId);
    if (delivererOptional.isPresent()) {
      Deliverer deliverer = delivererOptional.get();
      // remove related reviews
      List<Review> reviews = (List<Review>) reviewRepository.findAll();
      for (Review review : reviews) {
        if (review.getDeliverer().getId() == userId) {
          reviewRepository.deleteById(review.getId());
        }
      }
    }
    delivererRepository.deleteById(userId);
  }
}
