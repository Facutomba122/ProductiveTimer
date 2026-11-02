package com.productivetimer.service;

import com.productivetimer.dto.TimerRequestDto;
import com.productivetimer.dto.TimerResponseDto;
import com.productivetimer.persistence.models.TimerModel;
import com.productivetimer.persistence.services.TimerServicePersistence;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TimerService {

    private final TimerServicePersistence timerServicePersistence;

    public TimerService(TimerServicePersistence timerServicePersistence) {
        this.timerServicePersistence = timerServicePersistence;
    }

    public List<TimerResponseDto> getAllTimers() {
        return timerServicePersistence.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<TimerResponseDto> getTimersByUser(String user) {
        validateUserParam(user);
        return timerServicePersistence.findAllByUser(user.trim())
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public Optional<TimerResponseDto> getTimerById(UUID id) {
        Objects.requireNonNull(id, "El ID del temporizador no puede ser nulo");
        return timerServicePersistence.findById(id)
                .map(this::mapToResponseDto);
    }

    public TimerResponseDto createTimer(TimerRequestDto request) {
        validateRequestDto(request);
        TimerModel modelToPersist = mapToModel(request);
        TimerModel persistedModel = timerServicePersistence.create(modelToPersist);
        return mapToResponseDto(persistedModel);
    }

    public Optional<TimerResponseDto> updateTimer(UUID id, TimerRequestDto request) {
        Objects.requireNonNull(id, "El ID del temporizador no puede ser nulo");
        validateRequestDto(request);
        TimerModel updatedModelData = mapToModel(request);
        return timerServicePersistence.update(id, updatedModelData)
                .map(this::mapToResponseDto);
    }

    public boolean deleteTimer(UUID id) {
        Objects.requireNonNull(id, "El ID del temporizador no puede ser nulo");
        return timerServicePersistence.deleteById(id);
    }

    private TimerModel mapToModel(TimerRequestDto dto) {
        return TimerModel.builder()
                .timerType(dto.getTimerType())
                .label(dto.getLabel().trim())
                .duration(dto.getDuration())
                .user(dto.getUser().trim())
                .build();
    }

    private TimerResponseDto mapToResponseDto(TimerModel model) {
        return TimerResponseDto.builder()
                .id(model.getId())
                .timerType(model.getTimerType())
                .label(model.getLabel())
                .duration(model.getDuration())
                .user(model.getUser())
                .build();
    }

    private void validateRequestDto(TimerRequestDto request) {
        Objects.requireNonNull(request, "Los datos de la solicitud no pueden ser nulos");
        Objects.requireNonNull(request.getTimerType(), "El tipo de temporizador (timerType) es obligatorio");
        Objects.requireNonNull(request.getDuration(), "La duración (duration) es obligatoria");
        if (request.getLabel() == null || request.getLabel().isBlank()) {
            throw new IllegalArgumentException("La etiqueta (label) no puede estar vacía");
        }
        validateUserParam(request.getUser());
    }

    private void validateUserParam(String user) {
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("El usuario (user) no puede estar vacío");
        }
    }
}
