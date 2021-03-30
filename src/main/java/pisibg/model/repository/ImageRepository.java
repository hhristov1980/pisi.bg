package pisibg.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.model.pojo.Discount;
import pisibg.model.pojo.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image getById(int imageId);
}
