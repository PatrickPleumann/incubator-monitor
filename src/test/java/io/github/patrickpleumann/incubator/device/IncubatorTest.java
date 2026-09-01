package io.github.patrickpleumann.incubator.device;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
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
    void updateTemperatureDoesNotFireBelowThreshold()
    {
        //arrange
        final double changeBelowThreshold = 0.001;
        Incubator incubator = new Incubator(37.0, 0.5);
        AtomicInteger counter = new AtomicInteger(0);
        incubator.temperatureChanged().subscribe(value -> counter.incrementAndGet());

        //act
        incubator.updateTemperature(37.0);
        incubator.updateTemperature(37.0);
        incubator.updateTemperature(37.0 + changeBelowThreshold);

        //assert
        assertEquals(0, counter.get());
    }


    @Test
    void newTestForMultithreading() throws Exception
    {
        //Arrange
        Incubator incubator = new Incubator(37.0, 0.5);
        AtomicInteger counter = new AtomicInteger(0);
        List<Thread> threadList = new ArrayList<>();
        incubator.temperatureChanged().subscribe(value -> counter.incrementAndGet());
        CountDownLatch startSignal = new CountDownLatch(1);

        //Act
        for (int i = 0; i < 8; i++)
        {

            var currentThread = new Thread(() ->
            {
                try
                {
                    startSignal.await();
                }
                catch (InterruptedException ex)
                {
                    throw new RuntimeException(ex);
                }

                incubator.updateTemperature(38.0);
            });
            threadList.add(currentThread);
            currentThread.start();
        }
        startSignal.countDown();

        for (int i = 0; i < threadList.size(); i++)
        {
            threadList.get(i).join();
        }

        //Assert
        assertEquals(1, counter.get());
    }
}
