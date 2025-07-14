package weatherApp.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import weatherApp.response.RestApiResponse;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ Handle Custom APIException
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<RestApiResponse<Object>> handleApiException(ApiException ex) {
        log.warn("API Exception: {} - {}", ex.getCode(), ex.getMessage());

        ErrorModel error = ErrorModel.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(RestApiResponse.builder().error(error).build());
    }

    // ✅ Handle Validation Errors (POST body fields with @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());

        List<ErrorModel> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> ErrorModel.builder()
                        .code("VALIDATION_ERROR")
                        .message(err.getField() + " " + err.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(RestApiResponse.builder().errors(errors).build());
    }

    // ✅ Handle ConstraintViolations (@RequestParam or @PathVariable validations)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorModel> errors = ex.getConstraintViolations()
                .stream()
                .map(cv -> ErrorModel.builder()
                        .code("INVALID_PARAM")
                        .message(cv.getPropertyPath() + " " + cv.getMessage())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(RestApiResponse.builder().errors(errors).build());
    }

    // ✅ Handle any other uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("Unexpected exception occurred", ex);

        ErrorModel error = ErrorModel.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("Something went wrong. Please try again later.")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RestApiResponse.builder().error(error).build());
    }
}
