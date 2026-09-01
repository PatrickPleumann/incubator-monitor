package io.github.patrickpleumann.incubator.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;


class EventSupportTest
{
    @Test
    void firePassesEventToSubscribedListener()
    {
        //arrange
        EventSupport<String> support = new EventSupport<>();
        AtomicReference<String> received = new AtomicReference<>();
        support.subscribe(value -> received.set(value));

        //act
        support.fire("temperature changed");

        //assert
        assertEquals("temperature changed", received.get());
    }

    @Test
    void fireSubscribedEventsInOrder() {
        //Arrange
        EventSupport<String> support = new EventSupport<>();
        List<String> log = new ArrayList<>();

        support.subscribe(value -> log.add("1:" + value));
        support.subscribe(value -> log.add("2:" + value));
        support.subscribe(value -> log.add("3:" + value));

        //Act
        support.fire("temperature changed");

        //Assert
        assertEquals(List.of("1:temperature changed", "2:temperature changed", "3:temperature changed"), log);
    }
    @Test
    void closedSubscriptionReceivesNoEvent()
    {
        //Arrange
        EventSupport<String> support = new EventSupport<>();
        List<String> log = new ArrayList<>();
        Subscription temp = support.subscribe(value -> log.add("Test Value"));
        temp.close();


        //Act
        support.fire("Test Value");

        //Assert
        //list.of("") is not an empty list - it holds one element and test fails correctly(!)
        //but CAREFUL:  comparison view shows expected[] and actual[] result as the same empty array
        assertEquals(List.of(), log);
    }

    @Test
    void closeOnlyDeletesHandledSubscription()
        {
            //Arrange
            EventSupport<String> support = new EventSupport<>();
            List<String> log = new ArrayList<>();
            Consumer<String> consumer = value -> log.add("Test");

            Subscription first = support.subscribe(consumer);
            Subscription second = support.subscribe(consumer);

            first.close();
            first.close();

            //Act
            support.fire("Test Fire");

            //Assert
            assertEquals(List.of("Test"), log);
        }

    @Test
    void subscribeRejectsNull()
    {
        EventSupport<String> support = new EventSupport<>();

        assertThrows(NullPointerException.class, () -> support.subscribe(null));
    }

    @Test
    void subscriptionIsClosedWhenTryBlockExits()
    {
        //Arrange
        EventSupport<String> support = new EventSupport<>();
        List<String> log = new ArrayList<>();

        //Act
        try(Subscription currentSub = support.subscribe(value -> log.add("Test")))
        {
            support.fire("Test");
        }
        support.fire("Test");

        //Assert
        assertEquals(List.of("Test"), log);
    }

    @Test
    void throwingListenerIsReportedAndDeliveryContinues()
    {
        //Arrange
        List<String> log = new ArrayList<>();
        AtomicReference<RuntimeException> atomicReference = new AtomicReference<>();
        EventSupport<String> support = new EventSupport<>(value -> atomicReference.set(value));
        support.subscribe(value -> { throw new RuntimeException("Booooom"); });
        support.subscribe(value -> log.add("Not Boom"));

        //Act
        support.fire("Maybe Boom?");

        //Assert
        assertNotNull(atomicReference.get());
        assertEquals(List.of("Not Boom"), log);
    }
}
