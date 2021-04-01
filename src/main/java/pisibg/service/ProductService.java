package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dao.ProductDAO;
import pisibg.model.dto.productDTO.*;
import pisibg.model.pojo.*;
import pisibg.model.repository.DiscountRepository;
import pisibg.model.repository.ManufacturerRepository;
import pisibg.model.repository.ProductRepository;
import pisibg.model.repository.SubCategoryRepository;
import pisibg.utility.Constants;
import pisibg.utility.Validator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ManufacturerRepository manufacturerRepository;
    @Autowired
    private ProductDAO productDAO;

    public ProductResponseDTO add(ProductRequestDTO productRequestDTO) {

        String name = productRequestDTO.getName();
//        if (!Validator.isValidString(name)) {
//            throw new BadRequestException("You have entered empty text!");
//        }
        if (productRepository.findByName(name) != null) {
            throw new BadRequestException("Product already exists");
        }
        if (!isValidManufacturer(productRequestDTO)) {
            throw new BadRequestException("Invalid manufacturer id!");
        }
        if (!isValidSubcategory(productRequestDTO)) {
            throw new BadRequestException("Invalid subcategory id!");
        }
        if (!isValidDiscount(productRequestDTO)) {
            throw new BadRequestException("Invalid discount id!");
        }
//        if (!Validator.isValidString(productRequestDTO.getDescription())) {
//            throw new BadRequestException("You have entered and empty product's description!");
//        }
        if (!isValidQuantity(productRequestDTO.getQuantity())) {
            throw new BadRequestException("Please enter quantity equal or greater than 0!");
        }
        if (!isValidPrice(productRequestDTO.getPrice())) {
            throw new BadRequestException("Please enter price greater than 0!");
        }
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setQuantity(productRequestDTO.getQuantity());
        product.setPrice(productRequestDTO.getPrice());
        product.setDiscount(discountRepository.getById(productRequestDTO.getDiscountId()));
        product.setManufacturer(manufacturerRepository.getOne(productRequestDTO.getManufacturerId()));
        product.setSubcategory(subCategoryRepository.getById(productRequestDTO.getSubcategoryId()));
        product = productRepository.save(product);
        return new ProductResponseDTO(product);
    }

    public ProductResponseDTO edit(ProductEditRequestDTO productEditRequestDTO) {

//        if (!Validator.isValidInteger(productEditRequestDTO.getId())) {
//            throw new BadRequestException("Please enter id greater than 0!");
//        }
//        if (productRepository.getById(productEditRequestDTO.getId()) == null) {
//            throw new BadRequestException("Product with this id doesn't exists");
//        }
//        if (!Validator.isValidString(productEditRequestDTO.getNewName())) {
//            throw new BadRequestException("You put and empty name!");
//        }
        if (!productEditRequestDTO.getCurrentName().equals(productEditRequestDTO.getNewName()) && productRepository.findByName(productEditRequestDTO.getNewName()) != null) {
            throw new BadRequestException("This name already exists!");
        }
        if (!isValidQuantity(productEditRequestDTO.getNewQuantity())) {
            throw new BadRequestException("Please enter quantity greater or equal to 0!");
        }
        if (!isValidPrice(productEditRequestDTO.getNewPrice())) {
            throw new BadRequestException("Please enter price greater than 0!");
        }
//        if (!Validator.isValidInteger(productEditRequestDTO.getNewManufacturerId()) || !Validator.isValidInteger(productEditRequestDTO.getNewSubcategoryId())) {
//            throw new BadRequestException("Please number greater than 0!");
//        }
        if (manufacturerRepository.getById(productEditRequestDTO.getNewManufacturerId()) == null) {
            throw new NotFoundException("No manufacturer with this Id");
        }
        if (subCategoryRepository.getById(productEditRequestDTO.getNewSubcategoryId()) == null) {
            throw new NotFoundException("No subcategory with this Id");
        }
        if (productEditRequestDTO.getNewDiscountId() < 0) {
            throw new BadRequestException("Please number greater than 0 for new or 0 to delete discount!");
        }
        if (productEditRequestDTO.getNewDiscountId() > 0) {
            if (discountRepository.getById(productEditRequestDTO.getNewDiscountId()) == null) {
                throw new NotFoundException("No discount with this Id");
            }
//            if (!Validator.isValidString(productEditRequestDTO.getNewDescription())) {
//                throw new BadRequestException("You put and empty name!");
//            }
            Product product = new Product();
            product.setId(productEditRequestDTO.getId());
            product.setName(productEditRequestDTO.getNewName());
            product.setDescription(productEditRequestDTO.getNewDescription());
            product.setQuantity(productEditRequestDTO.getNewQuantity());
            product.setPrice(productEditRequestDTO.getNewPrice());
            product.setDiscount(discountRepository.getById(productEditRequestDTO.getNewDiscountId()));
            product.setManufacturer(manufacturerRepository.getOne(productEditRequestDTO.getNewManufacturerId()));
            product.setSubcategory(subCategoryRepository.getById(productEditRequestDTO.getNewSubcategoryId()));
            product = productRepository.save(product);
            return new ProductResponseDTO(product);
        } else {
            Product product = new Product();
            product.setId(productEditRequestDTO.getId());
            product.setName(productEditRequestDTO.getNewName());
            product.setDescription(productEditRequestDTO.getNewDescription());
            product.setQuantity(productEditRequestDTO.getNewQuantity());
            product.setPrice(productEditRequestDTO.getNewPrice());
            product.setDiscount(null);
            product.setManufacturer(manufacturerRepository.getOne(productEditRequestDTO.getNewManufacturerId()));
            product.setSubcategory(subCategoryRepository.getById(productEditRequestDTO.getNewSubcategoryId()));
            product = productRepository.save(product);
            return new ProductResponseDTO(product);
        }
    }

    public ProductDeleteResponseDTO delete(ProductDeleteRequestDTO productDeleteRequestDTO) {
        int id = productDeleteRequestDTO.getId();
//        if (!Validator.isValidInteger(productDeleteRequestDTO.getId())) {
//            throw new BadRequestException("Please enter id greater than 0!");
//        }
        if (productRepository.getById(id) == null) {
            throw new BadRequestException("Product with this id doesn't exists");
        }
        Product product = productRepository.getOne(productDeleteRequestDTO.getId());
        ProductDeleteResponseDTO productDeleteResponseDTO = new ProductDeleteResponseDTO();
        productDeleteResponseDTO.setId(product.getId());
        productDeleteResponseDTO.setDescription(Constants.DELETE);
        product.setQuantity(0);
        product.setPrice(0);
        product.setDiscount(null);
        product.setDescription(Constants.DELETE);
        //Other fields will stay for history
        productRepository.save(product);
        return productDeleteResponseDTO;
    }

    //    public List<ProductResponseDTO> getAll() {
