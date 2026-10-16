package com.marcos.msrelatorio.business.dto;

import com.marcos.msrelatorio.infrastructure.enums.TipoTopicoEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RelatorioResponseDTO {
    private String id;
    private String nomeCrianca;
    private String nomeTerapeuta;
    private TipoTopicoEnum tipoTopicoEnum;
    private String conteudo;
    private LocalDateTime dataCriacao;
}