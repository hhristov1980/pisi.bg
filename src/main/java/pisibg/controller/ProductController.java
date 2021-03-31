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
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ProductController extends AbstractController {
    static Logger log = Logger.getLogger(ProductController.class.getName());
    @Autowired
    private ProductService productService;
    @Autowired
    private SessionManager sessionManager;



    @PostMapping("/products")
    public ProductResponseDTO add(HttpSession ses,@Valid @RequestBody ProductRequestDTO productRequestDTO) {
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
    public ProductResponseDTO changeQuantity(HttpSession ses,@Valid @RequestBody ProductEditRequestDTO productEditRequestDTO) {
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
    public ProductDeleteResponseDTO deleteProduct(HttpSession ses,@Valid @RequestBody ProductDeleteRequestDTO productDeleteRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }

        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You don't have permission for that!");
        }
        return productService.delete(productDeleteRequestDTO);

    }

    @GetMapping("/products/{id}")
    public ProductResponseDTO getById(@PathVariable(name = "id") int productId) {
        return productService.getById(productId);
    }
<<<<<<< HEAD

    @PostMapping("/products/filter")
    public List<ProductFilterResponseDTO> getAll(@RequestBody ProductFilterRequestDTO productFilterRequestDTO) {
        try {
            return productDAO.getProducts(productFilterRequestDTO);
        } catch (SQLException throwables) {
            log.log(Level.ALL,throwables.getMessage());
            throw new MySQLException("Something get wrong!");
        }
    }

    @PostMapping("/products/filter/admin")
    public List<ProductAdminFilterResponseDTO> getAll(HttpSession ses,@RequestBody ProductAdminFilterRequestDTO productAdminFilterRequestDTO) {
=======
    @GetMapping("/products/{id}/admin")
    public ProductResponseDTO getById(@PathVariable(name = "id") int productId, HttpSession ses) {
>>>>>>> 70c99cf5b32df66de0e173b9cc1e346480b167dc
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }

        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You don't have permission for that!");
        }
<<<<<<< HEAD
        try {
            return productDAO.getAdminInfoProducts(productAdminFilterRequestDTO);
        } catch (SQLException throwables) {
            log.log(Level.ALL,throwables.getMessage());
            throw new MySQLException("Something get wrong!");
        }
=======
        return productService.getById(productId);
>>>>>>> 70c99cf5b32df66de0e173b9cc1e346480b167dc
    }

    @PostMapping("/products/filter")
    public List<ProductResponseDTO> getAll(@RequestBody ProductFilterRequestDTO productFilterRequestDTO) {
        try {
            return productService.getFilterAndSearchProducts(productFilterRequestDTO);
        } catch (SQLException throwables) {
            log.log(Level.ALL,throwables.getMessage());
            throw new MySQLException("Something get wrong!");
        }
    }

<<<<<<< HEAD
    @PostMapping("/products/search/admin")
    public List<ProductAdminFilterResponseDTO> searchAdminProducts(HttpSession ses,@RequestBody ProductSearchRequestDTO productSearchRequestDTO) {
=======
    @PostMapping("/products/filter/admin")
    public List<ProductResponseDTO> getAllAdmin(HttpSession ses, @RequestBody ProductFilterRequestDTO productFilterRequestDTO) {
>>>>>>> 70c99cf5b32df66de0e173b9cc1e346480b167dc
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You don't have permission for that!");
        }
        try {
            return productService.getFilterAndSearchProductsAdmin(productFilterRequestDTO);
        } catch (SQLException throwables) {
            log.log(Level.ALL,throwables.getMessage());
            throw new MySQLException("Something get wrong!");
        }
    }
}
