package pisibg.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.*;
import pisibg.model.pojo.User;
import pisibg.model.repository.UserRepository;
import pisibg.service.ProductService;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ProductController extends AbstractController{

    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/users/{user_id}/product/add")
    public ProductResponseDTO add(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody ProductRequestDTO productRequestDTO){
        if(sessionManager.getLoggedUser(ses)==null){
            throw new AuthenticationException("You have to be logged in!");
        }
        else {
            User user = sessionManager.getLoggedUser(ses);
            if (userId != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
        }
        return productService.add(productRequestDTO);
    }
    @PutMapping("/users/{user_id}/product/edit")
    public ProductResponseDTO changeQuantity(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody ProductEditRequestDTO productEditRequestDTO){
        if(sessionManager.getLoggedUser(ses)==null){
            throw new AuthenticationException("You have to be logged in!");
        }
        else {
            User user = sessionManager.getLoggedUser(ses);
            if (userId != user.getId()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
        }
        return productService.edit(productEditRequestDTO);
    }

    @GetMapping("/product")
    public List<ProductResponseDTO> getAll(){
        return productService.getAll();
    }
    @GetMapping("/product/{id}")
    public ProductResponseDTO getById(@PathVariable(name = "id") int productId){
        return productService.getById(productId);
    }
}
