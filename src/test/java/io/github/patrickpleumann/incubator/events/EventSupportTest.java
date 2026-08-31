package io.github.patrickpleumann.incubator.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

class EventSupportTest
{
    @Test
    void firePassesEventToSubscribedListener()
    {
        //arrange
        EventSupport<String> support = new EventSupport<>();
        AtomicReference<String> recieved = new AtomicReference<>();
        support.subscribe(value -> recieved.set(value));

        //act
        support.fire("temperature changed");

        //assert
        assertEquals("temperature changed", recieved.get());
    }

    @Test
    void fireSubscribedEventsInOrder()
    {
        //Arrange
        EventSupport<String> support = new EventSupport<>();
        List<String> log = new ArrayList<>();

        support.subscribe(value -> log.add("1:" + value));
        support.subscribe(value -> log.add("2:" + value));
        support.subscribe(value -> log.add("3:" + value));

        //Act
        support.fire("temperature changed");
        //Assert
        assertEquals("1:temperature changed", log.get(0));
        assertEquals("2:temperature changed", log.get(1));
        assertEquals("3:temperature changed", log.get(2));
    }
}
