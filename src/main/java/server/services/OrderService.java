package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
import server.repositories.OrderItemRepository;
import server.repositories.OrderRepository;
import server.repositories.RestaurateurRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
//@CrossOrigin(origins = "https://cs5610-project-client.herokuapp.com", allowCredentials = "true")
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
  @Autowired
  OrderItemRepository orderItemRepository;

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
      order = orderRepository.save(order);

      List<OrderItem> items = order.getItems();
      for (OrderItem item: items) {
        item.setOrder(order);
        orderItemRepository.save(item);
      }

      return orderRepository.save(order);
    }

    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }



  @GetMapping("/api/order/{orderId}/users")
  public List<BaseUser> findUsersForOrder(@PathVariable("orderId") int orderId) {
    Optional<Order> data = orderRepository.findById(orderId);
    if (data.isPresent()) {
      Order order = data.get();
      List<BaseUser> res = new ArrayList<BaseUser>();
      res.add(order.getCustomer());
      res.add(order.getRestaurateur());
      res.add(order.getDeliverer());
      return res;
    }
    return null;
  }

  @DeleteMapping("/api/order/{orderId}")
  public void deleteOrderForCustomer(@PathVariable("orderId") int orderId) {
    orderRepository.deleteById(orderId);
  }

  @PutMapping("/api/order/{orderId}/finish")
  public void finishOrder(@PathVariable("orderId") int orderId) {
    Optional<Order> data = orderRepository.findById(orderId);
    if (data.isPresent()) {
      Order order = data.get();
      order.setDelivered(!order.isDelivered());
      orderRepository.save(order);
    }
  }

  @GetMapping("/api/user/{userId}/orders")
  public List<Order> findOrdersForUser(@PathVariable("userId") int userId) {
    Optional<BaseUser> data = baseUserRepository.findById(userId);
    if (data.isPresent()) {
      BaseUser user = data.get();
      List<Order> res = new ArrayList<Order>();
      for (Order o : orderRepository.findAll()) {
        if (o.getCustomer().getId() == userId
                || o.getRestaurateur().getId() == userId
                || o.getDeliverer().getId() == userId) {
          res.add(o);
        }
      }
      return res;
    }
    return null;
  }
}
