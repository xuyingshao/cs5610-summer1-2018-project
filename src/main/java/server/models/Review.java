package server.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "review")
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @OneToOne
  private Customer customer;
  @OneToOne
  private Deliverer deliverer;
  private ReviewType reviewType;

  public ReviewType getReviewType() {
    return reviewType;
  }

  public void setReviewType(String reviewType) {
    switch (reviewType) {
      case "VERY_SATISFIED":
        this.reviewType = ReviewType.VERY_SATISFIED;
        return;
      case "SATISFIED":
        this.reviewType = ReviewType.SATISFIED;
        return;
      case "FAIR":
        this.reviewType = ReviewType.FAIR;
        return;
      case "UNSATISFIED":
        this.reviewType = ReviewType.UNSATISFIED;
        return;
      case "VERY_UNSATISFIED":
        this.reviewType = ReviewType.VERY_UNSATISFIED;
        return;
      default:
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Deliverer getDeliverer() {
    return deliverer;
  }

  public void setDeliverer(Deliverer deliverer) {
    this.deliverer = deliverer;
  }
}
