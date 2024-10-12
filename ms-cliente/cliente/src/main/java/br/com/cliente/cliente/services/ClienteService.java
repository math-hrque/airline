package br.com.cliente.cliente.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cliente.cliente.dtos.ClienteRequestDto;
import br.com.cliente.cliente.dtos.UsuarioRequestDto;
import br.com.cliente.cliente.exeptions.ClienteNaoExisteException;
import br.com.cliente.cliente.exeptions.OutroClienteDadosJaExistenteException;
import br.com.cliente.cliente.models.Cliente;
import br.com.cliente.cliente.repositories.ClienteRepository;
import br.com.cliente.cliente.repositories.MilhasRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MilhasRepository milhasRepository;

    public UsuarioRequestDto cadastrar(ClienteRequestDto clienteRequestDto) throws OutroClienteDadosJaExistenteException {
        Optional<List<Cliente>> existClienteBD = clienteRepository.findByCpfOrEmail(clienteRequestDto.getCpf(), clienteRequestDto.getEmail());
        if (existClienteBD.isPresent() && !existClienteBD.get().isEmpty()) {
            boolean cpfExists = false;
            boolean emailExists = false;
            for (Cliente cliente : existClienteBD.get()) {
                if (cliente.getCpf().equals(clienteRequestDto.getCpf())) {
                    cpfExists = true;
                }
                if (cliente.getEmail().equals(clienteRequestDto.getEmail())) {
                    emailExists = true;
                } 
            }
            if (cpfExists && emailExists) {
                throw new OutroClienteDadosJaExistenteException("Outro cliente com cpf e email ja existente!");
            } else if (cpfExists) {
                throw new OutroClienteDadosJaExistenteException("Outro cliente com cpf ja existente!");
            } else if (emailExists) {
                throw new OutroClienteDadosJaExistenteException("Outro cliente com email ja existente!");
            }
        }

        Cliente cliente = mapper.map(clienteRequestDto, Cliente.class);
        Cliente clienteCriadoBD = clienteRepository.save(cliente);
        UsuarioRequestDto usuarioRequestDto = mapper.map(clienteCriadoBD, UsuarioRequestDto.class);
        return usuarioRequestDto;
    }

    public ClienteRequestDto remover(String email) throws ClienteNaoExisteException {
        Optional<Cliente> clienteBD = clienteRepository.findByEmail(email);
        if (!clienteBD.isPresent()) {
            throw new ClienteNaoExisteException("Cliente nao existe!");
        }

        Cliente cliente = clienteBD.get();
        clienteRepository.deleteById(cliente.getIdCliente());
        ClienteRequestDto clienteRemovidoDto = mapper.map(cliente, ClienteRequestDto.class);
        return clienteRemovidoDto;
    }
}
