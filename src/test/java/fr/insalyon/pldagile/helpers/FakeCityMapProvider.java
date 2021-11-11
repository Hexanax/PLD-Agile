package fr.insalyon.pldagile.helpers;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;
import fr.insalyon.pldagile.xml.ExceptionXML;

import java.util.List;

/**
 * FakeCityMapProvider provides fake maps to tests while remaining independent from
 * XML parsing functions (which can be tested in their own test suite).
 *
 * Please be careful when deleting or changing lines in here as you could break a lot of tests...
 */
public abstract class FakeCityMapProvider {
    static public CityMap getSmallMap() throws ExceptionXML {
        Intersection[] intersections = {
                new Intersection(1L, new Coordinates(4, 5)),
                new Intersection(2L, new Coordinates(5, 8)),
                new Intersection(3L, new Coordinates(6, 9)),
                new Intersection(4L, new Coordinates(6, 3)),
                new Intersection(5L, new Coordinates(2, 7)),
                new Intersection(6L, new Coordinates(3, 3)),
                new Intersection(7L, new Coordinates(5, 1)),
                new Intersection(8L, new Coordinates(1, 3)),

        };
        Segment[] segments = {
                new Segment("1to2", 3, intersections[0], intersections[1]),
                new Segment("2to3", 7, intersections[1], intersections[2]),
                new Segment("3to4", 10, intersections[2], intersections[3]),
                new Segment("4to1", 3, intersections[3], intersections[0]),
                new Segment("1to5", 4.5, intersections[0], intersections[4]),
                new Segment("5to6", 2.5, intersections[4], intersections[5]),
                new Segment("6to3", 2.0, intersections[5], intersections[2]),

        };
        CityMap cityMap = new CityMap();
        for (Intersection i : intersections) {
            cityMap.add(i);
        }
        cityMap.addAllSegments(List.of(segments));
        return cityMap;
    }

    static public CityMap getMediumMap() throws ExceptionXML {
        List<Intersection> intersections = FakeIntersectionProvider.getFakeIntersections();
                Segment[] segments = {
                new Segment("Rue Matthieu Roux"    , 20, intersections.get(0),   intersections.get(6-1)),
                new Segment("Rue de l'église"      , 25, intersections.get(6-1), intersections.get(7-1)),
                new Segment("Boulevard Boulard"  , 40, intersections.get(7-1), intersections.get(3-1)),
                new Segment("Boulevard Delpech"    , 30, intersections.get(3-1), intersections.get(5-1)),
                new Segment("Chemin Metwalli"      , 75, intersections.get(5-1), intersections.get(2-1)),
                new Segment("Route de la Soie"     , 10, intersections.get(2-1), intersections.get(5-1)),
                new Segment("Avenue Saugier"       , 15, intersections.get(5-1), intersections.get(4-1)),
                new Segment("Boulevard Belateche"  , 35, intersections.get(0),   intersections.get(8-1)),
                new Segment("Place SGBD"           , 40, intersections.get(0),   intersections.get(9-1)),
                new Segment("Place Conficius"      , 55, intersections.get(4-1), intersections.get(9-1)),
                new Segment("Route Gandhi"         , 50, intersections.get(9-1), intersections.get(0)),
                new Segment("Chemin des pointeurs" , 5,  intersections.get(8-1), intersections.get(2-1)),
                new Segment("Impasse des partiels" , 15, intersections.get(9-1), intersections.get(7-1)),
                new Segment("Avenue de la Paix"    , 25, intersections.get(8-1), intersections.get(7-1)),
        };
        CityMap cityMap = new CityMap();
        for (Intersection i : intersections) {
            cityMap.add(i);
        }
        cityMap.addAllSegments(List.of(segments));
        return cityMap;
    }
}
