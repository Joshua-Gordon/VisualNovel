import java.awt.image.BufferedImage;

public class Background implements Renderable {

    private BufferedImage bi;

    public Background(BufferedImage bi) {
        this.bi = bi;
    }

    @Override
    public BufferedImage image(long time) {
        return bi;
    }
}
