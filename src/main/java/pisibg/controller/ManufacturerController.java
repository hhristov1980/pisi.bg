package pisibg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.model.dto.manufacturerDTO.ManufacturerEditRequestDTO;
import pisibg.model.dto.manufacturerDTO.ManufacturerRequestDTO;
import pisibg.model.dto.manufacturerDTO.ManufacturerResponseDTO;
import pisibg.model.pojo.User;
import pisibg.service.ManufacturerService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ManufacturerController extends AbstractController{
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private SessionManager sessionManager;


    @PostMapping("/manufacturers")
    public ManufacturerResponseDTO add(@Valid @RequestBody ManufacturerRequestDTO manufacturerRequestDTO, HttpSession ses) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        }
        User user = sessionManager.getLoggedUser(ses);
        if (!user.isAdmin()) {
            throw new DeniedPermissionException("You don't have permission for that!");
        }
        return manufacturerService.add(manufacturerRequestDTO);
    }

    @PutMapping("/manufacturers")
    public ManufacturerResponseDTO editManufacturer(HttpSession ses,@Valid @RequestBody ManufacturerEditRequestDTO manufacturerEditRequestDTO) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (!user.isAdmin()) {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
            return manufacturerService.edit(manufacturerEditRequestDTO);
        }
    }

    @GetMapping("/manufacturers")
    public List<ManufacturerResponseDTO> getAll() {
        return manufacturerService.getAll();
    }

    @GetMapping("/manufacturers/{id}")
    public ManufacturerResponseDTO getById(@PathVariable(name = "id") int manufacturerId) {
        return manufacturerService.getById(manufacturerId);
    }
}
