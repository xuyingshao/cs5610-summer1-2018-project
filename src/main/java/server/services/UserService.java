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
import server.models.Dish;
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
    Optional<BaseUser> userData = baseUserRepository.findUserByUsername(username);
    if (!userData.isPresent()) {
      session.setAttribute("user", restaurateur);
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
  public void logout(HttpSession session) {
    session.invalidate();
  }

  // FIXME, for dev purpose
  @GetMapping("/api/user")
  public List<BaseUser> findAllUsers() {
    return (List<BaseUser>)baseUserRepository.findAll();
  }
}
