package pisibg.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pisibg.exceptions.AuthenticationException;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.DeniedPermissionException;
import pisibg.exceptions.MyServerException;
import pisibg.model.pojo.Image;
import pisibg.model.pojo.User;
import pisibg.service.ImageService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ImageController extends AbstractController{

    static Logger log = Logger.getLogger(ImageController.class.getName());
    @Value("${file.path}")
    private String filePath;
    @Autowired
    private ImageService imageService;
    @Autowired
    private SessionManager sessionManager;

    @ResponseBody
    @PostMapping("/images/product/{product_id}")
    public Image upload(@PathVariable(name = "product_id") int productId, HttpSession ses, @Valid @RequestPart MultipartFile file) {
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            if (!user.isAdmin()) {
                throw new DeniedPermissionException("You don't have permission for that!");
            }
            try {
                return imageService.upload(file, productId);
            } catch (IOException e) {
                String stacktrace = ExceptionUtils.getStackTrace(e);
                log.log(Level.ALL,stacktrace);
                throw new MyServerException("Something get wrong!");
            }
        }
    }

    @GetMapping(value = "/images/{id}", produces = "image/*")
    public @ResponseBody
    byte[] download(@Valid @PathVariable int id) throws IOException {
        return imageService.download(id);
    }

}
