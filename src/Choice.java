import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Choice implements Renderable{

    String[] choices;
    BufferedImage render;
    FontMetrics fm;

    private int width;
    private int height;
    private int X;
    private int Y;

    public Choice(String[] choices) {
        this.choices = choices;
        render = new BufferedImage(Main.WIDTH,Main.HEIGHT,BufferedImage.TYPE_INT_ARGB);


        Graphics g = render.getGraphics();
        g.setFont(Main.GAME_FONT);
        fm = g.getFontMetrics();

        for(String s : choices) {
            s = TextBox.wrapText(s,fm);
        }

        for(String s : choices) {
            if(s.contains("\n")) {
                width = Math.max(width, s.indexOf('\n')); //Length of string until wrap
            } else {
                width = Math.max(width,s.length());
            }
        }

        for(String s : choices) {
            int count = 1;
            for(char c : s.toCharArray()) {
                if(c == '\n') count++;
            }
            height = Math.max(height,fm.getHeight()*count);
        }

        X = (Main.WIDTH-width*fm.charWidth('q'))/2;
        Y = (Main.HEIGHT - (height+20)*choices.length)/2;
    }

    @Override
    public int X() {
        return X;
    }

    @Override
    public int Y() {
        return Y;
    }

    @Override
    public BufferedImage image(long time) {
        Graphics g = render.getGraphics();
        g.setFont(Main.GAME_FONT);
        System.out.println("Rendering the choice boxes!");
        for(int i = 0; i < choices.length; ++i) {
            g.setColor(Color.BLACK);
            g.fillRect(X,Y + i*(height),width*fm.charWidth('q'),height);
            System.out.println(X + " " + Y);
            g.setColor(Color.WHITE);
            g.drawString(choices[i],X,Y + (i+1)*(height));
        }

        return render;
        /*try {
            return ImageIO.read(new File("res\\scenter.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    @Override
    public int layer() {
        return 2;
    }

    public int waitForChoice(Window w) {
        while (!w.getClicked());
        w.setClicked(false);
        int x = w.click.getX();
        int y = w.click.getY();

        System.out.println(X + width*fm.charWidth('q'));
        System.out.println(Y + height*choices.length);

        if(x > X && x < X + width*fm.charWidth('q') && y > Y && y < Y + height*choices.length){
            int count = y-Y;
            for(int i = 0; i < choices.length; ++i) {
                count -= height;
                if(count <= 0) return i;
            }
            System.err.println("Click did not register choice!");
        }
        return waitForChoice(w);
    }
}
