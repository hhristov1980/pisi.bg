package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pisibg.model.dto.CategoryRequestDTO;
import pisibg.model.dto.CategoryResponseDTO;
import pisibg.model.dto.ManufacturerRequestDTO;
import pisibg.model.dto.ManufacturerResponseDTO;
import pisibg.service.CategoryService;
import pisibg.service.ManufacturerService;

import javax.servlet.http.HttpSession;

@RestController
public class CategoryController extends AbstractController{

    @Autowired
    private CategoryService categoryService;

    @PutMapping("/users/{user_id}/categories/add")
    public CategoryResponseDTO addNewCategory(@PathVariable(name = "user_id") int userId, HttpSession ses, @RequestBody CategoryRequestDTO categoryRequestDTO){
//        if(ses.getAttribute("LoggedUser")==null){
//            throw new AuthenticationException("You have to be logged in!");
//        }
//        else {
//            int loggedId = (int)ses.getAttribute("LoggedUser");
//            if(userId!=loggedId){
//                throw new BadRequestException("You don't have permission for that!");
//            }
//        }
        return categoryService.addCategory(categoryRequestDTO);
    }
}
