package io.github.patrickpleumann.incubator.events;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventSupport<T> implements  Event<T>
{
    private final List<Consumer<? super T>> listeners = new CopyOnWriteArrayList<>();
    @Override
    public Subscription subscribe(Consumer<? super T> listener) {
        Objects.requireNonNull(listener);
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    public void fire(T event)
    {
        for (Consumer<? super T> listener : listeners)
        {
            try
            {
                listener.accept(event);
            }
            catch (RuntimeException ex)
            {
                System.err.println("Listener throwed Exception: " + ex);
            }
        }
    }
}
