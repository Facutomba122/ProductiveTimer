package com.productivetimer.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo o modalidad de temporizador")
public enum TimerType {
    POMODORO,
    SHORT_BREAK,
    LONG_BREAK,
    CUSTOM
}
