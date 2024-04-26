package edu.soulcode.elas_help.service;

import edu.soulcode.elas_help.domain.Chamado;
import edu.soulcode.elas_help.domain.Cliente;
import edu.soulcode.elas_help.model.ClienteDTO;
import edu.soulcode.elas_help.repos.ChamadoRepository;
import edu.soulcode.elas_help.repos.ClienteRepository;
import edu.soulcode.elas_help.util.NotFoundException;
import edu.soulcode.elas_help.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ChamadoRepository chamadoRepository;

    public ClienteService(final ClienteRepository clienteRepository,
            final ChamadoRepository chamadoRepository) {
        this.clienteRepository = clienteRepository;
        this.chamadoRepository = chamadoRepository;
    }

    public List<ClienteDTO> findAll() {
        final List<Cliente> clientes = clienteRepository.findAll(Sort.by("id"));
        return clientes.stream()
                .map(cliente -> mapToDTO(cliente, new ClienteDTO()))
                .toList();
    }

    public ClienteDTO get(final Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> mapToDTO(cliente, new ClienteDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ClienteDTO clienteDTO) {
        final Cliente cliente = new Cliente();
        mapToEntity(clienteDTO, cliente);
        return clienteRepository.save(cliente).getId();
    }

    public void update(final Long id, final ClienteDTO clienteDTO) {
        final Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(clienteDTO, cliente);
        clienteRepository.save(cliente);
    }

    public void delete(final Long id) {
        clienteRepository.deleteById(id);
    }

    private ClienteDTO mapToDTO(final Cliente cliente, final ClienteDTO clienteDTO) {
        clienteDTO.setId(cliente.getId());
        clienteDTO.setNome(cliente.getNome());
        clienteDTO.setEmail(cliente.getEmail());
        clienteDTO.setSenha(cliente.getSenha());
        clienteDTO.setSobrenome(cliente.getSobrenome());
        return clienteDTO;
    }

    private Cliente mapToEntity(final ClienteDTO clienteDTO, final Cliente cliente) {
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setSenha(clienteDTO.getSenha());
        cliente.setSobrenome(clienteDTO.getSobrenome());
        return cliente;
    }

    public boolean emailExists(final String email) {
        return clienteRepository.existsByEmailIgnoreCase(email);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Chamado clienteChamado = chamadoRepository.findFirstByCliente(cliente);
        if (clienteChamado != null) {
            referencedWarning.setKey("cliente.chamado.cliente.referenced");
            referencedWarning.addParam(clienteChamado.getId());
            return referencedWarning;
        }
        return null;
    }

}
