package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.tsp.Dijkstra;
import javafx.util.Pair;

import java.util.*;

public class TourBuilderV2 {

    private static SimulatedAnnealing simulatedAnnealing;

    public Tour buildTour(PlanningRequest planningRequest, CityMap cityMap) {

        //List of ordered intersections to visit during the tour
        List<Long> tourIntersections = new ArrayList<>();
        CityMapGraph cityMapGraph = new CityMapGraph(cityMap);

        //SimulatedAnnealing runs on the planningRequest applied to the graph to find an optimized tour
        simulatedAnnealing = new SimulatedAnnealing(planningRequest,cityMapGraph);

        Map<Long,Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();

        //ArrayList of ordered **STEPS** to visit, computed by the simulatedAnnealing algorithm.
        ArrayList<Long> intersectionSteps = new ArrayList<>(simulatedAnnealing.getStepsIntersectionId());

        //Iterate over all intersectionSteps to compute the full tour, with intermediary intersections
        //in the _tourIntersections_ List

        Long previousIntersection = intersectionSteps.get(0);
        for (Long destinationId : intersectionSteps.subList(1,intersectionSteps.size())) {

            Dijkstra bestPathsFromOrigin = bestPaths.get(previousIntersection);
            List<Long> localTravel = bestPathsFromOrigin.getShortestPath(destinationId);

            //Don't add the last intersection to the tourIntersections, because it will
            //Be added as the first intersection of the next travel
            tourIntersections.addAll(localTravel.subList(0,localTravel.size()-1));
            previousIntersection = destinationId;
        }
        //We have to manually add the depot intersection to the end of the list
        tourIntersections.add(intersectionSteps.get(0));





        Tour tour = new Tour(planningRequest.getRequests(),planningRequest.getDepot());
        Map<Long, Intersection> intersections = cityMap.getIntersections();
        Map<Pair<Long, Long>, Segment> segments = cityMap.getSegments();
        Long previous = tourIntersections.get(0);
        tour.addIntersection(intersections.get(previous));
        tourIntersections.remove(0);
        for(Long idIntersection : tourIntersections){
            Long current = idIntersection;
            Segment currentSegment = segments.get(new Pair<>(previous,current));
            tour.addSegment(currentSegment);
            previous = current;
            tour.addIntersection(intersections.get(previous));
        }

        for(Request request : planningRequest.getRequests()){
            tour.addPickupTime(request.getPickup().getDuration());
            tour.addDeliveryTime(request.getDelivery().getDuration());
        }
        tour.setStepsIdentifiers(simulatedAnnealing.getStepsIdentifiers());

        return tour;



    }


