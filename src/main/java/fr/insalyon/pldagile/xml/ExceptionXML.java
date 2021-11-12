package fr.insalyon.pldagile.xml;

/**
 * This class allows rising exception if the reading of an XML file goes wrong
 * @author Mrs Solnon
 * This code is copied from the example of the 4IF course - Object Oriented Design and AGILE software development by Mrs Solnon
 * <a href="https://moodle.insa-lyon.fr/mod/resource/view.php?id=110978"> Placo source code </a>
 */
public class ExceptionXML extends Exception {

    /**
     * version number
     */
    private static final long serialVersionUID = 1L;

    public static String FILE_NULL_ERROR = "Error file is null";
    public static String FILE_WRONG_FORMAT = "Wrong format";
    public static String INCORRECT_DESTINATION_ID = "Error when reading file: Wrong destination ID";
    public static String UNKNOWN_INTERSECTION = "Error when reading file: An intersection is unknown";
    public static String WRONG_ORIGIN_ID = "Error when reading file: Wrong origin ID";
    public static String WRONG_LENGTH = "Error when reading file: Wrong length";
    public static String INVALID_ID = "Error when reading file: id must be null or positive";
    public static String UNDEFINED_DEPOT = "Error when reading file : Depot is undefined";
    public static String PICKUP_SAME_AS_DELIVERY = "Error when reading file: pickup address can't be at the same place as delivery address";
    public static String PICKUP_CITY_MAP_MISMATCH = "Error when reading file: The pickup address doesn't match with the current city map";
    public static String DELIVERY_CITY_MAP_MISMATCH = "Error when reading file: The delivery address doesn't match with the current city map";
    public static String DEPOT_CITY_MAP_MISMATCH = "Error when reading file : The depot address doesn't match with the current city map";
    public static String NEGATIVE_PICKUP_DURATION = "Error when reading file: The pickup duration can't be negative";
    public static String NEGATIVE_DELIVERY_DURATION = "Error when reading file: The delivery duration can't be negative";
    public static String WRONG_DEPARTURE_TIME_FORMAT = "Error when reading file : Wrong departureTime format";

    /**
     * Constructor use to create a xml exeption with a specific message
     * @param message the error message that is throwed
     */
    public ExceptionXML(String message) {
        super(message);
    }

}
