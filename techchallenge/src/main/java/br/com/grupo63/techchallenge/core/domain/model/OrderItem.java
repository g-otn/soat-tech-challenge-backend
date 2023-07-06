package br.com.grupo63.techchallenge.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class OrderItem extends Domain {

    private Long quantity;
    private Double price;
    private Order order;
    private Product product;

    public OrderItem(Long quantity, Double price, Long productId) {
        this.quantity = quantity;
        this.price = price;
        this.product.setId(productId);
    }
}
