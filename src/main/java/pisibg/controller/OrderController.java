package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.OrderRequestDTO;
import pisibg.model.dto.OrderResponseDTO;
import pisibg.model.pojo.User;
import pisibg.service.OrderService;

import javax.servlet.http.HttpSession;

@RestController
public class OrderController {
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private OrderService orderService;

    @PutMapping("/users/{user_id}/pay")
    public OrderResponseDTO pay(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody OrderRequestDTO orderRequestDTO){
        if(sessionManager.getLoggedUser(ses)==null){
            throw new AuthenticationException("You have to be logged in!");
        }
        else {
            User user = sessionManager.getLoggedUser(ses);
            if (userId != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            else {
                return orderService.pay(orderRequestDTO, ses, userId);
            }
        }
    }
}
