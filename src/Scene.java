import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Scene {
    private TreeMap<String,Sprite> spriteMap;
    private ArrayList<String> characters;
    private ArrayList<String> instructions;
    private int line;
    private boolean[] visible;
    private String focusedSpeaker;

    private String title;
    private Background bg;

    private String nextScene;

    public Scene(String title, Background bg, TreeMap<String,Sprite> spriteMap, boolean[] visible, ArrayList<String> instructions, String next) {
        this.title = title;
        this.bg = bg;
        this.spriteMap = spriteMap;
        this.characters = new ArrayList<>();
        characters.addAll(spriteMap.keySet());
        this.instructions = instructions;
        this.visible = visible;
        this.nextScene = next;
        line = 0;
        focusedSpeaker = "";
    }

    public ArrayList<Renderable> next(Window w) {
        if(line == instructions.size()) {
            return null;
        }
        w.unfocusCharacter();
        ArrayList<Renderable> toRender = new ArrayList<>();
        toRender.add(bg);
        boolean display = false;
        while(!display) {
            if(line >= instructions.size()) break;
            String instruction = instructions.get(line).trim();
            if (instruction.contains(":")) {
                String speaker = instruction.substring(0, instruction.indexOf(":")).trim();
                String text = instruction.substring(instruction.indexOf(":") + 2);
                toRender.add(new TextBox(text, speaker));
                focusedSpeaker = speaker;
                display = true;
            } else if (instruction.contains("leaves")) {
                String character = instruction.substring(0, instruction.indexOf("leaves") - 1).trim();
                visible[characters.indexOf(character)] = false; //^^^Debug this if problems
            } else if (instruction.contains("appears")) {
                String character = instruction.substring(0, instruction.indexOf("appears") - 1).trim();
                visible[characters.indexOf(character)] = true;
            } else if (instruction.startsWith("Write")) {
                String fileName = instruction.split(" ")[1];
                try {
                    File f = new File(fileName);
                    if(!f.exists()) {
                        f.createNewFile();
                    }
                    FileWriter fw = new FileWriter(f);
                    fw.write(instruction.split(" ",3)[2]);
                    fw.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException aioobe) {

                }
            } else if (instruction.startsWith("Delete")) {
                File f = new File(instruction.split(" ")[1]);
                f.delete();
            } else if (instruction.equals("Save")) {
                File f = new File("save.txt");
                try {
                    if(!f.exists())
                        f.createNewFile();
                    FileWriter fw = new FileWriter(f);
                    fw.write(title + "\n" + line);
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            line++;
        }
        int numChars = 0;
        for(boolean b : visible) {
            if(b) numChars++;
        }
        for(int i = 0; i < characters.size(); ++i) {
            if(visible[i]){
                Sprite s = spriteMap.get(characters.get(i));
                s.x = (int)(Main.WIDTH*.1 + (Main.WIDTH*.8 / (numChars+1))*(i+1));
                toRender.add(1,s);
            }
        }
        return toRender;
    }

    public String getNextScene() {
        return nextScene;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return line == instructions.size();
    }

    public void setLine(int val) {
        line = val;
    }

    public int getLine() {
        return line;
    }
}
