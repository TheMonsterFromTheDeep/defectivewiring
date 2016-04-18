package alfredo.paint;

import alfredo.util.Resources;
import java.awt.image.BufferedImage;

/**
 * An Image is a graphical object that can be drawn onscreen that represents a single image.
 */
public class Image {
    public BufferedImage image;
    
    /**
     * Loads the Image from the specified path into the out object specified. If the Image cannot
     * be loaded from the path, this will create a small 1x1 dummy image and return false.
     * 
     * If the image was loaded successfully, it will return true.
     * 
     * If the path specified is null or the out Image specified is null, the image will not be loaded
     * and false will be returned.
     * 
     * This will overwrite any image that is currently stored in out.
     * @param path The path to load the Image from.
     * @param out The Image object to store the loaded image in.
     * @return Whether the Image was loaded successfully.
     */
    public static boolean load(String path, Image out) {
        if(path == null || out == null) { return false; }
        out.image = Resources.loadImage(path); //Try loading image
        if(out.image == null) {
            out.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); //If image is null (it failed to load), create dummy image and use that
            return false;
        }
        return true;
    }
    
    /**
     * Loads the Image from the specified path.
     * 
     * If the path specified is null or the out Image specified is null, the image will not be loaded
     * and a dummy image will be returned.
     * @param path The path to load the Image from.
     * @return Image data that will represent the image at the path or a dummy image if an exception occurs.
     */
    public static Image load(String path) {
        Image out = new Image();
        if(path == null) { out.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); } else {
            out.image = Resources.loadImage(path); //Try loading image
            if(out.image == null) {
                out.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            }
        }
        return out;
    }
    
    public Image() {
        //Create dummy image
        image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    }
    
    public Image(BufferedImage b) {
        image = b;
    }
}