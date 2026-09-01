package io.github.patrickpleumann.incubator.device;

/**
 * Reports a temperature change: the value before and the value after.
 * Pure data, no logic and no validation.
 */
public record TemperatureChangedEvent(double previousCelsius, double currentCelsius) { }
