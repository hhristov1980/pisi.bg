package pisibg.model.dao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.productDTO.*;
import pisibg.model.pojo.Discount;
import pisibg.model.pojo.Product;
import pisibg.model.repository.*;
import pisibg.utility.OffsetPageCalculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Getter
@Component
public class ProductDAO extends AbstractDAO{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getFilterSearchProducts(ProductFilterRequestDTO productFilterRequestDTO) throws SQLException {
        List<Product> products = new ArrayList<>();
        int productsPerPage = productFilterRequestDTO.getProductsPerPage();
        int pageToShow = productFilterRequestDTO.getPage(); //FrontEnd sends 1 for page 1, while in mySql is 0;
        int offset = OffsetPageCalculator.offsetPageCalculator(productsPerPage, pageToShow);
        if (pageToShow <= 0) {
            throw new BadRequestException("Please input page number greater than 0!");
        }
        if (productsPerPage <= 0) {
            throw new BadRequestException("Please input products per page greater than 0!");
        }
        String initialSqlQuery = "SELECT p.id, p.name, p.description, m.producer_name, c.name, sc.name, p.price, p.quantity, sd.percent " +
                "FROM products p LEFT JOIN manufacturer m ON p.manufacturer_id = m.id " +
                "LEFT JOIN subcategories sc ON p.subcategory_id = sc.id LEFT JOIN categories c ON sc.category_id = c.id " +
                "LEFT JOIN sale_discounts sd ON p.discount_id = sd.id";
        StringBuilder query = new StringBuilder(initialSqlQuery);
        int manufacturerId = productFilterRequestDTO.getManufacturerId();
        int categoryId = productFilterRequestDTO.getCategoryId();
        int subcategoryId = productFilterRequestDTO.getSubcategoryId();
        int discountId = productFilterRequestDTO.getDiscountId();
        Integer quantity = productFilterRequestDTO.getQuantity();
        Boolean isAsc = productFilterRequestDTO.getIsAsc();
        String keyWord = productFilterRequestDTO.getKeyword();
        boolean validManufacturer = false;
        boolean validCategory = false;
        boolean validSubcategory = false;
        boolean validDiscount = false;
        boolean foundValid = false;
        if (manufacturerId < 0 || categoryId < 0 || subcategoryId < 0 || discountId < 0) {
            throw new BadRequestException("Please enter number greater than 0 or null!");
        }
        if (manufacturerId > 0) {
            if (manufacturerRepository.getById(manufacturerId) == null) {
                throw new NotFoundException("Manufacturer with this id doesn't exists!");
            } else {
                foundValid = true;
                validManufacturer = true;
                query.append(" WHERE manufacturer_id = ?");
            }
        }
        if (categoryId > 0) {
            if (categoryRepository.getById(categoryId) == null) {
                throw new NotFoundException("Category with this id doesn't exists!");
            } else {
                validCategory = true;
                if (foundValid) {
                    query.append(" AND category_id = ?");
                } else {
                    foundValid = true;
                    query.append(" WHERE category_id = ?");
                }
            }
        }
        if (subcategoryId > 0) {
            if (subCategoryRepository.getById(subcategoryId) == null) {
                throw new NotFoundException("Subcategory with this id doesn't exists!");
            } else {
                validSubcategory = true;
                if (foundValid) {
                    query.append(" AND subcategory_id = ?");
                } else {
                    foundValid = true;
                    query.append(" WHERE subcategory_id = ?");
                }
            }
        }
        if (discountId > 0) {
            Discount discount = discountRepository.getById(discountId);
            if (discount == null || !discount.isActive()) {
                throw new NotFoundException("No active discount with this id!");
            } else {
                validDiscount = true;
                if (foundValid) {
                    query.append(" AND discount_id = ?");
                } else {
                    foundValid = true;
                    query.append(" WHERE discount_id = ?");
                }
            }
        }
        if (quantity != null) { //null means no matter of quantity
            if (quantity > 0) {
                if (foundValid) {
                    query.append(" AND p.quantity <=?");
                } else {
                    foundValid = true;
                    query.append(" WHERE p.quantity <=?");
                }
            } else {
                throw new BadRequestException("Please enter number greater than 0 or null!");
            }
        }
        if(keyWord!=null){
            keyWord = "%"+keyWord+"%";
            if(foundValid){
                query.append(" AND p.name LIKE ?");
            }
            else {
                foundValid = true;
                query.append(" WHERE p.name LIKE ?");
            }
        }
        if(isAsc){
            query.append(" ORDER BY p.price ASC LIMIT ? OFFSET ?");
        }
        else {
            query.append(" ORDER BY p.price DESC LIMIT ? OFFSET ?");
        }
        initialSqlQuery = query.toString();
        int columnIndex = 1;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(initialSqlQuery)) {
            if (validManufacturer) {
                preparedStatement.setInt(columnIndex++, manufacturerId);
            }
            if (validCategory) {
                preparedStatement.setInt(columnIndex++, categoryId);
            }
            if (validSubcategory) {
                preparedStatement.setInt(columnIndex++, subcategoryId);
            }
            if (validDiscount) {
                preparedStatement.setInt(columnIndex++, discountId);
            }
            if (quantity != null) {
                preparedStatement.setInt(columnIndex++, quantity);
            }
            if(keyWord !=null){
                preparedStatement.setString(columnIndex++, keyWord);
            }
            preparedStatement.setInt(columnIndex++, productsPerPage);
            preparedStatement.setInt(columnIndex, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(productRepository.getOne(resultSet.getInt(1)));
            }
        }
        return products;
    }
}
