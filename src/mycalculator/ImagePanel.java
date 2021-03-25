package mycalculator;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    private BufferedImage image;
    private Image dImage;

    private ImagePanel(){
        
    }
    
    public ImagePanel(String pathStr, int width, int height) {
       try {                
          image = ImageIO.read(new File(pathStr));
          dImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
          
       } catch (IOException ex) {
            // handle exception...
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(dImage, 0, 0, this); // see javadoc for more info on the parameters            
    }

}