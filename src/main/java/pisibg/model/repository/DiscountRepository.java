package pisibg.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.model.pojo.Category;
import pisibg.model.pojo.Discount;

import java.util.Set;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {

    Discount findByDescription(String description);
    Discount getById(Integer discountId);
}
