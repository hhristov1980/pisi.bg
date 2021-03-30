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
import pisibg.model.dto.userDTO.UserRegisterResponseDTO;
import pisibg.model.dto.userDTO.UserReportRequestDTO;
import pisibg.model.pojo.OrderStatus;
import pisibg.model.pojo.PaymentMethod;
import pisibg.model.pojo.User;
import pisibg.model.repository.OrderStatusRepository;
import pisibg.model.repository.PaymentMethodRepository;
import pisibg.model.repository.UserRepository;
import pisibg.utility.OffsetPageCalculator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAO extends AbstractDAO {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private PaymentMethodRepository methodRepository;

    public UserEditResponseDTO deleteUser(int id) throws SQLException {
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
            return new UserEditResponseDTO(user);
        }
    }

    public List<OrderReportDTO> monthlyOrders(OrderMonthlyReportRequestDTO dto) throws SQLException {
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
            List<OrderReportDTO> orders = new ArrayList<>();
            while (resultSet.next()) {
                OrderReportDTO order = new OrderReportDTO();
                order.setId(resultSet.getInt(1));
                User u = userRepository.getOne(resultSet.getInt(2));
                OrderStatus status = orderStatusRepository.getOne(resultSet.getInt(3));
                PaymentMethod method = methodRepository.getOne(resultSet.getInt(4));
                order.setUserNames(u.getFirstName() + " " + u.getLastName());
                order.setOrderStatus(status.getType());
                order.setPaymentMethodType(method.getType());
                order.setAddress(resultSet.getString(5));
                order.setCreatedAt(resultSet.getTimestamp(6).toLocalDateTime());
                order.setGrossValue(resultSet.getInt(7));
                order.setDiscount(resultSet.getInt(8));
                order.setNetValue(resultSet.getInt(9));
                order.setPaid(resultSet.getBoolean(10));
                orders.add(order);
            }
            return orders;
        }
    }

    public List<OrderReportDTO> yearlyOrders(OrderYearlyReportRequestDTO dto) throws SQLException {
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
            List<OrderReportDTO> orders = new ArrayList<>();
            while (resultSet.next()) {
                OrderReportDTO order = new OrderReportDTO();
                order.setId(resultSet.getInt(1));
                User u = userRepository.getOne(resultSet.getInt(2));
                OrderStatus status = orderStatusRepository.getOne(resultSet.getInt(3));
                PaymentMethod method = methodRepository.getOne(resultSet.getInt(4));
                order.setUserNames(u.getFirstName() + " " + u.getLastName());
                order.setOrderStatus(status.getType());
                order.setPaymentMethodType(method.getType());
                order.setAddress(resultSet.getString(5));
                order.setCreatedAt(resultSet.getTimestamp(6).toLocalDateTime());
                order.setGrossValue(resultSet.getInt(7));
                order.setDiscount(resultSet.getInt(8));
                order.setNetValue(resultSet.getInt(9));
                order.setPaid(resultSet.getBoolean(10));
                orders.add(order);
            }
            return orders;
        }
    }

    public List<UserRegisterResponseDTO> getAllUsers(UserReportRequestDTO dto) throws SQLException {
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
            List<UserRegisterResponseDTO> users = new ArrayList<>();
            while (resultSet.next()) {
                UserRegisterResponseDTO user = new UserRegisterResponseDTO();
                user.setId(resultSet.getInt(1));
                user.setEmail(resultSet.getString(2));
                user.setFirstName(resultSet.getString(3));
                user.setLastName(resultSet.getString(4));
                user.setPhoneNumber(resultSet.getString(5));
                user.setTurnover(resultSet.getDouble(6));
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

    public List<OrderReportDTO> dailyOrders(LocalDateTime from, LocalDateTime to, OrderDailyReportRequestDTO dto) throws SQLException {
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
            List<OrderReportDTO> orders = new ArrayList<>();
            while (resultSet.next()) {
                OrderReportDTO order = new OrderReportDTO();
                order.setId(resultSet.getInt(1));
                User u = userRepository.getOne(resultSet.getInt(2));
                OrderStatus status = orderStatusRepository.getOne(resultSet.getInt(3));
                PaymentMethod method = methodRepository.getOne(resultSet.getInt(4));
                order.setUserNames(u.getFirstName() + " " + u.getLastName());
                order.setOrderStatus(status.getType());
                order.setPaymentMethodType(method.getType());
                order.setAddress(resultSet.getString(5));
                order.setCreatedAt(resultSet.getTimestamp(6).toLocalDateTime());
                order.setGrossValue(resultSet.getInt(7));
                order.setDiscount(resultSet.getInt(8));
                order.setNetValue(resultSet.getInt(9));
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
}
