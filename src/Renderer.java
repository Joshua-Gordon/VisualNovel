import com.sun.org.apache.regexp.internal.RE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Renderer {

    private ArrayList<Renderable> renderables;
    private int focused;
    private BufferedImage rendered;
    /*
    Renderables is very special. The first index is the background. The other indices are sprites.
    At the end is the text box.
     */

    public Renderer() {
        renderables = new ArrayList<>();
        renderables.add(null); //The first background
        renderables.add(TextBox.blankText); //The first text box
        rendered = new BufferedImage(Main.WIDTH,Main.HEIGHT,BufferedImage.TYPE_INT_ARGB);
        focused = -1;
    }

    public void addSprite(Sprite r) {
        if(!renderables.contains(r))
        renderables.add(1,r);
        draw();
    }

    public void clear() {
        renderables.clear();
    }

    public void changeBackground(Background b) {
        if(renderables.isEmpty()) {
            renderables.add(b);
        }else {

            renderables.set(0, b);
        }
        draw();
    }

    public void changeText(TextBox t) {
        if(renderables.get(renderables.size()-1) instanceof TextBox)
        renderables.remove(renderables.size()-1);
        renderables.add(t);
        draw();
    }

    public Sprite getSprite(Sprite s) {
        return (Sprite) renderables.get(renderables.indexOf(s)); //Sprite is not being found
                                                                 //Debug this
    }

    public Sprite getSprite(int i) {
        return (Sprite) renderables.get(i);
    }

    private void draw() {
        Graphics rg = rendered.getGraphics();
        rg.clearRect(0,0,Main.WIDTH,Main.HEIGHT);
        long time = System.currentTimeMillis();
        for(Renderable r : renderables) {
            if(r==null) {
                renderables.remove(r);
            } else {
                rg.drawImage(r.image(time), r.X(), r.Y(), null);
            }
        }
        rg.dispose();
    }

    public void renderChoice(Choice c) {
        rendered.getGraphics().drawImage(c.image(System.currentTimeMillis()),0,0,null);
    }

    public BufferedImage getRenderedImage() {
        return rendered;
    }
    public int getFocused() {
        return focused;
    }

    public ArrayList<Renderable> getRenderables() {
        return renderables;
    }
}
