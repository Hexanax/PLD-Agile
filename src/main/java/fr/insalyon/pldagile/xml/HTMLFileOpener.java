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
public class HTMLFileOpener extends FileFilter {

    //Singleton
    private static HTMLFileOpener instance = null;
    private HTMLFileOpener(){}
    public static HTMLFileOpener getInstance(){
        if (instance == null) instance = new HTMLFileOpener();
        return instance;
    }

    public File open(FileChooseOption fileChooseOption) {
        File selectedFile = null;
        FileChooser fileChooserXML = new FileChooser();
        fileChooserXML.setTitle("Select a HTML file to load");
        fileChooserXML.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("HTML Files", "*.html")
        );
        Stage mainStage = Window.getMainStage();
        if (fileChooseOption == FileChooseOption.READ) {
            selectedFile = fileChooserXML.showOpenDialog(mainStage);
        } else if (fileChooseOption == FileChooseOption.SAVE) {
            fileChooserXML.setTitle("Save as");
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
        return extension.contentEquals("html");
    }

    @Override
    public String getDescription() {
        return "HTML file";
    }

    private String getExtension(File f) {
        String filename = f.getName();
        int i = filename.lastIndexOf('.');
        if (i>0 && i<filename.length()-1)
            return filename.substring(i+1).toLowerCase();
        return null;
    }
}