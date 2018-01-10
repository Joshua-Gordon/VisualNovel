import java.awt.*;
import java.awt.image.BufferedImage;

public class TextBox implements Renderable{

    Font f;

    String text;
    BufferedImage box;
    String speaker;

    public static TextBox blankText = new TextBox("","");

    public TextBox(String t, String speaker) {
        this.text = t;
        box = new BufferedImage((int)(Main.WIDTH*.8),(int)(Main.HEIGHT*.3),BufferedImage.TYPE_INT_ARGB);
        this.speaker = speaker;

        f = Main.GAME_FONT;
    }

    public static String wrapText(String s, FontMetrics fm) {
        int pixelWidth = (int) (Main.WIDTH*.8);
        int length = pixelWidth / fm.charWidth('q');
        if(fm.stringWidth(s) < pixelWidth) return s;
        int spaceIndex = Math.max(Math.max(s.lastIndexOf(" ",length),s.lastIndexOf("\t",length)),s.lastIndexOf("-",length));
        if(spaceIndex == -1) spaceIndex = length;
        System.out.println(spaceIndex);
        return s.substring(0,spaceIndex).trim() + "\n"+wrapText(s.substring(spaceIndex),fm);
    }

    @Override
    public int X() {
        return (int) (Main.WIDTH*.15);
    }

    @Override
    public int Y() {
        return (int) (Main.HEIGHT*.8);
    }

    @Override
    public BufferedImage image(long time) {
        Graphics bg = box.getGraphics();
        bg.setFont(f);
        bg.setColor(Color.BLACK);
        //Draw the nameplate
        FontMetrics fm = bg.getFontMetrics();
        text = wrapText(text,fm);
        int namePlateWidth = fm.stringWidth(speaker);
        bg.fillRect(0,0,namePlateWidth+20,fm.getHeight()+10);
        bg.setColor(Color.WHITE);
        bg.drawString(speaker,10,5 + fm.getHeight());
        //Draw the box
        int lineCount = text.split("\n").length;
        bg.setColor(Color.BLACK);
        bg.fillRect(0,fm.getHeight()+10,(int)(Main.WIDTH*.58),(fm.getHeight()+2)*lineCount);
        bg.setColor(Color.WHITE);
        int place = 2;
        for(String line : text.split("\n")) {
            bg.drawString(line,10,(fm.getHeight()+2)*place+2);
            place++;
        }
        //Finished
        bg.dispose();
        return box;
    }

    @Override
    public int layer() {
        return 0;
    }
}
