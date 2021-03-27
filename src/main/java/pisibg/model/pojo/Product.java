package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.model.dto.ProductRequestDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    @JsonBackReference
    private Manufacturer manufacturer;
    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    @JsonBackReference
    private Subcategory subcategory;
    private int quantity;
    private double price;
    @ManyToOne
    @JoinColumn(name = "discount_id")
    @JsonBackReference
    private Discount discount;
    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private Set<Image> images;

    public Product(ProductRequestDTO productRequestDTO){
        name = productRequestDTO.getName();
        description = productRequestDTO.getDescription();
        quantity = productRequestDTO.getQuantity();
        price = productRequestDTO.getPrice();
    }
}
