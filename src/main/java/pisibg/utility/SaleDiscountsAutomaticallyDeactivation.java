package pisibg.utility;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pisibg.model.pojo.Discount;
import pisibg.model.pojo.Product;
import pisibg.model.repository.DiscountRepository;
import pisibg.model.repository.ProductRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Setter
@Getter
@Component
public class SaleDiscountsAutomaticallyDeactivation extends Thread{
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private ProductRepository productRepository;
    private static final int DISCOUNT_CHECKER_INTERVAL = 1000*60*60; //1 hour


    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(DISCOUNT_CHECKER_INTERVAL);
                discountChecker();
            } catch (InterruptedException e) {
                e.printStackTrace(); //TODO HIDE STACK TRACE
            }
        }
    }

    @Transactional
    protected void discountChecker(){
        List<Discount> discounts = discountRepository.findAll();
        for(Discount d: discounts){
            if(d.isActive()&&d.getToDate().isBefore(LocalDateTime.now())){
                d.setActive(false);
                discountRepository.save(d);
                List<Product> products = productRepository.findByDiscountId(d.getId());
                for(Product p: products){
                    p.setDiscount(null); //Automatically deactivates expired promotions
                    productRepository.save(p);
                }
            }
        }
    }

}
