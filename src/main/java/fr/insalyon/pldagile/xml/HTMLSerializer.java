package fr.insalyon.pldagile.xml;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import fr.insalyon.pldagile.model.*;
import javafx.util.Pair;
import org.apache.log4j.PropertyConfigurator;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HTMLSerializer {
    public static void renderHTMLroadMap(Tour tour, File html) throws IOException, TransformerConfigurationException {




        List<Segment> segments = tour.getPath();

        List<Pair<Long, String>> stepsIdentifiers = tour.getSteps();
        stepsIdentifiers.remove(0);



        List<Intersection> intersections = tour.getIntersections();

        Map<Long, Request> requests = tour.getRequests();


        Address nextSpecificIntersection = getNextSpecificIntersection(requests, stepsIdentifiers.get(0));

        List<Map<String,Object>> rows = new ArrayList<>();



        Depot depot = tour.getDepot();
        boolean stepindepot = false;
        while(nextSpecificIntersection!= null && intersections.get(0).getId() == nextSpecificIntersection.getIntersection().getId()){
            rows.add(createSpecificIntersection(nextSpecificIntersection,stepsIdentifiers.get(0).getValue()));
            stepindepot=true;
            stepsIdentifiers.remove(0);
            nextSpecificIntersection = getNextSpecificIntersection(requests, stepsIdentifiers.get(0));
        }
        if(stepindepot){
            rows.add(createIntersection(intersections.get(0), true, null, segments.get(0).getName(),-1));
        }

        intersections.remove(0);

        double currentAngle = getAngleFromNorth(segments.get(0));
        for(Intersection current : intersections){
            rows.add(createSegment(segments.get(0)));
            segments.remove(0);
            boolean step = false;
            while(nextSpecificIntersection!= null && current.getId() == nextSpecificIntersection.getIntersection().getId()){

                rows.add(createSpecificIntersection(nextSpecificIntersection,stepsIdentifiers.get(0).getValue()));
                step=true;
                stepsIdentifiers.remove(0);
                nextSpecificIntersection = getNextSpecificIntersection(requests, stepsIdentifiers.get(0));
            }

            if(segments.size()>0){
                double followingAngle = getAngleFromNorth(segments.get(0));
                int orientation = compareOrientation(currentAngle, followingAngle);
                rows.add(createIntersection(current, step, depot.getIntersection().getId(), segments.get(0).getName(), orientation));
                currentAngle = followingAngle;
            } else {
                rows.add(createIntersection(current, step, depot.getIntersection().getId(), "",-1));
            }

        }








        FileWriter fstream = new FileWriter(html.getAbsolutePath(), false);
        BufferedWriter out = new BufferedWriter(fstream);
        PebbleEngine engine = new PebbleEngine.Builder().loader(new ClasspathLoader()).build();
        PebbleTemplate compiledTemplate = engine.getTemplate("templates/base.html.peb");
        Map<String, Object> context = new HashMap<>();
        context.put("travels_time", getTime(tour.getTravelsDuration()));
        context.put("pickups_time", getTime(tour.getPickupsDuration()));
        context.put("deliveries_time", getTime(tour.getDeliveriesDuration()));
        context.put("lenght", tour.getLength());
        context.put("total_time", getTime(tour.getTourDuration()));
        context.put("depot", tour.getDepot());
        context.put("rows", rows);

        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);

        String output = writer.toString();
        out.write(output);
        out.close();

    }


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

    public static Map<String, Object> createSegment(Segment segment){
        Map<String, Object> buffer = new HashMap<>();
        buffer.put("type", "Segment");
        buffer.put("object",segment );
        return buffer;
    }

    public static Map<String, Object> createSpecificIntersection(Address address, String subtype){
        Map<String, Object> buffer = new HashMap<>();
        buffer.put("type", "Address");
        buffer.put("subtype", subtype);
        buffer.put("object",address );
        return buffer;
    }

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
     * https://www.dcode.fr/azimut
     *
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
     * 0 Front
     * 1 right
     * 2 left
     * 3 back
     * @param angle1
     * @param angle2
     * @return
     */
    public static int compareOrientation(double angle1, double angle2) {
        int res = 0;
        double diff = (angle1 - angle2);

        if(diff < 35 && diff > -35) return 0;
        if(diff > 145 || diff < -145) return 3;
        if(diff>0) return 1;
        return 2;

    }

    public static String getTime(double seconde){
        int hour = (int) (seconde/3600);
        int min = (int) ((seconde-hour*3600)/60);
        int seconde_arround = (int) (seconde - min*60 - hour*3600);

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





}