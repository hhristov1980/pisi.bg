package pisibg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.*;
import pisibg.model.pojo.*;
import pisibg.model.repository.DiscountRepository;
import pisibg.model.repository.ManufacturerRepository;
import pisibg.model.repository.ProductRepository;
import pisibg.model.repository.SubCategoryRepository;
import pisibg.utility.Validator;

import java.util.ArrayList;
import java.util.Collections;
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

    public ProductResponseDTO add(ProductRequestDTO productRequestDTO){

        String name = productRequestDTO.getName();
        if(!Validator.isValidString(name)){
            throw new BadRequestException("You have entered empty text!");
        }
        else {
            if(productRepository.findByName(name) != null){
                throw new BadRequestException("Product already exists");
            }
            else{
                if(!isValidManufacturer(productRequestDTO)){
                    throw new BadRequestException("Invalid manufacturer id!");
                }
                else {
                    if(!isValidSubcategory(productRequestDTO)){
                        throw new BadRequestException("Invalid subcategory id!");
                    }
                    else {
                        if(!isValidDiscount(productRequestDTO)){
                            throw new BadRequestException("Invalid discount id!");
                        }
                        else {
                            if(!Validator.isValidString(productRequestDTO.getDescription())){
                                throw new BadRequestException("You have entered and empty product's description!");
                            }
                            else {
                                if(!isValidQuantity(productRequestDTO.getQuantity())){
                                    throw new BadRequestException("Please enter quantity greater than 0!");
                                }
                                else{
                                    if(!isValidPrice(productRequestDTO.getPrice())){
                                        throw new BadRequestException("Please enter price greater than 0!");
                                    }
                                    else {
                                        Product product = new Product();
                                        product.setName(productRequestDTO.getName());
                                        product.setDescription(product.getDescription());
                                        product.setQuantity(productRequestDTO.getQuantity());
                                        product.setPrice(productRequestDTO.getPrice());
                                        product.setDiscount(discountRepository.findById(productRequestDTO.getDiscountId()));
                                        product.setManufacturer(manufacturerRepository.findById(productRequestDTO.getManufacturerId()));
                                        product.setSubcategory(subCategoryRepository.getById(productRequestDTO.getSubcategoryId()));
                                        product = productRepository.save(product);
                                        return new ProductResponseDTO(product);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public ProductResponseDTO  edit(ProductEditRequestDTO productEditRequestDTO){

        if(!Validator.isValidInteger(productEditRequestDTO.getId())){
            throw new BadRequestException("Please enter id greater than 0!");
        }
        else{
            if(productRepository.findById(productEditRequestDTO.getId())==null){
                throw new BadRequestException("Product with this id doesn't exists");
            }
            else{
                if(!Validator.isValidString(productEditRequestDTO.getNewName())){
                    throw new BadRequestException("You put and empty name!");
                }
                else {
                    if(!productEditRequestDTO.getCurrentName().equals(productEditRequestDTO.getNewName())&&productRepository.findByName(productEditRequestDTO.getNewName())!=null){
                        throw new BadRequestException("This name already exists!");
                    }
                    else {
                        if(!isValidQuantity(productEditRequestDTO.getNewQuantity())){
                            throw new BadRequestException("Please enter quantity greater or equal to 0!");
                        }
                        else {
                            if(!isValidPrice(productEditRequestDTO.getNewPrice())){
                                throw new BadRequestException("Please enter price greater than 0!");
                            }
                            else {
                                if(!Validator.isValidInteger(productEditRequestDTO.getNewManufacturerId())||!Validator.isValidInteger(productEditRequestDTO.getNewSubcategoryId())){
                                    throw new BadRequestException("Please number greater than 0!");
                                }
                                else {
                                    if(manufacturerRepository.findById(productEditRequestDTO.getNewManufacturerId())==null){
                                        throw new NotFoundException("No manufacturer with this Id");
                                    }
                                    else {
                                        if(subCategoryRepository.getById(productEditRequestDTO.getNewSubcategoryId())==null){
                                            throw new NotFoundException("No subcategory with this Id");
                                        }
                                        else {
                                            if(productEditRequestDTO.getNewDiscountId()<0){
                                                throw new BadRequestException("Please number greater than 0 for new or 0 to delete discount!");
                                            }
                                            else {
                                                if(productEditRequestDTO.getNewDiscountId()>0){
                                                    if(discountRepository.findById(productEditRequestDTO.getNewDiscountId())==null){
                                                        throw new NotFoundException("No discount with this Id");
                                                    }
                                                    else {
                                                        if(!Validator.isValidString(productEditRequestDTO.getNewDescription())){
                                                            throw new BadRequestException("You put and empty name!");
                                                        }
                                                        else{
                                                            Product product = new Product();
                                                            product.setId(productEditRequestDTO.getId());
                                                            product.setName(productEditRequestDTO.getNewName());
                                                            product.setDescription(productEditRequestDTO.getNewDescription());
                                                            product.setQuantity(productEditRequestDTO.getNewQuantity());
                                                            product.setPrice(productEditRequestDTO.getNewPrice());
                                                            product.setDiscount(discountRepository.findById(productEditRequestDTO.getNewDiscountId()));
                                                            product.setManufacturer(manufacturerRepository.findById(productEditRequestDTO.getNewManufacturerId()));
                                                            product.setSubcategory(subCategoryRepository.getById(productEditRequestDTO.getNewSubcategoryId()));
                                                            product = productRepository.save(product);
                                                            return new ProductResponseDTO(product);
                                                        }
                                                    }
                                                }
                                                else {
                                                    Product product = new Product();
                                                    product.setId(productEditRequestDTO.getId());
                                                    product.setName(productEditRequestDTO.getNewName());
                                                    product.setDescription(productEditRequestDTO.getNewDescription());
                                                    product.setQuantity(productEditRequestDTO.getNewQuantity());
                                                    product.setPrice(productEditRequestDTO.getNewPrice());
                                                    product.setDiscount(null);
                                                    product.setManufacturer(manufacturerRepository.findById(productEditRequestDTO.getNewManufacturerId()));
                                                    product.setSubcategory(subCategoryRepository.getById(productEditRequestDTO.getNewSubcategoryId()));
                                                    product = productRepository.save(product);
                                                    return new ProductResponseDTO(product);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public List<ProductResponseDTO> getAll() {
        List<Product> products = productRepository.findAll();
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        if(products.isEmpty()){
            throw new NotFoundException("Products not found");
        }
        else {
            for(Product p: products){
                productResponseDTOList.add(new ProductResponseDTO(p));
            }
            Collections.sort(productResponseDTOList,((o1, o2) -> Double.compare(o1.getPrice(),o2.getPrice())));
            return productResponseDTOList;
        }

    }
    public ProductResponseDTO getById(int productId) {
        Product product = productRepository.findById(productId);
        if(product ==null){
            throw new NotFoundException("Product not found");
        }
        else {
            ProductResponseDTO productResponseDTO = new ProductResponseDTO(product);
            return productResponseDTO;
        }
    }


    private boolean isValidSubcategory(ProductRequestDTO productRequestDTO){
        int subcategoryId = productRequestDTO.getSubcategoryId();
        if(subcategoryId>0){
            Subcategory subcategory = subCategoryRepository.getById(subcategoryId);
            return subcategory!=null;
        }
        return false;
    }
    private boolean isValidDiscount(ProductRequestDTO productRequestDTO){
        int discountId = productRequestDTO.getDiscountId();
        if(discountId>0){
            Discount discount = discountRepository.findById(discountId);
            return discount!=null;
        }
        else {
            if(discountId == 0){
                return true;
            }
        }
        return false;
    }
    private boolean isValidManufacturer(ProductRequestDTO productRequestDTO){
        int manufacturerId = productRequestDTO.getManufacturerId();
        if(manufacturerId>0){
            Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId);
            return manufacturer!=null;
        }
        return false;

    }
    private boolean isValidQuantity(int quantity){
        return quantity>=0;
    }
    private boolean isValidPrice(double price){
        return price>0;
    }
}