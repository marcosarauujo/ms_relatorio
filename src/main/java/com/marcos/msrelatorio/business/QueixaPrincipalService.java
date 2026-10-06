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
public class QueixaPrincipalService {

    private final CoreClient coreClient;
    private final AnamneseClient anamneseClient;
    private final ChatModel chatModel;

    @Value("classpath:prompts/larissa-persona.md")
    private Resource arquivoPersonaLArissa;

    public String geraQueixaPrincipal(Long criancaId, String token) throws Exception {
        CriancaResponseDTO crianca = coreClient.buscarCriancaPorId(criancaId, token);
        List<TranscricaoResponseDTO> listaTranscricoes = anamneseClient.listarPorCrianca(criancaId, token);


        String historicoCompleto = listaTranscricoes.stream()
                .map(TranscricaoResponseDTO::getTextoBruto)
                .collect(Collectors.joining("\n\n")
                );

        String contextoLarissa = arquivoPersonaLArissa.getContentAsString(StandardCharsets.UTF_8);

        String prompt = montarQueixaPrincipal(crianca.getNomeCrianca(), contextoLarissa, historicoCompleto);

        return chatModel.call(prompt);
    }

    private String montarQueixaPrincipal(String nomeCrianca, String contextoLarissa, String textoBruto) {
        return """
                %s
                
                     Abaixo está a transcrição bruta do áudio da sessão referente ao paciente %s.
                
                                Sua tarefa é extrair as informações e redigir APENAS o tópico "Queixa Principal" para o relatório clínico, seguindo rigorosamente o seu estilo de escrita profissional descrito acima.
                
                                Para construir uma Queixa Principal rica e no padrão dos nossos relatórios, certifique-se de incluir no texto gerado:
                                1. O motivo central: Quem encaminhou a criança ou se foi busca espontânea da família.
                                2. O diagnóstico ou hipótese diagnóstica atual (caso tenha sido mencionado no áudio).
                                3. Os desafios funcionais relatados pela família (ex: dificuldades nas Atividades de Vida Diária, recusa alimentar, atraso motor, dificuldades escolares).
                                4. Aspectos de processamento sensorial e regulação (ex: agitação excessiva, choro frequente, sensibilidade a toques/sons).
                
                                Diretrizes de Formatação:
                                - Escreva em parágrafos densos, objetivos e clínicos.
                                - Inicie o texto com estruturas clássicas de prontuário, como: "A família busca intervenção terapêutica ocupacional devido a..." ou "Paciente encaminhado por [profissional] devido a queixas de...".
                                - Use sempre a voz passiva ou a 3ª pessoa (ex: "A mãe relata que", "A família refere").
                                - NUNCA invente sintomas ou dados funcionais. Extraia estritamente o que foi falado na transcrição, mas com o seu vocabulário técnico.
                
                                Transcrição da Sessão:
                "%s"
                """.formatted(contextoLarissa, nomeCrianca, textoBruto);
    }

}
