package com.marcos.msrelatorio.business;

import com.marcos.msrelatorio.business.dto.CriancaResponseDTO;
import com.marcos.msrelatorio.business.dto.TranscricaoResponseDTO;
import com.marcos.msrelatorio.infrastructure.client.AnamneseClient;
import com.marcos.msrelatorio.infrastructure.client.CoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnamneseService {

    private final CoreClient coreClient;
    private final AnamneseClient anamneseClient;
    private final ChatModel chatModel;

    @Value("classpath:prompts/TO_larissa.md")
    private Resource arquivoPersonaLarissa;

    @Value("classpath:prompts/prompt-anamnese.md")
    private Resource arquivoRoteiroAnamnese;

    public String gerarAnamnese(Long criancaId, String token) throws Exception{

        CriancaResponseDTO crianca = coreClient.buscarCriancaPorId(criancaId, token);
        List<TranscricaoResponseDTO> listaTranscricoes = anamneseClient.listarPorCrianca(criancaId, token);

        String historicoCompleto = listaTranscricoes.stream()
                .map(TranscricaoResponseDTO::getTextoBruto)
                .collect(Collectors.joining("\n\n")
                );

        String contextoLarissa = arquivoPersonaLarissa.getContentAsString(StandardCharsets.UTF_8);
        String roteiroAnamnese = arquivoRoteiroAnamnese.getContentAsString(StandardCharsets.UTF_8);

        String promptFinal = montarPrompt(contextoLarissa, roteiroAnamnese, historicoCompleto);
        return chatModel.call(promptFinal);
    }
    private String montarPrompt(String contextoLarissa, String roteiroAnamnese, String textoBruto) {
        return """
               %s
               
               %s
               
               "%s"
               """.formatted(contextoLarissa, roteiroAnamnese, textoBruto);
    }

}
