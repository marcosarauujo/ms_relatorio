package com.marcos.msrelatorio.infrastructure.entity;

import com.marcos.msrelatorio.infrastructure.enums.TipoTopicoEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collation = "relatorios")
@Data
@Builder
public class RelatorioEntity {
    @Id
    private String id;
    private Long criancaId;
    private Long terapeutaId;
    private TipoTopicoEnum tipoTopicoEnum;
    private String conteudo;
    private LocalDateTime dataCriacao;
}
