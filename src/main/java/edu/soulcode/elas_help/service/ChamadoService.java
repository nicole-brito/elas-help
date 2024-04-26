package edu.soulcode.elas_help.service;

import edu.soulcode.elas_help.domain.Chamado;
import edu.soulcode.elas_help.domain.Cliente;
import edu.soulcode.elas_help.domain.Tecnico;
import edu.soulcode.elas_help.model.ChamadoDTO;
import edu.soulcode.elas_help.repos.ChamadoRepository;
import edu.soulcode.elas_help.repos.ClienteRepository;
import edu.soulcode.elas_help.repos.TecnicoRepository;
import edu.soulcode.elas_help.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final ClienteRepository clienteRepository;
    private final TecnicoRepository tecnicoRepository;

    public ChamadoService(final ChamadoRepository chamadoRepository,
            final ClienteRepository clienteRepository, final TecnicoRepository tecnicoRepository) {
        this.chamadoRepository = chamadoRepository;
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    public List<ChamadoDTO> findAll() {
        final List<Chamado> chamadoes = chamadoRepository.findAll(Sort.by("id"));
        return chamadoes.stream()
                .map(chamado -> mapToDTO(chamado, new ChamadoDTO()))
                .toList();
    }

    public ChamadoDTO get(final Long id) {
        return chamadoRepository.findById(id)
                .map(chamado -> mapToDTO(chamado, new ChamadoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ChamadoDTO chamadoDTO) {
        final Chamado chamado = new Chamado();
        mapToEntity(chamadoDTO, chamado);
        return chamadoRepository.save(chamado).getId();
    }

    public void update(final Long id, final ChamadoDTO chamadoDTO) {
        final Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(chamadoDTO, chamado);
        chamadoRepository.save(chamado);
    }

    public void delete(final Long id) {
        chamadoRepository.deleteById(id);
    }

    private ChamadoDTO mapToDTO(final Chamado chamado, final ChamadoDTO chamadoDTO) {
        chamadoDTO.setId(chamado.getId());
        chamadoDTO.setDescricao(chamado.getDescricao());
        chamadoDTO.setAberto(chamado.getAberto());
        chamadoDTO.setPrioridade(chamado.getPrioridade());
        chamadoDTO.setSetor(chamado.getSetor());
        chamadoDTO.setCliente(chamado.getCliente() == null ? null : chamado.getCliente().getId());
        chamadoDTO.setTecnico(chamado.getTecnico() == null ? null : chamado.getTecnico().getId());
        return chamadoDTO;
    }

    private Chamado mapToEntity(final ChamadoDTO chamadoDTO, final Chamado chamado) {
        chamado.setDescricao(chamadoDTO.getDescricao());
        chamado.setAberto(chamadoDTO.getAberto());
        chamado.setPrioridade(chamadoDTO.getPrioridade());
        chamado.setSetor(chamadoDTO.getSetor());
        final Cliente cliente = chamadoDTO.getCliente() == null ? null : clienteRepository.findById(chamadoDTO.getCliente())
                .orElseThrow(() -> new NotFoundException("cliente not found"));
        chamado.setCliente(cliente);
        final Tecnico tecnico = chamadoDTO.getTecnico() == null ? null : tecnicoRepository.findById(chamadoDTO.getTecnico())
                .orElseThrow(() -> new NotFoundException("tecnico not found"));
        chamado.setTecnico(tecnico);
        return chamado;
    }

}
