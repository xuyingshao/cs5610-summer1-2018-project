package server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMER_PER_CLASS")
public class Customer extends BaseUser {
  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;
  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;
  @Column(name = "ADDRESS", nullable = false)
  private String address;
  @OneToMany(mappedBy = "customer",
          cascade = CascadeType.ALL,
          orphanRemoval = true)
  @JsonIgnore
  private List<Order> orders;

  public Customer() {}

  public Customer(BaseUser user) {
    this.setUsername(user.getUsername());
    this.setPassword(user.getPassword());
    this.setPhone(user.getPhone());
    this.setEmail(user.getEmail());
    this.firstName = "";
    this.lastName = "";
    this.address = "";
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }
}
