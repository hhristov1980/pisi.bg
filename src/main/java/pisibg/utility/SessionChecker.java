package pisibg.utility;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pisibg.controller.SessionManager;
import pisibg.model.dto.ProductOrderResponseDTO;
import pisibg.model.pojo.Product;
import pisibg.model.repository.ProductRepository;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

@NoArgsConstructor
@Setter
@Getter
@Component
@Service
public class SessionChecker extends Thread{
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private ProductRepository productRepository;
    private HttpSession ses;
    private Map<Integer, Queue<ProductOrderResponseDTO>> cart;
    private int maxInactiveIntervalBeforeEmptyCart;



    @Override
    public void run() {
        //TODO fix 60 to variable
        maxInactiveIntervalBeforeEmptyCart = ses.getMaxInactiveInterval()-40;
        while (true){
            if(System.currentTimeMillis()-ses.getLastAccessedTime()<maxInactiveIntervalBeforeEmptyCart){
                cart = (LinkedHashMap<Integer,Queue<ProductOrderResponseDTO>>)ses.getAttribute("cart");
            }
            else {
                if(!cart.isEmpty()){
                    for(Map.Entry<Integer, Queue<ProductOrderResponseDTO>> products: cart.entrySet()){
                        int quantity = products.getValue().size();
                        if(quantity>0){
                            Product product = productRepository.findById(products.getValue().peek().getId());
                            product.setQuantity(product.getQuantity() + quantity);
                            productRepository.save(product);
                        }
                    }
                    cart.clear();
                    ses.removeAttribute("cart");
                }
                break;
            }
        }
    }
}
