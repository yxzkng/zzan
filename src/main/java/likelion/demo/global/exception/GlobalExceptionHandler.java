package likelion.demo.global.exception;

import likelion.demo.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // TODO: @RestControllerAdvice 구현
    @ExceptionHandler(DuplicateLoginIdException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateLoginIdException(DuplicateLoginIdException e) {

        return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleLoginFailedException(LoginFailedException e) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    private ResponseEntity<ApiResponse<Void>> buildErrorResponse(HttpStatus status, String message){
        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(status.value(), message));
    }

}
