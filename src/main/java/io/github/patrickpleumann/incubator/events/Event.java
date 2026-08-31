package io.github.patrickpleumann.incubator.events;

import java.util.function.Consumer;
public interface Event<T>
{
    Subscription subscribe(Consumer<? super T> listener);
}
