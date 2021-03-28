package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.controller.SessionManager;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.exceptions.OutOfStockException;
import pisibg.model.dto.CartPriceResponseDTO;
import pisibg.model.dto.ProductOrderRequestDTO;
import pisibg.model.dto.ProductOrderResponseDTO;
import pisibg.model.pojo.Discount;
import pisibg.model.pojo.Product;
import pisibg.model.repository.DiscountRepository;
import pisibg.model.repository.ProductRepository;
import pisibg.model.repository.UserRepository;
import pisibg.utility.SessionChecker;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DiscountRepository discountRepository;

    public ProductOrderResponseDTO buy(ProductOrderRequestDTO orderDto,Map<Integer, Queue<ProductOrderResponseDTO>> cart,HttpSession ses){
        Product product = productRepository.findById(orderDto.getId());
        if(product!=null) {
            if (product.getQuantity() >= orderDto.getQuantity()) {
                for (int i = 0; i < orderDto.getQuantity(); i++) {
                    if (!cart.containsKey(product.getId())) {
                        cart.put(orderDto.getId(), new LinkedList<>());
                    }
                    cart.get(orderDto.getId()).offer(new ProductOrderResponseDTO(product,1));
                }
                product.setQuantity(product.getQuantity() - orderDto.getQuantity());
                productRepository.save(product);
                ses.setAttribute("cart",cart);
                return new ProductOrderResponseDTO(product, orderDto.getQuantity());
            } else {
                throw new OutOfStockException("Not enough quantity!");
            }
        }else {
            throw new NotFoundException("Product not found!");
        }
    }

    public ProductOrderResponseDTO addProd(ProductOrderRequestDTO orderDto, HttpSession ses) {
        if(ses.getAttribute("cart")==null){
            Map<Integer, Queue<ProductOrderResponseDTO>> cart = new LinkedHashMap<>();
            SessionChecker sessionChecker = new SessionChecker();
            sessionChecker.setCart(cart);
            sessionChecker.setSes(ses);
            sessionChecker.start();
            return buy(orderDto,cart,ses);
        }
        else {
            Map<Integer, Queue<ProductOrderResponseDTO>> cart = (LinkedHashMap<Integer,Queue<ProductOrderResponseDTO>>)ses.getAttribute("cart");
            return buy(orderDto,cart,ses);
        }
    }

    public ProductOrderResponseDTO removeProd(ProductOrderRequestDTO orderDto, HttpSession ses) {
        if(ses.getAttribute("cart")==null){
           throw new NotFoundException("Cart not found!");
        }
        else {
            Map<Integer, Queue<ProductOrderResponseDTO>> cart = (LinkedHashMap<Integer,Queue<ProductOrderResponseDTO>>)ses.getAttribute("cart");
            if(!cart.isEmpty()){
                if(cart.containsKey(orderDto.getId())) {
                    if (cart.get(orderDto.getId()).size() >= orderDto.getQuantity()) {
                        for (int i = 0; i < orderDto.getQuantity(); i++) {
                            cart.get(orderDto.getId()).poll();
                        }
                        Product product = productRepository.findById(orderDto.getId());
                        product.setQuantity(product.getQuantity() + orderDto.getQuantity());
                        productRepository.save(product);
                        return new ProductOrderResponseDTO(product, orderDto.getQuantity());
                    }
                    else {
                        throw new BadRequestException("You can't remove more item than available in your cart");
                    }
                }
                else {
                    throw new NotFoundException("Product not found!");
                }
            }
            else {
                throw new NotFoundException("Cart not found!");
            }
        }
    }

    public void emptyCart(HttpSession ses){
        if(ses.getAttribute("cart")==null){
            throw new NotFoundException("Cart not found!");
        }
        else {
            Map<Integer, Queue<ProductOrderResponseDTO>> cart = (LinkedHashMap<Integer,Queue<ProductOrderResponseDTO>>)ses.getAttribute("cart");
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
            else {
                throw new NotFoundException("Cart not found!");
            }
        }
    }
    public CartPriceResponseDTO calculatePrice(HttpSession ses){
        if(ses.getAttribute("cart")==null){
            throw new NotFoundException("Cart not found!");
        }
        double priceWithoutDiscount = 0.0;
        double priceAfterDiscount = 0.0;
        double discountAmount = 0.0;
        Map<Integer, Queue<ProductOrderResponseDTO>> cart = (LinkedHashMap<Integer,Queue<ProductOrderResponseDTO>>)ses.getAttribute("cart");
        if(!cart.isEmpty()){
            for(Map.Entry<Integer, Queue<ProductOrderResponseDTO>> products: cart.entrySet()){
                int quantity = products.getValue().size();
                if(quantity>0){
                    Product product = productRepository.findById(products.getValue().peek().getId());
                    double productPrice = product.getPrice();
                    int userId = sessionManager.getLoggedUser(ses).getId();
                    Discount discount = product.getDiscount();
                    int discountPercent = 0;
                    if(discount == null){
                        discountPercent = userRepository.getOne(userId).getPersonalDiscount();
                    }
                    else {
                        discountPercent = discountRepository.findById(discount.getId()).getPercent();
                    }
                    priceWithoutDiscount+=(productPrice*quantity);
                    discountAmount+=(productPrice*quantity*(discountPercent*1.0/100));
                }
            }
            priceAfterDiscount = priceWithoutDiscount-discountAmount;
            CartPriceResponseDTO cartPriceResponseDTO = new CartPriceResponseDTO();
            cartPriceResponseDTO.setPriceWithoutDiscount(priceWithoutDiscount);
            cartPriceResponseDTO.setDiscountAmount(discountAmount);
            cartPriceResponseDTO.setPriceAfterDiscount(priceAfterDiscount);
            return cartPriceResponseDTO;
        }
        else {
            throw new NotFoundException("Cart not found!");
        }
    }




}
