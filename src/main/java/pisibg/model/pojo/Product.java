package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products")
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
    @ManyToMany(mappedBy = "products")
    @JsonBackReference
    private Set<Order> order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

