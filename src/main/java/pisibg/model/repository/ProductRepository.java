package pisibg.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.model.pojo.Discount;
import pisibg.model.pojo.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
}
