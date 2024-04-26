package edu.soulcode.elas_help.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TecnicoDTO {

    private Long id;

    @NotNull
    @Size(max = 80)
    private String nome;

    @NotNull
    @Size(max = 80)
    @TecnicoEmailUnique
    private String email;

    @NotNull
    @Size(max = 80)
    private String senha;

    @NotNull
    private Setor setor;

}
