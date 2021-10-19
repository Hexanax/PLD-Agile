package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.PickyApplication;
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

    //Singleton
    private static XMLFileOpener instance = null;
    private XMLFileOpener(){}
    public static XMLFileOpener getInstance(){
        if (instance == null) instance = new XMLFileOpener();
        return instance;
    }

    public File open(FileChooseOption fileChooseOption) {
        File selectedFile = null;
        FileChooser fileChooserXML = new FileChooser();
        fileChooserXML.setTitle("Select a XML file to load");
        fileChooserXML.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        Stage mainStage = PickyApplication.getMainStage();
        if (fileChooseOption == FileChooseOption.READ) {
            selectedFile = fileChooserXML.showOpenDialog(mainStage);
        } else if (fileChooseOption == FileChooseOption.SAVE) {
            selectedFile = fileChooserXML.showSaveDialog(null);
        }
        return selectedFile;
    }

    @Override
    public boolean accept(File f) {
        if (f == null) return false;
        if (f.isDirectory()) return true;
        String extension = getExtension(f);
        if (extension == null) return false;
        return extension.contentEquals("xml");
    }

    @Override
    public String getDescription() {
        return "XML file";
    }

    private String getExtension(File f) {
        String filename = f.getName();
        int i = filename.lastIndexOf('.');
        if (i>0 && i<filename.length()-1)
            return filename.substring(i+1).toLowerCase();
        return null;
    }
}
