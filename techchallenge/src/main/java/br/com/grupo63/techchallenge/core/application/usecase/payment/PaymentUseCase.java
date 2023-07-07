package br.com.grupo63.techchallenge.core.application.usecase.payment;

import br.com.grupo63.techchallenge.core.application.external.payment.IMercadoPagoService;
import br.com.grupo63.techchallenge.core.application.usecase.dto.OrderDTO;
import br.com.grupo63.techchallenge.core.application.usecase.dto.PaymentDTO;
import br.com.grupo63.techchallenge.core.application.usecase.exception.NotFoundException;
import br.com.grupo63.techchallenge.core.application.usecase.exception.ValidationException;
import br.com.grupo63.techchallenge.core.application.usecase.order.OrderUseCase;
import br.com.grupo63.techchallenge.core.domain.model.Order;
import br.com.grupo63.techchallenge.core.domain.model.payment.Payment;
import br.com.grupo63.techchallenge.core.domain.model.payment.PaymentMethod;
import br.com.grupo63.techchallenge.core.domain.model.payment.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentUseCase implements IPaymentUseCase {

    private final MessageSource messageSource;

    private final IMercadoPagoService mercadoPagoService;
    private final OrderUseCase orderUseCase;

    @Override
    public String startPayment(@NotNull(message = "payment.order.id.notNull") Long orderId) throws NotFoundException {
        OrderDTO orderDTO = orderUseCase.read(orderId);

        String qrData = mercadoPagoService.generateQRCode(orderDTO.getId(), orderDTO.getTotalPrice());

        orderDTO.setPaymentDTO(new PaymentDTO(PaymentStatus.PENDING, PaymentMethod.MERCADO_PAGO_QR_CODE, qrData));
        orderUseCase.update(orderDTO, orderId);
        return qrData;
    }

    @Override
    public void finishPayment(@NotNull(message = "payment.order.id.notNull") Long orderId) throws ValidationException, NotFoundException {
        OrderDTO orderDTO = orderUseCase.read(orderId);

        if (orderDTO.getPaymentDTO() == null || PaymentStatus.PAID.equals(orderDTO.getPaymentDTO().getStatus())) {
            throw new ValidationException(
                    messageSource.getMessage("payment.confirm.title", null, LocaleContextHolder.getLocale()),
                    messageSource.getMessage("payment.confirm.alreadyPaid", null, LocaleContextHolder.getLocale())
            );
        }

        orderDTO.getPaymentDTO().setStatus(PaymentStatus.PAID);
        orderUseCase.update(orderDTO, orderId);
        orderUseCase.advanceOrderStatus(orderId);
    }

    @Override
    public PaymentStatus getPaymentStatus(@NotNull Long orderId) throws NotFoundException, ValidationException {
        OrderDTO orderDTO = orderUseCase.read(orderId);

        if (orderDTO.getPaymentDTO() == null) {
            throw new ValidationException(
                    messageSource.getMessage("payment.confirm.title", null, LocaleContextHolder.getLocale()),
                    messageSource.getMessage("payment.notStarted", null, LocaleContextHolder.getLocale())
            );
        }

        return orderDTO.getPaymentDTO().getStatus();
    }
}