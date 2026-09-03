package io.github.patrickpleumann.incubator.device;

import io.github.patrickpleumann.incubator.events.Event;
import io.github.patrickpleumann.incubator.events.EventSupport;
public class Incubator
{

    private final double toleranceCelsius;
    private double targetCelsius;
    private double measuredCelsius;
    private final Object threadLock = new Object();
    private static final double MINIMUM_CHANGE_CELSIUS = 0.001;
    private final EventSupport<TemperatureChangedEvent> eventSupport = new EventSupport<>();
    public Incubator(double targetCelsius, double toleranceCelsius)
    {
        requireValidTargetTemperature(targetCelsius);
        requireValidTolerance(toleranceCelsius);

        this.toleranceCelsius = toleranceCelsius;
        this.targetCelsius = targetCelsius;
        this.measuredCelsius = targetCelsius;
    }

    public Event<TemperatureChangedEvent> temperatureChanged()
    {
        return eventSupport;
    }

    public double getCurrentTemperature()
    {
        synchronized (threadLock)
        {
            return measuredCelsius;
        }
    }

    public double getTargetTemperature()
    {
        synchronized (threadLock)
        {
            return targetCelsius;
        }
    }

    public void setTargetTemperature(double celsius)
    {
        requireValidTargetTemperature(celsius);
        synchronized (threadLock)
        {
            targetCelsius = celsius;
        }
    }

    public void updateTemperature(double celsius)
    {
        TemperatureChangedEvent event;
        synchronized (threadLock)
        {
            if(Math.abs(celsius - measuredCelsius) <= MINIMUM_CHANGE_CELSIUS) { return; }
            var previousCelsius = measuredCelsius;
            measuredCelsius = celsius;
            event = new TemperatureChangedEvent(previousCelsius, measuredCelsius);
        }
        eventSupport.fire(event);
    }

    public boolean isWithinTolerance()
    {
        synchronized (threadLock)
        {
            return Math.abs(measuredCelsius - targetCelsius) <= toleranceCelsius;
        }
    }

    private static void requireValidTargetTemperature(double value)
    {
        if(!Double.isFinite(value))
            throw new IllegalArgumentException("Value: " + value + " is not a finite floating point number");
        if(value < 0 || value > 100)
            throw new IllegalArgumentException("Value: " + value + " can´t be below 0 or over 100 degrees.");
    }

    private static void requireValidTolerance(double value)
    {
        if(!Double.isFinite(value))
            throw new IllegalArgumentException("Value: " + value + " is not a finite floating point number");
        if(value < 0)
            throw new IllegalArgumentException("Value: " + value + " can´t be negative");
    }
}
