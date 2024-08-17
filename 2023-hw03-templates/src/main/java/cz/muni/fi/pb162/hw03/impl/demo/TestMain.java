package cz.muni.fi.pb162.hw03.impl.demo;

import cz.muni.fi.pb162.hw03.impl.MyFSTemplateEngine;
import cz.muni.fi.pb162.hw03.impl.model.MapModel;
import cz.muni.fi.pb162.hw03.template.FSTemplateEngine;
import cz.muni.fi.pb162.hw03.template.model.TemplateModel;

import java.util.HashMap;
import java.util.List;

/**
 * @author Jan Maly
 * to test how the things work
 */
public class TestMain {
    /**
     * testing method
     * @param args not used
     */
    public static void main(String[] args) {
        TemplateModel database = new MapModel(new HashMap<>());
        database.put("já", "Honza");
        database.put("cat", "Tom");
        database.put("mouse", "Jerry");
        database.put("yes", true);
        database.put("no", false);
        database.put("name", "Jan");
        database.put("surname", "Malý");
        database.put("names", List.of("Petr", "Pavel", "Šimon"));

        FSTemplateEngine templateEngine = new MyFSTemplateEngine();
        String text =  "Can we do inline with if only? {{ #if yes }} Yes we can!{{ #done }}\n" +
                "\n" +
                "Can we do inline with else? {{ #if no }} Yes we can! {{ #else }} Sure we can!{{ #done }}\n" +
                "\n" +
                "\n" +
                "BTW: The extra empty line is there on purpose. The next whitespace character " +
                "following a block command (those with #) is considered part of that command.\n" +
                "\n" +
                "We can also do multiple lines.\n" +
                "{{ #if yes }}\n" +
                "Tom is great.\n" +
                "{{ #else }}\n" +
                "Jerry is better.\n" +
                "{{ #done }}\n" +
                "Now this will be right under.";
        templateEngine.loadTemplate("test", text);
        System.out.println(templateEngine.evaluateTemplate("test", database));
    }
}
