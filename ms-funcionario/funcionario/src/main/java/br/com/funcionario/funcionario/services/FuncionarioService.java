package br.com.funcionario.funcionario.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.funcionario.funcionario.dtos.FuncionarioRequestDto;
import br.com.funcionario.funcionario.dtos.FuncionarioResponseDto;
import br.com.funcionario.funcionario.exeptions.FuncionarioNaoExisteException;
import br.com.funcionario.funcionario.exeptions.ListaFuncionarioVaziaException;
import br.com.funcionario.funcionario.exeptions.OutroFuncionarioDadosJaExistente;
import br.com.funcionario.funcionario.exeptions.OutroUsuarioDadosJaExistente;
import br.com.funcionario.funcionario.models.Funcionario;
import br.com.funcionario.funcionario.repositories.FuncionarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public FuncionarioResponseDto cadastrar(FuncionarioRequestDto funcionarioRequestDto) throws OutroFuncionarioDadosJaExistente, OutroUsuarioDadosJaExistente {
        if (funcionarioRepository.existsByCpf(funcionarioRequestDto.getCpf()) || funcionarioRepository.existsByEmail(funcionarioRequestDto.getEmail())) {
            throw new OutroFuncionarioDadosJaExistente("Outro funcionario com CPF e/ou Email ja existente!");
        }

        // #MENSAGERIA -> MS AUTH (cadastrar usuario)
        // #MENSAGERIA -> MS AUTH (verificar se email de outro usuario já existe)
        // throw new OutroUsuarioDadosJaExistente("Outro usuario com Email ja existente!");

        Funcionario funcionario = mapper.map(funcionarioRequestDto, Funcionario.class);
        funcionario.setIdFuncionario(0L);
        Funcionario funcionarioCriadoBD = funcionarioRepository.save(funcionario);
        FuncionarioResponseDto funcionarioCriadoDto = mapper.map(funcionarioCriadoBD, FuncionarioResponseDto.class);
        return funcionarioCriadoDto;
    }

    public FuncionarioResponseDto atualizar(Long idFuncionario, FuncionarioRequestDto funcionarioRequestDto) throws FuncionarioNaoExisteException, OutroFuncionarioDadosJaExistente, OutroUsuarioDadosJaExistente {
        Optional<Funcionario> funcionarioBD = funcionarioRepository.findByIdFuncionarioAndAtivo(idFuncionario, true);
        if (!funcionarioBD.isPresent()) {
            throw new FuncionarioNaoExisteException("Funcionario nao existe!");
        }

        Optional<List<Funcionario>> funcionarioExistente = funcionarioRepository.findByCpfOrEmail(funcionarioRequestDto.getCpf(), funcionarioRequestDto.getEmail());
        if (funcionarioExistente.isPresent()) {
            List<Funcionario> listaFuncionarioExistente = funcionarioExistente.get();
            boolean cpfOrEmailExists = listaFuncionarioExistente.stream().anyMatch(funcionario -> !funcionario.getIdFuncionario().equals(funcionarioBD.get().getIdFuncionario()));
            if (cpfOrEmailExists) {
                boolean cpfExists = listaFuncionarioExistente.stream().anyMatch(funcionario -> !funcionario.getIdFuncionario().equals(funcionarioBD.get().getIdFuncionario()) && funcionario.getCpf().equals(funcionarioBD.get().getCpf()));
                if (cpfExists) {
                    throw new OutroFuncionarioDadosJaExistente("Outro funcionario com cpf ja existente!");
                } else {
                    throw new OutroFuncionarioDadosJaExistente("Outro funcionario com email ja existente!");
                }
            }
        }

        // #MENSAGERIA -> MS AUTH (atualizar usuario)
        // #MENSAGERIA -> MS AUTH (verificar se email de outro usuario já existe)
        // throw new OutroUsuarioDadosJaExistente("Outro usuario com Email ja existente!");

        Funcionario funcionario = mapper.map(funcionarioRequestDto, Funcionario.class);
        funcionario.setIdFuncionario(idFuncionario);
        Funcionario funcionarioAtualizadoBD = funcionarioRepository.save(funcionario);
        FuncionarioResponseDto funcionarioCriadoDto = mapper.map(funcionarioAtualizadoBD, FuncionarioResponseDto.class);
        return funcionarioCriadoDto;
    }

    public FuncionarioResponseDto inativar(Long idFuncionario) throws FuncionarioNaoExisteException {
        Optional<Funcionario> funcionarioBD = funcionarioRepository.findByIdFuncionarioAndAtivo(idFuncionario, true);
        if (!funcionarioBD.isPresent()) {
            throw new FuncionarioNaoExisteException("Funcionario nao existe!");
        }

        // #MENSAGERIA -> MS AUTH (inativar usuario)
        // #MENSAGERIA -> MS AUTH (verificar se email do usuario existe)
        // throw new OutroUsuarioDadosJaExistente("Outro usuario com Email ja existente!");

        Funcionario funcionario = funcionarioBD.get();
        funcionario.setAtivo(false);
        Funcionario funcionarioInativadoBD = funcionarioRepository.save(funcionario);
        FuncionarioResponseDto funcionarioInativadoDto = mapper.map(funcionarioInativadoBD, FuncionarioResponseDto.class);
        return funcionarioInativadoDto;
    }

    public FuncionarioResponseDto consultarId(Long idFuncionario) throws FuncionarioNaoExisteException {
        Optional<Funcionario> funcionarioBD = funcionarioRepository.findByIdFuncionarioAndAtivo(idFuncionario, true);
        if (!funcionarioBD.isPresent()) {
            throw new FuncionarioNaoExisteException("Funcionario nao existe!");
        }

        Funcionario funcionarioConsultadoBD = funcionarioBD.get();
        FuncionarioResponseDto funcionarioConsultadoDto = mapper.map(funcionarioConsultadoBD, FuncionarioResponseDto.class);
        return funcionarioConsultadoDto;
    }

    public FuncionarioResponseDto consultarEmail(String email) throws FuncionarioNaoExisteException {
        Optional<Funcionario> funcionarioBD = funcionarioRepository.findByEmailAndAtivo(email, true);
        if (!funcionarioBD.isPresent()) {
            throw new FuncionarioNaoExisteException("Funcionario nao existe!");
        }

        Funcionario funcionarioConsultadoBD = funcionarioBD.get();
        FuncionarioResponseDto funcionarioConsultadoDto = mapper.map(funcionarioConsultadoBD, FuncionarioResponseDto.class);
        return funcionarioConsultadoDto;
    }

    public List<FuncionarioResponseDto> listar() throws ListaFuncionarioVaziaException {
        Optional<List<Funcionario>> listaFuncionarioBD = funcionarioRepository.findByAtivo(true);
        if (!listaFuncionarioBD.isPresent() && listaFuncionarioBD.get().isEmpty()) {
            throw new ListaFuncionarioVaziaException("Lista de funcionarios vazia!");
        }

        List<FuncionarioResponseDto> listaFuncionarioDto = listaFuncionarioBD.get().stream().map(funcionarioBD -> mapper.map(funcionarioBD, FuncionarioResponseDto.class)).collect(Collectors.toList());
        return listaFuncionarioDto;
    }
}
