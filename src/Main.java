import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    static final int WIDTH = 2000;
    static final int HEIGHT = 1000;

    static final Font GAME_FONT = new Font("Comic Sans",Font.PLAIN,28);


    static String RES = "res\\";


    static String SCRIPT = "testScript1.xml";

    public static void main(String[] args) {
        System.out.println("Hello!");

        if(Main.class.getResource("Main.class").toString().startsWith("jar")) {
            System.out.println("Path to resource folder:");
            RES = new Scanner(System.in).nextLine() + "\\";
        }


        SCRIPT = RES + SCRIPT;

        if(!loadGame())
            playGame(null);
    }

    public static void test1() {
        System.out.println("Hello world!");

        Window w = new Window("Doki Doki Robotics Club");

        try {
            BufferedImage testImage = ImageIO.read(new File("res\\clarkson_bg.jpg"));
            w.addImage(new Background(testImage));

            BufferedImage josh = ImageIO.read(new File("res\\Josh.PNG"));
            BufferedImage hal = ImageIO.read(new File("res\\Hal.PNG"));

            Sprite js = new Sprite(josh, 200, 400);
            Sprite hs = new Sprite(hal, 800,400);

            w.addImage(js);
            w.addImage(hs);

            TextBox tb = new TextBox("Hey there, this is a test of the text box functionality! Hey there, this is a test of the text box functionality! Hey there, this is a test of the text box functionality! Hey there, this is a test of the text box functionality! Hey there, this is a test of the text box functionality! Hey there, this is a test of the text box functionality! Hey there, this is a test of the text box functionality!","Josh");
            w.addImage(tb);

        } catch (IOException e) {
            e.printStackTrace();
        }

        w.begin();
        while (true) { //Game loop bitches
            w.render();
            System.out.println("Hi");
        }
    }

    public static void playScene(Scene scene, Window w, ScriptParser sp) {
        while(true) {
            w.setClicked(false);
            List<Renderable> sceneRender = scene.next(w);
            if(sceneRender == null) {
                return;
            } else {
                w.clear();
                for (Renderable r : sceneRender) {
                    w.addImage(r);
                }
                w.render();
            }
            while(!w.getClicked());
        }
    }

    public static void playGame(Scene start) {
        ScriptParser sp = new ScriptParser(SCRIPT);
        try {
            sp.extractScenes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Window w = new Window("Tests Yo");
        Scene scene;
        if(start == null)
            scene = sp.scenes.get(0);
        else
            scene = start;
        w.begin();
        while(true) {
            playScene(scene, w, sp);
            System.out.println(scene.getTitle());
            String nextScene;
            if(scene.getNextScene().equals("END")) {
                break;
            } else if(scene.getNextScene().contains(":")) {
                nextScene = w.getNextScene(scene.getNextScene());
            } else {
                nextScene = scene.getNextScene();
            }
            for (Scene s : sp.scenes) {
                if (s.getTitle().equals(nextScene)) {
                    scene = s;
                }
            }
        }
        System.out.println("You have been terminated.");
    }

    public static boolean loadGame() {
        File f = new File("save.txt");
        if(!f.exists()){
            System.err.println("No save file!");
            return false;
        }
        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String title = br.readLine();
            int line = Integer.parseInt(br.readLine());


            ScriptParser sp = new ScriptParser(SCRIPT);
            try {
                sp.extractScenes();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene start = null;

            for(Scene s : sp.scenes) {
                if(s.getTitle().equals(title)) {
                    start = s;
                }
            }

            if(start == null) {
                System.err.println("BADLY FORMATTED SAVE FILE");
            }

            start.setLine(line);
            playGame(start);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
