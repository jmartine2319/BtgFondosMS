package com.btg.fondos.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controller advice para las excepciones no controladas
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Controller cuando se genera un error en tiempo de ejecución
     * @param ex mensaje de la excepcion
     * @return respuesta del error escalado
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody(ex.getMessage()));
    }

    /**
     * Controller de un error inesperado no controlado
     * @param ex mensaje de la excepcion
     * @return respuesta del error escalado
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody(ex.getMessage()));
    }

    /**
     * Metodo para formatear el texto del error
     * @param mensaje
     * @return
     */
    private Map<String, Object> errorBody(String mensaje) {
        return Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "error", mensaje
        );
    }
}
