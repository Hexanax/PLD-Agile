package fr.insalyon.pldagile.service;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.services.BoundingRectangle;
import fr.insalyon.pldagile.services.CityMapUtils;
import fr.insalyon.pldagile.xml.ExceptionXML;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CityMapUtilsTest {
    CityMap cityMap;

    private static final double tolerance = 0.01;

    @BeforeEach
    void setup() throws ExceptionXML {
        cityMap = new CityMap();
        cityMap.add(new Intersection(0L, new Coordinates(1, 1)));
        cityMap.add(new Intersection(1L, new Coordinates(2, 2)));
    }

    @Test
    public void test_getCenter() {
        Coordinates expectedResult = new Coordinates(1.5, 1.5);
        Coordinates actualResult = CityMapUtils.getCenter(cityMap);

        // The results are never exact therefore we must use a tolerance in our
        // assertion
        assertTrue(expectedResult.getLatitude() - actualResult.getLatitude() < tolerance
                && expectedResult.getLatitude() - actualResult.getLatitude() > -tolerance);
        assertTrue(expectedResult.getLongitude() - actualResult.getLongitude() < tolerance
                || expectedResult.getLongitude() - actualResult.getLongitude() > -tolerance);
    }

    @Test
    public void test_getMinimumBoundingRectangle() {
        BoundingRectangle expectedResult = new BoundingRectangle(1, 1, 2, 2);
        BoundingRectangle actualResult = CityMapUtils.getMinimumBoundingRectangle(cityMap);

        assertEquals(expectedResult, actualResult);
    }
}
