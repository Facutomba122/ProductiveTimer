package com.productivetimer.persistence.models;

import com.productivetimer.model.TimerType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "timers",
        indexes = {
                @Index(name = "idx_timers_user", columnList = "user_identifier")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class TimerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36, updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @NotNull(message = "El tipo de temporizador (timerType) es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "timer_type", length = 30, nullable = false)
    private TimerType timerType;

    @NotBlank(message = "La etiqueta (label) no puede estar vacía")
    @Size(max = 150, message = "La etiqueta (label) no puede exceder los 150 caracteres")
    @Column(name = "label", length = 150, nullable = false)
    private String label;

    @NotNull(message = "La duración (duration) es obligatoria")
    @Column(name = "duration", nullable = false)
    private LocalDateTime duration;

    @NotBlank(message = "El usuario (user) no puede estar vacío")
    @Size(max = 100, message = "El usuario (user) no puede exceder los 100 caracteres")
    @Column(name = "user_identifier", length = 100, nullable = false)
    private String user;

    public TimerModel(TimerType timerType, String label, LocalDateTime duration, String user) {
        this.timerType = Objects.requireNonNull(timerType, "timerType no puede ser nulo");
        this.label = Objects.requireNonNull(label, "label no puede ser nulo").trim();
        this.duration = Objects.requireNonNull(duration, "duration no puede ser nulo");
        this.user = Objects.requireNonNull(user, "user no puede ser nulo").trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimerModel that = (TimerModel) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
