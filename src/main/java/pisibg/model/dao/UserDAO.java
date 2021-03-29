package pisibg.model.dao;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pisibg.model.dto.UserEditResponseDTO;
import pisibg.model.dto.UserWithoutPassDTO;
import pisibg.model.pojo.User;

import javax.persistence.Transient;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class UserDAO extends AbstractDAO {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public UserEditResponseDTO deleteUser(int id) throws SQLException {
        String sql = "UPDATE users SET email='deleted', password='deleted', first_name='deleted', last_name='deleted'," +
                " phone_number='deleted',address='deleted' , is_subscribed='0',deleted_at = ? WHERE id = ?;";
        String select="SELECT id, email, first_name,last_name,phone_number,town_name,address,is_subscribed WHERE id= ?";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             PreparedStatement ps = connection.prepareStatement(select);) {
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setInt(2, id);
            statement.executeUpdate();
            ResultSet resultSet = ps.executeQuery();
            User user = new User();
            if(resultSet.next()){
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

    public int countAdmin() throws SQLException{
        String sql = "SELECT * FROM users WHERE is_admin = 1";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            return statement.executeUpdate();
        }
    }
}
