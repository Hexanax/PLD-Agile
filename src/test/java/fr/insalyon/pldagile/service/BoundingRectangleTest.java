package fr.insalyon.pldagile.service;

import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.services.BoundingRectangle;
import fr.insalyon.pldagile.xml.ExceptionXML;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoundingRectangleTest {
    BoundingRectangle boundingRectangle;

    @BeforeEach
    void setup() throws ExceptionXML {
        boundingRectangle = new BoundingRectangle(0, 0, 1, 1);
    }

    @Test
    @DisplayName("Test get shortest path works")
    public void test_getShortestPath() {
        class TestCase {
            final boolean expectedResult;
            final Coordinates coords;

            public TestCase(boolean expectedResult, Coordinates coords) {
                this.expectedResult = expectedResult;
                this.coords = coords;
            }
        }

        TestCase[] tests = { new TestCase(false, new Coordinates(0, 0)), new TestCase(false, new Coordinates(0, 0)),
                new TestCase(true, new Coordinates(0.5, 0.5)), new TestCase(false, new Coordinates(2, 2)) };
        for (TestCase tc : tests) {
            boolean actualResult = boundingRectangle.isInsideRectangle(tc.coords);
            assertEquals(tc.expectedResult, actualResult);
        }
    }

}