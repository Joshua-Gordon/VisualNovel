import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite implements Renderable{

    static final int DEFAULT_HEIGHT = 400;

    BufferedImage image;
    int x, y;
    int layer;

    public Sprite(BufferedImage image, int x, int y) {
        layer = 1; //Default for sprites
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public Sprite(String imagePath, int x, int y) {
        layer = 1;
        this.x = x;
        this.y = y;
        try {
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int X() {
        return x;
    }

    @Override
    public int Y() {
        return y;
    }

    @Override
    public BufferedImage image(long time) {
        return image;
    }

    @Override
    public int layer() {
        return layer;
    }
}
