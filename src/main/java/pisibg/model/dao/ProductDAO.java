package pisibg.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import pisibg.exceptions.BadRequestException;
import pisibg.model.dto.ProductFilterRequestDTO;
import pisibg.model.pojo.Product;
import pisibg.utility.OffsetPageCalculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Product> getProducts(ProductFilterRequestDTO productFilterRequestDTO) throws SQLException {
        List<Product> products = new ArrayList<>();
        //TODO JOIN BY IDs
        String query = "SELECT p.name, p.description, m.producer_name, c.name, sc.name, p.quantity , p.price, sd.percent " +
                "FROM products p LEFT JOIN manufacturer m ON p.manufacturer_id = m.id " +
                "LEFT JOIN subcategories sc ON p.subcategory_id = sc.id LEFT JOIN categories c ON sc.category_id = c.id " +
                "LEFT JOIN sale_discounts sd ON p.discount_id = sd.id WHERE p.manufacturer_id = ? LIMIT ? OFFSET ?";
        int productsPerPage = productFilterRequestDTO.getProductsPerPage();
        int pageToShow = productFilterRequestDTO.getPage()-1; //FrontEnd sends 1 for page 1, while in mySql is 0;
        int offset = OffsetPageCalculator.offsetPageCalculator(productsPerPage,pageToShow);
        if(pageToShow<0){
            throw new BadRequestException("Please input page number greater than 0!");
        }
        if(productsPerPage<=0){
            throw new BadRequestException("Please input products per page greater than 0!");
        }

        int manufacturerId = productFilterRequestDTO.getManufacturerId();
        int categoryId = productFilterRequestDTO.getCategoryId();
        int subcategoryId = productFilterRequestDTO.getSubcategoryId();
        int discountId = productFilterRequestDTO.getDiscountId();
        String initialSqlQuery = "SELECT p.name, p.description, m.producer_name, c.name, sc.name, p.quantity , p.price, sd.percent " +
                "FROM products p LEFT JOIN manufacturer m ON p.manufacturer_id = m.id " +
                "LEFT JOIN subcategories sc ON p.subcategory_id = sc.id LEFT JOIN categories c ON sc.category_id = c.id " +
                "LEFT JOIN sale_discounts sd ON p.discount_id = sd.id LIMIT ? OFFSET ?";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,productsPerPage);
            preparedStatement.setInt(2,offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Product product = new Product();
                product.setId(resultSet.getInt(1));

            }
            };

        return products;
        }

}
