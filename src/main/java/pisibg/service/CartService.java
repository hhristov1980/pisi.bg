package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pisibg.controller.SessionManager;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.exceptions.OutOfStockException;
import pisibg.model.dto.cartDTO.CartPriceResponseDTO;
import pisibg.model.dto.productDTO.ProductOrderRequestDTO;
import pisibg.model.dto.productDTO.ProductOrderResponseDTO;
import pisibg.model.pojo.Discount;
import pisibg.model.pojo.Product;
import pisibg.model.pojo.User;
import pisibg.model.repository.DiscountRepository;
import pisibg.model.repository.ProductRepository;
import pisibg.model.repository.UserRepository;
import pisibg.utility.Constants;
import pisibg.utility.RoundFloat;

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


    public ProductOrderResponseDTO addProd(ProductOrderRequestDTO orderDto, Map<Integer, Queue<ProductOrderResponseDTO>> cart) {
        Product product = productRepository.getById(orderDto.getId());
        if (product != null) {
            if (product.getQuantity() >= orderDto.getQuantity()) {
                for (int i = 0; i < orderDto.getQuantity(); i++) {
                    if (!cart.containsKey(product.getId())) {
                        cart.put(orderDto.getId(), new LinkedList<>());
                    }
                    cart.get(orderDto.getId()).offer(new ProductOrderResponseDTO(product, 1));
                }
                return new ProductOrderResponseDTO(product, orderDto.getQuantity());
            } else {
                throw new OutOfStockException("Not enough quantity!");
            }
        } else {
            throw new NotFoundException("Product not found!");
        }
    }

    public ProductOrderResponseDTO removeProd(ProductOrderRequestDTO orderDto, Map<Integer, Queue<ProductOrderResponseDTO>> cart) {
        if (!cart.isEmpty()) {
            if (cart.containsKey(orderDto.getId())) {
                if (cart.get(orderDto.getId()).size() >= orderDto.getQuantity()) {
                    for (int i = 0; i < orderDto.getQuantity(); i++) {
                        cart.get(orderDto.getId()).poll();
                    }
                    Product product = productRepository.getById(orderDto.getId());
                    return new ProductOrderResponseDTO(product, orderDto.getQuantity());
                } else {
                    throw new BadRequestException("You can't remove more item than available in your cart");
                }
            } else {
                throw new NotFoundException("Product not found!");
            }
        } else {
            throw new NotFoundException("Cart is empty!");
        }
    }

    public void emptyCart(Map<Integer, Queue<ProductOrderResponseDTO>> cart) {
        if (!cart.isEmpty()) {
            for (Map.Entry<Integer, Queue<ProductOrderResponseDTO>> products : cart.entrySet()) {
                int quantity = products.getValue().size();
                if (quantity > 0) {
                    Product product = productRepository.getById(products.getValue().peek().getId());
                    product.setQuantity(product.getQuantity() + quantity);
                    productRepository.save(product);
                }
            }
            cart.clear();
        } else {
            throw new NotFoundException("Cart not found!");
        }
    }

    public CartPriceResponseDTO checkout(Map<Integer, Queue<ProductOrderResponseDTO>> cart, User user) {
        double priceWithoutDiscount = 0.0;
        double priceAfterDiscount = 0.0;
        double discountAmount = 0.0;
        if (!cart.isEmpty()) {
            Set<Product> allProducts = new HashSet<>();
            for (Map.Entry<Integer, Queue<ProductOrderResponseDTO>> products : cart.entrySet()) {
                int quantity = products.getValue().size();
                if (quantity > 0) {
                    Product product = productRepository.getById(products.getValue().peek().getId());
                    product.setQuantity(quantity);
                    allProducts.add(product);
                    double productPrice = product.getPrice();
                    Discount discount = productRepository.getById(products.getValue().peek().getId()).getDiscount();
                    int discountPercent = 0;
                    if (discount == null) {
                        discountPercent = user.getPersonalDiscount();
                    } else {
                        discountPercent = discountRepository.getById(discount.getId()).getPercent();
                    }
                    priceWithoutDiscount += RoundFloat.round((productPrice * quantity), Constants.TWO_DECIMAL_PLACES);
                    discountAmount += RoundFloat.round(priceWithoutDiscount * discountPercent * 1.0 / 100, Constants.TWO_DECIMAL_PLACES);
                }
            }
            priceAfterDiscount = RoundFloat.round((priceWithoutDiscount - discountAmount),2);
            CartPriceResponseDTO cartPriceResponseDTO = new CartPriceResponseDTO();
            cartPriceResponseDTO.setProducts(allProducts);
            cartPriceResponseDTO.setPriceWithoutDiscount(priceWithoutDiscount);
            cartPriceResponseDTO.setDiscountAmount(discountAmount);
            cartPriceResponseDTO.setPriceAfterDiscount(priceAfterDiscount);
            return cartPriceResponseDTO;
        } else {
            throw new NotFoundException("Cart not found!");
        }
    }

    @Transactional
    public boolean checkProductsAndRemoveFromDB(Map<Integer, Queue<ProductOrderResponseDTO>> cart) {
        if (!cart.isEmpty()) {
            for (Map.Entry<Integer, Queue<ProductOrderResponseDTO>> products : cart.entrySet()) {
                int orderQuantity = products.getValue().size();
                System.out.println("Order quantity" + orderQuantity);
                if (orderQuantity > 0) {
                    Product product = productRepository.getById(products.getValue().peek().getId());
                    int quantityDB = product.getQuantity();
                    System.out.println("quantity in DB  = " + quantityDB);
                    if (orderQuantity <= quantityDB) {
                        int updatedQuantity = quantityDB - orderQuantity;
                        product.setQuantity(updatedQuantity);
                        productRepository.save(product);
                        System.out.println("Updated quantity" + updatedQuantity);
                        System.out.println("Quantity in DB " + productRepository.getOne(products.getValue().peek().getId()).getQuantity());
                    } else {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}