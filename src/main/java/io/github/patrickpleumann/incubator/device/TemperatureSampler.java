package io.github.patrickpleumann.incubator.device;

import java.time.Duration;
import java.util.Objects;

public final class TemperatureSampler implements AutoCloseable
{
    private final Incubator incubator;
    private final TemperatureSource source;
    private final Duration interval;

    public TemperatureSampler(Incubator incubator, TemperatureSource source, Duration interval)
    {
        this.incubator  =   Objects.requireNonNull(incubator);
        this.source     =   Objects.requireNonNull(source);
        this.interval   =   Objects.requireNonNull(interval);
    }

    public void start()
    {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void close()
    {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
