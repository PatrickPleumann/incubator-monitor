package io.github.patrickpleumann.incubator.device;
import java.util.Random;
import java.util.Objects;

public final class SimulatedTemperatureSource implements TemperatureSource
{
    private final Random rng;
    private static final double PULL_FACTOR = 0.25;
    private static final double NOISE_CELSIUS = 0.2;
    public SimulatedTemperatureSource(Random rng)
    {
        this.rng = Objects.requireNonNull(rng, "rng");
    }
    @Override
    public double nextTemperature(double currentCelsius, double targetCelsius)
    {
        double gap = targetCelsius - currentCelsius;
        double noise = (rng.nextDouble() * 2.0 - 1.0) * NOISE_CELSIUS;
        return currentCelsius + gap * PULL_FACTOR + noise;
    }
}
