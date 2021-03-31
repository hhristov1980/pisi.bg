package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.model.dto.cartDTO.CartPriceResponseDTO;
import pisibg.model.dto.productDTO.ProductOrderRequestDTO;
import pisibg.model.dto.productDTO.ProductOrderResponseDTO;
import pisibg.model.pojo.User;
import pisibg.service.CartService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

@RestController
public class CartController extends AbstractController {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private CartService cartService;


    @PutMapping("/cart")
    public ProductOrderResponseDTO addProduct(@Valid @RequestBody ProductOrderRequestDTO orderDto, HttpSession ses) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            Map<Integer, Queue<ProductOrderResponseDTO>> cart = new LinkedHashMap<>();
            if (ses.getAttribute("cart") == null) {
                ses.setAttribute("cart", cart);
            } else {
                cart = (LinkedHashMap<Integer, Queue<ProductOrderResponseDTO>>) ses.getAttribute("cart");
            }
            return cartService.addProd(orderDto, cart);
        }
    }

    @DeleteMapping("/cart")
    public ProductOrderResponseDTO removeProduct(@Valid @RequestBody ProductOrderRequestDTO orderDto, HttpSession ses) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            if (ses.getAttribute("cart") == null) {
                throw new BadRequestException("You haven't cart!");
            } else {
                Map<Integer, Queue<ProductOrderResponseDTO>> cart = (LinkedHashMap<Integer, Queue<ProductOrderResponseDTO>>) ses.getAttribute("cart");
                return cartService.removeProd(orderDto, cart);
            }
        }
    }

    @DeleteMapping("/cart/all")
    public void emptyCart(HttpSession ses) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            if (ses.getAttribute("cart") == null) {
                throw new BadRequestException("You haven't cart!");
            } else {
                Map<Integer, Queue<ProductOrderResponseDTO>> cart = (LinkedHashMap<Integer, Queue<ProductOrderResponseDTO>>) ses.getAttribute("cart");
                cartService.emptyCart(cart);
            }
        }
    }

    @GetMapping("/cart")
    public CartPriceResponseDTO checkout(HttpSession ses) {
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
            return cartService.checkout(cart, user);
        }
    }

}
