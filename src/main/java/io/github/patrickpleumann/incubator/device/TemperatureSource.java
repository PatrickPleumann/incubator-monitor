package io.github.patrickpleumann.incubator.device;

public interface TemperatureSource
{
    double nextTemperature(double currentCelsius, double targetCelsius);
}
