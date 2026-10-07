package com.marcos.msrelatorio.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marcos.msrelatorio.infrastructure.enums.TipoTopicoEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "relatorios")
@Data
@Builder
@JsonFormat
public class RelatorioEntity {
    @Id
    private String id;
    private Long criancaId;
    private Long terapeutaId;
    private TipoTopicoEnum tipoTopicoEnum;
    private String conteudo;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private LocalDateTime dataCriacao;
}
