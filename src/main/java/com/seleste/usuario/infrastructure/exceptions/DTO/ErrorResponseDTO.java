package com.seleste.usuario.infrastructure.exceptions.DTO;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int statusCode,
        String error,
        String message,
        String path
) {}
