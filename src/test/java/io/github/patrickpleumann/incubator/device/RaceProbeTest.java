package io.github.patrickpleumann.incubator.device;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

class RaceProbeTest
{
    @Test
    void probe() throws Exception
    {
        int rounds = 0;
        int worst = 1;
        for (int round = 0; round < 200; round++)
        {
            Incubator incubator = new Incubator(37.0, 0.5);
            AtomicInteger counter = new AtomicInteger(0);
            CountDownLatch startSignal = new CountDownLatch(1);
            List<Thread> threads = new ArrayList<>();
            incubator.temperatureChanged().subscribe(value -> counter.incrementAndGet());

            for (int i = 0; i < 200; i++)
            {
                Thread thread = new Thread(() ->
                {
                    try { startSignal.await(); } catch (InterruptedException ex) { throw new RuntimeException(ex); }
                    incubator.updateTemperature(38.0);
                });
                threads.add(thread);
                thread.start();
            }
            startSignal.countDown();
            for (Thread thread : threads) thread.join();

            if (counter.get() != 1) { rounds++; worst = Math.max(worst, counter.get()); }
        }
        System.out.println("PROBE: rounds with more than one event = " + rounds + " / 200, worst count = " + worst);
    }
}
