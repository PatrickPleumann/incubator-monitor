package io.github.patrickpleumann.incubator.device;

import io.github.patrickpleumann.incubator.events.Event;
import io.github.patrickpleumann.incubator.events.EventSupport;
public class Incubator
{

    private final double toleranceCelsius;
    private double targetCelsius;
    private double measuredCelsius;
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
        return measuredCelsius;
    }

    public double getTargetTemperature()
    {
       return targetCelsius;
    }

    public void setTargetTemperature(double celsius)
    {
        requireValidTargetTemperature(celsius);
        targetCelsius = celsius;
    }

    public void updateTemperature(double celsius)
    {
        var previousCelsius = measuredCelsius;
        measuredCelsius = celsius;
        eventSupport.fire(new TemperatureChangedEvent(previousCelsius, measuredCelsius));
    }

    public boolean isWithinTolerance()
    {
        return Math.abs(measuredCelsius - targetCelsius) <= toleranceCelsius;
    }

    private static void requireValidTargetTemperature(double value)
    {
        if(!Double.isFinite(value))
            throw new IllegalArgumentException(value + " is not a finite floating point number");
        if(value < 0 || value > 100)
            throw new IllegalArgumentException(value + " can´t be below 0 or over 100 degrees.");
    }

    private static void requireValidTolerance(double value)
    {
        if(!Double.isFinite(value))
            throw new IllegalArgumentException(value + " is not a finite floating point number");
        if(value < 0)
            throw new IllegalArgumentException(value + " can´t be negative");
    }
}
