package com.marcos.msrelatorio.business.dto;

import lombok.Data;

@Data
public class TranscricaoResponseDTO {

    private String id;
    private Long criancaId;
    private Long terapeutaId;
    private String textoBruto;

}
