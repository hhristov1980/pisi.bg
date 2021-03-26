package pisibg.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.ManufacturerRequestDTO;
import pisibg.model.dto.ManufacturerResponseDTO;
import pisibg.model.dto.UserRegisterRequestDTO;
import pisibg.model.dto.UserRegisterResponseDTO;
import pisibg.model.pojo.User;
import pisibg.model.repository.ManufacturerRepository;
import pisibg.model.repository.UserRepository;
import pisibg.service.ProductService;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class ProductController extends AbstractController{

    @Autowired
    private ProductService productService;

}
