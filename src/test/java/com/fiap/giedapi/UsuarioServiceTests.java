package com.fiap.giedapi;

import com.fiap.giedapi.repository.UsuarioDao;
import com.fiap.giedapi.domain.enums.TipoUsuario;
import com.fiap.giedapi.domain.model.Usuario;
import com.fiap.giedapi.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock // Cria um mock (objeto falso) da interface UsuarioDao
    private UsuarioDao usuarioDao;

    @InjectMocks // Cria uma instância de UsuarioService e injeta o mock 'usuarioDao' nela
    private UsuarioService usuarioService;

    private Usuario usuario;
    private String senhaPlana = "senha123";
    private String senhaHasheada;

    @BeforeEach
    void setUp() {
        // Configuração inicial executada antes de cada teste
        senhaHasheada = BCrypt.hashpw(senhaPlana, BCrypt.gensalt());
        usuario = new Usuario(1L, "Usuário Teste", "teste", senhaHasheada, TipoUsuario.DEFAULT);
    }

    @Test
    void deveAutenticarUsuarioComSucesso() {
        // Cenário
        when(usuarioDao.findByLogin("teste")).thenReturn(Optional.of(usuario));

        // Ação
        Usuario usuarioAutenticado = usuarioService.autenticar("teste", senhaPlana);

        // Verificação
        assertNotNull(usuarioAutenticado);
        assertEquals("Usuário Teste", usuarioAutenticado.getNome());
        verify(usuarioDao).findByLogin("teste"); // Verifica se o método findByLogin foi chamado
    }

    @Test
    void deveLancarExcecaoAoAutenticarComSenhaInvalida() {
        // Cenário
        when(usuarioDao.findByLogin("teste")).thenReturn(Optional.of(usuario));

        // Ação & Verificação
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            usuarioService.autenticar("teste", "senhaErrada");
        });

        assertEquals("Login ou Senha Inválidos", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoAoAutenticarUsuarioInexistente() {
        // Cenário
        when(usuarioDao.findByLogin("inexistente")).thenReturn(Optional.empty());

        // Ação & Verificação
        assertThrows(SecurityException.class, () -> {
            usuarioService.autenticar("inexistente", "qualquerSenha");
        });
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        // Cenário
        Usuario novoUsuario = new Usuario(null, "Novo Usuário", "novoLogin", null, TipoUsuario.DEFAULT);

        // Simula o comportamento do DAO: quando o login não existe e quando o usuário é criado
        when(usuarioDao.findByLogin("novoLogin")).thenReturn(Optional.empty());
        when(usuarioDao.criar(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(2L); // Simula a geração de um ID pelo banco de dados
            return u;
        });

        // Ação
        Usuario usuarioCriado = usuarioService.criarUsuario(novoUsuario, "novaSenha");

        // Verificação
        assertNotNull(usuarioCriado);
        assertEquals(2L, usuarioCriado.getId());
        assertEquals("novoLogin", usuarioCriado.getLogin());
        assertNotNull(usuarioCriado.getSenhaHash());
        verify(usuarioDao).criar(any(Usuario.class)); // Verifica se o método criar foi chamado
    }

    @Test
    void deveLancarExcecaoAoCriarUsuarioComLoginExistente() {
        // Cenário
        when(usuarioDao.findByLogin("teste")).thenReturn(Optional.of(usuario));
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setLogin("teste");

        // Ação & Verificação
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            usuarioService.criarUsuario(usuarioExistente, "outraSenha");
        });

        assertEquals("Já existe um usuário com este login.", exception.getMessage());
    }
}