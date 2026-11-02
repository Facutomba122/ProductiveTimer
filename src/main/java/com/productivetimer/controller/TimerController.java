package com.productivetimer.controller;

import com.productivetimer.dto.TimerRequestDto;
import com.productivetimer.dto.TimerResponseDto;
import com.productivetimer.service.TimerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/timers")
@Tag(name = "Temporizadores", description = "Endpoints para la creación, consulta, actualización y eliminación de temporizadores")
public class TimerController {

    private final TimerService timerService;

    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @GetMapping
    @Operation(summary = "Listar temporizadores", description = "Obtiene la lista de todos los temporizadores registrados, o filtra por usuario si se provee el parámetro 'user'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimerResponseDto.class)))
    })
    public ResponseEntity<List<TimerResponseDto>> getAllTimers(
            @Parameter(description = "Filtrar opcionalmente por usuario propietario", example = "facundo")
            @RequestParam(required = false) String user) {
        if (user != null && !user.isBlank()) {
            return ResponseEntity.ok(timerService.getTimersByUser(user));
        }
        return ResponseEntity.ok(timerService.getAllTimers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener temporizador por ID", description = "Devuelve el detalle de un temporizador específico según su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Temporizador encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimerResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Temporizador no encontrado")
    })
    public ResponseEntity<TimerResponseDto> getTimerById(
            @Parameter(description = "Identificador único (UUID) del temporizador", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        return timerService.getTimerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo temporizador", description = "Crea y persiste una nueva sesión de temporizador en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Temporizador creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimerResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<TimerResponseDto> createTimer(
            @Valid @RequestBody TimerRequestDto request) {
        TimerResponseDto created = timerService.createTimer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un temporizador", description = "Actualiza los datos de un temporizador existente en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Temporizador actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimerResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Temporizador no encontrado")
    })
    public ResponseEntity<TimerResponseDto> updateTimer(
            @Parameter(description = "UUID del temporizador", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Valid @RequestBody TimerRequestDto request) {
        return timerService.updateTimer(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un temporizador", description = "Elimina un temporizador existente por su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Temporizador eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Temporizador no encontrado")
    })
    public ResponseEntity<Void> deleteTimer(
            @Parameter(description = "UUID del temporizador", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        if (timerService.deleteTimer(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
