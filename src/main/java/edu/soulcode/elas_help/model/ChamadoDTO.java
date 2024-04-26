package edu.soulcode.elas_help.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChamadoDTO {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String descricao;

    @NotNull
    private Boolean aberto;

    @NotNull
    private Integer prioridade;

    @NotNull
    private List<@Size(max = 255) String> setor;

    @NotNull
    private Long cliente;

    private Long tecnico;

}
