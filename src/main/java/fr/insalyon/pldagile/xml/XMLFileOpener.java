package fr.insalyon.pldagile.xml;


import fr.insalyon.pldagile.view.Window;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import javax.swing.filechooser.*;

/**
 * This class allows to open an XML file in order to explore it
 * @author Mrs Solnon
 * This code is copied from the example of the 4IF course - Object Oriented Design and AGILE software development by Mrs Solnon
 * <a href="https://moodle.insa-lyon.fr/mod/resource/view.php?id=110978"> Placo source code </a>
 */
public class XMLFileOpener extends FileFilter {

    /**
     * instance of the object
     * It is a singleton
     */
    private static XMLFileOpener instance = null;

    /**
     * Create the singleton
     */
    private XMLFileOpener(){}

    /**
     * Allows to retrieve the instance of the XMLFileOpener class by creating the singleton if needed
     * @return the instance of the object
     */
    public static XMLFileOpener getInstance(){
        if (instance == null) instance = new XMLFileOpener();
        return instance;
    }

    /**
     * Opens a window for the user to choose, depending on the option in parameter, an xml file to read or an html file to save
     * @param fileChooseOption READ or SAVE depending of the action needed
     * @return the file selected
     */
    public File open(FileChooseOption fileChooseOption) {
        File selectedFile = null;
        //the window displayed to select the file
        FileChooser fileChooserXML = new FileChooser();
        Stage mainStage = Window.getMainStage();
        if (fileChooseOption == FileChooseOption.READ) {
            fileChooserXML.setTitle("Select a XML file to load");

            //READ only the .xml file
            fileChooserXML.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XML Files", "*.xml")
            );
            selectedFile = fileChooserXML.showOpenDialog(mainStage);
        } else if (fileChooseOption == FileChooseOption.SAVE) {
            fileChooserXML.setTitle("Save as");

            //SAVE only the .html file
            fileChooserXML.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("HTML Files", "*.html")
            );
            selectedFile = fileChooserXML.showSaveDialog(mainStage);
        }
        return selectedFile;
    }

    /**
     * Allows to now if a file in parameter is a .xml file or not
     * @param f a file of which we want to know if its an xml file
     * @return true if the file is a .xml, false otherwise
     */
    @Override
    public boolean accept(File f) {
        if (f == null) return false;
        if (f.isDirectory()) return false;
        String extension = getExtension(f);
        if (extension == null) return false;
        return extension.contentEquals("xml");
    }

    /**
     * Allows to get the description of file we use
     * @return All the file we use is a kind of xml file
     */
    @Override
    public String getDescription() {
        return "XML file";
    }

    /**
     * Allows to get the extension of the file in parameter
     * @param f a file of which we want to know the extension
     * @return the extension of the file
     */
    private String getExtension(File f) {
        String filename = f.getName();
        int i = filename.lastIndexOf('.');
        if (i>0 && i<filename.length()-1)
            return filename.substring(i+1).toLowerCase();
        return null;
    }
}
