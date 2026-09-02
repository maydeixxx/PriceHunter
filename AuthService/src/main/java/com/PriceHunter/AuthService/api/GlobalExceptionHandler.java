package com.PriceHunter.AuthService.api;

import com.PriceHunter.AuthService.models.dto.ErrorResponseDTO;
import com.PriceHunter.AuthService.models.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthArgumentException(AuthArgumentException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "auth error", ex.getMessage(), 404);
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(AuthExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthExistsException(AuthExistsException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "auth error", ex.getMessage(), 409);
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler(AuthNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthNotFoundException(AuthNotFoundException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "auth error", ex.getMessage(), 404);
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(MalformedTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleMalformedTokenException(MalformedTokenException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "token error", ex.getMessage(), 409);
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler(RefreshTokenArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleRefreshTokenArgumentException(RefreshTokenArgumentException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "refresh token error", ex.getMessage(), 400);
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponseDTO> handleRefreshTokenExpiredException(RefreshTokenExpiredException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "refresh token error", ex.getMessage(), 400);
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "refresh token error", ex.getMessage(), 404);
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(TokenStoleException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenStoleException(TokenStoleException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "token error", ex.getMessage(), 409);
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining());

        ErrorResponseDTO errorResponse = buildErrorResponse(request, "validation error", errorMessage, 400);
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex, WebRequest request) {
        ErrorResponseDTO errorResponse = buildErrorResponse(request, "server error", ex.getMessage(), 500);
        return ResponseEntity.status(500).body(errorResponse);
    }

    private static ErrorResponseDTO buildErrorResponse(WebRequest request, String error, String errorMessage, int code) {
        return ErrorResponseDTO.builder()
                .error(error)
                .errorMessage(errorMessage)
                .code(code)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }
}
