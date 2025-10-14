package com.fiap.giedapi.controller;

import com.fiap.giedapi.domain.model.Usuario;
import com.fiap.giedapi.dto.mappers.UserMapper;
import com.fiap.giedapi.dto.request.LoginRequestDTO;
import com.fiap.giedapi.dto.request.SenhaRequestDTO;
import com.fiap.giedapi.dto.request.UserRequestDTO;
import com.fiap.giedapi.dto.response.UserResponseDTO;
import com.fiap.giedapi.exception.EntityNotFoundException;
import com.fiap.giedapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/user")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Operation(summary = "Cria um novo usuário" ,description = "Endpoint para" +
            " cadastrar um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Campos " +
                    "preenchidos incorretamente"),
            @ApiResponse(responseCode = "409", description = "Um usuário já " +
                    "existe com esses dados")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> criarUsuario (@Valid @RequestBody UserRequestDTO userRequest){
        Usuario user = UserMapper.toUser(userRequest);
        String senhaClara = userRequest.senha();

        Usuario response = service.criarUsuario(user,senhaClara);
        UserResponseDTO responseDTO = UserMapper.toDTO(response);
        return ResponseEntity.created(URI.create("/api/user/"+responseDTO.id())).body(responseDTO);
    }

    @Operation(summary = "Buscar Usuario" , description = "Endpoint " +
            "que busca o usuário pelo id fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Retorna o " +
                    "usuário"),
            @ApiResponse(responseCode = "404", description = "Usuário com id " +
                    "inexistente")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> buscarPorId(@PathVariable Long id){
        try{
            UserResponseDTO responseDTO = UserMapper.toDTO(service.buscarPorId(id));
            return ResponseEntity.ok(responseDTO);
        }catch(IllegalStateException e){
            return ResponseEntity.notFound().build();
        }

    }

    @Operation(summary = "Deleta um usuário", description = "Deleta um " +
            "usuário pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário " +
                    "deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário com id " +
                    "não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        boolean removido = service.deletarUsuario(id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary="Alterar senha do usuário", description = "Altera a " +
            "senha do usuario pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Senha alterada " +
                    "com sucesso.")
    })
    @PutMapping("/alterar-senha/{id}")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id,
                                             @Valid @RequestBody SenhaRequestDTO dto){
        service.alterarSenha(id,dto.senhaAtual(),dto.senhaNova());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Login", description = "Endpoint para realizar login" +
            " do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado" +
                    " com sucesso"),
            @ApiResponse(responseCode = "401", description = "Crendenciais " +
                    "incorretas")
    })
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        try {
            Usuario usuario = service.autenticar(dto.login(), dto.senha());
            UserResponseDTO response = UserMapper.toDTO(usuario);
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
