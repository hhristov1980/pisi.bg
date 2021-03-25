package pisibg.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractDAO {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
}
