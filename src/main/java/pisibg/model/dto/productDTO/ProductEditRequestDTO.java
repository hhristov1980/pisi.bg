package pisibg.model.dto.productDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
@Component
@Validated
public class ProductEditRequestDTO {
    @NotNull(message = "Product id cannot be null!")
    @Min(value=1, message="Minimum product id should be 1")
    private Integer id;
    @NotBlank(message = "Product current name is mandatory!")
    private String currentName;
    @NotBlank(message = "Product new name is mandatory!")
    private String newName;
    @NotBlank(message = "Product current description is mandatory!")
    private String currentDescription;
    @NotBlank(message = "Product new description is mandatory!")
    private String newDescription;
    @NotNull(message = "Current manufacturerId cannot be null!")
    @Min(value=1, message="Minimum current manufacturerId should be 1")
    private Integer currentManufacturerId;
    @NotNull(message = "New manufacturerId cannot be null!")
    @Min(value=1, message="Minimum new manufacturerId should be 1")
    private Integer newManufacturerId;
    @NotNull(message = "Current subcategoryId cannot be null!")
    @Min(value=1, message="Minimum current subcategoryId should be 1")
    private Integer currentSubcategoryId;
    @NotNull(message = "New subcategoryId cannot be null!")
    @Min(value=1, message="Minimum new subcategoryId should be 1")
    private Integer newSubcategoryId;
    @NotNull(message = "Current quantity cannot be null!")
    @Min(value=0, message="Minimum Current quantity should be 0")
    private Integer currentQuantity;
    @NotNull(message = "New quantity cannot be null!")
    @Min(value=0, message="Minimum new quantity should be 0")
    private Integer newQuantity;
    @NotNull(message = "Current price cannot be null!")
    private Double currentPrice;
    @NotNull(message = "New price cannot be null!")
    private Double newPrice;
    private Integer currentDiscountId;
    private Integer newDiscountId;
}
