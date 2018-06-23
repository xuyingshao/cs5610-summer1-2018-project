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

import server.models.BaseUser;
import server.models.Dish;
import server.models.Order;
import server.models.Restaurant;
import server.repositories.BaseUserRepository;
import server.repositories.CustomerRepository;
import server.repositories.DelivererRepository;
import server.repositories.DishRepository;
import server.repositories.OrderRepository;
import server.repositories.RestaurantRepository;
import server.repositories.RestaurateurRepository;

@RestController
@CrossOrigin(origins = "*")
public class OrderService {
  @Autowired
  BaseUserRepository baseUserRepository;
  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  DelivererRepository delivererRepository;
  @Autowired
  RestaurateurRepository restaurateurRepository;
  @Autowired
  OrderRepository orderRepository;


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
