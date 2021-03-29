package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.OrderRequestDTO;
import pisibg.model.dto.OrderResponseDTO;
import pisibg.model.dto.ProductOrderResponseDTO;
import pisibg.model.pojo.User;
import pisibg.service.OrderService;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

@RestController
public class OrderController {
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private OrderService orderService;

    @PutMapping("/pay")
    public OrderResponseDTO pay(HttpSession ses, @RequestBody OrderRequestDTO orderRequestDTO){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            Map<Integer, Queue<ProductOrderResponseDTO>> cart = new LinkedHashMap<>();
            if(ses.getAttribute("cart")==null){
                ses.setAttribute("cart",cart);
            }
            else {
                cart = (LinkedHashMap<Integer, Queue<ProductOrderResponseDTO>>)ses.getAttribute("cart");
            }
            User user = sessionManager.getLoggedUser(ses);
            return orderService.pay(orderRequestDTO, cart, user);
        }
    }
}
