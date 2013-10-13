/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author vitorfs
 */
public class ImageUploader {
    
    private static final int IMG_WIDTH = 250;
    private static final int IMG_HEIGHT = 250;
    
    private BufferedImage image;
    
    public BufferedImage getImage() {
        return image;
    }
    
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public void upload(File file) {         
        try {
            image = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        /*try {
            BufferedImage img = ImageIO.read(new File("media/" + file.getName()));               
            ImageIcon icon = new ImageIcon(img);
            JLabel label = new JLabel(icon);
            JOptionPane.showMessageDialog(null, label);
         } catch (IOException e) {
            e.printStackTrace();
         }*/
    }
    
    public void save(String name) {
        try {
            File theDir = new File("media/");
            if (!theDir.exists()) theDir.mkdir();
            
            int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
            BufferedImage resizeImageJpg = resizeImage(image, type);
            ImageIO.write(resizeImageJpg, "jpg", new File("media/" + name + ".jpg"));    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
       
    public BufferedImage resizeImage (BufferedImage originalImage, int type, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;        
    }
    
    public BufferedImage resizeImage(BufferedImage originalImage, int type){
        return resizeImage(originalImage, type, IMG_WIDTH, IMG_HEIGHT);
    }
    
    public BufferedImage resizeImage(int width, int height) {
        int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
        return resizeImage(image, type, width, height);
    }
    
}
