package pisibg.exceptions;

public class DeniedPermissionException extends RuntimeException{

    public DeniedPermissionException(String msg){
        super(msg);
    }
}
