package pisibg.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.model.dto.SubCategoryRequestDTO;

import javax.persistence.*;

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

    public Subcategory(SubCategoryRequestDTO subCategoryRequestDTO){
        name = subCategoryRequestDTO.getName();
    }


}
