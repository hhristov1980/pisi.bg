package pisibg.controller;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.MyServerException;
import pisibg.model.dto.productDTO.*;
import pisibg.model.pojo.User;
import pisibg.service.ProductService;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

@RestController
public class ProductController extends AbstractController {

    @Autowired
    private ProductService productService;
    @Autowired
    private SessionManager sessionManager;



    @PostMapping("/products")
    public ProductResponseDTO add(HttpSession ses, @Valid @RequestBody ProductRequestDTO productRequestDTO) {
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
    public ProductResponseDTO edit(HttpSession ses,@Valid @RequestBody ProductEditRequestDTO productEditRequestDTO) {
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
    public ProductDeleteResponseDTO deleteProduct(HttpSession ses, @Valid @RequestBody ProductDeleteRequestDTO productDeleteRequestDTO) {
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
    public ProductResponseDTO getById(@Valid @PathVariable(name = "id") int productId) {
        return productService.getById(productId);
    }
    @GetMapping("/products/{id}/admin")
    public ProductResponseDTO getById(@Valid @PathVariable(name = "id") int productId, HttpSession ses) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }

        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {

            throw new DeniedPermissionException("You don't have permission for that!");
        }
        return productService.getById(productId);
    }

    @PostMapping("/products/filter")
    public List<ProductResponseDTO> getAll(@Valid @RequestBody ProductFilterRequestDTO productFilterRequestDTO) {
        try {
            return productService.getFilterAndSearchProducts(productFilterRequestDTO);
        } catch (SQLException throwables) {
            String stacktrace = ExceptionUtils.getStackTrace(throwables);
            log.log(Level.ALL,stacktrace,throwables);
            throw new MyServerException("Something get wrong!");
        }
    }

    @PostMapping("/products/filter/admin")
    public List<ProductResponseDTO> getAllAdmin(@Valid HttpSession ses, @RequestBody ProductFilterRequestDTO productFilterRequestDTO) {
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
            String stacktrace = ExceptionUtils.getStackTrace(throwables);
            log.log(Level.ALL,stacktrace);
            throw new MyServerException("Something get wrong!");
        }
    }
}
