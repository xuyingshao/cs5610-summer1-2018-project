package server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Restaurant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String yelpId;
  private String name;
  private String image_url;
  private String phone;
  private String display_phone;
  private String address;
  private int rating;
  private String category;
  @OneToMany(mappedBy = "restaurant",
          cascade = CascadeType.ALL,
          orphanRemoval = true)
  private List<Dish> dishes;
  @OneToMany(mappedBy = "restaurant")
  private List<Order> orders;
  @JsonIgnore
  @OneToOne
  private Restaurateur restaurateur;

  public Restaurateur getRestaurateur() {
    return restaurateur;
  }

  public void setRestaurateur(Restaurateur restaurateur) {
    this.restaurateur = restaurateur;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getYelpId() {
    return yelpId;
  }

  public void setYelpId(String yelpId) {
    this.yelpId = yelpId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImage_url() {
    return image_url;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getDisplay_phone() {
    return display_phone;
  }

  public void setDisplay_phone(String display_phone) {
    this.display_phone = display_phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public List<Dish> getDishes() {
    return dishes;
  }

  public void setDishes(List<Dish> dishes) {
    this.dishes = dishes;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }
}
