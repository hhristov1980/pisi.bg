package pisibg.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.model.pojo.Category;
import pisibg.model.pojo.Subcategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<Subcategory, Integer> {
    Subcategory findByName(String name);

    Subcategory getByNameAndCategory_Id(String name, int subcategoryId);

    Subcategory getById(int subcategoryId);
}