    //TODO enhance
    //TODO replace dijskra
    //TODO test
    public Tour deleteRequest(CityMap cityMap, Tour tour, Request request){

        Map<Long, Request> requests = tour.getRequests();
        Depot depot = tour.getDepot();
        Map<Long, Intersection> intersectionsMap = cityMap.getIntersections();
        Map<Pair<Long, Long>, Segment> segments = cityMap.getSegments();


        int [] indexAroundStep = new int[4];

        List<Intersection> intersections = tour.getIntersections();
        List<Intersection> newIntersections = new ArrayList<>();
        List<Pair<Long,String>> steps =new ArrayList<Pair<Long,String>>(tour.getSteps());
        steps.remove(0);

        boolean found = false;
        boolean pickup = false;
        boolean delivery = false;
        int lastFoundIndex =0;
        int index = 1;
        long nextSpecificIntersection = getValueOfNextIntersection(depot, requests, steps.get(0));
        while(!found){
            if(intersections.get(index).getId()==nextSpecificIntersection){
                if(Objects.equals(steps.get(0).getKey(), request.getId())){
                    if(steps.get(0).getValue()=="pickup"){
                        indexAroundStep[0] = lastFoundIndex;
                        pickup = true;
                    } else {
                        indexAroundStep[2] = lastFoundIndex;
                        delivery = true;
                    }
                } else {
                    lastFoundIndex = index;
                    if(pickup){
                        indexAroundStep[1] = lastFoundIndex;
                    }
                    if(delivery){
                        indexAroundStep[3] = lastFoundIndex;
                        found =true;
                    }
                }
                steps.remove(0);
                if(!found) {
                    nextSpecificIntersection = getValueOfNextIntersection(depot, requests, steps.get(0));
                }

            }
            index ++;

        }






        tour.getSteps().removeIf(step -> Objects.equals(step.getKey(), request.getId()));

        index = 0;
        while(index != indexAroundStep[0]){
            newIntersections.add(intersections.get(index));
            index ++;
        }

        if(indexAroundStep[0]==indexAroundStep[2]){
            Map<Long, Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();
            Dijkstra dijkstra = bestPaths.get(intersections.get(indexAroundStep[0]).getId());

            for(long idIntersection : dijkstra.getShortestPath(intersections.get(indexAroundStep[1]).getId())){
                newIntersections.add(intersectionsMap.get(idIntersection));
            }
        } else if(indexAroundStep[1]==indexAroundStep[2]){
            Map<Long, Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();
            Dijkstra dijkstra = bestPaths.get(intersections.get(indexAroundStep[0]).getId());

            for(long idIntersection : dijkstra.getShortestPath(intersections.get(indexAroundStep[1]).getId())){
                newIntersections.add(intersectionsMap.get(idIntersection));
            }

            newIntersections.remove(newIntersections.size()-1);


            dijkstra = bestPaths.get(intersections.get(indexAroundStep[2]).getId());
            for(long idIntersection : dijkstra.getShortestPath(intersections.get(indexAroundStep[3]).getId())){
                newIntersections.add(intersectionsMap.get(idIntersection));
            }
        } else {
            Map<Long, Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();
            Dijkstra dijkstra = bestPaths.get(intersections.get(indexAroundStep[0]).getId());

            for(long idIntersection : dijkstra.getShortestPath(intersections.get(indexAroundStep[1]).getId())){
                newIntersections.add(intersectionsMap.get(idIntersection));
            }

            index = indexAroundStep[1]+1;
            while(index != indexAroundStep[2]){
                newIntersections.add(intersections.get(index));
                index ++;
            }

            dijkstra = bestPaths.get(intersections.get(indexAroundStep[2]).getId());
            for(long idIntersection : dijkstra.getShortestPath(intersections.get(indexAroundStep[3]).getId())){
                newIntersections.add(intersectionsMap.get(idIntersection));
            }

        }

        index = indexAroundStep[3]+1;
        while(index != intersections.size()){
            newIntersections.add(intersections.get(index));
            index ++;
        }




        tour.setIntersections(newIntersections);
        tour.reset();



        long previous = newIntersections.get(0).getId();
        newIntersections.remove(0);
        for(Intersection intersection : newIntersections){
            Long current = intersection.getId();
            Segment currentSegment = segments.get(new Pair<>(previous,current));
            tour.addSegment(currentSegment);
            previous = current;
        }

        tour.getRequests().remove(request.getId());

        for (Map.Entry<Long,Request> m : tour.getRequests().entrySet()) {
            tour.addPickupTime(m.getValue().getPickup().getDuration());
            tour.addDeliveryTime(m.getValue().getDelivery().getDuration());
        }


        return tour;
    }


