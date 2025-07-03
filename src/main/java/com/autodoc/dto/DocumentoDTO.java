package com.autodoc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
public class DocumentoDTO {
    private String type;
    private String title;

    @JsonProperty("form_data")
    private Map<String, Object> formData;

    @JsonProperty("creator_email")
    private String creatorEmail;
}
