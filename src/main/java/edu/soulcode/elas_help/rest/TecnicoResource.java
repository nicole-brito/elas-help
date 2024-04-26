package edu.soulcode.elas_help.rest;

import edu.soulcode.elas_help.model.TecnicoDTO;
import edu.soulcode.elas_help.service.TecnicoService;
import edu.soulcode.elas_help.util.ReferencedException;
import edu.soulcode.elas_help.util.ReferencedWarning;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/tecnicos", produces = MediaType.APPLICATION_JSON_VALUE)
public class TecnicoResource {

    private final TecnicoService tecnicoService;

    public TecnicoResource(final TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @GetMapping
    public ResponseEntity<List<TecnicoDTO>> getAllTecnicos() {
        return ResponseEntity.ok(tecnicoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoDTO> getTecnico(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(tecnicoService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createTecnico(@RequestBody @Valid final TecnicoDTO tecnicoDTO) {
        final Long createdId = tecnicoService.create(tecnicoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTecnico(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TecnicoDTO tecnicoDTO) {
        tecnicoService.update(id, tecnicoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTecnico(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = tecnicoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        tecnicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
