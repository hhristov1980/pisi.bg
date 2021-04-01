package pisibg.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.model.pojo.Discount;
import pisibg.model.pojo.Manufacturer;
import pisibg.model.pojo.User;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {

    Manufacturer findByProducerName(String producerName);

    Manufacturer getById(Integer manufacturerId);
}
