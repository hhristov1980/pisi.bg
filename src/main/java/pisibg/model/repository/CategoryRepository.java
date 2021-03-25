package pisibg.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pisibg.model.pojo.Category;
import pisibg.model.pojo.Manufacturer;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Category findByCategoryName(String producerName);
}
