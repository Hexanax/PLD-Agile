package fr.insalyon.pldagile.xml;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class XMLSerializer {
    public static void renderHTMLroadMap() throws IOException {
        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getTemplate("templates/base.html.peb");

        Map<String, Object> context = new HashMap<>();
        context.put("name", "Mitchell");

        /*Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);

        String output = writer.toString();
        System.out.println("test");
        System.out.println(output);*/
    }
}