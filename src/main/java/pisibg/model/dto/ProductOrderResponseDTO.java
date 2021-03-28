package pisibg.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.Product;

@NoArgsConstructor
@Setter
@Getter
@Component
public class ProductOrderResponseDTO {
    private int id;
    private String name;
    private String description;
    private String producerName;
    private String subcategoryName;
    private double price;
    private int discountPercent;
    private int quantity;

    public ProductOrderResponseDTO(Product product, int quantity){
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        producerName = product.getManufacturer().getProducerName();
        subcategoryName = product.getSubcategory().getName();
        price = product.getPrice();
        discountPercent = product.getDiscount().getPercent();
        this.quantity = quantity;
    }
}
