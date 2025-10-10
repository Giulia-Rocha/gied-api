package com.fiap.giedapi.dto.response;

public record UserResponseDTO (
        Long id,
        String nome,
        String login,
        String tipo
){
}
