package server.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import server.models.Customer;
import server.models.Restaurateur;

public interface RestaurateurRepository extends CrudRepository<Restaurateur, Integer> {
  @Query("SELECT u FROM Restaurateur u WHERE u.username=:username")
  Optional<Restaurateur> findRestaurateurByUsername(
          @Param("username") String username
  );


  @Query("SELECT u FROM Restaurateur u WHERE u.username=:username AND u.password=:password")
  Optional<Restaurateur> findRestaurateurByCredentials(
          @Param("username") String username,
          @Param("password") String password
  );
}
