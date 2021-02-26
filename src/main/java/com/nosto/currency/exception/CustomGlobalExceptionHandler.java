package com.nosto.currency.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> springHandleNotFound(NotFoundException ex,HttpServletResponse response) throws IOException {
    	 ErrorMessage errors = creatError(ex,HttpStatus.NOT_FOUND);
         return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UnSupportedCurrencyException.class)
    public ResponseEntity<ErrorMessage> springUnSupportedFieldPatch(UnSupportedCurrencyException ex,HttpServletResponse response) throws IOException {
    	 ErrorMessage errors = creatError(ex,HttpStatus.METHOD_NOT_ALLOWED);
         return new ResponseEntity<>(errors, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorMessage> springUnSupportedFieldPatch(IOException ex,HttpServletResponse response) throws IOException {
    	 ErrorMessage errors = creatError(ex,HttpStatus.INTERNAL_SERVER_ERROR);
    	 errors.setError("Server connection error, try again later...");
    	 return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // @Validate For Validating Path Variables and Request Parameters
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> constraintViolationException(ConstraintViolationException ex,HttpServletResponse response) throws IOException {
        ErrorMessage errors = creatError(ex,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        
    }


	private ErrorMessage creatError(Exception ex,HttpStatus status) {
		ErrorMessage errors = new ErrorMessage();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(status.value());
		return errors;
	}

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object>
    handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                 HttpHeaders headers,
                                 HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);

    }

}