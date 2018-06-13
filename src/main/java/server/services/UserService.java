package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import server.models.BaseUser;
import server.models.Customer;
import server.models.Deliverer;
import server.models.Restaurateur;
import server.repositories.BaseUserRepository;
import server.repositories.CustomerRepository;
import server.repositories.DelivererRepository;
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


  @PutMapping("/api/login/customer")
  public Customer customerLogin(@RequestBody BaseUser user, HttpSession session) {
    String username = user.getUsername();
    String password = user.getPassword();

    Optional<Customer> data = customerRepository.findCustomerByCredentials(username, password);
    if (data.isPresent()) {
      Customer customer = data.get();
      session.setAttribute("user", customer);
      return customer;
    }
    return null;
  }


  @PutMapping("/api/login/deliverer")
  public Deliverer delivererLogin(@RequestBody BaseUser user, HttpSession session) {
    String username = user.getUsername();
    String password = user.getPassword();

    Optional<Deliverer> data = delivererRepository.findDelivererByCredentials(username, password);
    if (data.isPresent()) {
      Deliverer deliverer = data.get();
      session.setAttribute("user", deliverer);
      return deliverer;
    }
    return null;
  }


  @PutMapping("/api/login/restaurateur")
  public Restaurateur restaurateurLogin(@RequestBody BaseUser user, HttpSession session) {
    String username = user.getUsername();
    String password = user.getPassword();

    Optional<Restaurateur> data = restaurateurRepository.findRestaurateurByCredentials(username, password);
    if (data.isPresent()) {
      Restaurateur restaurateur = data.get();
      session.setAttribute("user", restaurateur);
      return restaurateur;
    }
    return null;
  }

  @PostMapping("/api/register/customer")
  public Customer customerRegister(@RequestBody Customer customer, HttpSession session) {
    String username = customer.getUsername();
    Optional<BaseUser> data = baseUserRepository.findUserByUsername(username);
    if (!data.isPresent()) {
      session.setAttribute("user", customer);
      return customerRepository.save(customer);
    }
    return null;
  }

  @PostMapping("/api/register/deliverer")
  public Deliverer delivererRegister(@RequestBody Deliverer deliverer, HttpSession session) {
    String username = deliverer.getUsername();
    Optional<BaseUser> data = baseUserRepository.findUserByUsername(username);
    if (!data.isPresent()) {
      session.setAttribute("user", deliverer);
      return delivererRepository.save(deliverer);
    }
    return null;
  }

  @PostMapping("/api/register/restaurateur")
  public Restaurateur restaurateurRegister(@RequestBody Restaurateur restaurateur, HttpSession session) {
    String username = restaurateur.getUsername();
    Optional<BaseUser> data = baseUserRepository.findUserByUsername(username);
    if (!data.isPresent()) {
      session.setAttribute("user", restaurateur);
      return restaurateurRepository.save(restaurateur);
    }
    return null;
  }


  @PostMapping("/api/logout")
  public void logout(HttpSession session) {
    session.invalidate();
  }
}
