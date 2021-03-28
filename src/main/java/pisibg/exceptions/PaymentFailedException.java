package pisibg.exceptions;


public class PaymentFailedException extends RuntimeException{
    public PaymentFailedException(String msg){
        super(msg);
    }
}
