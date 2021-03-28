package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.CategoryRequestDTO;
import pisibg.model.dto.CategoryResponseDTO;
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

    @PutMapping("/users/{user_id}/checkout")
    public OrderResponseDTO checkout(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody OrderRequestDTO orderRequestDTO){
        if(sessionManager.getLoggedUser(ses)==null){
            throw new AuthenticationException("You have to be logged in!");
        }
        else {
            User user = sessionManager.getLoggedUser(ses);
            if (userId != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            else {
                return orderService.checkout(orderRequestDTO, ses, userId);
            }
        }
    }
}
