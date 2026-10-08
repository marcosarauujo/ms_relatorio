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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Relatorios de IA", description = "Endpoints para gerar os topicos do documento clinico usando ChatGPT")
@SecurityRequirement(name = "BearerAuth")
public class RelatorioController {

    private final QueixaPrincipalService queixaPrincipalService;
    private final AnamneseService anamneseService;
    private final DesafiosService desafiosService;
    private final ConclusaoService conclusaoService;
    private final RelatorioRepository relatorioRepository;

    @PostMapping("/queixa-principal")
    @Operation(
            summary = "Gerar Queixa Principal",
            description = "Busca todas as transcricoes do paciente, envia para o ChatGPT " +
                    "e gera o topico de Queixa Principal do relatorio clinico."
    )
    @ApiResponse(responseCode = "201", description = "Topico de Queixa Principal gerado e salvo com sucesso.")
    @ApiResponse(responseCode = "401", description = "Token JWT invalido ou ausente.")
    @ApiResponse(responseCode = "404", description = "Nenhuma transcricao encontrada para esta crianca. Faca o upload de audio antes.")
    @ApiResponse(responseCode = "500", description = "Erro ao se comunicar com a API do ChatGPT. Verifique a chave da OpenAI.")
    public ResponseEntity<String> queixaPrincipal(
            @RequestParam("criancaId") Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token)
            throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                queixaPrincipalService.geraQueixaPrincipal(criancaId, token)
        );
    }

    @PostMapping("/anamnese")
    @Operation(
            summary = "Gerar Historico Clinico e Anamnese",
            description = "Busca todas as transcricoes do paciente, envia para o ChatGPT " +
                    "e gera o topico de Anamnese detalhada do relatorio clinico."
    )
    @ApiResponse(responseCode = "201", description = "Topico de Anamnese gerado e salvo com sucesso.")
    @ApiResponse(responseCode = "401", description = "Token JWT invalido ou ausente.")
    @ApiResponse(responseCode = "404", description = "Nenhuma transcricao encontrada para esta crianca. Faca o upload de audio antes.")
    @ApiResponse(responseCode = "500", description = "Erro ao se comunicar com a API do ChatGPT. Verifique a chave da OpenAI.")
    public ResponseEntity<String> gerarAnamnese(
            @RequestParam("criancaId") Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token)
            throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                anamneseService.gerarAnamnese(criancaId, token)
        );
    }

    @PostMapping("/desafios")
    @Operation(
            summary = "Gerar Desafios e Participacoes",
            description = "Busca todas as transcricoes do paciente, envia para o ChatGPT " +
                    "e gera o topico de Desafios e Perfil Sensorial/Motor do relatorio clinico."
    )
    @ApiResponse(responseCode = "201", description = "Topico de Desafios gerado e salvo com sucesso.")
    @ApiResponse(responseCode = "401", description = "Token JWT invalido ou ausente.")
    @ApiResponse(responseCode = "404", description = "Nenhuma transcricao encontrada para esta crianca. Faca o upload de audio antes.")
    @ApiResponse(responseCode = "500", description = "Erro ao se comunicar com a API do ChatGPT. Verifique a chave da OpenAI.")

    public ResponseEntity<String> gerarDesafios(
            @RequestParam("criancaId") Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token)
            throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                desafiosService.gerarDesafios(criancaId, token)
        );
    }

    @PostMapping("/conclusao")
    @Operation(
            summary = "Gerar Conclusao e Objetivos Terapeuticos",
            description = "Busca todas as transcricoes do paciente, envia para o ChatGPT " +
                    "e gera a sintese clinica com os objetivos terapeuticos."
    )
    @ApiResponse(responseCode = "201", description = "Topico de Conclusao gerado e salvo com sucesso.")
    @ApiResponse(responseCode = "401", description = "Token JWT invalido ou ausente.")
    @ApiResponse(responseCode = "404", description = "Nenhuma transcricao encontrada para esta crianca. Faca o upload de audio antes.")
    @ApiResponse(responseCode = "500", description = "Erro ao se comunicar com a API do ChatGPT. Verifique a chave da OpenAI.")

    public ResponseEntity<String> gerarConclusao(
            @RequestParam("criancaId") Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token)
            throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                conclusaoService.gerarConclusao(criancaId, token)
        );
    }

    @GetMapping("/relatorios/crianca/{criancaId}")
    @Operation(
            summary = "Listar relatorios por crianca",
            description = "Retorna todos os topicos de relatorio ja gerados para um determinado paciente, " +
                    "incluindo tipo do topico, conteudo e data de criacao."
    )
    @ApiResponse(responseCode = "200", description = "Lista de relatorios retornada com sucesso.")
    @ApiResponse(responseCode = "401", description = "Token JWT invalido ou ausente.")
    @ApiResponse(responseCode = "404", description = "Nenhum relatorio encontrado para esta crianca.")

    public ResponseEntity<List<RelatorioEntity>> listarPorCrianca(
            @PathVariable Long criancaId,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token) {

        return ResponseEntity.ok(relatorioRepository.findByCriancaId(criancaId));
    }

    @GetMapping("/relatorio/{id}")
    @Operation(
            summary = "Buscar relatorio por ID",
            description = "Retorna um topico especifico de relatorio pelo seu ID do MongoDB (string hexadecimal)."
    )
    @ApiResponse(responseCode = "200", description = "Relatorio encontrado com sucesso.")
    @ApiResponse(responseCode = "401", description = "Token JWT invalido ou ausente.")
    @ApiResponse(responseCode = "404", description = "Nenhum relatorio encontrado para o ID informado.")
    public ResponseEntity<RelatorioEntity> buscarPorId(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false) String token) {

        return ResponseEntity.ok(relatorioRepository.findById(id)
                .orElseThrow(() -> new RelatorioNotFoundException("Relatorio nao encontrado para o ID: " + id))
        );
    }
}
