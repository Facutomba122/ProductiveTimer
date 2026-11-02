package com.productivetimer.dto;

import com.productivetimer.model.TimerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para crear o actualizar una sesión de temporizador")
public class TimerRequestDto {

    @Schema(description = "Tipo o modalidad de temporizador (POMODORO, SHORT_BREAK, LONG_BREAK, CUSTOM)", example = "POMODORO")
    @NotNull(message = "El tipo de temporizador (timerType) es obligatorio")
    private TimerType timerType;

    @Schema(description = "Etiqueta o descripción de la tarea/sesión", example = "Configurar Swagger en Spring Boot")
    @NotBlank(message = "La etiqueta (label) no puede estar vacía")
    @Size(max = 150, message = "La etiqueta (label) no puede exceder los 150 caracteres")
    private String label;

    @Schema(description = "Duración / marca de tiempo objetivo de la sesión", example = "2026-11-02T16:30:00")
    @NotNull(message = "La duración (duration) es obligatoria")
    private LocalDateTime duration;

    @Schema(description = "Identificador del usuario propietario del temporizador", example = "facundo")
    @NotBlank(message = "El usuario (user) no puede estar vacío")
    @Size(max = 100, message = "El usuario (user) no puede exceder los 100 caracteres")
    private String user;

}
