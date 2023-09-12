package baza.trainee.domain.dto;

import baza.trainee.domain.enums.ContentType;

public record SearchResponse(
    String id,
    String title,
    String description,
    ContentType contentType
) { }
