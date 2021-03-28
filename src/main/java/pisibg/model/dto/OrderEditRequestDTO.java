package pisibg.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private double discount;
    private double netValue;
    @JsonProperty(value = "isPaid")
    private boolean isPaid;
}

