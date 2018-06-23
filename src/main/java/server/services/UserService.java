package server.services;

import com.fasterxml.jackson.databind.ser.Serializers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import server.models.Restaurant;
import server.models.Restaurateur;
import server.repositories.BaseUserRepository;
import server.repositories.CustomerRepository;
import server.repositories.DelivererRepository;
import server.repositories.RestaurantRepository;
import server.repositories.RestaurateurRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true")
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
    if (session != null) {
      return (BaseUser) session.getAttribute("user");
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }
}
