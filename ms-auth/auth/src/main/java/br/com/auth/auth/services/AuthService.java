package br.com.auth.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.auth.auth.dtos.TokenDto;
import br.com.auth.auth.dtos.UsuarioRequestDto;
import br.com.auth.auth.models.Usuario;
import br.com.auth.auth.repositories.UsuarioRepository;
import br.com.auth.auth.security.TokenService;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public TokenDto login(UsuarioRequestDto usuarioRequestDto) {
        manager = context.getBean(AuthenticationManager.class);
        var authenticationToken = new UsernamePasswordAuthenticationToken(usuarioRequestDto.email(), usuarioRequestDto.senha());
        var authentication = this.manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        System.out.println("Token gerado: " + tokenJWT);
        return new TokenDto(tokenJWT);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        return new User(usuario.getEmail(), usuario.getSenha(), usuario.getAuthorities());
    }    

}