    //TODO refactor
    //TODO test
    //TODO enhance
    public Tour addRequest(CityMap cityMap, Tour tour, Pair<Integer,Pickup> pickup, Pair<Integer,Delivery> delivery) {
        //Create and add the new request
        Request newRequest = new Request(pickup.getValue(), delivery.getValue());
        newRequest.setId(tour.getNextRequestId());
        tour.addRequest(newRequest);
        Map<Long,Request> requests = tour.getRequests();

        //Rebuild the tour
        Map<Long, Intersection> intersectionsMap = cityMap.getIntersections();
        Depot depot = tour.getDepot();
        List<Intersection> intersections = tour.getIntersections();
        List<Intersection> newIntersections = new ArrayList<>();
        List<Pair<Long,String>> steps =new ArrayList<Pair<Long,String>>(tour.getSteps());

        ArrayList<Pair<Long,String>> newSteps = new ArrayList<>();

        simulatedAnnealing.addBestPath(pickup.getValue().getIntersection().getId());
        simulatedAnnealing.addBestPath(delivery.getValue().getIntersection().getId());
        Map<Long, Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();

        int indexStep =0;
        int indexIntersection = 0;
        boolean add= true;
        boolean complete = false;
        for(Pair<Long,String> step : tour.getSteps()) {
            newSteps.add(step);
            long nextSpecificIntersection = getValueOfNextIntersection(depot, requests, steps.get(indexStep));
            while(intersections.get(indexIntersection).getId()!=nextSpecificIntersection){
                if(add){
                    newIntersections.add(intersections.get(indexIntersection));
                }
                indexIntersection++;
            }
            if(indexStep == pickup.getKey()){
                newSteps.add(new Pair(newRequest.getId(),"pickup"));

                long afterPickupAction = getValueOfNextIntersection(depot, requests, steps.get(indexStep+1));

                Dijkstra dijkstra = bestPaths.get(nextSpecificIntersection);

                for(long idIntersection : dijkstra.getShortestPath(pickup.getValue().getIntersection().getId())){
                    newIntersections.add(intersectionsMap.get(idIntersection));
                }

                newIntersections.remove(newIntersections.size()-1);

                long idIntersectionRelay = pickup.getValue().getIntersection().getId();
                if(Objects.equals(pickup.getKey(), delivery.getKey())){
                    complete = true;
                    newSteps.add(new Pair(newRequest.getId(),"delivery"));
                    dijkstra = bestPaths.get(idIntersectionRelay);

                    for(long idIntersection : dijkstra.getShortestPath(delivery.getValue().getIntersection().getId())){
                        newIntersections.add(intersectionsMap.get(idIntersection));
                    }

                    newIntersections.remove(newIntersections.size()-1);
                    idIntersectionRelay = delivery.getValue().getIntersection().getId();

                }


                dijkstra = bestPaths.get(idIntersectionRelay);

                for(long idIntersection : dijkstra.getShortestPath(afterPickupAction)){
                    newIntersections.add(intersectionsMap.get(idIntersection));
                }
                newIntersections.remove(newIntersections.size()-1);
                add=false;


            }
            else if(indexStep == delivery.getKey() && !complete){
                newSteps.add(new Pair(newRequest.getId(),"delivery"));

                long afterPickupAction = getValueOfNextIntersection(depot, requests, steps.get(indexStep+1));

                Dijkstra dijkstra = bestPaths.get(nextSpecificIntersection);

                for(long idIntersection : dijkstra.getShortestPath(delivery.getValue().getIntersection().getId())){
                    newIntersections.add(intersectionsMap.get(idIntersection));
                }
                newIntersections.remove(newIntersections.size()-1);

                dijkstra = bestPaths.get(delivery.getValue().getIntersection().getId());

                for(long idIntersection : dijkstra.getShortestPath(afterPickupAction)){
                    newIntersections.add(intersectionsMap.get(idIntersection));
                }
                newIntersections.remove(newIntersections.size()-1);


                add=false;
            } else {
                add=true;
            }
            indexStep++;

        }

        if(newIntersections.get(newIntersections.size()-1).getId()!=depot.getIntersection().getId()){
            newIntersections.add(depot.getIntersection());
        }



        tour.setIntersections(newIntersections);
        tour.setStepsIdentifiers(newSteps);
        tour.reset();


        Map<Pair<Long, Long>, Segment> segments = cityMap.getSegments();

        long previous = newIntersections.get(0).getId();
        newIntersections.remove(0);
        for(Intersection intersection : newIntersections){
            Long current = intersection.getId();
            Segment currentSegment = segments.get(new Pair<>(previous,current));
            tour.addSegment(currentSegment);
            previous = current;
        }


        for (Map.Entry<Long,Request> m : tour.getRequests().entrySet()) {
            tour.addPickupTime(m.getValue().getPickup().getDuration());
            tour.addDeliveryTime(m.getValue().getDelivery().getDuration());
        }







        return tour;
    }



    private long getValueOfNextIntersection(Depot depot, Map<Long, Request> requests, Pair<Long, String> step)
    {

        if(step.getValue()=="pickup"){
            return requests.get(step.getKey()).getPickup().getIntersection().getId();
        }
        if(step.getValue()=="delivery")
        {
            return requests.get(step.getKey()).getDelivery().getIntersection().getId();
        }
        return depot.getIntersection().getId();
    }

    private Tour computeTour(Tour tour, List<Intersection> intersections){

        return tour;
    }











}