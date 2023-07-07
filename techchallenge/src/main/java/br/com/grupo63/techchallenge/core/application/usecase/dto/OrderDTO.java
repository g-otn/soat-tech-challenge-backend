package br.com.grupo63.techchallenge.core.application.usecase.dto;

import br.com.grupo63.techchallenge.core.domain.model.Client;
import br.com.grupo63.techchallenge.core.domain.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {

    @Schema(defaultValue = "0")
    private Long id;

    @Schema(defaultValue = "15.99")
    private Double totalPrice;

    @Schema(defaultValue = "Recebido")
    private String status;

    private ClientDTO clientDTO;

    private List<OrderItemDTO> itemsDTO = new ArrayList<>();

    private PaymentDTO paymentDTO;

    public void toDomain(Order order) {
        Client client = new Client();

        clientDTO.toDomain(client);

        order.setId(id);
        order.setTotalPrice(totalPrice);
        order.setStatus(status != null ? Order.Status.valueOf(status) : null);
        order.setClient(client);
        order.setItems(itemsDTO.stream().map(OrderItemDTO::toDomain).toList());
        order.setPayment(paymentDTO.toDomain());
    }

    public static OrderDTO toDto(Order order) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(order.getId());
        orderDTO.setStatus(orderDTO.getStatus());
        orderDTO.setTotalPrice(order.getTotalPrice());

        orderDTO.setPaymentDTO(order.getPayment() != null ? PaymentDTO.toDto(order.getPayment()) : null);
        orderDTO.setClientDTO(ClientDTO.toDto(order.getClient()));
        orderDTO.setItemsDTO(order.getItems().stream().map(OrderItemDTO::toDto).toList());

        return orderDTO;
    }
}