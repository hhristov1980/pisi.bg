package pisibg.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.MySQLException;
import pisibg.model.dao.ProductDAO;
import pisibg.model.dto.productDTO.*;
import pisibg.model.pojo.User;
import pisibg.service.ProductService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class ProductController extends AbstractController {

    @Autowired
    private ProductService productService;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private ProductDAO productDAO;


    @PostMapping("/products")
    public ProductResponseDTO add(HttpSession ses, @RequestBody ProductRequestDTO productRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You don't have permission for that!");
        }
        return productService.add(productRequestDTO);
    }

    @PutMapping("/products")
    public ProductResponseDTO changeQuantity(HttpSession ses, @RequestBody ProductEditRequestDTO productEditRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }

        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You don't have permission for that!");
        }
        return productService.edit(productEditRequestDTO);

    }

    @DeleteMapping("/products")
    public ProductDeleteResponseDTO deleteProduct(HttpSession ses, @RequestBody ProductDeleteRequestDTO productDeleteRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }

        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You don't have permission for that!");
        }
        return productService.delete(productDeleteRequestDTO);

    }

//    @GetMapping("/products")
//    public List<ProductResponseDTO> getAll() {
//        return productService.getAll();
//    }

    @GetMapping("/products/{id}")
    public ProductResponseDTO getById(@PathVariable(name = "id") int productId) {
        return productService.getById(productId);
    }

    @PostMapping("/products/filter")
    public List<ProductFilterResponseDTO> getAll(@RequestBody ProductFilterRequestDTO productFilterRequestDTO) {
        try {
            return productDAO.getProducts(productFilterRequestDTO);
        } catch (SQLException throwables) {
            throw new MySQLException("Something get wrong!");
        }
    }

    @PostMapping("/products/filter/admin")
    public List<ProductAdminFilterResponseDTO> getAll(HttpSession ses, @RequestBody ProductAdminFilterRequestDTO productAdminFilterRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You don't have permission for that!");
        }
        try {
            return productDAO.getAdminInfoProducts(productAdminFilterRequestDTO);
        } catch (SQLException throwables) {
            throw new MySQLException("Something get wrong!");
        }
    }

    @PostMapping("/products/search")
    public List<ProductFilterResponseDTO> searchProduct(@RequestBody ProductSearchRequestDTO productSearchRequestDTO) {
        try {
            return productDAO.searchProducts(productSearchRequestDTO);
        } catch (SQLException throwables) {
            throw new MySQLException("Something get wrong!");
        }
    }

    @PostMapping("/products/search/admin")
    public List<ProductAdminFilterResponseDTO> searchAdminProducts(HttpSession ses, @RequestBody ProductSearchRequestDTO productSearchRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You don't have permission for that!");
        }
        try {
            return productDAO.searchAdminProducts(productSearchRequestDTO);
        } catch (SQLException throwables) {
            throw new MySQLException("Something get wrong!");
        }
    }

}
