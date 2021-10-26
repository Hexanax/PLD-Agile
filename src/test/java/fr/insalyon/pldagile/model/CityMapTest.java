package fr.insalyon.pldagile.model;

import fr.insalyon.pldagile.helpers.FakeCityMapProvider;
import fr.insalyon.pldagile.xml.ExceptionXML;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CityMapTest {
    CityMap cityMap;
    @BeforeEach
    void setup() throws Exception{
        this.cityMap = FakeCityMapProvider.getMediumMap();
    }

    @Test
    public void testGetCenter() {
        class TestCase {
            final Coordinates expectedResult;

            public TestCase(Coordinates expectedResult) {
                this.expectedResult = expectedResult;
            }
        }
        TestCase[] tests = {new TestCase(new Coordinates(45.61117752676385,4.977950632220205)),
        };
        for (TestCase tc : tests) {
            Coordinates actualResult = cityMap.getCenter();
            assertEquals(tc.expectedResult, actualResult);
        }
    }

}
