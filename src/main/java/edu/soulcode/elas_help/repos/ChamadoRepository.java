package edu.soulcode.elas_help.repos;

import edu.soulcode.elas_help.domain.Chamado;
import edu.soulcode.elas_help.domain.Cliente;
import edu.soulcode.elas_help.domain.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    Chamado findFirstByCliente(Cliente cliente);

    Chamado findFirstByTecnico(Tecnico tecnico);

}
