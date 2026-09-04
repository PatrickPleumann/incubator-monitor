package io.github.patrickpleumann.incubator.device;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public final class TemperatureSampler implements AutoCloseable
{
    private final Incubator incubator;
    private final TemperatureSource source;
    private final Duration interval;
    private ScheduledExecutorService scheduler;
    private final Object threadlock = new Object();


    public TemperatureSampler(Incubator incubator, TemperatureSource source, Duration interval)
    {
        this.incubator  =   Objects.requireNonNull(incubator);
        this.source     =   Objects.requireNonNull(source);
        this.interval   =   Objects.requireNonNull(interval);
    }

    public void start()
    {
        synchronized (threadlock)
        {
            if(scheduler != null)
                return;
            else
            {
                ThreadFactory factory = runnable ->
                {
                  Thread thread = new Thread(runnable, "temperature-sampler");
                  thread.setDaemon(true);
                  return thread;
                };

                scheduler = Executors.newSingleThreadScheduledExecutor(factory);
                Runnable measurement = () ->
                {
                    try
                    {
                        double current = incubator.getCurrentTemperature();
                        double next = source.nextTemperature(current, incubator.getTargetTemperature());
                        incubator.updateTemperature(next);
                    }
                    catch (RuntimeException ex)
                    {
                        ex.printStackTrace();
                    }
                };

                scheduler.scheduleWithFixedDelay(measurement, 0, interval.toMillis(), TimeUnit.MILLISECONDS);
            }
        }
    }

    public void stop()
    {
        synchronized (threadlock)
        {
            if(scheduler == null)
                return;
            else
            {
                try
                {
                    scheduler.shutdown();
                    if(!scheduler.awaitTermination(1, TimeUnit.SECONDS))
                        scheduler.shutdownNow();
                }
                catch (InterruptedException ex)
                {
                    scheduler.shutdownNow();
                    Thread.currentThread().interrupt();
                }
                scheduler = null;
            }
        }
    }
    public boolean isRunning()
    {
        synchronized (threadlock)
        {
            return scheduler != null;
        }
    }

    @Override
    public void close()
    {
        stop();
    }
}
