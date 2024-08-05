package com.backendbloggingapp.blog_app.exceptions;

import com.backendbloggingapp.blog_app.payloads.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Ye annotation ek exception handler ki tareh isse represent karegi
//iske andar saare exceptions honge,koi particular exception aati hai,toh woh exceptions chal jaayenge
// with help of this, hum global exception handling karte hain apne poore controller ki
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)//jab ye exception aayegi,toh ye method execute hoga
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        // resource not found exception ka object mil jaayega isse,->ex, and woh exception throw karenge,
        // uss message ke sath jo wahan de rahe thay
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> resp = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            resp.put(fieldName, message);
        });

        return new ResponseEntity<Map<String, String>>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex) {
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, true);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
    }

}
