package io.github.patrickpleumann.incubator.device;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;


public class TemperatureSamplerTest
{
    @Test
    void startedSamplerUpdatesTheIncubator() throws Exception
    {
        //arrange
        Incubator incubator = new Incubator(37.0, 0.5);
        TemperatureSource source = (current, target) -> current + 1.0;
        CountDownLatch received = new CountDownLatch(3);
        incubator.temperatureChanged().subscribe((event -> received.countDown()));

        //act
        try(TemperatureSampler sampler =
                new TemperatureSampler(incubator, source, Duration.ofMillis(10)))
        {
            sampler.start();

            //assert
            assertTrue(received.await(2, TimeUnit.SECONDS), "No updates arrived from the sampler");
        }
    }

    @Test
    void noUpdatesArriveAfterClose() throws Exception
    {
        //arrange
        Incubator incubator = new Incubator(37.0, 0.5);
        TemperatureSource source = (current, target) -> current + 1.0;
        AtomicInteger counter = new AtomicInteger();
        CountDownLatch received = new CountDownLatch(3);
        incubator.temperatureChanged().subscribe(value -> { counter.incrementAndGet(); received.countDown();});

        //act
        TemperatureSampler sampler = new TemperatureSampler(incubator, source, Duration.ofMillis(10));
        sampler.start();
        assertTrue(received.await(2, TimeUnit.SECONDS), "sampler never started");
        sampler.close();
        int afterClose = counter.get();
        Thread.sleep(100);

        //assert
        assertEquals(afterClose, counter.get());
        assertDoesNotThrow(()-> sampler.close());
    }
}
