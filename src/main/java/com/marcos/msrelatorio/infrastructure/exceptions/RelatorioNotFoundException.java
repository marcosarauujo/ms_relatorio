package com.marcos.msrelatorio.infrastructure.exceptions;

public class RelatorioNotFoundException extends RuntimeException {

    public RelatorioNotFoundException(String mensagem) {
        super(mensagem);
    }
}
