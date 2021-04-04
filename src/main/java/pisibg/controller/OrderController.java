package pisibg.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.MyServerException;
import pisibg.model.dto.orderDTO.OrderRequestDTO;
import pisibg.model.dto.orderDTO.OrderResponseDTO;
import pisibg.model.dto.productDTO.ProductOrderResponseDTO;
import pisibg.model.pojo.User;
import pisibg.service.OrderService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;

@RestController
public class OrderController extends AbstractController{
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private OrderService orderService;

    @PutMapping("/pay")
    public OrderResponseDTO pay(HttpSession ses,@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            Map<Integer, Queue<ProductOrderResponseDTO>> cart = new LinkedHashMap<>();
            if (ses.getAttribute("cart") == null) {
                ses.setAttribute("cart", cart);
            } else {
                cart = (LinkedHashMap<Integer, Queue<ProductOrderResponseDTO>>) ses.getAttribute("cart");
            }
            User user = sessionManager.getLoggedUser(ses);

            try {
                return orderService.pay(orderRequestDTO, cart, user);
            } catch (SQLException | InterruptedException throwables) {
                String stacktrace = ExceptionUtils.getStackTrace(throwables);
                log.log(Level.ALL,stacktrace);
                throw new MyServerException("Something get wrong!");
            }
        }
    }
}
