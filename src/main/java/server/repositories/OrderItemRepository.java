package server.repositories;

import org.springframework.data.repository.CrudRepository;

import server.models.OrderItem;

public interface OrderItemRepository
extends CrudRepository<OrderItem, Integer>{
}
