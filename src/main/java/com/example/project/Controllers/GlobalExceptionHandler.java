package com.example.project.Controllers;


import com.example.project.Domain.Dtos.ErrorDto;
import com.example.project.Exceptions.OrdinateurAlreadyExistException;
import com.example.project.Exceptions.ServiceAlreadyExistsException;
import com.example.project.Exceptions.ServiceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.action.internal.UnresolvedEntityInsertActions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleServiceAlreadyExistsException(ServiceAlreadyExistsException ex) {
        log.error("Caught ServiceAlreadyExistsException", ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("service déjà existant");
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleServiceNotFoundException(ServiceNotFoundException ex){
        log.error("Caught ServiceNotFoundException", ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("service n'existe pas ");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(OrdinateurAlreadyExistException.class)
    public ResponseEntity<ErrorDto> handleOrdinateurAlreadyExistException(OrdinateurAlreadyExistException ex) {
        log.error("Caught OrdinateurAlreadyExistException", ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("ordinateur déjà existant");
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ) {
        log.error("Caught MethodArgumentNotValidException", ex);
        ErrorDto errorDto = new ErrorDto();

        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .orElse("Validation error occurred");

        errorDto.setError(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(
            ConstraintViolationException ex
    ) {
        log.error("Caught ConstraintViolationException", ex);
        ErrorDto errorDto = new ErrorDto();

        String errorMessage = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation ->
                        violation.getPropertyPath() + ": " + violation.getMessage()
                ).orElse("Constraint violation occurred");

        errorDto.setError(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error("Caught exception", ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("An unknown error occurred");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
