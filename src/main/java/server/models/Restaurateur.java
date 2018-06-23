package server.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RESTAURATEUR_PER_CLASS")
public class Restaurateur extends BaseUser {
  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;
  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;
  @OneToOne
  private Restaurant restaurant;
  @OneToMany(mappedBy = "restaurateur")
  private List<Order> orders;

  public Restaurant getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
