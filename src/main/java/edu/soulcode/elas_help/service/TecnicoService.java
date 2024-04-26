package edu.soulcode.elas_help.service;

import edu.soulcode.elas_help.domain.Chamado;
import edu.soulcode.elas_help.domain.Tecnico;
import edu.soulcode.elas_help.model.TecnicoDTO;
import edu.soulcode.elas_help.repos.ChamadoRepository;
import edu.soulcode.elas_help.repos.TecnicoRepository;
import edu.soulcode.elas_help.util.NotFoundException;
import edu.soulcode.elas_help.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final ChamadoRepository chamadoRepository;

    public TecnicoService(final TecnicoRepository tecnicoRepository,
            final ChamadoRepository chamadoRepository) {
        this.tecnicoRepository = tecnicoRepository;
        this.chamadoRepository = chamadoRepository;
    }

    public List<TecnicoDTO> findAll() {
        final List<Tecnico> tecnicoes = tecnicoRepository.findAll(Sort.by("id"));
        return tecnicoes.stream()
                .map(tecnico -> mapToDTO(tecnico, new TecnicoDTO()))
                .toList();
    }

    public TecnicoDTO get(final Long id) {
        return tecnicoRepository.findById(id)
                .map(tecnico -> mapToDTO(tecnico, new TecnicoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TecnicoDTO tecnicoDTO) {
        final Tecnico tecnico = new Tecnico();
        mapToEntity(tecnicoDTO, tecnico);
        return tecnicoRepository.save(tecnico).getId();
    }

    public void update(final Long id, final TecnicoDTO tecnicoDTO) {
        final Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tecnicoDTO, tecnico);
        tecnicoRepository.save(tecnico);
    }

    public void delete(final Long id) {
        tecnicoRepository.deleteById(id);
    }

    private TecnicoDTO mapToDTO(final Tecnico tecnico, final TecnicoDTO tecnicoDTO) {
        tecnicoDTO.setId(tecnico.getId());
        tecnicoDTO.setNome(tecnico.getNome());
        tecnicoDTO.setEmail(tecnico.getEmail());
        tecnicoDTO.setSenha(tecnico.getSenha());
        tecnicoDTO.setSetor(tecnico.getSetor());
        return tecnicoDTO;
    }

    private Tecnico mapToEntity(final TecnicoDTO tecnicoDTO, final Tecnico tecnico) {
        tecnico.setNome(tecnicoDTO.getNome());
        tecnico.setEmail(tecnicoDTO.getEmail());
        tecnico.setSenha(tecnicoDTO.getSenha());
        tecnico.setSetor(tecnicoDTO.getSetor());
        return tecnico;
    }

    public boolean emailExists(final String email) {
        return tecnicoRepository.existsByEmailIgnoreCase(email);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Chamado tecnicoChamado = chamadoRepository.findFirstByTecnico(tecnico);
        if (tecnicoChamado != null) {
            referencedWarning.setKey("tecnico.chamado.tecnico.referenced");
            referencedWarning.addParam(tecnicoChamado.getId());
            return referencedWarning;
        }
        return null;
    }

}
