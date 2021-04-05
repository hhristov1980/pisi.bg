package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.pojo.Image;
import pisibg.model.repository.ImageRepository;
import pisibg.model.repository.ProductRepository;
import pisibg.utility.Validator;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.nio.file.Files;

@Service
public class ImageService {
    @Value("${file.path}")
    private String filePath;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ProductRepository productRepository;

    @ResponseBody
    public Image upload(MultipartFile file, int productId) throws IOException {
        if (!Validator.isValidInteger(productId)) {
            throw new BadRequestException("You must enter integer greater than 0!");
        }
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Product not found! Wrong product_id!");
        }
        if(checker(file)){
            File imageFile = new File(filePath + File.separator + productId + "_" + System.nanoTime() + ".jpg");
            try (OutputStream os = new FileOutputStream(imageFile)) {
                os.write(file.getBytes());
                Image image = new Image();
                image.setUrl(imageFile.getAbsolutePath());
                image.setProduct(productRepository.getById(productId));
                imageRepository.save(image);
                return image;
            }
        }
        else {
            throw new BadRequestException("The file you try to upload is not an image file");
        }

    }

    public byte[] download(int imageId) throws IOException {
        if (!Validator.isValidInteger(imageId)) {
            throw new BadRequestException("You must enter integer greater than 0!");
        }
        Image image = imageRepository.getById(imageId);
        String url = image.getUrl();
        File pFile = new File(url);
        return Files.readAllBytes(pFile.toPath());
    }

    private boolean checker(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if(fileName!=null && fileName.contains(".")) {
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg")) {
                return true;
            } else {
                return false;
            }
        }
        else{
            throw new BadRequestException("Wrong filename");
        }
    }
}
