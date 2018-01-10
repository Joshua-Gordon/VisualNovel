import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class ScriptParser {

    private String filePath;
    Document script;

    ArrayList<Scene> scenes;

    public ScriptParser(String fp) {
        this.filePath = fp;
        try {
            script = loadDoc();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            System.err.println("Problems with script parser!");
        }
        scenes = new ArrayList<>();
    }

    public Document loadDoc() throws FileNotFoundException{
        SAXBuilder sb = new SAXBuilder();
        try {
            return sb.build(new File(filePath));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println("SCRIPT IS NULL. CHECK PARSER");
        return null;
    }

    public void extractScenes() throws IOException {
        Element root = script.getRootElement();

        for(Element e : root.getChildren()) {
            //TODO: Make scenes
            ArrayList<Element> sceneElements = new ArrayList<>(e.getChildren());
            String title = sceneElements.remove(0).getText();
            Background bg = new Background(ImageIO.read(new File(Main.RES+sceneElements.remove(0).getText()+".jpg")));
            //Get characters
            TreeMap<String,Sprite> spriteMap = new TreeMap<>();
            do {
                Element charE = sceneElements.remove(0);
                String name = charE.getText();
                Sprite s = new Sprite(Main.RES+name+".PNG",0,Sprite.DEFAULT_HEIGHT);
                spriteMap.put(name,s);
            } while(sceneElements.get(0).getName()=="character");

            //Characters are done. Now for dialogue


            String dialogue = sceneElements.remove(0).getText();
            ArrayList<String> instructions = new ArrayList<>();
            for(String s : dialogue.split("\n")) {
                if(s.trim()!="")
                    instructions.add(s);
            }
            //Instructions are done

            //Starting visibility isn't a feature yet.

            boolean[] visible = new boolean[spriteMap.size()];
            for(int i = 0; i < visible.length; ++i) {
                visible[i] = true;
            }

            //Handle the next scene

            String next = sceneElements.remove(0).getText();

            Scene s = new Scene(title,bg,spriteMap,visible,instructions,next);
            scenes.add(s);
        }
    }


}
