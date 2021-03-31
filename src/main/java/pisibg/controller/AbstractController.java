package pisibg.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pisibg.exceptions.*;
import pisibg.model.dto.ErrorDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractController {

    static Logger log = Logger.getLogger(AbstractController.class.getName());

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(BadRequestException e) {
        log.log(Level.ALL,e.getMessage());
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(NotFoundException e) {
        log.log(Level.ALL,e.getMessage());
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleAuthentication(AuthenticationException e) {
        log.log(Level.ALL,e.getMessage());
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(DeniedPermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handlePermission(DeniedPermissionException e) {
        log.log(Level.ALL,e.getMessage());
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(MySQLException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDTO handleSQL(MySQLException e) {
        log.log(Level.ALL,e.getMessage());
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(OutOfStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleSQL(OutOfStockException e) {
        log.log(Level.ALL,e.getMessage());
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(PaymentFailedException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ErrorDTO handleSQL(PaymentFailedException e) {
        log.log(Level.ALL,e.getMessage());
        return new ErrorDTO(e.getMessage());
    }

    public static void main(String[] args) throws IOException, SQLException {
        log.info("Hello this is an info message");
    }
}
