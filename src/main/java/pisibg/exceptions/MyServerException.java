package pisibg.exceptions;

import java.sql.SQLException;

public class MyServerException extends RuntimeException {

    public MyServerException(String msg){
        super(msg);
    }
}
