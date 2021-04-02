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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Component
public class DiscountEditRequestDTO {
    @NotNull(message = "id cannot be null!")
    @Min(value=1, message="Minimum discount id should be 1")
    private Integer id;
    @NotBlank(message = "Current description is mandatory!")
    private String currentDescription;
    @NotBlank(message = "New description is mandatory!")
    private String newDescription;
    @NotNull(message = "Percent cannot be null!")
    @Min(value=1, message="Minimum percent should be 1")
    @Max(value=50, message="Maximum percent should be 50")
    private Integer currentPercent;
    @NotNull(message = "New percent cannot be null!")
    @Min(value=1, message="New minimum percent should be 1")
    @Max(value=50, message="Maximum percent should be 50")
    private Integer newPercent;
    @NotNull(message = "Current fromDate field cannot be null!")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("currentFromDate")
    private LocalDateTime currentFromDate;
    @NotNull(message = "New fromDate field cannot be null!")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("newFromDate")
    private LocalDateTime newFromDate;
    @NotNull(message = "Current toDate field cannot be null!")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("currentToDate")
    private LocalDateTime currentToDate;
    @NotNull(message = "New toDate field cannot be null!")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("newToDate")
    private LocalDateTime newToDate;
    @NotNull(message = "Current isActive field cannot be null!")
    @JsonProperty(value = "currentIsActive")
    private Boolean currentIsActive;
    @NotNull(message = "New isActive field cannot be null!")
    @JsonProperty(value = "newIsActive")
    private Boolean newIsActive;

}
