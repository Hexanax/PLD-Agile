package fr.insalyon.pldagile.xml;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.apache.log4j.PropertyConfigurator;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class XMLSerializer {
    public static void renderHTMLroadMap() throws IOException, TransformerConfigurationException {
        File html = HTMLFileOpener.getInstance().open(FileChooseOption.SAVE);
        FileWriter fstream = new FileWriter(html.getAbsolutePath(), true);
        BufferedWriter out = new BufferedWriter(fstream);



        PebbleEngine engine = new PebbleEngine.Builder().loader(new ClasspathLoader()).build();


        PebbleTemplate compiledTemplate = engine.getTemplate("templates/base.html.peb");

        Map<String, Object> context = new HashMap<>();
        context.put("name", "Mitchell");

        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);

        String output = writer.toString();
        out.write(output);
        out.close();

    }
}