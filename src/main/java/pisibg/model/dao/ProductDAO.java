package pisibg.model.dao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pisibg.exceptions.BadRequestException;
import pisibg.exceptions.NotFoundException;
import pisibg.model.dto.productDTO.*;
import pisibg.model.pojo.Discount;
import pisibg.model.repository.CategoryRepository;
import pisibg.model.repository.DiscountRepository;
import pisibg.model.repository.ManufacturerRepository;
import pisibg.model.repository.SubCategoryRepository;
import pisibg.utility.OffsetPageCalculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Component
public class ProductDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private DiscountRepository discountRepository;

    public List<ProductFilterResponseDTO> getProducts(ProductFilterRequestDTO productFilterRequestDTO) throws SQLException {
        List<ProductFilterResponseDTO> products = new ArrayList<>();
        int productsPerPage = productFilterRequestDTO.getProductsPerPage();
        int pageToShow = productFilterRequestDTO.getPage(); //FrontEnd sends 1 for page 1, while in mySql is 0;
        int offset = OffsetPageCalculator.offsetPageCalculator(productsPerPage, pageToShow);
        if (pageToShow <= 0) {
            throw new BadRequestException("Please input page number greater than 0!");
        }
        if (productsPerPage <= 0) {
            throw new BadRequestException("Please input products per page greater than 0!");
        }
        String initialSqlQuery = "SELECT p.id, p.name, p.description, m.producer_name, c.name, sc.name, p.price, sd.percent " +
                "FROM products p LEFT JOIN manufacturer m ON p.manufacturer_id = m.id " +
                "LEFT JOIN subcategories sc ON p.subcategory_id = sc.id LEFT JOIN categories c ON sc.category_id = c.id " +
                "LEFT JOIN sale_discounts sd ON p.discount_id = sd.id";

        int manufacturerId = productFilterRequestDTO.getManufacturerId();
        int categoryId = productFilterRequestDTO.getCategoryId();
        int subcategoryId = productFilterRequestDTO.getSubcategoryId();
        int discountId = productFilterRequestDTO.getDiscountId();
        boolean validManufacturer = false;
        boolean validCategory = false;
        boolean validSubcategory = false;
        boolean validDiscount = false;
        boolean foundValid = false;
        if (manufacturerId < 0 || categoryId < 0 || subcategoryId < 0 || discountId < 0) {
            throw new BadRequestException("Please enter number greater than 0 or null!");
        }
        if (manufacturerId > 0) {
            if (manufacturerRepository.findById(manufacturerId) == null) {
                throw new NotFoundException("Manufacturer with this id doesn't exists!");
            } else {
                foundValid = true;
                validManufacturer = true;
                initialSqlQuery = initialSqlQuery + " WHERE manufacturer_id = ?";
            }
        }
        if (categoryId > 0) {
            if (categoryRepository.findById(categoryId) == null) {
                throw new NotFoundException("Category with this id doesn't exists!");
            } else {
                validCategory = true;
                if (foundValid) {
                    initialSqlQuery = initialSqlQuery + " AND category_id = ?";
                } else {
                    foundValid = true;
                    initialSqlQuery = initialSqlQuery + " WHERE category_id = ?";
                }
            }
        }
        if (subcategoryId > 0) {
            if (subCategoryRepository.getById(subcategoryId) == null) {
                throw new NotFoundException("Subcategory with this id doesn't exists!");
            } else {
                validSubcategory = true;
                if (foundValid) {
                    initialSqlQuery = initialSqlQuery + " AND subcategory_id = ?";
                } else {
                    foundValid = true;
                    initialSqlQuery = initialSqlQuery + " WHERE subcategory_id = ?";
                }
            }
        }
        if (discountId > 0) {
            Discount discount = discountRepository.findById(discountId);
            if (discount == null || !discount.isActive()) {
                throw new NotFoundException("No active discount with this id!");
            } else {
                validDiscount = true;
                if (foundValid) {
                    initialSqlQuery = initialSqlQuery + " AND discount_id = ?";
                } else {
                    foundValid = true;
                    initialSqlQuery = initialSqlQuery + " WHERE discount_id = ?";
                }
            }
        }

        initialSqlQuery = initialSqlQuery + " ORDER BY p.price ASC LIMIT ? OFFSET ?";
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
            preparedStatement.setInt(columnIndex++, productsPerPage);
            preparedStatement.setInt(columnIndex, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ProductFilterResponseDTO productFilterResponseDTO = new ProductFilterResponseDTO();
                productFilterResponseDTO.setId(resultSet.getInt(1));
                productFilterResponseDTO.setName(resultSet.getString(2));
                productFilterResponseDTO.setDescription(resultSet.getString(3));
                productFilterResponseDTO.setProducerName(resultSet.getString(4));
                productFilterResponseDTO.setCategoryName(resultSet.getString(5));
                productFilterResponseDTO.setSubcategoryName(resultSet.getString(6));
                productFilterResponseDTO.setPrice(resultSet.getDouble(7));
                productFilterResponseDTO.setDiscountPercent(resultSet.getInt(8));
                products.add(productFilterResponseDTO);
            }
        }
        return products;
    }

    public List<ProductAdminFilterResponseDTO> getAdminInfoProducts(ProductAdminFilterRequestDTO productAdminFilterRequestDTO) throws SQLException {
        List<ProductAdminFilterResponseDTO> products = new ArrayList<>();
        int productsPerPage = productAdminFilterRequestDTO.getProductsPerPage();
        int pageToShow = productAdminFilterRequestDTO.getPage(); //FrontEnd sends 1 for page 1, while in mySql is 0;
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
                "LEFT JOIN sale_discounts sd ON p.discount_id = sd.id ";

        int manufacturerId = productAdminFilterRequestDTO.getManufacturerId();
        int categoryId = productAdminFilterRequestDTO.getCategoryId();
        int subcategoryId = productAdminFilterRequestDTO.getSubcategoryId();
        int discountId = productAdminFilterRequestDTO.getDiscountId();
        Integer quantity = productAdminFilterRequestDTO.getQuantity();
        boolean validManufacturer = false;
        boolean validCategory = false;
        boolean validSubcategory = false;
        boolean validDiscount = false;
        boolean foundValid = false;
        if (manufacturerId < 0 || categoryId < 0 || subcategoryId < 0 || discountId < 0) {
            throw new BadRequestException("Please enter number greater than 0 or null!");
        }
        if (manufacturerId > 0) {
            if (manufacturerRepository.findById(manufacturerId) == null) {
                throw new NotFoundException("Manufacturer with this id doesn't exists!");
            } else {
                foundValid = true;
                validManufacturer = true;
                initialSqlQuery = initialSqlQuery + " WHERE manufacturer_id = ?";
            }
        }
        if (categoryId > 0) {
            if (categoryRepository.findById(categoryId) == null) {
                throw new NotFoundException("Category with this id doesn't exists!");
            } else {
                validCategory = true;
                if (foundValid) {
                    initialSqlQuery = initialSqlQuery + " AND category_id = ?";
                } else {
                    foundValid = true;
                    initialSqlQuery = initialSqlQuery + " WHERE category_id = ?";
                }
            }
        }
        if (subcategoryId > 0) {
            if (subCategoryRepository.getById(subcategoryId) == null) {
                throw new NotFoundException("Subcategory with this id doesn't exists!");
            } else {
                validSubcategory = true;
                if (foundValid) {
                    initialSqlQuery = initialSqlQuery + " AND subcategory_id = ?";
                } else {
                    foundValid = true;
                    initialSqlQuery = initialSqlQuery + " WHERE subcategory_id = ?";
                }
            }
        }
        if (discountId > 0) {
            Discount discount = discountRepository.findById(discountId);
            if (discount == null || !discount.isActive()) {
                throw new NotFoundException("No active discount with this id!");
            } else {
                validDiscount = true;
                if (foundValid) {
                    initialSqlQuery = initialSqlQuery + " AND discount_id = ?";
                } else {
                    foundValid = true;
                    initialSqlQuery = initialSqlQuery + " WHERE discount_id = ?";
                }
            }
        }
        if (foundValid && quantity != null) { //null means no matter of quantity
            if (quantity > 0) {
                if (foundValid) {
                    initialSqlQuery = initialSqlQuery + " AND p.quantity <=?";
                } else {
                    initialSqlQuery = initialSqlQuery + " WHERE p.quantity <=?";
                }
            } else {
                throw new BadRequestException("Please enter number greater than 0 or null!");

            }

        }

        initialSqlQuery = initialSqlQuery + " ORDER BY p.price ASC LIMIT ? OFFSET ?";
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
            preparedStatement.setInt(columnIndex++, productsPerPage);
            preparedStatement.setInt(columnIndex, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ProductAdminFilterResponseDTO productAdminFilterResponseDTO = new ProductAdminFilterResponseDTO();
                productAdminFilterResponseDTO.setId(resultSet.getInt(1));
                productAdminFilterResponseDTO.setName(resultSet.getString(2));
                productAdminFilterResponseDTO.setDescription(resultSet.getString(3));
                productAdminFilterResponseDTO.setProducerName(resultSet.getString(4));
                productAdminFilterResponseDTO.setCategoryName(resultSet.getString(5));
                productAdminFilterResponseDTO.setSubcategoryName(resultSet.getString(6));
                productAdminFilterResponseDTO.setPrice(resultSet.getDouble(7));
                productAdminFilterResponseDTO.setQuantity(resultSet.getInt(8));
                productAdminFilterResponseDTO.setDiscountPercent(resultSet.getInt(9));
                products.add(productAdminFilterResponseDTO);
            }
        }
        return products;
    }

    public List<ProductFilterResponseDTO> searchProducts(ProductSearchRequestDTO productSearchRequestDTO) throws SQLException {
        List<ProductFilterResponseDTO> products = new ArrayList<>();
        int productsPerPage = productSearchRequestDTO.getProductsPerPage();
        int pageToShow = productSearchRequestDTO.getPage(); //FrontEnd sends 1 for page 1, while in mySql is 0;
        int offset = OffsetPageCalculator.offsetPageCalculator(productsPerPage, pageToShow);
        String keyWord = productSearchRequestDTO.getKeyWord();
        if (pageToShow <= 0) {
            throw new BadRequestException("Please input page number greater than 0!");
        }
        if (productsPerPage <= 0) {
            throw new BadRequestException("Please input products per page greater than 0!");
        }
        if (keyWord.length() < 3) {
            throw new NotFoundException("Please enter at least 3 characters");
        }
        String initialSqlQuery = "SELECT p.id, p.name, p.description, m.producer_name, c.name, sc.name, p.price, sd.percent " +
                "FROM products p LEFT JOIN manufacturer m ON p.manufacturer_id = m.id " +
                "LEFT JOIN subcategories sc ON p.subcategory_id = sc.id LEFT JOIN categories c ON sc.category_id = c.id " +
                "LEFT JOIN sale_discounts sd ON p.discount_id = sd.id WHERE p.name LIKE \"%" + keyWord + "%\" ORDER BY p.price ASC";

        initialSqlQuery = initialSqlQuery + " LIMIT ? OFFSET ?";
        int columnIndex = 1;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(initialSqlQuery)) {
            preparedStatement.setInt(columnIndex++, productsPerPage);
            preparedStatement.setInt(columnIndex, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ProductFilterResponseDTO productFilterResponseDTO = new ProductFilterResponseDTO();
                productFilterResponseDTO.setId(resultSet.getInt(1));
                productFilterResponseDTO.setName(resultSet.getString(2));
                productFilterResponseDTO.setDescription(resultSet.getString(3));
                productFilterResponseDTO.setProducerName(resultSet.getString(4));
                productFilterResponseDTO.setCategoryName(resultSet.getString(5));
                productFilterResponseDTO.setSubcategoryName(resultSet.getString(6));
                productFilterResponseDTO.setPrice(resultSet.getDouble(7));
                productFilterResponseDTO.setDiscountPercent(resultSet.getInt(8));
                products.add(productFilterResponseDTO);
            }
        }
        return products;
    }

    public List<ProductAdminFilterResponseDTO> searchAdminProducts(ProductSearchRequestDTO productSearchRequestDTO) throws SQLException {
        List<ProductAdminFilterResponseDTO> products = new ArrayList<>();
        int productsPerPage = productSearchRequestDTO.getProductsPerPage();
        int pageToShow = productSearchRequestDTO.getPage(); //FrontEnd sends 1 for page 1, while in mySql is 0;
        int offset = OffsetPageCalculator.offsetPageCalculator(productsPerPage, pageToShow);
        String keyWord = productSearchRequestDTO.getKeyWord();
        if (pageToShow <= 0) {
            throw new BadRequestException("Please input page number greater than 0!");
        }
        if (productsPerPage <= 0) {
            throw new BadRequestException("Please input products per page greater than 0!");
        }
        if (keyWord.length() < 3) {
            throw new NotFoundException("Please enter at least 3 characters");
        }
        String initialSqlQuery = "SELECT p.id, p.name, p.description, m.producer_name, c.name, sc.name, p.price, p.quantity, sd.percent " +
                "FROM products p LEFT JOIN manufacturer m ON p.manufacturer_id = m.id " +
                "LEFT JOIN subcategories sc ON p.subcategory_id = sc.id LEFT JOIN categories c ON sc.category_id = c.id " +
                "LEFT JOIN sale_discounts sd ON p.discount_id = sd.id WHERE p.name LIKE \"%" + keyWord + "%\" ORDER BY p.price ASC";

        initialSqlQuery = initialSqlQuery + " LIMIT ? OFFSET ?";
        int columnIndex = 1;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(initialSqlQuery)) {
            preparedStatement.setInt(columnIndex++, productsPerPage);
            preparedStatement.setInt(columnIndex, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ProductAdminFilterResponseDTO productAdminFilterResponseDTO = new ProductAdminFilterResponseDTO();
                productAdminFilterResponseDTO.setId(resultSet.getInt(1));
                productAdminFilterResponseDTO.setName(resultSet.getString(2));
                productAdminFilterResponseDTO.setDescription(resultSet.getString(3));
                productAdminFilterResponseDTO.setProducerName(resultSet.getString(4));
                productAdminFilterResponseDTO.setCategoryName(resultSet.getString(5));
                productAdminFilterResponseDTO.setSubcategoryName(resultSet.getString(6));
                productAdminFilterResponseDTO.setPrice(resultSet.getDouble(7));
                productAdminFilterResponseDTO.setQuantity(resultSet.getInt(8));
                productAdminFilterResponseDTO.setDiscountPercent(resultSet.getInt(9));
                products.add(productAdminFilterResponseDTO);
            }
        }
        return products;
    }

}
