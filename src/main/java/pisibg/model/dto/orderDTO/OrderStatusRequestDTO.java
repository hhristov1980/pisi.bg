package pisibg.model.dto.orderDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Setter
@Getter
@Component
public class OrderStatusRequestDTO {
    @NotBlank(message = "Order status type is mandatory!")
    private String type;

}
