package io.github.patrickpleumann.incubator.events;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventSupport<T> implements  Event<T>
{
    private final List<Registration> listeners = new CopyOnWriteArrayList<>();
    private final Consumer<RuntimeException> errorHandler;

    public EventSupport(Consumer<RuntimeException> errorHandler)
    {
        Objects.requireNonNull(errorHandler);
        this.errorHandler = errorHandler;
    }
    public EventSupport() { this(value -> value.printStackTrace()); }


    @Override
    public Subscription subscribe(Consumer<? super T> listener)
    {
        Objects.requireNonNull(listener);
        var currentRegistration = new Registration(listener);
        listeners.add(currentRegistration);
        return currentRegistration;
    }

    public void fire(T event)
    {
        for (Registration registration : listeners)
        {
            try
            {
                registration.consumer.accept(event);
            }
            catch (RuntimeException ex)
            {
                errorHandler.accept(ex);
            }
        }
    }

    private class Registration implements Subscription
    {
        private final Consumer<? super T> consumer;

        private Registration(Consumer<? super T> consumer)
        {
            this.consumer = consumer;
        }

        @Override
        public void close()
        {
            listeners.remove(this);
        }
    }
}
