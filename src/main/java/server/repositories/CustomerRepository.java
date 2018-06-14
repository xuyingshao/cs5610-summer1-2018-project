package server.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import server.models.BaseUser;
import server.models.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

  @Query("SELECT u FROM Customer u WHERE u.username=:username")
  Optional<Customer> findCustomerByUsername(
          @Param("username") String username
  );


  @Query("SELECT u FROM Customer u WHERE u.username=:username AND u.password=:password")
  Optional<Customer> findCustomerByCredentials(
          @Param("username") String username,
          @Param("password") String password
  );
}
