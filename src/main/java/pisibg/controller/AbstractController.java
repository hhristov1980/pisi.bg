package pisibg.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pisibg.exceptions.*;
import pisibg.model.dto.ErrorDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractController {
    static Logger log = Logger.getLogger(AbstractController.class.getName());

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(BadRequestException e) {
        String stacktrace = ExceptionUtils.getStackTrace(e);
        log.log(Level.ALL,stacktrace);
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(NotFoundException e) {
        String stacktrace = ExceptionUtils.getStackTrace(e);
        log.log(Level.ALL,stacktrace);
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleAuthentication(AuthenticationException e) {
        String stacktrace = ExceptionUtils.getStackTrace(e);
        log.log(Level.ALL,stacktrace);
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(DeniedPermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handlePermission(DeniedPermissionException e) {
        String stacktrace = ExceptionUtils.getStackTrace(e);
        log.log(Level.ALL,stacktrace);
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(MyServerException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDTO handleSQL(MyServerException e) {
        String stacktrace = ExceptionUtils.getStackTrace(e);
        log.log(Level.ALL,stacktrace);
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(OutOfStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleSQL(OutOfStockException e) {
        String stacktrace = ExceptionUtils.getStackTrace(e);
        log.log(Level.ALL,stacktrace);
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(PaymentFailedException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ErrorDTO handleSQL(PaymentFailedException e) {
        String stacktrace = ExceptionUtils.getStackTrace(e);
        log.log(Level.ALL,stacktrace);
        return new ErrorDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
