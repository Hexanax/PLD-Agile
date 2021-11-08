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

    /**
     * Constructor use to create a xml exeption with a specific message
     * @param message the error message that is throwed
     */
    public ExceptionXML(String message) {
        super(message);
    }

}
