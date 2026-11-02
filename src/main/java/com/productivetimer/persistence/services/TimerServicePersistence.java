package com.productivetimer.persistence.services;

import com.productivetimer.persistence.models.TimerModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@Validated
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TimerServicePersistence {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create: Valida y persiste un nuevo registro de TimerModel en la base de datos con rollback en caso de error.
     */
    @Transactional(rollbackFor = Exception.class)
    public TimerModel create(@NotNull(message = "La entidad TimerModel no puede ser nula") @Valid TimerModel timerModel) {
        validateTimerPayload(timerModel);
        entityManager.persist(timerModel);
        entityManager.flush();
        return timerModel;
    }

    /**
     * Read by ID: Busca un TimerModel por su identificador único (UUID).
     */
    public Optional<TimerModel> findById(@NotNull(message = "El ID no puede ser nulo") UUID id) {
        Objects.requireNonNull(id, "El ID del TimerModel no puede ser nulo");
        return Optional.ofNullable(entityManager.find(TimerModel.class, id));
    }

    /**
     * Read by ID & User: Busca un TimerModel por ID verificando el usuario propietario mediante setParameter.
     */
    public Optional<TimerModel> findByIdAndUser(
            @NotNull(message = "El ID no puede ser nulo") UUID id,
            @NotBlank(message = "El usuario no puede estar vacío") String user) {
        Objects.requireNonNull(id, "El ID del TimerModel no puede ser nulo");
        String sanitizedUser = sanitizeRequiredText(user, "user");

        TypedQuery<TimerModel> query = entityManager.createQuery(
                "SELECT t FROM TimerModel t WHERE t.id = :id AND t.user = :user",
                TimerModel.class
        );
        query.setParameter("id", id);
        query.setParameter("user", sanitizedUser);

        return query.getResultStream().findFirst();
    }

    /**
     * Read All: Obtiene todos los registros de TimerModel almacenados.
     */
    public List<TimerModel> findAll() {
        return entityManager.createQuery("SELECT t FROM TimerModel t", TimerModel.class)
                .getResultList();
    }

    /**
     * Read All by User: Obtiene todos los TimerModel de un usuario utilizando parámetros seguros (setParameter).
     */
    public List<TimerModel> findAllByUser(@NotBlank(message = "El usuario no puede estar vacío") String user) {
        String sanitizedUser = sanitizeRequiredText(user, "user");

        TypedQuery<TimerModel> query = entityManager.createQuery(
                "SELECT t FROM TimerModel t WHERE t.user = :user",
                TimerModel.class
        );
        query.setParameter("user", sanitizedUser);
        return query.getResultList();
    }

    /**
     * Update: Actualiza un TimerModel existente por su UUID dentro de una transacción segura con rollback.
     */
    @Transactional(rollbackFor = Exception.class)
    public Optional<TimerModel> update(
            @NotNull(message = "El ID no puede ser nulo") UUID id,
            @NotNull(message = "Los datos de actualización no pueden ser nulos") @Valid TimerModel updatedData) {
        Objects.requireNonNull(id, "El ID del TimerModel no puede ser nulo");
        validateTimerPayload(updatedData);

        TimerModel existing = entityManager.find(TimerModel.class, id);
        if (existing == null) {
            return Optional.empty();
        }

        existing.setTimerType(updatedData.getTimerType());
        existing.setLabel(updatedData.getLabel().trim());
        existing.setDuration(updatedData.getDuration());
        existing.setUser(updatedData.getUser().trim());

        return Optional.of(entityManager.merge(existing));
    }

    /**
     * Update by ID & User: Actualiza un TimerModel existente validando que pertenezca al usuario propietario.
     */
    @Transactional(rollbackFor = Exception.class)
    public Optional<TimerModel> updateByIdAndUser(
            @NotNull(message = "El ID no puede ser nulo") UUID id,
            @NotBlank(message = "El usuario no puede estar vacío") String user,
            @NotNull(message = "Los datos de actualización no pueden ser nulos") @Valid TimerModel updatedData) {
        validateTimerPayload(updatedData);

        Optional<TimerModel> existingOpt = findByIdAndUser(id, user);
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }

        TimerModel existing = existingOpt.get();
        existing.setTimerType(updatedData.getTimerType());
        existing.setLabel(updatedData.getLabel().trim());
        existing.setDuration(updatedData.getDuration());

        return Optional.of(entityManager.merge(existing));
    }

    /**
     * Delete: Elimina un TimerModel por su UUID con rollback en caso de excepción.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(@NotNull(message = "El ID no puede ser nulo") UUID id) {
        Objects.requireNonNull(id, "El ID del TimerModel no puede ser nulo");

        TimerModel existing = entityManager.find(TimerModel.class, id);
        if (existing == null) {
            return false;
        }
        entityManager.remove(existing);
        return true;
    }

    /**
     * Delete by ID & User: Elimina un TimerModel verificando que pertenezca al usuario indicado.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIdAndUser(
            @NotNull(message = "El ID no puede ser nulo") UUID id,
            @NotBlank(message = "El usuario no puede estar vacío") String user) {
        Optional<TimerModel> existingOpt = findByIdAndUser(id, user);
        if (existingOpt.isEmpty()) {
            return false;
        }
        entityManager.remove(existingOpt.get());
        return true;
    }

    private void validateTimerPayload(TimerModel timerModel) {
        Objects.requireNonNull(timerModel, "La instancia de TimerModel no puede ser nula");
        Objects.requireNonNull(timerModel.getTimerType(), "El campo timerType es obligatorio y no puede ser nulo");
        Objects.requireNonNull(timerModel.getDuration(), "El campo duration es obligatorio y no puede ser nulo");

        String cleanLabel = sanitizeRequiredText(timerModel.getLabel(), "label");
        String cleanUser = sanitizeRequiredText(timerModel.getUser(), "user");

        timerModel.setLabel(cleanLabel);
        timerModel.setUser(cleanUser);
    }

    private String sanitizeRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El campo obligatorio '" + fieldName + "' no puede ser nulo ni estar vacío");
        }
        String trimmed = value.trim();
        if (trimmed.contains("\0")) {
            throw new IllegalArgumentException("El campo '" + fieldName + "' contiene caracteres nulos no permitidos");
        }
        return trimmed;
    }
}
