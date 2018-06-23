package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import server.models.BaseUser;
import server.models.Customer;
import server.models.Deliverer;
import server.models.Order;
import server.models.OrderItem;
import server.models.Restaurateur;
import server.repositories.BaseUserRepository;
import server.repositories.CustomerRepository;
import server.repositories.DelivererRepository;
import server.repositories.OrderRepository;
import server.repositories.RestaurateurRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OrderService {
  @Autowired
  OrderRepository orderRepository;
  @Autowired
  RestaurateurRepository restaurateurRepository;
  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  DelivererRepository delivererRepository;
  @Autowired
  BaseUserRepository baseUserRepository;


  @PostMapping("/api/order/restaurateur/{restaurateurId}")
  public Order createOrder(@PathVariable("restaurateurId") int restaurateurId,
                           @RequestBody Order order,
                           HttpServletResponse response, HttpServletRequest request) {
    // find restaurateur
    Optional<Restaurateur> restaurateurOptional = restaurateurRepository.findById(restaurateurId);

    // assign deliverer
    List<Deliverer> deliverers = (List<Deliverer>)delivererRepository.findAll();
    int randomNum = (int)Math.random() * (deliverers.size());
    Deliverer deliverer = deliverers.get(randomNum);

    // retrieve customer using session
    HttpSession session = request.getSession(false);
    BaseUser currentUser = null;
    Optional<Customer> customerOptional = null;
    if (session == null) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return null;
    }
    else {
      currentUser = (BaseUser)session.getAttribute("user");
      customerOptional = customerRepository.findById(currentUser.getId());
    }

    if (restaurateurOptional.isPresent() && customerOptional.isPresent()) {
      order.setRestaurateur(restaurateurOptional.get());
      order.setDeliverer(deliverer);
      order.setCustomer(customerOptional.get());
      return orderRepository.save(order);
    }

    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }



//  @PostMapping("/api/order/{orderId}/item")
//  public Order saveItemToOrder(@PathVariable("orderId") int orderId,
//                               @RequestBody OrderItem item) {
//
//  }
}
