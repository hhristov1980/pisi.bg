package pisibg.model.dto.orderDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@Component
public class OrderRequestDTO {
    private String address;
    @NotNull(message = "Payment method cannot be null!")
    @Min(value = 1,message = "Minimum Payment method id should be 1")
    private int paymentMethodId;
}
