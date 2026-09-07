package com.PriceHunter.UserService.api;

import com.PriceHunter.UserService.models.dto.ErrorResponseDTO;
import com.PriceHunter.UserService.models.exceptions.NotificationSettingsArgException;
import com.PriceHunter.UserService.models.exceptions.UserArgumentException;
import com.PriceHunter.UserService.models.exceptions.UserNotFoundException;
import com.PriceHunter.UserService.models.exceptions.UserUpdateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotificationSettingsArgException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotificationSettingsArgException(NotificationSettingsArgException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = buildErrorResponse(request, "Not valid args in notification settings", ex.getMessage(), 400);
        return ResponseEntity.status(errorResponseDTO.getCode()).body(errorResponseDTO);
    }

    @ExceptionHandler(UserArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserArgumentException(UserArgumentException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = buildErrorResponse(request, "Not valid arg in user model", ex.getMessage(), 400);
        return ResponseEntity.status(errorResponseDTO.getCode()).body(errorResponseDTO);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = buildErrorResponse(request, "User not found", ex.getMessage(), 404);
        return ResponseEntity.status(errorResponseDTO.getCode()).body(errorResponseDTO);
    }

    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserUpdateException(UserUpdateException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = buildErrorResponse(request, "Error updating user", ex.getMessage(), 400);
        return ResponseEntity.status(errorResponseDTO.getCode()).body(errorResponseDTO);
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
