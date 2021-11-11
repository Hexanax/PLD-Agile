package fr.insalyon.pldagile.xml;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import fr.insalyon.pldagile.model.*;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * This class allows to save the raod map of a tour in a .html file using the Pebble java template based on Twig the equivalent in PHP.
 * <a href="https://pebbletemplates.io/"> pebbletemplates </a>
 */
public class HTMLSerializer {

    /**
     * Allows by using Pebble to compile the informations of the Tour we need to create the road map
     * The road map is rendered by following an html Pebble template and saved in the html file in parameter
     * @param tour The tour
     * @param html The file that is going to contain the roadmap
     * @throws IOException Exception thrown if problems with buffer and html file
     *
     */
    public static void renderHTMLroadMap(Tour tour, File html) throws IOException {
        List<Segment> segments = tour.getPath();
        Iterator<Segment> segmentIterator = segments.iterator();

        // List of Pairs such that Pair<Long : idStep, String :typeStep>
        // <-1, "depot"> ; <0, "pickup"> ; <1,"delivery">
        List<Pair<Long, String>> stepsIdentifiers = tour.getSteps();
        //stepIdentifiers  [-1=begin, 0=pickup, 0=delivery, 1=pickup, 1=delivery, 5=end]

        // Remove the begin step from the identifiers
        stepsIdentifiers.remove(0);

        List<Intersection> intersections = tour.getIntersections();
        Iterator<Intersection> intersectionIterator = intersections.iterator();


        Map<Long, Request> requests = tour.getRequests();


        Address nextSpecificIntersection = getNextSpecificIntersection(requests, stepsIdentifiers.get(0));

        List<Map<String,Object>> rows = new ArrayList<>();
        Depot depot = tour.getDepot();

        //Build the Pebble specific intersection
        boolean stepInDepot = false;
        while(nextSpecificIntersection!= null && intersections.get(0).getId() == nextSpecificIntersection.getIntersection().getId()){
            rows.add(createSpecificIntersection(nextSpecificIntersection,stepsIdentifiers.get(0).getValue(), stepsIdentifiers.get(0).getKey()));
            stepInDepot=true;
            stepsIdentifiers.remove(0);
            nextSpecificIntersection = getNextSpecificIntersection(requests, stepsIdentifiers.get(0));
        }
        if(stepInDepot){
            rows.add(createIntersection(intersections.get(0), true, null, segments.get(0).getName(),-1));
        }

        intersectionIterator.next();

        //Build the pebble segments and intersections

        if(segmentIterator.hasNext() && intersectionIterator.hasNext()){

            Segment currentSegment = segmentIterator.next();
            Intersection currentIntersection  = intersectionIterator.next();
            double currentAngle = getAngleFromNorth(currentSegment);
            Way currentWay = new Way(currentSegment);

            while(segmentIterator.hasNext() && intersectionIterator.hasNext()){

                Segment nextSegment = segmentIterator.next();
                Intersection nextIntersection = intersectionIterator.next();

                //Orientation between the two segments
                double followingAngle = getAngleFromNorth(nextSegment);

                boolean hasChangedWay= false;
                if(nextSegment.getName().equals(currentSegment.getName())){
                    currentWay.addSegment(currentSegment);
                }else{
                    rows.add(createWay(currentWay));
                    currentWay = new Way(nextSegment);
                    hasChangedWay = true;
                }
                boolean step = false;
                //Add the next specific intersection if needed
                while(nextSpecificIntersection!= null && currentIntersection.getId() == nextSpecificIntersection.getIntersection().getId()){
                    rows.add(createSpecificIntersection(nextSpecificIntersection,stepsIdentifiers.get(0).getValue(), stepsIdentifiers.get(0).getKey()));
                    step=true;
                    stepsIdentifiers.remove(0);
                    nextSpecificIntersection = getNextSpecificIntersection(requests, stepsIdentifiers.get(0));
                }
                //Check the orientation of the next segment
                if(hasChangedWay){
                    int orientation = compareOrientation(currentAngle, followingAngle);
                    //Add the intersection with the direction to follow
                    String nextSegmentName = nextSegment.getName();
                    if(nextSegmentName.equals("")) nextSegmentName = "road";
                    rows.add(createIntersection(currentIntersection, step, depot.getIntersection().getId(),nextSegmentName, orientation));
                }

                currentSegment = nextSegment;
                currentAngle = followingAngle;
                currentIntersection = nextIntersection;

            }
        }

        //Initialize the FileWriter to edit the .html file
        FileWriter fstream = new FileWriter(html.getAbsolutePath(), false);
        BufferedWriter out = new BufferedWriter(fstream);

        //Initialize the Pebble engine to load the .html templates
        PebbleEngine engine = new PebbleEngine.Builder().loader(new ClasspathLoader()).build();
        PebbleTemplate compiledTemplate = engine.getTemplate("templates/base.html.peb");

        //Contains all the information of the road map loaded in the template by Pebble
        Map<String, Object> context = new HashMap<>();

        // Calculate the arrival time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tour.getDepot().getDepartureTime());
        calendar.add(Calendar.SECOND, (int) tour.getTourDuration());
        Date arrivalTime = calendar.getTime();

        context.put("travels_time", getTime(tour.getTravelsDuration()));
        context.put("pickups_time", getTime(tour.getPickupsDuration()));
        context.put("deliveries_time", getTime(tour.getDeliveriesDuration()));
        context.put("length", tour.getLength());
        context.put("total_time", getTime(tour.getTourDuration()));
        context.put("arrival_time", arrivalTime );
        context.put("depot", tour.getDepot());
        context.put("rows", rows);

        //Pebble compiles the information in the template and copies it into a Writer to give the result
        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);
        String output = writer.toString();
        out.write(output);
        out.close();
    }

    /**
     * Allows to put a segment on a road  map used by pebble to compile the information in the template
     * @param segment the segment whose information we want to collect
     * @return the segment information usable by pebble
     */
    public static Map<String, Object> createSegment(Segment segment){
        Map<String, Object> buffer = new HashMap<>();
        buffer.put("type", "Segment");
        buffer.put("object",segment );
        return buffer;
    }

    /**
     * Allows to put a way on a road map used by pebble to compile the information in the template. Gather all the segments
     * that compose a single way.
     * @param way the way whose information we want to collect
     * @return the segment information usable by pebble
     */
    public static Map<String, Object> createWay(Way way){
        Map<String, Object> buffer = new HashMap<>();
        buffer.put("type", "Way");
        buffer.put("name",way.getName() );
        buffer.put("total_length", way.getTotalLength());
        return buffer;
    }

    /**
     * Allows to put an address in a map used by pebble to compile the information in the template
     * @param address the address whose information we want to collect
     * @param subtype type of the address : depot, pickup or delivery
     * @param requestID the id of the request associated with the address
     * @return the address information usable by pebble
     */
    public static Map<String, Object> createSpecificIntersection(Address address, String subtype, long requestID){
        Map<String, Object> buffer = new HashMap<>();
        buffer.put("type", "Address");
        buffer.put("request", requestID+1);
        buffer.put("subtype", subtype);
        buffer.put("object",address );
        return buffer;
    }

    /**
     * Allows to put an intersection in a map used by pebble to compile the information in the template
     * @param intersection the intersection whose information we want to collect
     * @param parcels true if the intersection is also a specific intersection with an Address related
     * @param idDepot the intersection id of the depot
     * @param segment_name the next segment name
     * @param orientation the next orientation to follow at the intersection
     * @return the intersection information usable by pebble
     */
    public static Map<String, Object> createIntersection(Intersection intersection, boolean parcels, Long idDepot, String segment_name, int orientation ){
        Map<String, Object> buffer = new HashMap<>();
        buffer.put("type", "Intersection");
        buffer.put("step", parcels);
        if(idDepot !=null && idDepot== intersection.getId()){
            buffer.put("finish", true);
        } else {
            buffer.put("segment_name",segment_name);
            buffer.put("orientation", orientation);
        }
        buffer.put("object",intersection );
        return buffer;
    }

    /**
     * Allows to compute the azimut of a segment
     * We use the geographical north
     * Inspired from <a> https://www.dcode.fr/azimut </a> method
     * The azimut is the angle between the direction on the earth and the north by using 2 GPS points
     * @param segment the segment that we want to know the azimut
     * @return the azimut of the segment
     */
    public static double getAngleFromNorth(Segment segment){
        Coordinates origin = segment.getOrigin().getCoordinates();
        Coordinates destination = segment.getDestination().getCoordinates();
        double lat1 = origin.getLatitude();
        double long1 = origin.getLongitude();
        double lat2 = destination.getLatitude();
        double long2 = destination.getLongitude();
        double x = Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(long2-long1);
        double y = Math.sin(long2 - long1)*Math.cos(lat2);
        return Math.toDegrees(Math.atan2( y, x ));
    }

    /**
     * Allow to compute the angle between two directions and give the orientation
     * The two angles are computed between the direction and the north
     * 0 Front
     * 1 right
     * 2 left
     * 3 back
     * @param angle1 the angle between the current direction and the north
     * @param angle2 the angle between the next direction and the north
     * @return an int corresponding to the next orientation to follow
     */
    public static int compareOrientation(double angle1, double angle2) {
        int res = 0;
        double diff = (angle1 - angle2);
        //diff goes from -180° to 180°
        //We use the horary direction
        if(diff < 35 && diff > -35) return 0;
        if(diff > 145 || diff < -145) return 3;
        if(diff>0) return 1;
        return 2;

    }

    /**
     * Allow to convert a time in seconds in H:min:s
     * @param second time in seconds
     * @return the time in H:m:s format as a String
     */
    public static String getTime(double second){
        int hour = (int) (second/3600);
        int min = (int) ((second-hour*3600)/60);
        int seconde_arround = (int) (second - min*60 - hour*3600);

        String time ="";
        if(hour>0){
            time+= hour+"H ";
        }
        if(min>0){
            time+= min+"min ";
        }
        if(seconde_arround>0)
        {
            time+= seconde_arround+"s ";
        }
        return time;
    }

    /**
     * Allows to get the next Address of the incoming step in the tour
     * @param requests requests list of the tour
     * @param step next step in the tour
     * @return the next specific address, a pickup, a delivery or null if its a depot
     */
    public static Address getNextSpecificIntersection(Map<Long, Request> requests, Pair<Long, String> step){
        Address result = null;
        Request request = requests.get(step.getKey());
        if(step.getValue().equals("pickup")){
            result = request.getPickup();
        } else if(step.getValue().equals("delivery")){
            result = request.getDelivery();
        }
        return result;
    }





}