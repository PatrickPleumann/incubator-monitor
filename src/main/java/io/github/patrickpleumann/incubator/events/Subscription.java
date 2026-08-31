package io.github.patrickpleumann.incubator.events;

@FunctionalInterface
public interface Subscription extends AutoCloseable
{
    @Override
    void close();
}
