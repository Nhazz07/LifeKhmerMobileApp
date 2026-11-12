package com.lifekhmer.common.exception;
import com.lifekhmer.common.response.ApiErrorResponse;
import com.lifekhmer.common.response.ApiErrorResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ApiErrorResponseFactory errorResponses;

    public GlobalExceptionHandler(ApiErrorResponseFactory errorResponses) {
        this.errorResponses = errorResponses;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validation(MethodArgumentNotValidException e, HttpServletRequest r) {
        Map<String,String> details = e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a,b)->a));
        return response(HttpStatus.BAD_REQUEST, "Validation failed", r, details);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> constraintViolation(ConstraintViolationException e, HttpServletRequest r) {
        Map<String, String> details = e.getConstraintViolations().stream().collect(Collectors.toMap(
                violation -> violation.getPropertyPath().toString(), violation -> violation.getMessage(), (a, b) -> a));
        return response(HttpStatus.BAD_REQUEST, "Validation failed", r, details);
    }
    @ExceptionHandler(ResourceNotFoundException.class) public ResponseEntity<ApiErrorResponse> missing(ResourceNotFoundException e, HttpServletRequest r) { return response(HttpStatus.NOT_FOUND,e.getMessage(),r,null); }
    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class}) public ResponseEntity<ApiErrorResponse> bad(RuntimeException e, HttpServletRequest r) { return response(HttpStatus.BAD_REQUEST,e.getMessage(),r,null); }
    @ExceptionHandler(UnauthorizedException.class) public ResponseEntity<ApiErrorResponse> unauthorized(UnauthorizedException e, HttpServletRequest r) { return response(HttpStatus.UNAUTHORIZED,e.getMessage(),r,null); }
    @ExceptionHandler(ForbiddenException.class) public ResponseEntity<ApiErrorResponse> forbidden(ForbiddenException e, HttpServletRequest r) { return response(HttpStatus.FORBIDDEN,e.getMessage(),r,null); }
    @ExceptionHandler({ConflictException.class, DuplicateResourceException.class}) public ResponseEntity<ApiErrorResponse> conflict(RuntimeException e, HttpServletRequest r) { return response(HttpStatus.CONFLICT,e.getMessage(),r,null); }
    @ExceptionHandler(HttpMessageNotReadableException.class) public ResponseEntity<ApiErrorResponse> unreadable(HttpMessageNotReadableException e, HttpServletRequest r) { return response(HttpStatus.BAD_REQUEST,"Malformed JSON request",r,null); }
    @ExceptionHandler(DataIntegrityViolationException.class) public ResponseEntity<ApiErrorResponse> dataIntegrity(DataIntegrityViolationException e, HttpServletRequest r) { log.warn("Data integrity violation on {}", r.getRequestURI()); return response(HttpStatus.CONFLICT,"The request conflicts with existing data",r,null); }
    @ExceptionHandler(Exception.class) public ResponseEntity<ApiErrorResponse> other(Exception e, HttpServletRequest r) { log.error("Unhandled exception on {}", r.getRequestURI(), e); return response(HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred",r,null); }
    private ResponseEntity<ApiErrorResponse> response(HttpStatus status, String message, HttpServletRequest request, Map<String,String> details) { return ResponseEntity.status(status).body(errorResponses.create(status,message,request,details)); }
}
