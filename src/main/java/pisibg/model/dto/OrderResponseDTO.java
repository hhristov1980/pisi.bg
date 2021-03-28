package pisibg.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

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
    private double grossValue;
    private double discount;
    private double netValue;
    @JsonProperty(value = "isPaid")
    private boolean isPaid;
    private Set<Product> products;

    public OrderResponseDTO(Order order){
        id =  order.getId();
        userNames = order.getUser().getFirstName()+" "+order.getUser().getLastName();
        address = order.getAddress();
        paymentMethodType = order.getPaymentMethod().getType();
        createdAt = order.getCreatedAt();
        grossValue = order.getGrossValue();
        discount = order.getDiscount();
        netValue = order.getNetValue();
        isPaid = order.isPaid();
    }

}
