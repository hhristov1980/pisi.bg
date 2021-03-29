package pisibg.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import pisibg.model.dto.ProductPageRequestDTO;
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


    public List<Product> getProducts(ProductPageRequestDTO productPageRequestDTO) throws SQLException {
        List<Product> products = new ArrayList<>();
        //TODO JOIN BY IDs
        String query = "SELECT * FROM products LIMIT ? OFFSET ?;"; 
        int productsPerPage = productPageRequestDTO.getProductsPerPage();
        int pageToShow = productPageRequestDTO.getPage();
        int offset = OffsetPageCalculator.offsetPageCalculator(productsPerPage,pageToShow);

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
