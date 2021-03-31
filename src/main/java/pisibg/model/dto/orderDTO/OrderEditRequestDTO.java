package pisibg.model.dto.orderDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
@Setter
@Getter
@NoArgsConstructor
@Component
public class OrderEditRequestDTO {
    private String address;
    private double grossValue;
    @Nullable
    private double discount;
    private double netValue;
    @JsonProperty(value = "isPaid")
    private boolean isPaid;
}

