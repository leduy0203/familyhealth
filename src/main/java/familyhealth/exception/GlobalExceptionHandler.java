package familyhealth.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import familyhealth.model.dto.response.ErrorResponse;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);

        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errors = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(errors.size() > 1 ? String.valueOf(errors) : errors.get(0))
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleIdentityException(AppException exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);

        ErrorCode errorCode = exception.getErrorCode();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(errorCode.getCode())
                .error(errorCode.getHttpStatus().name())
                .message(errorCode.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }
}
