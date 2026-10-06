package com.marcos.msrelatorio.infrastructure.client;


import com.marcos.msrelatorio.business.dto.CriancaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "core-client", url = "${core.url}")
public interface CoreClient {

    @GetMapping("/crianca/{id}")
    CriancaResponseDTO buscarCriancaPorId(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization", required = false) String token);

}
