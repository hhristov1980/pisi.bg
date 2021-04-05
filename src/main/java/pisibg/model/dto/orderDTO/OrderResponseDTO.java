package pisibg.model.dto.orderDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.dto.productDTO.ProductOrderResponseDTO;
import pisibg.model.pojo.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;

@Setter
@Getter
@NoArgsConstructor
@Component
public class OrderResponseDTO {

    private int id;
    private String userNames;
    private String orderStatus;
    private String address;
    private String paymentMethodType;
    private LocalDateTime createdAt;
    private BigDecimal grossValue;
    private BigDecimal discount;
    private BigDecimal netValue;
    @JsonProperty(value = "isPaid")
    private boolean isPaid;
    private int totalProducts;

    public OrderResponseDTO( Order order, Map<Integer, Queue<ProductOrderResponseDTO>> cart){
        id =  order.getId();
        userNames = order.getUser().getFirstName()+" "+order.getUser().getLastName();
        address = order.getAddress();
        orderStatus=order.getOrderStatus().getType();
        paymentMethodType = order.getPaymentMethod().getType();
        createdAt = order.getCreatedAt();
        grossValue = order.getGrossValue();
        discount = order.getDiscount();
        netValue = order.getNetValue();
        isPaid = order.isPaid();
        for(Map.Entry<Integer, Queue<ProductOrderResponseDTO>> p: cart.entrySet()) {
            totalProducts += p.getValue().size();
        }

    }
}
