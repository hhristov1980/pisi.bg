package pisibg.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pisibg.model.pojo.Discount;
import pisibg.model.pojo.Manufacturer;
import pisibg.model.pojo.Product;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByName(String productName);

    Product getById(Integer productId);

    List<Product> findByDiscountId(int discountId);
}
