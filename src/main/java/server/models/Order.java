package server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "_ORDER")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdTime;
  private double total;
  private boolean delivered;
//  @OneToMany(mappedBy = "order")
//  private List<Dish> dishes;
  @OneToMany(mappedBy = "order")       // FIXME
  private List<OrderItem> items;
  @ManyToOne
  @JsonIgnore
  private Customer customer;
  @ManyToOne
  @JsonIgnore
  private Restaurant restaurant;
  @ManyToOne
  @JsonIgnore
  private Deliverer deliverer;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  public boolean isDelivered() {
    return delivered;
  }

  public void setDelivered(boolean delivered) {
    this.delivered = delivered;
  }

  public List<OrderItem> getItems() {
    return items;
  }

  public void setItems(List<OrderItem> items) {
    this.items = items;
  }

  //  public List<Dish> getDishes() {
//    return dishes;
//  }
//
//  public void setDishes(List<Dish> dishes) {
//    this.dishes = dishes;
//  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }

  public Deliverer getDeliverer() {
    return deliverer;
  }

  public void setDeliverer(Deliverer deliverer) {
    this.deliverer = deliverer;
  }
}
