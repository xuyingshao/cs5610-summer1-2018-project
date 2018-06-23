package server.repositories;

import org.springframework.data.repository.CrudRepository;

import server.models.Favorite;

public interface FavoriteRepository extends CrudRepository<Favorite, Integer> {
}
