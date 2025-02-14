package se.myhappyplants.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaterCalculatorTest {

    @Test
    void lowerBound(){
        assertEquals(4, WaterCalculator.calculateWaterFrequencyForWatering(1));
    }
}