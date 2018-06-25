package server.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import server.models.Customer;
import server.models.Dish;

public interface DishRepository
        extends CrudRepository<Dish, Integer> {
}
