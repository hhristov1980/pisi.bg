package pisibg.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.model.pojo.Manufacturer;
import pisibg.model.pojo.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
