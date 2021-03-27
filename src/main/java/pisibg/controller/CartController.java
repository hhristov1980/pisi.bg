package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.ProductOrderRequestDTO;
import pisibg.model.dto.ProductOrderResponseDTO;
import pisibg.model.dto.UserEditRequestDTO;
import pisibg.model.dto.UserEditResponseDTO;
import pisibg.model.pojo.User;
import pisibg.service.CartService;

import javax.servlet.http.HttpSession;

@RestController
public class CartController extends AbstractController{

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private CartService cartService;

    @PutMapping("/users/{id}/cart")
    public ProductOrderResponseDTO addProduct(@PathVariable int id, @RequestBody ProductOrderRequestDTO orderDto, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            return cartService.addProd(orderDto,ses);
        }
    }

    @DeleteMapping("/users/{id}/cart")
    public ProductOrderResponseDTO removeProduct(@PathVariable int id, @RequestBody ProductOrderRequestDTO orderDto, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (id != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            return cartService.removeProd(orderDto,ses);
        }
    }

}
