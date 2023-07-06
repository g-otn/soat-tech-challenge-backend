package br.com.grupo63.techchallenge.core.application.repository;

import br.com.grupo63.techchallenge.core.domain.model.Order;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends IRepository<Order> {

    List<Order> findByStatusDoneAndDeletedFalseOrderByCreationDate();
    Optional<Order> findByIdAndDeletedFalse(Long id);
    Order saveAndFlush(Order order);

}
