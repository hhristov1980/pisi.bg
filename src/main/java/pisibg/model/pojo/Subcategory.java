package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.model.dto.SubCategoryRequestDTO;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="subcategories")
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;
    @OneToMany(mappedBy = "subcategory")
    @JsonManagedReference
    private Set<Product> products;

    public Subcategory(SubCategoryRequestDTO subCategoryRequestDTO){
        name = subCategoryRequestDTO.getName();
    }


}
