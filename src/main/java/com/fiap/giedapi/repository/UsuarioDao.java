package com.fiap.giedapi.repository;

import com.fiap.giedapi.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioDao {
     Optional<Usuario> findByLogin(String login);
     Usuario criar(Usuario usuario);
     void atualizar(Usuario usuario);
     List<Usuario> findAll();
     Usuario findById(Long id);
     void delete(Long id);

}
