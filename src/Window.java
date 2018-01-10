import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Window {

    public int w, h;

    public JFrame frame;
    private JLabel label;

    private Renderer r;
    private boolean rerender;

    private boolean clicked;
    public MouseEvent click;

    public Window(String title) {


        this.w = Main.WIDTH;
        this.h = Main.HEIGHT;
        frame = new JFrame(title);
        frame.setSize(w,h);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        label = new JLabel();

        frame.add(label);

        r = new Renderer();
        rerender = true;

        clicked = false;
        click = null;

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setClicked(true);
                click = e;
            }
        });
    }


    public void begin() {
        frame.setVisible(true);
    }

    public void addImage(Renderable re) {
        rerender = true;
        if(re instanceof Background) {
            r.changeBackground((Background) re);
        } else if(re instanceof Sprite) {
            r.addSprite((Sprite) re);
        } else if(re instanceof TextBox) {
            r.changeText((TextBox) re);
        }
    }

    public void clear() {
        r.clear();
    }

    public void focusCharacter(Sprite s) {
        r.getSprite(s).y+=10;
    }

    public void unfocusCharacter() {
        if(r.getFocused()!=-1)
        r.getSprite(r.getFocused()).y -= 10;
    }

    public void render() {
        if(rerender) {
            label.setIcon(new ImageIcon(r.getRenderedImage()));
            rerender = false;
        }
    }

    public boolean getClicked() {
        return clicked;
    }

    public void setClicked(boolean val) {
        clicked = val;
    }

    public String getNextScene(String choiceMap) {
        String[] lines = choiceMap.split("\n");

        String[] choices = new String[lines.length-2];
        String[] scenes = new String[lines.length-2];

        for(int i = 1; i < lines.length-1; ++i) { //Bounds take off leading and trailing newline
            String[] data = lines[i].trim().split(":");
            choices[i-1] = data[0]; //-1 to handle leading null value
            scenes[i-1] = data[1];
        }
        Choice c = new Choice(choices);
        r.renderChoice(c);
        label.setIcon(new ImageIcon(r.getRenderedImage()));
        return scenes[c.waitForChoice(this)];
    }

    public void playMusic(String soundFile) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(new File(soundFile).getAbsoluteFile());
                    Clip c = AudioSystem.getClip();
                    c.open(ais);
                    c.start();
                    Thread.sleep(c.getMicrosecondLength()*1000);
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

    }
}
