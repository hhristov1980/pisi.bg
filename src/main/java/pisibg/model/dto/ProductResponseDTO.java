package pisibg.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.model.pojo.Discount;
import pisibg.model.pojo.Manufacturer;
import pisibg.model.pojo.Product;
import pisibg.model.pojo.Subcategory;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ProductResponseDTO {
    private int id;
    private String name;
    private String description;
    private String producerName;
    private String subcategoryName;
//    private Manufacturer manufacturer;
//    private Subcategory subcategory;
    private int quantity;
    private double price;
    private Integer discountPercent;
//    private Discount discount;

    public ProductResponseDTO(Product product){
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        producerName = product.getManufacturer().getProducerName();
        subcategoryName = product.getSubcategory().getName();
//        manufacturer = product.getManufacturer();
//        subcategory = product.getSubcategory();
        quantity = product.getQuantity();
        price = product.getPrice();
        if(product.getDiscount() == null){
            discountPercent = null;
        }
        else {
            discountPercent = product.getDiscount().getPercent();
        }

//        discount = product.getDiscount();
    }
}
