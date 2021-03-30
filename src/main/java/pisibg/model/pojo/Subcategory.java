package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.model.dto.subcategoryDTO.SubCategoryRequestDTO;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "subcategories")
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

    public Subcategory(SubCategoryRequestDTO subCategoryRequestDTO) {
        name = subCategoryRequestDTO.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subcategory that = (Subcategory) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
