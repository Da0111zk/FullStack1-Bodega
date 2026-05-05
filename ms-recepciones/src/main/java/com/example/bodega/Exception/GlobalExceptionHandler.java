package com.example.bodega.Exception;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacion(
            MethodArgumentNotValidException ex) {
        Map<String, String> campos = new HashMap<>();
        for (FieldError e : ex.getBindingResult().getFieldErrors())
            campos.put(e.getField(), e.getDefaultMessage());
        Map<String, Object> resp = new HashMap<>();
        resp.put("timestamp", LocalDateTime.now().toString());
        resp.put("status", 400);
        resp.put("error", "Validación fallida");
        resp.put("campos", campos);
        return ResponseEntity.badRequest().body(resp);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("timestamp", LocalDateTime.now().toString());
        resp.put("status", 400);
        resp.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("timestamp", LocalDateTime.now().toString());
        resp.put("status", 500);
        resp.put("error", "Error interno del servidor");
        resp.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }
}