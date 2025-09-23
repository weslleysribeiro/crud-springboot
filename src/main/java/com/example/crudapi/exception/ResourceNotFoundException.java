package com.example.crudapi.exception; //pacote onde a classe está

public class ResourceNotFoundException extends RuntimeException { //classe que extende a RuntimeException
    public ResourceNotFoundException(String message) { //construtor que recebe uma mensagem
        super(message);
    }
}