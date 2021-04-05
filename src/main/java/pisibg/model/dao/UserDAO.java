package pisibg.model.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pisibg.exceptions.BadRequestException;
import pisibg.model.dto.orderDTO.OrderDailyReportRequestDTO;
import pisibg.model.dto.orderDTO.OrderMonthlyReportRequestDTO;
import pisibg.model.dto.orderDTO.OrderReportDTO;
import pisibg.model.dto.orderDTO.OrderYearlyReportRequestDTO;
import pisibg.model.dto.userDTO.UserEditResponseDTO;
import pisibg.model.dto.userDTO.UserLoginDTO;
import pisibg.model.dto.userDTO.UserRegisterResponseDTO;
import pisibg.model.dto.userDTO.UserReportRequestDTO;
import pisibg.model.pojo.*;
import pisibg.model.repository.OrderStatusRepository;
import pisibg.model.repository.PaymentMethodRepository;
import pisibg.model.repository.UserRepository;
import pisibg.utility.Constants;
import pisibg.utility.OffsetPageCalculator;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAO extends AbstractDAO {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private PaymentMethodRepository methodRepository;

    public User deleteUser(int id) throws SQLException {
        String sql = "UPDATE users SET email='deleted', password='deleted', first_name='deleted', last_name='deleted'," +
                " phone_number='deleted',address='deleted' , is_subscribed='0',deleted_at = ? WHERE id = ?;";
        String select = "SELECT id, email, first_name,last_name,phone_number,town_name,address,is_subscribed WHERE id= ?";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             PreparedStatement ps = connection.prepareStatement(select);) {
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setInt(2, id);
            statement.executeUpdate();
            ResultSet resultSet = ps.executeQuery();
            User user = new User();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                user.setEmail(resultSet.getString(2));
                user.setFirstName(resultSet.getString(4));
                user.setLastName(resultSet.getString(5));
                user.setPhoneNumber(resultSet.getString(6));
                user.setTownName(resultSet.getString(9));
                user.setTownName(resultSet.getString(9));
                user.setAddress(resultSet.getString(10));
                user.setSubscribed(resultSet.getBoolean(13));
            }
            return user;
        }
    }

    public List<Order> monthlyOrders(OrderMonthlyReportRequestDTO dto) throws SQLException {
        String sql = "SELECT * FROM orders WHERE MONTH(created_at)=? LIMIT ? OFFSET ?;";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dto.getMonth());
            int productsPerPage = dto.getOrdersPerPage();
            int pageToShow = dto.getPage(); //FrontEnd sends 1 for page 1, while in mySql is 0;
            int offset = OffsetPageCalculator.offsetPageCalculator(productsPerPage, pageToShow);
            if (pageToShow < 0) {
                throw new BadRequestException("Please input page number greater than 0!");
            }
            if (productsPerPage <= 0) {
                throw new BadRequestException("Please input products per page greater than 0!");
            }
            ps.setInt(2, productsPerPage);
            ps.setInt(3, offset);
            ResultSet resultSet = ps.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt(1));
                order.setUser(userRepository.getOne(resultSet.getInt(2)));
                order.setOrderStatus(orderStatusRepository.getOne(resultSet.getInt(3)));
                order.setPaymentMethod(methodRepository.getOne(resultSet.getInt(4)));
                order.setAddress(resultSet.getString(5));
                order.setCreatedAt(resultSet.getTimestamp(6).toLocalDateTime());
                order.setGrossValue(resultSet.getBigDecimal(7));
                order.setDiscount(resultSet.getBigDecimal(8));
                order.setNetValue(resultSet.getBigDecimal(9));
                order.setPaid(resultSet.getBoolean(10));
                orders.add(order);
            }
            return orders;
        }
    }

    public List<Order> yearlyOrders(OrderYearlyReportRequestDTO dto) throws SQLException {
        String sql = "SELECT * FROM orders WHERE YEAR(created_at)=? LIMIT ? OFFSET ?;";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dto.getYear());
            int productsPerPage = dto.getOrdersPerPage();
            int pageToShow = dto.getPage(); //FrontEnd sends 1 for page 1, while in mySql is 0;
            int offset = OffsetPageCalculator.offsetPageCalculator(productsPerPage, pageToShow);
            if (pageToShow < 0) {
                throw new BadRequestException("Please input page number greater than 0!");
            }
            if (productsPerPage <= 0) {
                throw new BadRequestException("Please input products per page greater than 0!");
            }
            ps.setInt(2, productsPerPage);
            ps.setInt(3, offset);
            ResultSet resultSet = ps.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt(1));
                order.setUser(userRepository.getOne(resultSet.getInt(2)));
                order.setOrderStatus(orderStatusRepository.getOne(resultSet.getInt(3)));
                order.setPaymentMethod(methodRepository.getOne(resultSet.getInt(4)));
                order.setAddress(resultSet.getString(5));
                order.setCreatedAt(resultSet.getTimestamp(6).toLocalDateTime());
                order.setGrossValue(resultSet.getBigDecimal(7));
                order.setDiscount(resultSet.getBigDecimal(8));
                order.setNetValue(resultSet.getBigDecimal(9));
                order.setPaid(resultSet.getBoolean(10));
                orders.add(order);
            }
            return orders;
        }
    }

    public List<User> getAllUsers(UserReportRequestDTO dto) throws SQLException {
        String sql = "SELECT id,email,first_name,last_name,phone_number,turnover," +
                "personal_discount,town_name,address,created_at,is_subscribed,is_admin" +
                " FROM users LIMIT ? OFFSET ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int productsPerPage = dto.getUsersPerPage();
            int pageToShow = dto.getPage(); //FrontEnd sends 1 for page 1, while in mySql is 0;
            int offset = OffsetPageCalculator.offsetPageCalculator(productsPerPage, pageToShow);
            if (pageToShow < 0) {
                throw new BadRequestException("Please input page number greater than 0!");
            }
            if (productsPerPage <= 0) {
                throw new BadRequestException("Please input products per page greater than 0!");
            }
            ps.setInt(1, productsPerPage);
            ps.setInt(2, offset);
            ResultSet resultSet = ps.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setEmail(resultSet.getString(2));
                user.setFirstName(resultSet.getString(3));
                user.setLastName(resultSet.getString(4));
                user.setPhoneNumber(resultSet.getString(5));
                user.setTurnover(resultSet.getBigDecimal(6));
                user.setPersonalDiscount(resultSet.getInt(7));
                user.setTownName(resultSet.getString(8));
                user.setAddress(resultSet.getString(9));
                user.setCreatedAt(resultSet.getTimestamp(10).toLocalDateTime());
                user.setSubscribed(resultSet.getBoolean(11));
                user.setAdmin(resultSet.getBoolean(12));
                users.add(user);
            }
            return users;
        }
    }

    public List<Order> dailyOrders(LocalDateTime from, LocalDateTime to, OrderDailyReportRequestDTO dto) throws SQLException {
        String sql = "SELECT * FROM orders WHERE created_at BETWEEN ? AND ? LIMIT ? OFFSET ?;";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            Timestamp hourFrom = Timestamp.valueOf(from);
            Timestamp hourTo = Timestamp.valueOf(to);
            ps.setTimestamp(1, hourFrom);
            ps.setTimestamp(2, hourTo);
            int productsPerPage = dto.getOrdersPerPage();
            int pageToShow = dto.getPage(); //FrontEnd sends 1 for page 1, while in mySql is 0;
            int offset = OffsetPageCalculator.offsetPageCalculator(productsPerPage, pageToShow);
            if (pageToShow < 0) {
                throw new BadRequestException("Please input page number greater than 0!");
            }
            if (productsPerPage <= 0) {
                throw new BadRequestException("Please input products per page greater than 0!");
            }
            ps.setInt(3, productsPerPage);
            ps.setInt(4, offset);
            ResultSet resultSet = ps.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt(1));
                order.setUser(userRepository.getOne(resultSet.getInt(2)));
                order.setOrderStatus(orderStatusRepository.getOne(resultSet.getInt(3)));
                order.setPaymentMethod(methodRepository.getOne(resultSet.getInt(4)));
                order.setAddress(resultSet.getString(5));
                order.setCreatedAt(resultSet.getTimestamp(6).toLocalDateTime());
                order.setGrossValue(resultSet.getBigDecimal(7));
                order.setDiscount(resultSet.getBigDecimal(8));
                order.setNetValue(resultSet.getBigDecimal(9));
                order.setPaid(resultSet.getBoolean(10));
                orders.add(order);
            }
            return orders;
        }
    }


    public int countAdmin() throws SQLException {
        String sql = "SELECT * FROM users WHERE is_admin = 1";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {
            return statement.executeUpdate();
        }
    }

    public void updateTurnoverAndPersonalDiscountPercent(BigDecimal orderAmount, int userId) throws SQLException {
        String query1 = "SELECT turnover, personal_discount FROM users WHERE id = ?;";
        String query2 = "UPDATE users SET turnover = ?, personal_discount = ? WHERE id = ?;";
        BigDecimal currentTurnover = new BigDecimal(0);
        int currentPersonalDiscountPercent = 0;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(query1)) {
            ps.setInt(1, userId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                currentTurnover = resultSet.getBigDecimal(1);
                currentPersonalDiscountPercent = resultSet.getInt(2);
            }
            BigDecimal newTurnover = currentTurnover.add(orderAmount);
            int newPersonalDiscountPercent = 0;
            int coefficientTurnoverToIncreaseStep = newTurnover.intValue()/Constants.DISCOUNT_INCREASE_TURNOVER_STEP;
            if (currentPersonalDiscountPercent + coefficientTurnoverToIncreaseStep <= Constants.MAX_PERSONAL_DISCOUNT_PERCENT) {
                newPersonalDiscountPercent = currentPersonalDiscountPercent + coefficientTurnoverToIncreaseStep;
            }
            try (Connection connection1 = jdbcTemplate.getDataSource().getConnection();
                 PreparedStatement ps1 = connection.prepareStatement(query2)) {
                ps1.setBigDecimal(1, newTurnover);
                ps1.setInt(2, newPersonalDiscountPercent);
                ps1.setInt(3, userId);
                ps1.executeUpdate();
            }
        }
    }
    public User returnLoginDetails(UserLoginDTO userLoginDTO) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?;";
        String email = userLoginDTO.getEmail();
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1,email);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setEmail(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                user.setFirstName(resultSet.getString(4));
                user.setLastName(resultSet.getString(5));
                user.setPhoneNumber(resultSet.getString(6));
                user.setTurnover(resultSet.getBigDecimal(7));
                user.setPersonalDiscount(resultSet.getInt(8));
                user.setTownName(resultSet.getString(9));
                user.setAddress(resultSet.getString(10));
                user.setCreatedAt(resultSet.getTimestamp(11).toLocalDateTime());
                user.setAdmin(resultSet.getBoolean(13));
                user.setSubscribed(resultSet.getBoolean(14));
                return user;
            }
        }
        return null;
    }
}
