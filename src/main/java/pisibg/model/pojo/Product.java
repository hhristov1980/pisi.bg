package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;

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
    private double price;
    @ManyToOne
    @JoinColumn(name = "discount_id")
    @JsonBackReference
    private Discount discount;
    private ArrayList<Image> images;
}
