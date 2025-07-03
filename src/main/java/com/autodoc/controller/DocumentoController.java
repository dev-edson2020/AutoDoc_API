package com.autodoc.controller;

import com.autodoc.dto.DocumentoDTO;
import com.autodoc.model.Documento;
import com.autodoc.model.StatusDocumento;
import com.autodoc.model.Usuario;
import com.autodoc.repository.DocumentoRepository;
import com.autodoc.repository.UsuarioRepository;
import com.autodoc.service.DocumentoService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documento")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DocumentoController {

    private final DocumentoService documentoService;
    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/gerar")
    public ResponseEntity<?> criarDocumento(@RequestBody DocumentoDTO dto) {
        // Validações
        if (dto.getCreatorEmail() == null || dto.getCreatorEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Campo 'creatorEmail' é obrigatório");
        }
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            return ResponseEntity.badRequest().body("Campo 'title' é obrigatório");
        }
        if (dto.getType() == null || dto.getType().isBlank()) {
            return ResponseEntity.badRequest().body("Campo 'type' é obrigatório");
        }
        if (dto.getFormData() == null || dto.getFormData().isEmpty()) {
            return ResponseEntity.badRequest().body("Campo 'formData' é obrigatório");
        }

        try {
            // Verifica se o usuário existe
            Usuario usuario = usuarioRepository.findByEmail(dto.getCreatorEmail())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com email: " + dto.getCreatorEmail()));

            Documento documento = documentoService.gerarDocumento(dto);
            return ResponseEntity.ok(documento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao gerar documento: " + e.getMessage());
        }
    }

    @Data
    public static class StatusUpdateRequest {
        private String status;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        try {
            StatusDocumento novoStatus = StatusDocumento.valueOf(request.getStatus().toUpperCase());
            documentoService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Status inválido: " + request.getStatus());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar status: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Documento>> listarDocumentos() {
        List<Documento> documentos = documentoService.listarDocumentos();
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/dashboard/{email}")
    public ResponseEntity<?> getDashboard(@PathVariable String email) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            List<Documento> allDocs = documentoRepository.findByCreatedBy(email);
            LocalDate now = LocalDate.now();

            // Documentos deste mês
            long documentsThisMonth = allDocs.stream()
                    .filter(doc -> doc.getCreatedDate().toLocalDate().getMonth() == now.getMonth() &&
                            doc.getCreatedDate().toLocalDate().getYear() == now.getYear())
                    .count();

            // Documentos recentes (últimos 5)
            List<Documento> recentDocuments = allDocs.stream()
                    .sorted(Comparator.comparing(Documento::getCreatedDate).reversed())
                    .limit(5)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("documentsThisMonth", documentsThisMonth);
            response.put("recentDocuments", recentDocuments);
            response.put("plan", usuario.getPlan());
            response.put("userName", usuario.getFullName());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao carregar dashboard");
        }
    }
}