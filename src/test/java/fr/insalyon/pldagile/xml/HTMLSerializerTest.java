package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.helpers.FakeCityMapProvider;
import fr.insalyon.pldagile.helpers.FakeIntersectionProvider;
import fr.insalyon.pldagile.model.*;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTMLSerializerTest {
    private List<Segment> segments;
    private Request[] requests;
    private List<Intersection> intersections;
    private CityMap cityMap;
    private Depot depot;
    private PlanningRequest planningRequest;

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:m:s");

    @BeforeEach
    public void firstSetup() throws ParseException, ExceptionXML {

        cityMap = FakeCityMapProvider.getSmallMap();
        intersections = FakeIntersectionProvider.getFakeIntersections();
        // Request creation
         this.requests = new Request[]{
                 new Request(
                         new Pickup(cityMap.getIntersection(2L), 420),
                         new Delivery(cityMap.getIntersection(5L), 600)
                 ),
                 new Request(
                         new Pickup(cityMap.getIntersection(4L), 420),
                         new Delivery(cityMap.getIntersection(7L), 480)
                 ),
                 new Request(
                         new Pickup(cityMap.getIntersection(2L), 420),
                         new Delivery(cityMap.getIntersection(7L), 600)
                 ),

         };


        this.segments = List.of(
                new Segment(
                        "way1",
                        100.0,
                        intersections.get(0),
                        intersections.get(1)
                ),
                new Segment(
                        "way1",
                        123.0,
                        intersections.get(1),
                        intersections.get(2)
                ),
                new Segment(
                        "way2",
                        100.0,
                        intersections.get(2),
                        intersections.get(3)
                )
        );

        Date departureTime = simpleDateFormat.parse("8:0:0");
        this.depot = new Depot(this.intersections.get(1),departureTime);
    }

    @Test
    @DisplayName("Test createWay works")
    public void test_createWay() throws InterruptedException {
        class TestCase {
            final Map<String, Object> expectedResult;
            final Way way;

            public TestCase(Map<String, Object> expectedResult, Way way ) throws InterruptedException {
                this.expectedResult = expectedResult;
                this.way = way;
            }
        }
        Map<String, Object> buffer = new HashMap<>();
        buffer.put("type", "Way");
        buffer.put("name","way1" );
        buffer.put("total_length",(Double) 100.0);

        Way way = new Way();
        way.addSegment(segments.get(0));

        Map<String, Object> buffer1 = new HashMap<>();
        buffer1.put("type", "Way");
        buffer1.put("name","way1" );
        buffer1.put("total_length",(Double) 223.0);

        Way way1 = new Way();
        way1.addSegment(segments.get(0));
        way1.addSegment(segments.get(1));

        Map<String, Object> buffer2 = new HashMap<>();
        buffer2.put("type", "Way");
        buffer2.put("name","way2" );
        buffer2.put("total_length",(Double) 100.0);

        Way way2 = new Way(segments.get(2));

        TestCase[] testCases = {
                new TestCase(
                        buffer,
                        way
                ),
                new TestCase(
                        buffer1,
                        way1
                ),
                new TestCase(
                        buffer2,
                        way2
                )

        };
        for (TestCase tc: testCases ) {
            assertEquals(tc.expectedResult, HTMLSerializer.createWay(tc.way));
        }
    }

    @Test
    @DisplayName("Test createIntersection works")
    public void test_createIntersection() throws InterruptedException {
        class TestCase {
            final Map<String, Object> expectedResult;
            final Intersection intersection;
            final boolean parcels;
            final Long idDepot;
            final String segment_name;
            final int orientation;


            public TestCase(Map<String, Object> expectedResult, Intersection intersection, boolean parcels, Long idDepot,
                            String segment_name, int orientation  ) throws InterruptedException {
                this.expectedResult = expectedResult;
                this.intersection = intersection;
                this.parcels = parcels;
                this. idDepot = idDepot;
                this.segment_name = segment_name;
                this.orientation = orientation;
            }
        }
        Map<String, Object> buffer = new HashMap<>();
        buffer.put("type", "Intersection");
        buffer.put("step", true);
        buffer.put("finish",true);
        buffer.put("object",intersections.get(0));

        Map<String, Object> buffer1 = new HashMap<>();
        buffer1.put("type", "Intersection");
        buffer1.put("step", true);
        buffer1.put("object",intersections.get(1));
        buffer1.put("segment_name","notDepot");
        buffer1.put("orientation",2);

        TestCase[] testCases = {
                new TestCase(
                        buffer,
                        intersections.get(0),
                        true,
                        intersections.get(0).getId(),
                        "depot",
                        1
                ),
                new TestCase(
                        buffer1,
                        intersections.get(1),
                        true,
                        intersections.get(0).getId(),
                        "notDepot",
                        2
                )
        };
        for (TestCase tc: testCases ) {
            assertEquals(tc.expectedResult,
                    HTMLSerializer.createIntersection(tc.intersection, tc.parcels, tc.idDepot, tc.segment_name, tc.orientation));
        }
    }

    @Test
    @DisplayName("Test createSpecificIntersection works")
    public void test_createSpecificIntersection() throws InterruptedException {
        class TestCase {
            final Map<String, Object> expectedResult;
            final Address address;
            final String subtype;
            final long requestID;


            public TestCase(Map<String, Object> expectedResult, Address address, String subtype, long requestID) throws InterruptedException {
                this.expectedResult = expectedResult;
                this.address = address;
                this.subtype = subtype;
                this.requestID = requestID;
            }
        }
        Address address1 = new Address(intersections.get(0));
        Map<String, Object> buffer = new HashMap<>();
        buffer.put("type", "Address");
        buffer.put("request", 0L);
        buffer.put("subtype", "depot");
        buffer.put("object",address1 );

        Address address2 = new Address(intersections.get(1));
        Map<String, Object> buffer1 = new HashMap<>();
        buffer1.put("type", "Address");
        buffer1.put("request", 2L);
        buffer1.put("subtype", "pickup");
        buffer1.put("object",address2 );

        Address address3 = new Address(intersections.get(2));
        Map<String, Object> buffer2 = new HashMap<>();
        buffer2.put("type", "Address");
        buffer2.put("request", 2L);
        buffer2.put("subtype", "delivery");
        buffer2.put("object",address3 );

        TestCase[] testCases = {
                new TestCase(
                        buffer,
                        address1,
                        "depot",
                        -1
                ),
                new TestCase(
                        buffer1,
                        address2,
                        "pickup",
                        1
                ),
                new TestCase(
                        buffer2,
                        address3,
                        "delivery",
                        1
                ),
        };
        for (TestCase tc: testCases ) {

            Map<String, Object> result = HTMLSerializer.createSpecificIntersection(tc.address, tc.subtype, tc.requestID);

            assertEquals(tc.expectedResult.get("subtype"),result.get("subtype"));
            assertEquals(tc.expectedResult.get("request"),result.get("request"));
            assertEquals(tc.expectedResult.get("object"),result.get("object"));

        }
    }

    @Test
    @DisplayName("Test getNextSpecificIntersection works")
    public void test_getNextSpecificIntersection() throws InterruptedException {
        class TestCase {
            final Address expectedResult;
            final Map<Long, Request> requests;
            final Pair<Long, String> step;

            public TestCase(Address expectedResult, Map<Long, Request> requests, Pair<Long, String> step ) throws InterruptedException {
                this.expectedResult = expectedResult;
                this.requests =requests;
                this.step = step;
            }
        }

        Address address1 = new Pickup(cityMap.getIntersection(2L), 420);
        Address address2 = new Delivery(cityMap.getIntersection(5L), 600);

        Pair<Long, String> step1 =  new Pair(1L, "pickup");
        Pair<Long, String> step2 = new Pair(1L,"delivery");

        Request request0 = requests[0];
        request0.setId(1L);

        Map<Long, Request> mapRequests = new HashMap<>();
        mapRequests.put(1L, request0);

        TestCase[] testCases = {
                new TestCase(
                        address1,
                        mapRequests,
                        step1
                ),
                new TestCase(
                        address2,
                        mapRequests,
                        step2
                )
        };
        for (TestCase tc: testCases ) {

            Address result = HTMLSerializer.getNextSpecificIntersection(tc.requests, tc.step);

            assertEquals(tc.expectedResult,result);

        }

    }


    @Test
    @DisplayName("Test getTime works")
    public void test_getAngleFromNorth(){
        double angle0;
        angle0 =  -2.690889897942206;

        assertEquals(angle0,HTMLSerializer.getAngleFromNorth(segments.get(0)));
    }


    @Test
    @DisplayName("Test getTime works")
    public void test_getTime(){
        double testSeconds0, testSeconds1, testSeconds2, testSeconds3,testSeconds4;
        testSeconds0 = 1.0;
        testSeconds1 = 3600.0;
        testSeconds2 = 11111.0;
        testSeconds3 = 90000.0;
        testSeconds4 = -1.0;
        assertEquals("1s ",HTMLSerializer.getTime(testSeconds0));
        assertEquals("1H ",HTMLSerializer.getTime(testSeconds1));
        assertEquals("3H 5min 11s ",HTMLSerializer.getTime(testSeconds2));
        assertEquals("25H ",HTMLSerializer.getTime(testSeconds3));
        assertEquals("",HTMLSerializer.getTime(testSeconds4));
    }

    @Test
    @DisplayName("Test compareOrientation works")
    public void test_compareOrientation(){
        double angle0, angle1, angle2, angle3,angle4;
        angle0 = 1.0;
        angle1 = 60.0;
        angle2 = -108.0;
        angle3 = 33.0;
        angle4 = 20.0;
        assertEquals(0,HTMLSerializer.compareOrientation(angle0, angle0)); // 0
        assertEquals(1,HTMLSerializer.compareOrientation(angle1, angle0)); // 59
        assertEquals(3,HTMLSerializer.compareOrientation(angle2, angle1)); //-168
        assertEquals(0,HTMLSerializer.compareOrientation(angle4, angle3)); //-13
        assertEquals(2,HTMLSerializer.compareOrientation(angle2, angle3)); //-141
    }
}
