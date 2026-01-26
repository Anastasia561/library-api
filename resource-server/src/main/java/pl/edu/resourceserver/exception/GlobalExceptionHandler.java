package pl.edu.resourceserver.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.edu.resourceserver.wrapper.FieldValidationError;
import pl.edu.resourceserver.wrapper.ResponseWrapper;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class, InvalidFileInputException.class, FolderNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException e) {
        return buildError(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, org.springframework.http.HttpStatusCode status,
                                                                  org.springframework.web.context.request.WebRequest request) {

        List<FieldValidationError> validationErrors = ex.getBindingResult().getAllErrors()
                .stream().map(error -> {
                    if (error instanceof FieldError fe) {
                        return new FieldValidationError(fe.getField(), fe.getDefaultMessage());
                    }
                    return new FieldValidationError(error.getObjectName(), error.getDefaultMessage());
                }).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseWrapper.withValidationError(HttpStatus.BAD_REQUEST, "Validation failed", validationErrors));
    }

    private ResponseEntity<Object> buildError(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ResponseWrapper.withError(status, message));
    }
}
