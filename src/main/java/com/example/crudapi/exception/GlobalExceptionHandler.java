package com.example.crudapi.exception;

import org.springframework.dao.DataIntegrityViolationException; //importação da DataIntegrityViolationException - ex. email duplicado
import org.springframework.http.HttpStatus; //importação do HttpStatus
import org.springframework.http.ResponseEntity; //importação do ResponseEntity - resposta http
import org.springframework.web.bind.MethodArgumentNotValidException; //importação do MethodArgumentNotValidException - ex. campo anotados com @NotBlank
import org.springframework.web.bind.annotation.ExceptionHandler; //importação do ExceptionHandler - trata as exceções espeficicas
import org.springframework.web.bind.annotation.ControllerAdvice; //importação do ControllerAdvice - trata todas as exceções 

import java.util.HashMap;
import java.util.Map; //importação do map - retorno json

@ControllerAdvice 
public class GlobalExceptionHandler { //classe que trata as exceções

    @ExceptionHandler(ResourceNotFoundException.class) //trata a exceção ResourceNotFoundException
    public ResponseEntity<Map<String,String>> handleNotFound(ResourceNotFoundException ex) {
        Map<String,String> resp = new HashMap<>(); //mapa para retornar a mensagem de erro
        resp.put("message", ex.getMessage()); //adiciona a mensagem de erro ao mapa
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp); //retorna a resposta com o status NOT_FOUND
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //trata a exceção MethodArgumentNotValidException
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>(); //mapa para retornar a mensagem de erro
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()) //adiciona a mensagem de erro ao mapa
        );
        return ResponseEntity.badRequest().body(errors); //retorna a resposta com o status BAD_REQUEST
    }

    @ExceptionHandler({IllegalArgumentException.class, DataIntegrityViolationException.class}) //trata a exceção IllegalArgumentException e DataIntegrityViolationException
    public ResponseEntity<Map<String,String>> handleBadRequest(Exception ex) {
        Map<String,String> resp = new HashMap<>(); //mapa para retornar a mensagem de erro
        resp.put("message", ex.getMessage()); //adiciona a mensagem de erro ao mapa
        return ResponseEntity.badRequest().body(resp); //retorna a resposta com o status BAD_REQUEST
    }
}
