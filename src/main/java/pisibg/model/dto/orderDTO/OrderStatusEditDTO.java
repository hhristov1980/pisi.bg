package pisibg.model.dto.orderDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
@Component
public class OrderStatusEditDTO {
        @NotNull(message = "id cannot be null!")
        @Min(value=1, message="Minimum order status id should be 1")
        private Integer id;
        @NotBlank(message = "Current order status type is mandatory field!")
        private String type;
        @NotBlank(message = "New order status type is mandatory field!")
        private String newType;
}
