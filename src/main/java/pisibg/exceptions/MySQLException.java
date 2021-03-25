package pisibg.exceptions;

import java.sql.SQLException;

public class MySQLException extends RuntimeException {

    public MySQLException(String msg){
        super(msg);
    }
}
