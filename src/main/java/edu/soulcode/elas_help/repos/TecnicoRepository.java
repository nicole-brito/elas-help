package edu.soulcode.elas_help.repos;

import edu.soulcode.elas_help.domain.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

    boolean existsByEmailIgnoreCase(String email);

}
