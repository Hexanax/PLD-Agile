package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.helpers.FakeCityMapProvider;
import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;
import fr.insalyon.pldagile.xml.ExceptionXML;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class WayTest {

    private List<Segment> segments;

    @BeforeEach
    void setup() throws ExceptionXML{
        segments = new ArrayList<>();

    }
}
