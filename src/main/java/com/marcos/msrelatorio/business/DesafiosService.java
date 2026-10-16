package com.marcos.msrelatorio.business;

import com.marcos.msrelatorio.business.dto.CriancaResponseDTO;
import com.marcos.msrelatorio.business.dto.TerapeutaResponseDTO;
import com.marcos.msrelatorio.business.dto.TranscricaoResponseDTO;
import com.marcos.msrelatorio.infrastructure.client.AnamneseClient;
import com.marcos.msrelatorio.infrastructure.client.CoreClient;
import com.marcos.msrelatorio.infrastructure.entity.RelatorioEntity;
import com.marcos.msrelatorio.infrastructure.enums.TipoTopicoEnum;
import com.marcos.msrelatorio.infrastructure.exceptions.TranscricaoNotFoundException;
import com.marcos.msrelatorio.infrastructure.repository.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesafiosService {

    private final CoreClient coreClient;
    private final AnamneseClient anamneseClient;
    private final ChatModel chatModel;
    private final RelatorioRepository relatorioRepository;

    @Value("classpath:prompts/TO_larissa.md")
    private Resource arquivoPersonaLarissa;

    @Value("classpath:prompts/prompt-desafios.md")
    private Resource arquivoRoteiroDesafios;

    public String gerarDesafios(Long criancaId, String token) throws Exception {

        CriancaResponseDTO crianca = coreClient.buscarCriancaPorId(criancaId, token);
        TerapeutaResponseDTO terapeuta = coreClient.buscarPerfilTerapeuta(token);
        List<TranscricaoResponseDTO> listaTranscricoes = anamneseClient.listarPorCrianca(criancaId, token);

        if (listaTranscricoes.isEmpty()) {
            throw new TranscricaoNotFoundException
                    ("Não é possível gerar o relatório: Nenhuma transcrição de áudio foi encontrada para esta criança.");
        }

        String historicoCompleto = listaTranscricoes.stream()
                .map(TranscricaoResponseDTO::getTextoBruto)
                .collect(Collectors.joining("\n\n")
                );

        String contextoLarissa = arquivoPersonaLarissa.getContentAsString(StandardCharsets.UTF_8);
        String roteiroDesafios = arquivoRoteiroDesafios.getContentAsString(StandardCharsets.UTF_8);

        String promptFinal = montarPrompt(contextoLarissa, roteiroDesafios, historicoCompleto);

        String textoGerado = chatModel.call(promptFinal);

        RelatorioEntity relatorio = RelatorioEntity.builder()
                .criancaId(criancaId)
                .nomeCrianca(crianca.getNomeCrianca())
                .terapeutaId(terapeuta.getId())
                .nomeTerapeuta(terapeuta.getNomeTerapeuta())
                .tipoTopicoEnum(TipoTopicoEnum.DESAFIOS_E_PARTICIPACOES)
                .conteudo(textoGerado)
                .dataCriacao(LocalDateTime.now())
                .build();

        relatorioRepository.save(relatorio);

        return textoGerado;

    }

    private String montarPrompt(String contextoLarissa, String roteiroDesafios, String textoBruto) {
        return """
               %s
               
               %s
               
               "%s"
               """.formatted(contextoLarissa, roteiroDesafios, textoBruto);
    }
}
