package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
@CrossOrigin(origins = "*")
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
  public Customer findCustomerById(@PathVariable("userId") int userId) {
    Optional<Customer> data = customerRepository.findById(userId);
    if (data.isPresent()) {
      return data.get();
    }
    return null;
  }

  @GetMapping("/api/profile/restaurateur/{userId}/account")
  public Restaurateur findRestaurateurById(@PathVariable("userId") int userId) {
    Optional<Restaurateur> data = restaurateurRepository.findById(userId);
    if (data.isPresent()) {
      return data.get();
    }
    return null;
  }

  @GetMapping("/api/profile/deliverer/{userId}/account")
  public Deliverer findDelivererById(@PathVariable("userId") int userId) {
    Optional<Deliverer> data = delivererRepository.findById(userId);
    if (data.isPresent()) {
      return data.get();
    }
    return null;
  }

  @PutMapping("/api/login/customer")
  @ResponseBody
  public Customer customerLogin(@RequestBody BaseUser user, HttpSession session, HttpServletResponse response) {
    String username = user.getUsername();
    String password = user.getPassword();

    Optional<Customer> data = customerRepository.findCustomerByCredentials(username, password);
    if (data.isPresent()) {
      Customer customer = data.get();
      session.setAttribute("user", customer);
      return customer;
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }


  @PutMapping("/api/login/deliverer")
  @ResponseBody
  public Deliverer delivererLogin(@RequestBody BaseUser user, HttpSession session, HttpServletResponse response) {
    String username = user.getUsername();
    String password = user.getPassword();

    Optional<Deliverer> data = delivererRepository.findDelivererByCredentials(username, password);
    if (data.isPresent()) {
      Deliverer deliverer = data.get();
      session.setAttribute("user", deliverer);
      return deliverer;
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }


  @PutMapping("/api/login/restaurateur")
  @ResponseBody
  public Restaurateur restaurateurLogin(@RequestBody BaseUser user, HttpSession session, HttpServletResponse response) {
    String username = user.getUsername();
    String password = user.getPassword();

    Optional<Restaurateur> data = restaurateurRepository.findRestaurateurByCredentials(username, password);
    if (data.isPresent()) {
      Restaurateur restaurateur = data.get();
      session.setAttribute("user", restaurateur);
      return restaurateur;
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }

  @PostMapping("/api/register/customer")
  @ResponseBody
  public Customer customerRegister(@RequestBody Customer customer, HttpSession session, HttpServletResponse response) {
    String username = customer.getUsername();
    Optional<BaseUser> data = baseUserRepository.findUserByUsername(username);
    if (!data.isPresent()) {
      session.setAttribute("user", customer);
      return customerRepository.save(customer);
    }
    response.setStatus(HttpServletResponse.SC_CONFLICT);
    return null;
  }

  @PostMapping("/api/register/deliverer")
  @ResponseBody
  public Deliverer delivererRegister(@RequestBody Deliverer deliverer, HttpSession session, HttpServletResponse response) {
    String username = deliverer.getUsername();
    Optional<BaseUser> data = baseUserRepository.findUserByUsername(username);
    if (!data.isPresent()) {
      session.setAttribute("user", deliverer);
      return delivererRepository.save(deliverer);
    }
    response.setStatus(HttpServletResponse.SC_CONFLICT);
    return null;
  }

  @PostMapping("/api/register/restaurateur/{restaurantId}")
  public Restaurateur restaurateurRegister(
          @RequestBody Restaurateur restaurateur, @PathVariable("restaurantId") int restaurantId,
          HttpSession session, HttpServletResponse response) {
    String username = restaurateur.getUsername();
    Optional<BaseUser> data = baseUserRepository.findUserByUsername(username);
    if (!data.isPresent()) {
      session.setAttribute("user", restaurateur);
      Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
      restaurant.ifPresent(restaurateur::setRestaurant);
      return restaurateurRepository.save(restaurateur);
    }
    response.setStatus(HttpServletResponse.SC_CONFLICT);
    return null;
  }

  @PostMapping("/api/logout")
  public void logout(HttpSession session) {
    session.invalidate();
  }
}
