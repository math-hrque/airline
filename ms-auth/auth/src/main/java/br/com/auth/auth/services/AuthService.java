package br.com.auth.auth.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.auth.auth.dtos.LoginDto;
import br.com.auth.auth.dtos.TokenDto;
import br.com.auth.auth.dtos.UsuarioRequestAtualizarDto;
import br.com.auth.auth.dtos.UsuarioRequestCadastrarDto;
import br.com.auth.auth.dtos.UsuarioResponseDto;
import br.com.auth.auth.exeptions.OutroUsuarioDadosJaExistenteException;
import br.com.auth.auth.exeptions.UsuarioNaoExisteException;
import br.com.auth.auth.models.Usuario;
import br.com.auth.auth.repositories.UsuarioRepository;
import br.com.auth.auth.security.TokenService;
import br.com.auth.auth.utils.Generate;
import br.com.auth.auth.utils.RedisUsuarioCache;

@Service
public class AuthService implements UserDetailsService {

    private AuthenticationManager manager;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisUsuarioCache redisUsuarioCache;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioResponseDto cadastrar(UsuarioRequestCadastrarDto usuarioRequestCadastrarDto) throws OutroUsuarioDadosJaExistenteException {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioRequestCadastrarDto.getEmail());
        if (usuarioExistente.isPresent() && usuarioExistente.get().isEnabled()) {
            throw new OutroUsuarioDadosJaExistenteException("Outro usuario ativo com email ja existente!");
        }

        if (usuarioRequestCadastrarDto.getSenha() == null) {
            usuarioRequestCadastrarDto.setSenha("");
        }
        String senha = Generate.generatePassword();
        Usuario usuario = mapper.map(usuarioRequestCadastrarDto, Usuario.class);
        usuario.setSenha(passwordEncoder.encode(senha));
        if (usuarioExistente.isPresent() && !usuarioExistente.get().isEnabled()) {
            usuario.setId(usuarioExistente.get().getId());
        }
        Usuario usuarioCriadoBD = usuarioRepository.save(usuario);
        UsuarioResponseDto usuarioCriadoDto = mapper.map(usuarioCriadoBD, UsuarioResponseDto.class);

        String mensagem = "<p>Bem vindo ao Company!<br><br>" + 
            "Acesse a sua conta com o email cadastrado e a senha abaixo:<br><br>" +
            "<strong style='font-size: 16px;'>Sua senha: " + senha + "</strong></p>";
        emailService.enviarEmail(usuarioCriadoDto.getEmail(), "Cadastro Company - Empresa Aérea", mensagem);

        return usuarioCriadoDto;
    }

    public UsuarioResponseDto atualizar(UsuarioRequestAtualizarDto usuarioRequestAtualizarDto) throws UsuarioNaoExisteException, OutroUsuarioDadosJaExistenteException {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuarioRequestAtualizarDto.getOldEmail());
        if (!usuarioBD.isPresent()) {
            throw new UsuarioNaoExisteException("Usuario nao existe!");
        }
        if (!usuarioBD.get().isEnabled()) {
            throw new OutroUsuarioDadosJaExistenteException("Usuario desse email esta inativo!");
        }
        if (!usuarioRequestAtualizarDto.getEmail().equals(usuarioRequestAtualizarDto.getOldEmail())) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioRequestAtualizarDto.getEmail());
            if (usuarioExistente.isPresent()) {
                if (usuarioExistente.get().isEnabled()) {
                    throw new OutroUsuarioDadosJaExistenteException("Outro usuario ativo com email ja existente!");
                } else {
                    throw new OutroUsuarioDadosJaExistenteException("Outro usuario inativo com email ja existente!");
                }
            }
        }
        
        if (usuarioRequestAtualizarDto.getSenha() == null) {
            usuarioRequestAtualizarDto.setSenha("");
        }
        Usuario usuario = mapper.map(usuarioRequestAtualizarDto, Usuario.class);
        usuario.setId(usuarioBD.get().getId());

        if (usuarioRequestAtualizarDto.getSenha().isEmpty()) {
            usuario.setSenha(usuarioBD.get().getSenha());
        } else {
            usuario.setSenha(passwordEncoder.encode(usuarioRequestAtualizarDto.getSenha()));
        }

        Usuario usuarioCache = redisUsuarioCache.getCache(usuarioBD.get().getId());
        if (usuarioCache == null) {
            redisUsuarioCache.saveCache(usuarioBD.get());
        }

        Usuario usuarioAtualizadoBD = usuarioRepository.save(usuario);
        UsuarioResponseDto usuarioAtualizadoDto = mapper.map(usuarioAtualizadoBD, UsuarioResponseDto.class);
        return usuarioAtualizadoDto;
    }

    public UsuarioResponseDto inativar(String email) throws UsuarioNaoExisteException {
        Usuario usuarioBD = usuarioRepository.findByEmailAndAtivo(email, true);
        if (usuarioBD == null) {
            throw new UsuarioNaoExisteException("Usuario ativo nao existe!");
        }

        usuarioBD.setAtivo(false);
        Usuario usuarioInativadoBD = usuarioRepository.save(usuarioBD);
        UsuarioResponseDto usuarioInativadoDto = mapper.map(usuarioInativadoBD, UsuarioResponseDto.class);
        return usuarioInativadoDto;
    }

    public UsuarioResponseDto ativar(String email) throws UsuarioNaoExisteException {
        Usuario usuarioBD = usuarioRepository.findByEmailAndAtivo(email, false);
        if (usuarioBD == null) {
            throw new UsuarioNaoExisteException("Usuario inativo nao existe!");
        }

        usuarioBD.setAtivo(true);
        Usuario usuarioAtivadoBD = usuarioRepository.save(usuarioBD);
        UsuarioResponseDto usuarioAtivadoDto = mapper.map(usuarioAtivadoBD, UsuarioResponseDto.class);
        return usuarioAtivadoDto;
    }

    public UsuarioResponseDto remover(String email) throws UsuarioNaoExisteException {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(email);
        if (!usuarioBD.isPresent()) {
            throw new UsuarioNaoExisteException("Usuario nao existe!");
        }

        Usuario usuario = usuarioBD.get();
        usuarioRepository.deleteById(usuario.getId());
        UsuarioResponseDto usuarioRemovidoDto = mapper.map(usuarioBD, UsuarioResponseDto.class);
        return usuarioRemovidoDto;
    }

    public UsuarioResponseDto reverter(String email) throws UsuarioNaoExisteException {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(email);
        if (!usuarioBD.isPresent()) {
            throw new UsuarioNaoExisteException("Usuario nao existe!");
        }

        Usuario usuarioCache = redisUsuarioCache.getCache(usuarioBD.get().getId());
        if (usuarioCache == null) {
            throw new UsuarioNaoExisteException("Usuario nao existe no cache!");
        }

        Usuario usuario = usuarioRepository.save(usuarioCache);
        redisUsuarioCache.removeCache(usuarioCache.getId());
        UsuarioResponseDto usuarioRevertidoDto = mapper.map(usuario, UsuarioResponseDto.class);
        return usuarioRevertidoDto;
    }

    public TokenDto login(LoginDto loginDto) {
        manager = context.getBean(AuthenticationManager.class);
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha());
        var authentication = this.manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return new TokenDto(tokenJWT);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails usuario = usuarioRepository.findByEmailAndAtivo(username, true);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario ativo nao encontrado: " + username);
        }

        return usuario;
    }
}
