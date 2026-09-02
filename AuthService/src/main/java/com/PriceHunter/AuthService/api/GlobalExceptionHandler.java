package com.PriceHunter.AuthService.api;

import com.PriceHunter.AuthService.models.dto.ErrorResponseDTO;
import com.PriceHunter.AuthService.models.exceptions.AuthArgumentException;
import com.PriceHunter.AuthService.models.exceptions.AuthExistsException;
import com.PriceHunter.AuthService.models.exceptions.AuthNotFoundException;
import com.PriceHunter.AuthService.models.exceptions.MalformedTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthArgumentException(AuthArgumentException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "not valid auth argument", ex.getMessage(), 404);
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(AuthExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthExistsException(AuthExistsException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "auth exists", ex.getMessage(), 409);
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler(AuthNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthNotFoundException(AuthNotFoundException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "auth not found", ex.getMessage(), 404);
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(MalformedTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleMalformedTokenException(MalformedTokenException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "token malformed", ex.getMessage(), 409);
        return ResponseEntity.status(409).body(errorResponse);
    }

    private static ErrorResponseDTO buildErrorResponse(WebRequest request, String error, String errorMessage, int code) {
        try {
            return ErrorResponseDTO.builder()
                    .error(error)
                    .errorMessage(errorMessage)
                    .code(code)
                    .path(request.getDescription(false).replace("uri=", ""))
                    .build();
        } catch (Exception e) {
            log.error("Error building error response for controller advice: {}", e.getMessage());
        }
    }
}
