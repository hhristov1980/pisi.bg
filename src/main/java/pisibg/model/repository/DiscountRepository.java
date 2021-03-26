package pisibg.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.model.pojo.Category;
import pisibg.model.pojo.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer> {

    Discount findByDescription(String description);
}
