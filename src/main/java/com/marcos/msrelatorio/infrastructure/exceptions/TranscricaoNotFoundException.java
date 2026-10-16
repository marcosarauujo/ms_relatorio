package com.marcos.msrelatorio.infrastructure.exceptions;

public class TranscricaoNotFoundException extends RuntimeException {

    public TranscricaoNotFoundException(String mensagem) {
        super(mensagem);
    }
}
