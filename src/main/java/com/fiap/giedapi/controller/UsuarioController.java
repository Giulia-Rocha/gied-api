package com.fiap.giedapi.controller;

import com.fiap.giedapi.domain.model.Usuario;
import com.fiap.giedapi.dto.mappers.UserMapper;
import com.fiap.giedapi.dto.request.LoginRequestDTO;
import com.fiap.giedapi.dto.request.SenhaRequestDTO;
import com.fiap.giedapi.dto.request.UserRequestDTO;
import com.fiap.giedapi.dto.response.UserResponseDTO;
import com.fiap.giedapi.exception.EntityNotFoundException;
import com.fiap.giedapi.service.UsuarioService;
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

    @PostMapping
    public ResponseEntity<UserResponseDTO> criarUsuario (@RequestBody UserRequestDTO userRequest){
        Usuario user = UserMapper.toUser(userRequest);
        String senhaClara = userRequest.senha();

        Usuario response = service.criarUsuario(user,senhaClara);
        UserResponseDTO responseDTO = UserMapper.toDTO(response);
        return ResponseEntity.created(URI.create("/api/user/"+responseDTO.id())).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> buscarPorId(@PathVariable Long id){
        try{
            UserResponseDTO responseDTO = UserMapper.toDTO(service.buscarPorId(id));
            return ResponseEntity.ok(responseDTO);
        }catch(IllegalStateException e){
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        boolean removido = service.deletarUsuario(id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/alterar-senha/{id}")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id,
                                             @RequestBody SenhaRequestDTO dto){
        service.alterarSenha(id,dto.senhaAtual(),dto.senhaNova());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        try {
            Usuario usuario = service.autenticar(dto.login(), dto.senha());
            UserResponseDTO response = UserMapper.toDTO(usuario);
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
