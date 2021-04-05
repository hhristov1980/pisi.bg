package pisibg.model.dto.discountDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.Discount;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Component
public class DiscountResponseDTO {
    private int id;
    private String description;
    private int percent;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("fromDate")
    private LocalDateTime fromDate;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("toDate")
    private LocalDateTime toDate;
    @JsonProperty(value = "isActive")
    private boolean isActive;

    public DiscountResponseDTO(Discount discount){
        id = discount.getId();
        description = discount.getDescription();
        percent = discount.getPercent();
        fromDate = discount.getFromDate();
        toDate = discount.getToDate();
        isActive = discount.isActive();
    }
}
