package br.com.auth.auth.services;

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

import br.com.auth.auth.dtos.Login;
import br.com.auth.auth.dtos.TokenDto;
import br.com.auth.auth.dtos.UsuarioRequestDto;
import br.com.auth.auth.dtos.UsuarioResponseDto;
import br.com.auth.auth.exeptions.OutroUsuarioDadosJaExistente;
import br.com.auth.auth.exeptions.UsuarioNaoExisteException;
import br.com.auth.auth.models.Usuario;
import br.com.auth.auth.repositories.UsuarioRepository;
import br.com.auth.auth.security.TokenService;
import br.com.auth.auth.util.Generate;

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
    private UsuarioRepository usuarioRepository;

    public TokenDto login(Login login) {
        manager = context.getBean(AuthenticationManager.class);
        var authenticationToken = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getSenha());
        var authentication = this.manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return new TokenDto(tokenJWT);
    }

    public UsuarioResponseDto cadastrar(UsuarioRequestDto usuarioRequestDto) throws OutroUsuarioDadosJaExistente {
        if (usuarioRepository.existsById(usuarioRequestDto.getEmail())) {
            throw new OutroUsuarioDadosJaExistente("Outro usuario com Email ja existente!");
        }
        
        String senha = Generate.generatePassword();
        Usuario usuario = mapper.map(usuarioRequestDto, Usuario.class);
        usuario.setSenha(passwordEncoder.encode(senha));
        Usuario usuarioCriadoBD = usuarioRepository.save(usuario);
        UsuarioResponseDto usuarioCriadoDto = mapper.map(usuarioCriadoBD, UsuarioResponseDto.class);

        String mensagem = "<p>Bem vindo ao Company!<br><br>" + 
            "Acesse a sua conta com o email cadastrado e a senha abaixo:<br><br>" +
            "<strong style='font-size: 16px;'>Sua senha: " + senha + "</strong></p>";
        emailService.enviarEmail(usuarioCriadoDto.getEmail(), "Cadastro Company - Empresa Aérea", mensagem);

        return usuarioCriadoDto;
    }

    public UsuarioResponseDto atualizar(String email, UsuarioRequestDto usuarioRequestDto) throws UsuarioNaoExisteException, OutroUsuarioDadosJaExistente {
        Usuario usuarioExistente = usuarioRepository.findByEmailAndAtivo(email, true);
        if (usuarioExistente == null) {
            throw new UsuarioNaoExisteException("Usuario nao existe!");
        } 
        if (usuarioRepository.existsById(usuarioRequestDto.getEmail())) {
            throw new OutroUsuarioDadosJaExistente("Outro usuario com email ja existente!");
        }

        Usuario usuario = mapper.map(usuarioRequestDto, Usuario.class);
        if (usuarioRequestDto.getSenha() == null && usuarioRequestDto.getSenha().isEmpty()) {
            usuario.setSenha(usuarioExistente.getSenha());
        } else {
            usuario.setSenha(passwordEncoder.encode(usuarioRequestDto.getSenha()));
        }
        usuarioRepository.deleteById(email);
        Usuario usuarioAtualizadoBD = usuarioRepository.save(usuario);
        UsuarioResponseDto usuarioCriadoDto = mapper.map(usuarioAtualizadoBD, UsuarioResponseDto.class);
        return usuarioCriadoDto;
    }

    public UsuarioResponseDto inativar(String email) throws UsuarioNaoExisteException {
        Usuario usuarioBD = usuarioRepository.findByEmailAndAtivo(email, true);
        if (usuarioBD == null) {
            throw new UsuarioNaoExisteException("Usuario nao existe!");
        }

        usuarioBD.setAtivo(false);
        Usuario usuarioInativadoBD = usuarioRepository.save(usuarioBD);
        UsuarioResponseDto funcionarioInativadoDto = mapper.map(usuarioInativadoBD, UsuarioResponseDto.class);
        return funcionarioInativadoDto;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails usuario = usuarioRepository.findByEmailAndAtivo(username, true);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario nao encontrado: " + username);
        }

        return usuario;
    }    
}
