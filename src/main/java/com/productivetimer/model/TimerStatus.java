package com.productivetimer.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado del temporizador")
public enum TimerStatus {
    PENDING,
    RUNNING,
    PAUSED,
    COMPLETED,
    CANCELLED
}
