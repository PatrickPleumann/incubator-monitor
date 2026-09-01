package io.github.patrickpleumann.incubator.device;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class IncubatorTest
{
    @Test
    void temperatureIsValidNumber()
    {
        assertThrows(IllegalArgumentException.class, () -> new Incubator(Double.NaN, 0.5));
        assertThrows(IllegalArgumentException.class, () -> new Incubator(-1.0, 0.5));
        assertThrows(IllegalArgumentException.class, () -> new Incubator(101.0, 0.5));
    }

    @Test
    void setTargetTemperatureRejectsInvalidValues()
    {
        Incubator incubator = new Incubator(37.0, 0.5);

        assertThrows(IllegalArgumentException.class, () -> incubator.setTargetTemperature(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> incubator.setTargetTemperature(-1.0));
        assertThrows(IllegalArgumentException.class, () -> incubator.setTargetTemperature(101.0));
    }
    @Test
    void constructorRejectsInvalidTolerance()
    {
        assertThrows(IllegalArgumentException.class, () -> new Incubator(37.0, Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new Incubator(37.0, -0.5));
    }

    @Test
    void isWithinToleranceIsTrueAtTheBoundary()
    {
        //Arrange
        Incubator incubator = new Incubator(37.0, 0.5);

        //Act
        incubator.updateTemperature(37.5);

        //Assert
        assertTrue(incubator.isWithinTolerance());
    }

    @Test
    void updateTemperatureFiresEventWithPreviousAndCurrentValue()
    {
        //arrange
        Incubator incubator = new Incubator(37.0, 0.5);
        AtomicReference<TemperatureChangedEvent> received = new AtomicReference<>();

        incubator.temperatureChanged().subscribe(value -> received.set(value));

        //act
        incubator.updateTemperature(38.0);

        //assert
        assertEquals(new TemperatureChangedEvent(37.0, 38.0), received.get());
    }

    @Test
    void NewTest()
    {
        Incubator incubator = new Incubator(37.0, 0.5);
        AtomicInteger counter = new AtomicInteger();
    }
}
