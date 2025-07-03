package com.autodoc.service;

import com.autodoc.dto.DocumentoDTO;
import com.autodoc.model.Documento;
import com.autodoc.model.StatusDocumento;
import com.autodoc.repository.DocumentoRepository;
import com.autodoc.util.PdfGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final DocumentoRepository repository;
    private final PdfGenerator pdfGenerator;
    private final ObjectMapper objectMapper;

    public Documento gerarDocumento(DocumentoDTO dto) {
        Documento documento = new Documento();
        documento.setType(dto.getType());
        documento.setTitle(dto.getTitle());

        try {
            String formDataJson = objectMapper.writeValueAsString(dto.getFormData());
            documento.setConteudoJson(formDataJson);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao converter formData para JSON string");
        }

        documento.setCreatedBy(dto.getCreatorEmail());  // Agora corretamente o email
        documento.setCreatedDate(LocalDateTime.now());
        documento.setStatus(StatusDocumento.GENERATED);
        documento.setPdfUrl(pdfGenerator.gerarPdf(documento));

        return repository.save(documento);
    }

    public List<Documento> listarDocumentos() {
        return repository.findAllByOrderByIdDesc();
    }

    public void atualizarStatus(Long id, StatusDocumento status) {
        Documento documento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado com ID: " + id));
        documento.setStatus(status);
        repository.save(documento);
    }

    public List<Documento> listarPorCriador(String email) {
        return repository.findByCreatedBy(email);
    }
}
