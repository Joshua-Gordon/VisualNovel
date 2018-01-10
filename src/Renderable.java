import java.awt.image.BufferedImage;

public interface Renderable {

    default int X(){
        return 0;
    }   //0 is for background images and CG
    default int Y(){
        return 0;
    }
    BufferedImage image(long time); //time is for animations in the future

    default int layer() {
        return 0;
    }


}
