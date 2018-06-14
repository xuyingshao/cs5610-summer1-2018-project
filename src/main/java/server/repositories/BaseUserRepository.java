package server.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import javax.persistence.GeneratedValue;

import server.models.BaseUser;

public interface BaseUserRepository extends CrudRepository<BaseUser, Integer> {

  @Query("SELECT u FROM BaseUser u WHERE u.username=:username")
  Optional<BaseUser> findUserByUsername(
          @Param("username") String username
  );


  @Query("SELECT u FROM BaseUser u WHERE u.username=:username AND u.password=:password")
  Optional<BaseUser> findUserByCredentials(
          @Param("username") String username,
          @Param("password") String password
  );
}
