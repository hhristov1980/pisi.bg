package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.exceptions.OutOfStockException;
import pisibg.model.dto.ProductOrderRequestDTO;
import pisibg.model.dto.ProductOrderResponseDTO;
import pisibg.model.pojo.Product;
import pisibg.model.repository.ProductRepository;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    public ProductOrderResponseDTO buy(ProductOrderRequestDTO orderDto,Map<Integer, Queue<ProductOrderResponseDTO>> cart,HttpSession ses){
        Product product = productRepository.findById(orderDto.getId());
        if(product!=null) {
            if (product.getQuantity() >= orderDto.getQuantity()) {
                for (int i = 0; i < orderDto.getQuantity(); i++) {
                    if (!cart.containsKey(product.getId())) {
                        cart.put(orderDto.getId(), new LinkedList<>());
                    }
                    cart.get(orderDto.getId()).offer(new ProductOrderResponseDTO(product));
                }
                product.setQuantity(product.getQuantity() - orderDto.getQuantity());
                productRepository.save(product);
                ses.setAttribute("cart",cart);
                return new ProductOrderResponseDTO(product);
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
                        return new ProductOrderResponseDTO(product);
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
                throw new NotFoundException("Car not found!");
            }
        }
    }
}
