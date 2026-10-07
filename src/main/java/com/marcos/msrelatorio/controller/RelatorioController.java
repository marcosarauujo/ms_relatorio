package com.marcos.msrelatorio.controller;

import com.marcos.msrelatorio.business.AnamneseService;
import com.marcos.msrelatorio.business.ConclusaoService;
import com.marcos.msrelatorio.business.DesafiosService;
import com.marcos.msrelatorio.business.QueixaPrincipalService;
import com.marcos.msrelatorio.infrastructure.entity.RelatorioEntity;
import com.marcos.msrelatorio.infrastructure.exceptions.RelatorioNotFoundException;
import com.marcos.msrelatorio.infrastructure.repository.RelatorioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ia")

@Tag(name = "Relatórios de IA", description = "Endpoints para gerar tópicos do documento clínico")
@SecurityRequirement(name = "BearerAuth")

public class RelatorioController {

    private final QueixaPrincipalService queixaPrincipalService;
    private final AnamneseService anamneseService;
    private final DesafiosService desafiosService;
    private final ConclusaoService conclusaoService;
    private final RelatorioRepository relatorioRepository;


    @PostMapping("/queixa-principal")

    @Operation(summary = "Gerar Queixa Principal",
            description = "Consome a transcrição do MS2 e gera a queixa principal via ChatGPT")

    public ResponseEntity<String> queixaPrincipal(
            @RequestParam("criancaId") Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token)
            throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                queixaPrincipalService.geraQueixaPrincipal(criancaId, token)
        );
    }

    @PostMapping("/anamnese")

    @Operation(summary = "Gerar Histórico Clínico e Anamnese",
            description = "Consome a transcrição do MS2 e gera a anamnese detalhada via ChatGPT")

    public ResponseEntity<String> gerarAnamnese(
            @RequestParam("criancaId") Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                anamneseService.gerarAnamnese(criancaId, token)
        );
    }

    @PostMapping("/desafios")

    @Operation(summary = "Gerar Desafios e Participações",
            description = "Gera o perfil sensorial e motor da criança")

    public ResponseEntity<String> gerarDesafios(
            @RequestParam("criancaId") Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body
                (desafiosService.gerarDesafios(criancaId, token)
                );
    }

    @PostMapping("/conclusao")

    @Operation(summary = "Gerar Conclusão e Objetivos",
            description = "Gera a síntese clínica e os objetivos terapêuticos baseados no áudio")

    public ResponseEntity<String> gerarConclusao(
            @RequestParam("criancaId") Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body
                (conclusaoService.gerarConclusao(criancaId, token)
                );
    }

    @GetMapping("/relatorios/crianca/{criancaId}")

    @Operation(summary = "Listar relatórios por criança",
            description = "Retorna todos os tópicos já gerados de uma criança")

    public ResponseEntity<List<RelatorioEntity>> listarPorCrianca(
            @PathVariable Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token) {

        return ResponseEntity.ok(relatorioRepository.findByCriancaId(criancaId)
        );
    }

    @GetMapping("/relatorio/{id}")

    @Operation(summary = "Buscar relatório por ID",
            description = "Retorna um tópico específico pelo seu ID")

    public ResponseEntity<RelatorioEntity> buscarPorId(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token) {

        return ResponseEntity.ok(relatorioRepository.findById(id)
                .orElseThrow(() -> new RelatorioNotFoundException("Relatório não encontrado para o ID: " + id))
        );
    }


}
