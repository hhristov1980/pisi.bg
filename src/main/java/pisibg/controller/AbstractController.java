package pisibg.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pisibg.exceptions.*;
import pisibg.model.dto.ErrorDTO;
import pisibg.model.pojo.Payment;

public class AbstractController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(BadRequestException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(NotFoundException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleAuthentication(AuthenticationException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(DeniedPermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handlePermission(DeniedPermissionException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(MySQLException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDTO handleSQL(MySQLException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(OutOfStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleSQL(OutOfStockException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(PaymentFailedException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ErrorDTO handleSQL(PaymentFailedException e) {
        return new ErrorDTO(e.getMessage());
    }
}
