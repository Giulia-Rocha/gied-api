package com.fiap.giedapi.dto.request;

public record UserRequestDTO (
        String nome,
        String login,
        String senha,
        String tipoUsuario

){
}
