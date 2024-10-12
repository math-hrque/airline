package br.com.funcionario.funcionario.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.funcionario.funcionario.dtos.FuncionarioRequestDto;
import br.com.funcionario.funcionario.dtos.FuncionarioResponseDto;
import br.com.funcionario.funcionario.dtos.UsuarioRequestAtualizarDto;
import br.com.funcionario.funcionario.dtos.UsuarioRequestCadastrarDto;
import br.com.funcionario.funcionario.exeptions.FuncionarioNaoExisteException;
import br.com.funcionario.funcionario.exeptions.ListaFuncionarioVaziaException;
import br.com.funcionario.funcionario.exeptions.OutroFuncionarioDadosJaExistenteException;
import br.com.funcionario.funcionario.models.Funcionario;
import br.com.funcionario.funcionario.repositories.FuncionarioRepository;
import br.com.funcionario.funcionario.utils.RedisFuncionarioCache;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RedisFuncionarioCache redisFuncionarioCache;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public UsuarioRequestCadastrarDto cadastrar(FuncionarioRequestDto funcionarioRequestDto) throws OutroFuncionarioDadosJaExistenteException {
        Optional<List<Funcionario>> existFuncionarioBD = funcionarioRepository.findByCpfOrEmail(funcionarioRequestDto.getCpf(), funcionarioRequestDto.getEmail());
        if (existFuncionarioBD.isPresent() && !existFuncionarioBD.get().isEmpty()) {
            boolean cpfExists = false;
            boolean emailExists = false;
            for (Funcionario funcionario : existFuncionarioBD.get()) {
                if (funcionario.isAtivo()) {
                    if (funcionario.getCpf().equals(funcionarioRequestDto.getCpf())) {
                        cpfExists = true;
                    }
                    if (funcionario.getEmail().equals(funcionarioRequestDto.getEmail())) {
                        emailExists = true;
                    } 
                }
            }
            if (cpfExists && emailExists) {
                throw new OutroFuncionarioDadosJaExistenteException("Outro funcionario ativo com cpf e email ja existente!");
            } else if (cpfExists) {
                throw new OutroFuncionarioDadosJaExistenteException("Outro funcionario ativo com cpf ja existente!");
            } else if (emailExists) {
                throw new OutroFuncionarioDadosJaExistenteException("Outro funcionario ativo com email ja existente!");
            }
        }

        Funcionario funcionario = mapper.map(funcionarioRequestDto, Funcionario.class);

        if (existFuncionarioBD.get().size() == 0) {
            funcionario.setIdFuncionario(0L);
        } else if (existFuncionarioBD.get().size() == 1) {
            funcionario.setIdFuncionario(existFuncionarioBD.get().get(0).getIdFuncionario());
        } else if (existFuncionarioBD.get().size() > 1) {
            throw new OutroFuncionarioDadosJaExistenteException("Outros funcionarios inativos, um com cpf e outro com email, ja existentes!");
        }

        Funcionario funcionarioCriadoBD = funcionarioRepository.save(funcionario);
        UsuarioRequestCadastrarDto usuarioRequestCadastrarDto = mapper.map(funcionarioCriadoBD, UsuarioRequestCadastrarDto.class);
        usuarioRequestCadastrarDto.setSenha(funcionarioRequestDto.getSenha());
        return usuarioRequestCadastrarDto;
    }

    public UsuarioRequestAtualizarDto atualizar(FuncionarioRequestDto funcionarioRequestDto) throws FuncionarioNaoExisteException, OutroFuncionarioDadosJaExistenteException {
        Optional<Funcionario> funcionarioBD = funcionarioRepository.findByIdFuncionarioAndAtivo(funcionarioRequestDto.getIdFuncionario(), true);
        if (!funcionarioBD.isPresent()) {
            throw new FuncionarioNaoExisteException("Funcionario ativo nao existe!");
        }

        Optional<List<Funcionario>> funcionarioExistente = funcionarioRepository.findByCpfOrEmail(funcionarioRequestDto.getCpf(), funcionarioRequestDto.getEmail());
        if (funcionarioExistente.isPresent()) {
            List<Funcionario> listaFuncionarioExistente = funcionarioExistente.get();
            boolean cpfOrEmailExists = listaFuncionarioExistente.stream().anyMatch(funcionario -> !funcionario.getIdFuncionario().equals(funcionarioBD.get().getIdFuncionario()));
            if (cpfOrEmailExists) {
                boolean cpfExists = listaFuncionarioExistente.stream().anyMatch(funcionario -> !funcionario.getIdFuncionario().equals(funcionarioBD.get().getIdFuncionario()) && funcionario.getCpf().equals(funcionarioBD.get().getCpf()));
                if (cpfExists) {
                    throw new OutroFuncionarioDadosJaExistenteException("Outro funcionario com cpf ja existente!");
                } else {
                    throw new OutroFuncionarioDadosJaExistenteException("Outro funcionario com email ja existente!");
                }
            }
        }

        Funcionario funcionario = mapper.map(funcionarioRequestDto, Funcionario.class);
        funcionario.setIdFuncionario(funcionarioRequestDto.getIdFuncionario());

        Funcionario funcionarioCache = redisFuncionarioCache.getCache(funcionarioBD.get().getIdFuncionario());
        if (funcionarioCache == null) {
            redisFuncionarioCache.saveCache(funcionarioBD.get());
        }

        Funcionario funcionarioAtualizadoBD = funcionarioRepository.save(funcionario);
        UsuarioRequestAtualizarDto usuarioRequestAtualizarDto = mapper.map(funcionarioAtualizadoBD, UsuarioRequestAtualizarDto.class);
        usuarioRequestAtualizarDto.setId(funcionarioBD.get().getIdFuncionario());
        usuarioRequestAtualizarDto.setOldEmail(funcionarioBD.get().getEmail());
        usuarioRequestAtualizarDto.setSenha(funcionarioRequestDto.getSenha());
        return usuarioRequestAtualizarDto;
    }

    public FuncionarioResponseDto inativar(String email) throws FuncionarioNaoExisteException {
        Optional<Funcionario> funcionarioBD = funcionarioRepository.findByEmailAndAtivo(email, true);
        if (!funcionarioBD.isPresent()) {
            throw new FuncionarioNaoExisteException("Funcionario ativo nao existe!");
        }

        Funcionario funcionario = funcionarioBD.get();
        funcionario.setAtivo(false);
        Funcionario funcionarioInativadoBD = funcionarioRepository.save(funcionario);
        FuncionarioResponseDto funcionarioInativadoDto = mapper.map(funcionarioInativadoBD, FuncionarioResponseDto.class);
        return funcionarioInativadoDto;
    }

    public FuncionarioResponseDto ativar(String email) throws FuncionarioNaoExisteException {
        Optional<Funcionario> funcionarioBD = funcionarioRepository.findByEmailAndAtivo(email, false);
        if (!funcionarioBD.isPresent()) {
            throw new FuncionarioNaoExisteException("Funcionario inativo nao existe!");
        }

        Funcionario funcionario = funcionarioBD.get();
        funcionario.setAtivo(true);
        Funcionario funcionarioAtivadoBD = funcionarioRepository.save(funcionario);
        FuncionarioResponseDto funcionarioAtivadoDto = mapper.map(funcionarioAtivadoBD, FuncionarioResponseDto.class);
        return funcionarioAtivadoDto;
    }

    public FuncionarioResponseDto remover(String email) throws FuncionarioNaoExisteException {
        Optional<Funcionario> funcionarioBD = funcionarioRepository.findByEmail(email);
        if (!funcionarioBD.isPresent()) {
            throw new FuncionarioNaoExisteException("Funcionario nao existe!");
        }

        Funcionario funcionario = funcionarioBD.get();
        funcionarioRepository.deleteById(funcionario.getIdFuncionario());
        FuncionarioResponseDto funcionarioRemovidoDto = mapper.map(funcionario, FuncionarioResponseDto.class);
        return funcionarioRemovidoDto;
    }

    public FuncionarioResponseDto reverter(Long idFuncionario) throws FuncionarioNaoExisteException {
        Optional<Funcionario> funcionarioBD = funcionarioRepository.findById(idFuncionario);
        if (!funcionarioBD.isPresent()) {
            throw new FuncionarioNaoExisteException("Funcionario nao existe!");
        }

        Funcionario funcionarioCache = redisFuncionarioCache.getCache(idFuncionario);
        if (funcionarioCache == null) {
            throw new FuncionarioNaoExisteException("Funcionario nao existe no cache!");
        }

        Funcionario funcionario = funcionarioRepository.save(funcionarioCache);
        redisFuncionarioCache.removeCache(funcionarioCache.getIdFuncionario());
        FuncionarioResponseDto funcionarioRevertidoDto = mapper.map(funcionario, FuncionarioResponseDto.class);
        return funcionarioRevertidoDto;
    }


    public List<FuncionarioResponseDto> listar() throws ListaFuncionarioVaziaException {
        Optional<List<Funcionario>> listaFuncionarioBD = funcionarioRepository.findByAtivo(true);
        if (!listaFuncionarioBD.isPresent() && listaFuncionarioBD.get().isEmpty()) {
            throw new ListaFuncionarioVaziaException("Lista de funcionarios ativos vazia!");
        }

        List<FuncionarioResponseDto> listaFuncionarioDto = listaFuncionarioBD.get().stream().map(funcionarioBD -> mapper.map(funcionarioBD, FuncionarioResponseDto.class)).collect(Collectors.toList());
        return listaFuncionarioDto;
    }

    public FuncionarioResponseDto consultarEmail(String email) throws FuncionarioNaoExisteException {
        Optional<Funcionario> funcionarioBD = funcionarioRepository.findByEmailAndAtivo(email, true);
        if (!funcionarioBD.isPresent()) {
            throw new FuncionarioNaoExisteException("Funcionario ativo nao existe!");
        }

        Funcionario funcionarioConsultadoBD = funcionarioBD.get();
        FuncionarioResponseDto funcionarioConsultadoDto = mapper.map(funcionarioConsultadoBD, FuncionarioResponseDto.class);
        return funcionarioConsultadoDto;
    }

    public FuncionarioResponseDto consultarIdFuncionario(Long idFuncionario) throws FuncionarioNaoExisteException {
        Optional<Funcionario> funcionarioBD = funcionarioRepository.findByIdFuncionarioAndAtivo(idFuncionario, true);
        if (!funcionarioBD.isPresent()) {
            throw new FuncionarioNaoExisteException("Funcionario ativo nao existe!");
        }

        Funcionario funcionarioConsultadoBD = funcionarioBD.get();
        FuncionarioResponseDto funcionarioConsultadoDto = mapper.map(funcionarioConsultadoBD, FuncionarioResponseDto.class);
        return funcionarioConsultadoDto;
    }
}
