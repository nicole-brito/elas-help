package edu.soulcode.elas_help.rest;

import edu.soulcode.elas_help.model.ChamadoDTO;
import edu.soulcode.elas_help.service.ChamadoService;
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
@RequestMapping(value = "/api/chamados", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChamadoResource {

    private final ChamadoService chamadoService;

    public ChamadoResource(final ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @GetMapping
    public ResponseEntity<List<ChamadoDTO>> getAllChamados() {
        return ResponseEntity.ok(chamadoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoDTO> getChamado(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(chamadoService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createChamado(@RequestBody @Valid final ChamadoDTO chamadoDTO) {
        final Long createdId = chamadoService.create(chamadoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateChamado(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ChamadoDTO chamadoDTO) {
        chamadoService.update(id, chamadoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChamado(@PathVariable(name = "id") final Long id) {
        chamadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
