package pisibg.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.model.pojo.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
