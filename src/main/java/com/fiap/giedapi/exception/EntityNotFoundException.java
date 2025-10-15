package com.fiap.giedapi.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Long id) {
        super("Entidade não encontrada com o id: "+id);
    }
}
