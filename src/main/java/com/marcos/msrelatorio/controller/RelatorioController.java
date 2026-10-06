package com.marcos.msrelatorio.controller;

import com.marcos.msrelatorio.business.AnamneseService;
import com.marcos.msrelatorio.business.QueixaPrincipalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ia")

@Tag(name = "Relatórios de IA", description = "Endpoints para gerar tópicos do documento clínico")
@SecurityRequirement(name = "BearerAuth")

public class RelatorioController {

    private final QueixaPrincipalService queixaPrincipalService;
    private final AnamneseService anamneseService;

    @PostMapping("/queixa-principal")

    @Operation(summary = "Gerar Queixa Principal",
            description = "Consome a transcrição do MS2 e gera a queixa principal via ChatGPT")

    public ResponseEntity<String> queixaPrincipal(
            @RequestParam("criancaId") Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token)
            throws Exception {

        return ResponseEntity.ok(queixaPrincipalService.geraQueixaPrincipal(criancaId, token));
    }

    @PostMapping("/anamnese")

    @Operation(summary = "Gerar Histórico Clínico e Anamnese",
            description = "Consome a transcrição do MS2 e gera a anamnese detalhada via ChatGPT")

    public ResponseEntity<String> gerarAnamnese(
            @RequestParam("criancaId") Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token)
            throws Exception {
        return ResponseEntity.ok(anamneseService.gerarAnamnese(criancaId, token));
    }
}