//        List<Product> products = productRepository.findAll();
//        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
//        if(products.isEmpty()){
//            throw new NotFoundException("Products not found");
//        }
//        else {
//            for(Product p: products){
//                productResponseDTOList.add(new ProductResponseDTO(p));
//            }
//            Collections.sort(productResponseDTOList,((o1, o2) -> Double.compare(o1.getPrice(),o2.getPrice())));
//            return productResponseDTOList;
//        }
//    }
    public ProductResponseDTO getByIdAdmin(int productId) {
        Product product = productRepository.getById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found!");
        } else {
            return new ProductResponseDTO(product);
        }
    }

    public ProductResponseDTO getById(int productId) {
        Product product = productRepository.getById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found!");
        } else {
            ProductResponseDTO productResponseDTO = new ProductResponseDTO(product);
            productResponseDTO.setQuantity(null);
            return new ProductResponseDTO(product);
        }
    }

    public List<ProductResponseDTO> getFilterAndSearchProducts(ProductFilterRequestDTO productFilterRequestDTO) throws SQLException {
        List<Product> products = productDAO.getFilterSearchProducts(productFilterRequestDTO);
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        for(Product p: products){
            ProductResponseDTO productResponseDTO = new ProductResponseDTO(p);
            productResponseDTO.setQuantity(null);
            if(!productResponseDTO.getDescription().equals(Constants.DELETE)) {
                productResponseDTOList.add(productResponseDTO);
            }
        }
            return productResponseDTOList;
    }

    public List<ProductResponseDTO> getFilterAndSearchProductsAdmin(ProductFilterRequestDTO productFilterRequestDTO) throws SQLException {
        List<Product> products = productDAO.getFilterSearchProducts(productFilterRequestDTO);
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        for(Product p: products){
            ProductResponseDTO productResponseDTO = new ProductResponseDTO(p);
            if(!productResponseDTO.getDescription().equals(Constants.DELETE)) {
                productResponseDTOList.add(productResponseDTO);
            }
        }
        return productResponseDTOList;
    }



    private boolean isValidSubcategory(ProductRequestDTO productRequestDTO) {
        int subcategoryId = productRequestDTO.getSubcategoryId();
        if (subcategoryId > 0) {
            Subcategory subcategory = subCategoryRepository.getById(subcategoryId);
            return subcategory != null;
        }
        return false;
    }

    private boolean isValidDiscount(ProductRequestDTO productRequestDTO) {
        Integer discountId = productRequestDTO.getDiscountId();
        if(discountId==null){
            return true;
        }
        else {
            if (discountId > 0) {
                Discount discount = discountRepository.getById(discountId);
                return discount != null;
            } else {
                return false;
            }
        }
    }

    private boolean isValidManufacturer(ProductRequestDTO productRequestDTO) {
        int manufacturerId = productRequestDTO.getManufacturerId();
        if (manufacturerId > 0) {
            Manufacturer manufacturer = manufacturerRepository.getById(manufacturerId);
            return manufacturer != null;
        }
        return false;

    }

    private boolean isValidQuantity(int quantity) {
        return quantity >= 0;
    }

    private boolean isValidPrice(double price) {
        return price > 0;
    }
}
