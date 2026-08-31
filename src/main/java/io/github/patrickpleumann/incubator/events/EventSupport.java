package io.github.patrickpleumann.incubator.events;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventSupport<T> implements  Event<T>
{
    private final List<Registration> listeners = new CopyOnWriteArrayList<>();
    @Override
    public Subscription subscribe(Consumer<? super T> listener) {
        Objects.requireNonNull(listener);
        var currentRegistration = new Registration(listener);
        listeners.add(currentRegistration);
        return currentRegistration;
    }

    public void fire(T event)
    {
        for (Registration listener : listeners)
        {
            try
            {
                listener.consumer.accept(event);
            }
            catch (RuntimeException ex)
            {
                System.err.println("Listener throwed Exception: " + ex);
            }
        }
    }

    private class Registration implements Subscription
    {
        private final Consumer<? super T> consumer;

        private Registration(Consumer<? super T> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void close()
        {
            listeners.remove(this);
        }
    }
}
