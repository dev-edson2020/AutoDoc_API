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
            Documento documento = documentoService.gerarDocumento(dto);
            return ResponseEntity.ok(documento);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao gerar documento: " + e.getMessage());
        }
    }

    @Data
    public static class StatusUpdateRequest {
        private String status;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        StatusDocumento novoStatus;
        try {
            novoStatus = StatusDocumento.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        documentoService.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Documento>> listarDocumentos() {
        List<Documento> documentos = documentoService.listarDocumentos();
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/dashboard/{email}")
    public ResponseEntity<Map<String, Object>> getDashboard(@PathVariable String email) {
        Optional<Usuario> userOpt = Optional.ofNullable(usuarioRepository.findByEmail(email));
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuário não encontrado"));
        }
        Usuario usuario = userOpt.get();

        List<Documento> allDocs = documentoRepository.findByCreatedBy(email);

        LocalDate now = LocalDate.now();
        long documentsThisMonth = allDocs.stream()
                .filter(doc -> {
                    LocalDate created = doc.getCreatedDate().toLocalDate();
                    return created.getMonth() == now.getMonth() && created.getYear() == now.getYear();
                })
                .count();

        List<Documento> recentDocuments = allDocs.stream()
                .sorted(Comparator.comparing(Documento::getCreatedDate).reversed())
                .limit(5)
                .toList();

        Map<String, Object> resp = new HashMap<>();
        resp.put("documentsThisMonth", documentsThisMonth);
        resp.put("recentDocuments", recentDocuments);
        resp.put("plan", usuario.getPlan());

        return ResponseEntity.ok(resp);
    }
}
