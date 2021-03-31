package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.categoryDTO.CategoryEditRequestDTO;
import pisibg.model.dto.categoryDTO.CategoryRequestDTO;
import pisibg.model.dto.categoryDTO.CategoryResponseDTO;
import pisibg.model.pojo.User;
import pisibg.model.repository.UserRepository;
import pisibg.service.CategoryService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


@RestController
public class CategoryController extends AbstractController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/categories")
    public CategoryResponseDTO add(HttpSession ses,@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (!user.isAdmin()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            return categoryService.add(categoryRequestDTO);
        }
    }


    @PutMapping("/categories")
    public CategoryResponseDTO edit(HttpSession ses,@Valid @RequestBody CategoryEditRequestDTO categoryEditRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (!user.isAdmin()) {
                throw new DeniedPermissionException("You dont have permission for that!");
            }
            return categoryService.edit(categoryEditRequestDTO);
        }
    }

    @GetMapping("/categories")
    public List<CategoryResponseDTO> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/categories/{id}")
    public CategoryResponseDTO getById(@PathVariable(name = "id") int categoryId) {
        return categoryService.getById(categoryId);
    }
}
