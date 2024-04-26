package edu.soulcode.elas_help.repos;

import edu.soulcode.elas_help.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmailIgnoreCase(String email);

}
