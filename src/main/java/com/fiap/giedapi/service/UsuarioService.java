package com.fiap.giedapi.service;

import com.fiap.giedapi.exception.EntityNotFoundException;
import com.fiap.giedapi.exception.WrongCredentialsException;
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
            throw new WrongCredentialsException("Login e/ou senha inválidos");
        }
    }

    public Usuario criarUsuario(Usuario novoUsuario, String senha) {
        if (usuarioDao.findByLogin(novoUsuario.getLogin()).isPresent()) {
            throw new WrongCredentialsException("Já existe um usuário com este login.");
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
            throw new EntityNotFoundException(id);
        }
        return usuario;
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioDao.findById(id);
        if (usuario == null) {
            throw new EntityNotFoundException(id);

        }
        usuarioDao.delete(id);
    }

    public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
        // Busca o usuário no banco
        Usuario usuario = usuarioDao.findById(usuarioId);
        if (usuario == null) {
            throw new EntityNotFoundException(usuarioId);
        }

        // Verifica se a senha atual fornecida corresponde à senha armazenada no banco
        if (!BCrypt.checkpw(senhaAtual, usuario.getSenhaHash())) {
            throw new WrongCredentialsException("A senha atual está incorreta.");
        }

        // Gera o hash da nova senha
        String novaSenhaHash = BCrypt.hashpw(novaSenha, BCrypt.gensalt());
        usuario.setSenhaHash(novaSenhaHash);

        // Salva o usuário com a nova senha no banco
        usuarioDao.atualizar(usuario);
    }
}