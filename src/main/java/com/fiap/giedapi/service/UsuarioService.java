package com.fiap.giedapi.service;

import com.fiap.giedapi.repository.UsuarioDao;
import com.fiap.giedapi.domain.model.Usuario;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UsuarioService {
    private final UsuarioDao usuarioDao;

    public UsuarioService(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Usuario autenticar(String login, String senha) {
        Optional<Usuario> user = usuarioDao.findByLogin(login);
        if (user.isPresent() && BCrypt.checkpw(senha, user.get().getSenhaHash())) {
            return user.get();
        } else {
            throw new SecurityException("Login ou Senha Inválidos");
        }
    }

    public Usuario criarUsuario(Usuario novoUsuario, String senha) {
        if (novoUsuario.getLogin() == null || novoUsuario.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("O login do usuário é obrigatório.");
        }
        if (usuarioDao.findByLogin(novoUsuario.getLogin()).isPresent()) {
            throw new IllegalStateException("Já existe um usuário com este login.");
        }

        // Gera o hash da senha e o define no objeto
        String senhaHasheada = BCrypt.hashpw(senha, BCrypt.gensalt());
        novoUsuario.setSenhaHash(senhaHasheada);

        Usuario usuarioCriado = usuarioDao.criar(novoUsuario);
        return usuarioCriado;
    }

    public Usuario buscarPorId(Long id) {
        Usuario usuario = usuarioDao.findById(id);
        if (usuario == null) {
            throw new IllegalStateException("Usuário com ID " + id + " não encontrado.");
        }
        return usuario;
    }

    public boolean deletarUsuario(Long id) {
        // Primeiro, verifica se o usuário existe
        if(id != null){
            buscarPorId(id);
            usuarioDao.delete(id);
            return true;
        }
        else{
            return false;
        }
    }

    public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
        // Busca o usuário no banco
        Usuario usuario = usuarioDao.findById(usuarioId);
        if (usuario == null) {
            throw new IllegalStateException("Usuário não encontrado. A sessão pode ter expirado.");
        }

        // Verifica se a senha atual fornecida corresponde à senha armazenada no banco
        if (!BCrypt.checkpw(senhaAtual, usuario.getSenhaHash())) {
            throw new SecurityException("A senha atual está incorreta.");
        }

        // Valida se a nova senha não está em branco
        if (novaSenha == null || novaSenha.trim().isEmpty()) {
            throw new IllegalArgumentException("A nova senha não pode estar em branco.");
        }

        // Gera o hash da nova senha
        String novaSenhaHash = BCrypt.hashpw(novaSenha, BCrypt.gensalt());
        usuario.setSenhaHash(novaSenhaHash);

        // Salva o usuário com a nova senha no banco
        usuarioDao.atualizar(usuario);
    }
}