package com.marcos.msrelatorio.infrastructure.client;


import com.marcos.msrelatorio.business.dto.TranscricaoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "anamnese-client", url = "${anamnese.url}")
public interface AnamneseClient {

    @GetMapping("/transcricao/crianca/{criancaId}")
    List<TranscricaoResponseDTO> listarPorCrianca(@PathVariable Long criancaId,
                                                  @RequestHeader(name = "Authorization", required = false) String token);

}
