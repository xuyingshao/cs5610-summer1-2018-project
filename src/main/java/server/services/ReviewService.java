package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import server.models.BaseUser;
import server.models.Customer;
import server.models.Deliverer;
import server.models.Review;
import server.repositories.CustomerRepository;
import server.repositories.DelivererRepository;
import server.repositories.ReviewRepository;

@RestController
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@CrossOrigin(origins = "https://cs5610-project-client.herokuapp.com", allowCredentials = "true")
public class ReviewService {
  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  DelivererRepository delivererRepository;
  @Autowired
  ReviewRepository reviewRepository;

  @GetMapping("/api/review/{delivererId}")
  public Review findReview(@PathVariable("delivererId") int delivererId,
                                       HttpServletRequest request, HttpServletResponse response) {
    Optional<Deliverer> delivererOptional = delivererRepository.findById(delivererId);

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

    if (delivererOptional.isPresent() && customerOptional.isPresent()) {
      Deliverer deliverer = delivererOptional.get();
      Customer customer = customerOptional.get();

      List<Review> revHistory = (List<Review>) reviewRepository.findAll();

      for (Review review : revHistory) {
        if (review.getCustomer().getId() == customer.getId()
                && review.getDeliverer().getId() == deliverer.getId()) {
          return review;
        }
      }
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }


  @PostMapping("/api/review/{reviewType}/{delivererId}")
  public Review customerReviewDeliver(@PathVariable("reviewType") String reviewType,
                                    @PathVariable("delivererId") int delivererId,
                                    HttpServletRequest request, HttpServletResponse response) {
    // find deliverer by id
    Optional<Deliverer> delivererOptional = delivererRepository.findById(delivererId);

    // retrieve user by session
    HttpSession session = request.getSession(false);
    BaseUser currentUser = null;
    Optional<Customer> customerOptional = null;
    if (session == null) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return null;
    } else {
      currentUser = (BaseUser) session.getAttribute("user");
      customerOptional = customerRepository.findById(currentUser.getId());
    }

    if (delivererOptional.isPresent() && customerOptional.isPresent()) {
      Deliverer deliverer = delivererOptional.get();
      Customer customer = customerOptional.get();

      List<Review> revHistory = (List<Review>) reviewRepository.findAll();
      for (Review review : revHistory) {
        if (review.getCustomer().getId() == customer.getId()
                && review.getDeliverer().getId() == deliverer.getId()) {
          review.setReviewType(reviewType);
          return reviewRepository.save(review);
        }
      }

      Review rev = new Review();
      rev.setDeliverer(deliverer);
      rev.setCustomer(customer);
      rev.setReviewType(reviewType);
      return reviewRepository.save(rev);
    }
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return null;
  }
}
