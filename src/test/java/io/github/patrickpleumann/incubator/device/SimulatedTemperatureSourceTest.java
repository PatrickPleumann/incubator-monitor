package io.github.patrickpleumann.incubator.device;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.Random;


public class SimulatedTemperatureSourceTest
{
    @Test
    void sameSeedProducesSameSequence()
    {
        //arrange
        int steps = 20;
        double target = 37.0;
        SimulatedTemperatureSource source1 = new SimulatedTemperatureSource(new Random(20));
        SimulatedTemperatureSource source2 = new SimulatedTemperatureSource(new Random(20));

        double[] source1Array = new double[steps];
        double[] source2Array = new double[steps];

        double current1 = target;
        double current2 = target;
        //act
        for( int i = 0; i < steps; i++)
        {
            current1 = source1.nextTemperature(current1,target);
            current2 = source2.nextTemperature(current2,target);

            source1Array[i] = current1;
            source2Array[i] = current2;
        }

        //assert
        assertArrayEquals(source1Array, source2Array);
    }

    @Test
    void valuesStayWithinBandAroundTarget()
    {
        //arrange
        int steps = 1000;
        double target = 37.0;
        double current = target;
        double allowedDeviation = 5.0;
        SimulatedTemperatureSource source = new SimulatedTemperatureSource(new Random(20));

        //act + assert
        for (int i = 0; i < steps; i++)
        {
            current = source.nextTemperature(current, target);
            final int currentStep = i;
            final double value = current;
            assertTrue(Math.abs(current - target) <= allowedDeviation,
                    () -> " step " + currentStep + ": " + value);
        }
    }

    @Test
    void moveTowardsTarget()
    {
        //arrange
        double target = 37.0;
        double current = 20.0;
        int steps = 20;
        SimulatedTemperatureSource source = new SimulatedTemperatureSource(new Random(20));

        //act
        for (int i = 0; i < steps; i++)
        {
            current = source.nextTemperature(current, target);
        }

        //assert
        assertTrue(Math.abs(current - target) <= 1.0,
                "Did not approach target: " + current);
    }
}
