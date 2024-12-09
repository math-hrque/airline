package br.com.cliente.cliente.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cliente.cliente.dtos.ClienteDto;
import br.com.cliente.cliente.dtos.ClienteMilhasDto;
import br.com.cliente.cliente.dtos.ReservaManterDto;
import br.com.cliente.cliente.dtos.SaldoMilhasDto;
import br.com.cliente.cliente.dtos.UsuarioRequestCadastrarDto;
import br.com.cliente.cliente.exeptions.ClienteNaoExisteException;
import br.com.cliente.cliente.exeptions.OutroClienteDadosJaExistenteException;
import br.com.cliente.cliente.exeptions.SemSaldoMilhasSuficientesClienteException;
import br.com.cliente.cliente.models.Cliente;
import br.com.cliente.cliente.repositories.ClienteRepository;
import br.com.cliente.cliente.utils.RedisClienteCache;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RedisClienteCache redisClienteCache;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MilhasService milhasService;

    public UsuarioRequestCadastrarDto cadastrarCliente(ClienteDto clienteDto) throws OutroClienteDadosJaExistenteException {
        Optional<List<Cliente>> existClienteBD = clienteRepository.findByCpfOrEmail(clienteDto.getCpf(), clienteDto.getEmail());
        if (existClienteBD.isPresent() && !existClienteBD.get().isEmpty()) {
            boolean cpfExists = false;
            boolean emailExists = false;
            for (Cliente cliente : existClienteBD.get()) {
                if (cliente.getCpf().equals(clienteDto.getCpf())) {
                    cpfExists = true;
                }
                if (cliente.getEmail().equals(clienteDto.getEmail())) {
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

        Cliente cliente = mapper.map(clienteDto, Cliente.class);
        cliente.setIdCliente(0L);
        Cliente clienteCriadoBD = clienteRepository.save(cliente);
        UsuarioRequestCadastrarDto usuarioRequestCadastrarDto = mapper.map(clienteCriadoBD, UsuarioRequestCadastrarDto.class);
        return usuarioRequestCadastrarDto;
    }

    public ClienteDto removerCliente(String email) throws ClienteNaoExisteException {
        Optional<Cliente> clienteBD = clienteRepository.findByEmail(email);
        if (!clienteBD.isPresent()) {
            throw new ClienteNaoExisteException("Cliente nao existe!");
        }

        Cliente cliente = clienteBD.get();
        clienteRepository.deleteById(cliente.getIdCliente());
        ClienteDto clienteRemovidoDto = mapper.map(cliente, ClienteDto.class);
        return clienteRemovidoDto;
    }

    public ReservaManterDto milhasReservaCadastrar(ReservaManterDto reservaManterDto) throws ClienteNaoExisteException, SemSaldoMilhasSuficientesClienteException {
        if (reservaManterDto.getMilhasUtilizadas() != null && reservaManterDto.getMilhasUtilizadas() > 0) {
            Optional<Cliente> clienteBD = clienteRepository.findById(reservaManterDto.getIdCliente());
            if (!clienteBD.isPresent()) {
                throw new ClienteNaoExisteException("Cliente nao existe!");
            }
            if (clienteBD.get().getSaldoMilhas() < reservaManterDto.getMilhasUtilizadas()) {
                throw new SemSaldoMilhasSuficientesClienteException("Sem saldo de milhas suficientes!");
            }
    
            Cliente clienteCache = redisClienteCache.getCache(clienteBD.get().getIdCliente());
            if (clienteCache == null) {
                redisClienteCache.saveCache(clienteBD.get());
            }
    
            clienteBD.get().setSaldoMilhas(clienteBD.get().getSaldoMilhas() - reservaManterDto.getMilhasUtilizadas());
            clienteRepository.save(clienteBD.get());
            milhasService.milhasReservaCadastrar(reservaManterDto, clienteBD.get());
        }
        return reservaManterDto;
    }

    public ReservaManterDto reverterMilhasReservaCadastrada(ReservaManterDto reservaManterDto) throws ClienteNaoExisteException {
        if (reservaManterDto.getMilhasUtilizadas() != null && reservaManterDto.getMilhasUtilizadas() > 0) {
            Optional<Cliente> clienteBD = clienteRepository.findById(reservaManterDto.getIdCliente());
            if (!clienteBD.isPresent()) {
                throw new ClienteNaoExisteException("Cliente nao existe!");
            }
    
            Cliente clienteCache = redisClienteCache.getCache(clienteBD.get().getIdCliente());
            if (clienteCache != null) {
                clienteRepository.save(clienteCache);
                redisClienteCache.removeCache(clienteCache.getIdCliente());
                milhasService.milhasReservaRemover(reservaManterDto);
            }
        }
        return reservaManterDto;
    }

    public ReservaManterDto milhasReservaCancelar(ReservaManterDto reservaManterDto) throws ClienteNaoExisteException {
        if (reservaManterDto.getMilhasUtilizadas() != null && reservaManterDto.getMilhasUtilizadas() > 0) {
            Optional<Cliente> clienteBD = clienteRepository.findById(reservaManterDto.getIdCliente());
            if (!clienteBD.isPresent()) {
                throw new ClienteNaoExisteException("Cliente nao existe!");
            }
    
            Cliente clienteCache = redisClienteCache.getCache(clienteBD.get().getIdCliente());
            if (clienteCache == null) {
                redisClienteCache.saveCache(clienteBD.get());
            }
    
            clienteBD.get().setSaldoMilhas(clienteBD.get().getSaldoMilhas() + reservaManterDto.getMilhasUtilizadas());
            clienteRepository.save(clienteBD.get());
            milhasService.milhasReservaCancelar(reservaManterDto, clienteBD.get());
        }
        return reservaManterDto;
    }

    public ReservaManterDto reverterMilhasReservaCancelada(ReservaManterDto reservaManterDto) throws ClienteNaoExisteException {
        if (reservaManterDto.getMilhasUtilizadas() != null && reservaManterDto.getMilhasUtilizadas() > 0) {
            Optional<Cliente> clienteBD = clienteRepository.findById(reservaManterDto.getIdCliente());
            if (!clienteBD.isPresent()) {
                throw new ClienteNaoExisteException("Cliente nao existe!");
            }
    
            Cliente clienteCache = redisClienteCache.getCache(clienteBD.get().getIdCliente());
            if (clienteCache != null) {
                clienteRepository.save(clienteCache);
                redisClienteCache.removeCache(clienteCache.getIdCliente());
                milhasService.milhasReservaCadastrar(reservaManterDto, clienteBD.get());
            }
        }
        return reservaManterDto;
    }

    public void milhasReservasCancelarVoo(List<ReservaManterDto> listaReservaManterDto) throws ClienteNaoExisteException {
        for (ReservaManterDto reservaManterDto : listaReservaManterDto) {
            if (reservaManterDto.getMilhasUtilizadas() != null && reservaManterDto.getMilhasUtilizadas() > 0) {
                Optional<Cliente> cliente = clienteRepository.findById(reservaManterDto.getIdCliente());
                if (cliente.isPresent()) {
                    Cliente clienteCache = redisClienteCache.getCache(cliente.get().getIdCliente());
                    if (clienteCache == null) {
                        redisClienteCache.saveCache(cliente.get());
                    }
                    cliente.get().setSaldoMilhas(cliente.get().getSaldoMilhas() + reservaManterDto.getMilhasUtilizadas());
                    clienteRepository.save(cliente.get());
                    milhasService.milhasReservaCancelarVoo(reservaManterDto, cliente.get());
                }
            }
        }
    }

    public void reverterMilhasReservasCanceladasVoo(List<ReservaManterDto> listaReservaManterDto) throws ClienteNaoExisteException {
        for (ReservaManterDto reservaManterDto : listaReservaManterDto) {
            if (reservaManterDto.getMilhasUtilizadas() != null && reservaManterDto.getMilhasUtilizadas() > 0) {
                Optional<Cliente> cliente = clienteRepository.findById(reservaManterDto.getIdCliente());
                if (cliente.isPresent()) {
                    Cliente clienteCache = redisClienteCache.getCache(cliente.get().getIdCliente());
                    if (clienteCache != null) {
                        clienteRepository.save(clienteCache);
                        redisClienteCache.removeCache(clienteCache.getIdCliente());
                        milhasService.milhasReservaCadastrar(reservaManterDto, cliente.get());
                    }
                }
            }
        }
    }

    public SaldoMilhasDto comprarMilhas(int quantidadeMilhas, Long idCliente) throws ClienteNaoExisteException{
        Optional<Cliente> clienteBD = clienteRepository.findById(idCliente);
        if (!clienteBD.isPresent()) {
            throw new ClienteNaoExisteException("Cliente nao existe!");
        }

        Cliente clienteConsultadoBD = clienteBD.get();
        clienteConsultadoBD.setSaldoMilhas(clienteBD.get().getSaldoMilhas() + quantidadeMilhas);
        clienteRepository.save(clienteBD.get());  
        milhasService.comprarMilhas(quantidadeMilhas, clienteConsultadoBD);
        SaldoMilhasDto novoSaldo = new SaldoMilhasDto();
        novoSaldo.setIdCliente(idCliente);
        novoSaldo.setSaldoMilhas(clienteConsultadoBD.getSaldoMilhas());
        return novoSaldo;
    }

    public ClienteMilhasDto consultarExtratoMilhas(Long idCliente) throws ClienteNaoExisteException {
        Optional<Cliente> clienteBD = clienteRepository.findById(idCliente);
        if (!clienteBD.isPresent()) {
            throw new ClienteNaoExisteException("Cliente nao existe!");
        }

        Cliente clienteConsultadoBD = clienteBD.get();
        ClienteMilhasDto clienteMilhasConsultadoDto = mapper.map(clienteConsultadoBD, ClienteMilhasDto.class);
        clienteMilhasConsultadoDto.setListaMilhas(milhasService.consultarExtratoMilhas(clienteConsultadoBD));
        return clienteMilhasConsultadoDto;
    }

    public ClienteDto consultarEmail(String email) throws ClienteNaoExisteException {
        Optional<Cliente> clienteBD = clienteRepository.findByEmail(email);
        if (!clienteBD.isPresent()) {
            throw new ClienteNaoExisteException("Cliente ativo nao existe!");
        }

        Cliente clienteConsultadoBD = clienteBD.get();
        ClienteDto clienteConsultadoDto = mapper.map(clienteConsultadoBD, ClienteDto.class);
        return clienteConsultadoDto;
    }

    public ClienteDto consultarIdCliente(Long idCliente) throws ClienteNaoExisteException {
        Optional<Cliente> clienteBD = clienteRepository.findById(idCliente);
        if (!clienteBD.isPresent()) {
            throw new ClienteNaoExisteException("Cliente ativo nao existe!");
        }

        Cliente clienteConsultadoBD = clienteBD.get();
        ClienteDto clienteConsultadoDto = mapper.map(clienteConsultadoBD, ClienteDto.class);
        return clienteConsultadoDto;
    }
}
