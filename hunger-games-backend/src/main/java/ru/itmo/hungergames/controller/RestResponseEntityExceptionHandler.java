package ru.itmo.hungergames.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.itmo.hungergames.exception.ChatExistsException;
import ru.itmo.hungergames.exception.NoAccessToChatException;
import ru.itmo.hungergames.exception.UserExistsException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ChatExistsException.class, UserExistsException.class})
    protected ResponseEntity<Object> handleExistsException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {NoAccessToChatException.class})
    protected ResponseEntity<Object> handleNoAccessToChatException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
}
