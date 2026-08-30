package co.taskflow.ecommerce.exception;

import co.taskflow.ecommerce.payload.ErrorResponse;
import co.taskflow.ecommerce.payload.FieldErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.utils.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleApp(AppException e) {
        log.warn("App error [{}]: {}", e.getStatus(), e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.of(e.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException e) {

        List<FieldErrorResponse> detail = e.getFieldErrors().stream()
            .map(fieldError -> new FieldErrorResponse(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            ))
            .toList();

        log.warn("Validation failed: {}", detail.size());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(
                        HttpStatus.BAD_REQUEST,
                        "Request data is invalid",
                        detail
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = String.format("Invalid value '%s' for parameter '%s'", e.getValue(), e.getName());
        log.warn("Type mismatch: {}", message);
        return ResponseEntity.badRequest().body(ErrorResponse.of(HttpStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handlerNotReadable(HttpMessageNotReadableException e) {
        log.warn("Malformed request body: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "Malformed or missing request body"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handlerNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("Method not allowed: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST,e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handelGeneric(Exception e) {
        log.error("unexpected error", e);
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"));
    }

}
