package com.productivetimer.dto;

import com.productivetimer.model.TimerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con la información persistida del temporizador")
public class TimerResponseDto {

    @Schema(description = "Identificador único del temporizador (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Tipo de temporizador", example = "POMODORO")
    private TimerType timerType;

    @Schema(description = "Etiqueta o descripción de la sesión", example = "Configurar Swagger en Spring Boot")
    private String label;

    @Schema(description = "Duración / marca de tiempo de la sesión", example = "2026-11-02T16:30:00")
    private LocalDateTime duration;

    @Schema(description = "Usuario propietario del temporizador", example = "facundo")
    private String user;

}
