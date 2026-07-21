package ism.gnims.coutcyclevie.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Format de la requête invalide. Vérifiez les types de données ou les valeurs .";
        if (ex.getCause() != null && ex.getCause().getMessage().contains("not one of the values accepted for Enum")) {
            message = "La valeur fournie n'est pas acceptée par l'application (Erreur de correspondance d'Enum).";
        }
        Map<String, String> errors = new HashMap<>();
        errors.put("info", message);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}