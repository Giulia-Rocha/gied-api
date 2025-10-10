package com.fiap.giedapi.dto.mappers;

import com.fiap.giedapi.domain.enums.TipoUsuario;
import com.fiap.giedapi.domain.model.Usuario;
import com.fiap.giedapi.dto.request.SenhaRequestDTO;
import com.fiap.giedapi.dto.request.UserRequestDTO;
import com.fiap.giedapi.dto.response.UserResponseDTO;

import java.util.Map;

public class UserMapper {
    public static Usuario toUser(UserRequestDTO dto){
        Usuario user = new Usuario();
        user.setNome(dto.nome());
        user.setLogin(dto.login());

        if (dto.tipoUsuario() != null){
            try{
                user.setTipo(TipoUsuario.valueOf(dto.tipoUsuario().toUpperCase()));

            }catch (IllegalArgumentException e){
                throw new RuntimeException("Tipo de usuário inválido: " +dto.tipoUsuario());
            }
        }
        return user;
    }

    public static UserResponseDTO toDTO(Usuario user){
        return new UserResponseDTO(
                user.getId(),
                user.getNome(),
                user.getLogin(),
                user.getTipo() != null ? user.getTipo().name() : null
        );
    }



}
