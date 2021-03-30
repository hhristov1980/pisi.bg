package pisibg.model.dto.orderDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.OrderStatus;

@NoArgsConstructor
@Setter
@Getter
@Component
public class OrderStatusResponseDTO {

    private String type;

    public OrderStatusResponseDTO(OrderStatus status){
        this.type = status.getType();
    }
}
