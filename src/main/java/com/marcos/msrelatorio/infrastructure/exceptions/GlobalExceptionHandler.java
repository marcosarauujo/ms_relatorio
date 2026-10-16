package com.marcos.msrelatorio.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RelatorioNotFoundException.class)
    public ResponseEntity<String> handleRelatorioNotFound(RelatorioNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(feign.FeignException.class)
    public ResponseEntity<String> handleFeignException(feign.FeignException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body("Erro ao comunicar com outro microsserviço: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno no servidor: " + ex.getMessage());
    }

    @ExceptionHandler(TranscricaoNotFoundException.class)
    public ResponseEntity<String> handleTranscricaoNotFound(TranscricaoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
