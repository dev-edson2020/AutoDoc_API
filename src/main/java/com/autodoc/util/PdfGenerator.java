package com.autodoc.util;

import org.springframework.stereotype.Component;

@Component
public class PdfGenerator {

    public String gerarPdf(Object documento) {
        // Aqui você pode colocar a lógica real de geração de PDF
        // Por enquanto, simulação do Base64 de um PDF
        return "data:application/pdf;base64,PDF_SIMULADO";
    }
}
