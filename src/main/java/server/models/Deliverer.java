package server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "DELIVERER_PER_CLASS")
public class Deliverer extends BaseUser {
  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;
  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;
  @OneToMany(mappedBy = "deliverer",
          cascade = CascadeType.ALL,
          orphanRemoval = true)
  @JsonIgnore
  private List<Order> orders;

  public Deliverer() {}

  public Deliverer(BaseUser user) {
    this.setUsername(user.getUsername());
    this.setPassword(user.getPassword());
    this.setPhone(user.getPhone());
    this.setEmail(user.getEmail());
    this.firstName = "";
    this.lastName = "";
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

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }
}
