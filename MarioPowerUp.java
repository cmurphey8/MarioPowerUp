//*******************************************************************
//
//   File: MarioDraw.java          
//
//   Class: MarioDraw
// 
//   We Do: 1) Complete the helper method drawBox
//          2) Update the helper method drawRow
//          3) Draw the left hand side (LHS) pyramid
//
//   You Do: Draw a right hand side (RHS) pyramid separated 
//           from the LHS pyramid by one block-width
//
//*******************************************************************

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File; 
import java.io.IOException;
import java.awt.image.BufferedImage;

public class MarioPowerUp 
{
    // global variables defining the size of our DrawingPanel
    static int width = 575;
    static int height = 400;

    // init panel && graphics
    static DrawingPanel panel = new DrawingPanel(width, height);
    static Graphics2D g = panel.getGraphics();

    // background images
    static BufferedImage pb;
    static BufferedImage brick;

    // spritesheet images
    static BufferedImage[] marios;
    static BufferedImage[] flowers;
    
    // main routine tests helper method printPyramid
    public static void main(String[] args) throws IOException
    {
        // load bg and spritesheet images
        loadImages();

        for (int i = 0; i < 24; i++){
            growFlower(i);
        }

        // // draw background
        // drawBackground();

        // // draw bricks
        // drawBricks();

        // // initial (x, y) coordinates for mario
        // int mariox = 210;
        // int marioy = 275;        

        // g.drawImage(marios[0], mariox, marioy, null);

        // g.drawImage(flowers[0], 40, marioy - 108, null);
    }

    // mario jumping
    public static void growFlower(int index)
    {
        // a frame time of 17 ms corresponds to about 60 frames per second
        final int FRAME_T = 17;

        // WE DO: animate flower grow-up in one second
        for (double t = 0; t < 1; t += FRAME_T/1000.0) 
        {
            // WE DO: draw background from bg image file onto off screen image
            drawBackground();

            // you can pick any two values that satisfy the kinematic constraints
            g.drawImage(flowers[index], 40, pos(200, -33, 0, t), null);

            drawBricks();

            // WE DO: sleep for FRAME_T milliseconds
            panel.sleep(FRAME_T);
        }     
        panel.sleep(1000);
    }
    
    // displacement formula to find mario's current position in Y
    // NOTE: vy is variable -> dy = y - y0 = vy * t + .5 * ay * t * t
    public static int pos(double y0, double vy, double ay, double t) {
        return (int) (y0 + vy * t + .5 * ay * t * t);
    }

    public static void drawBricks()
    {
        // draw bricks
        g.drawImage(brick, 0, 200, null);
        g.drawImage(pb, 35, 200, null);
        g.drawImage(brick, 70, 200, null);
        g.drawImage(brick, 105, 200, null);
        g.drawImage(brick, 140, 200, null);
        g.drawImage(brick, 245, 200, null);
        g.drawImage(brick, 280, 200, null);
    }

    public static void drawBackground()
    {
        // draw background
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, 575, 400);
        g.setColor(Color.GREEN);
        g.fillRect(0, 325, 575, 400);
    }

    public static void loadImages() throws IOException
    {
        // load backgruond images
        pb = ImageIO.read(new File("Power_Block.png"));
        brick = ImageIO.read(new File("Brick.png"));

        // load sprite sheet
        BufferedImage bigImg = ImageIO.read(new File("Mario_Powerups.png"));

        // load mario sprites
        int width = 45;
        int height = 50;
        int rows = 3;
        int cols = 8;
        marios = new BufferedImage[rows * cols];
        
        int shift = 95;
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++)
            {
                marios[(j * cols) + i] = bigImg.getSubimage(i * width, shift * j, width, height);
            }
        }
        
        // load flower sprites
        height = 35;
        int crop = 52;
        flowers = new BufferedImage[rows * cols];
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++)
            {
                flowers[(j * cols) + i] = bigImg.getSubimage(i * width + 3*i, shift * j + crop, width - 3*i, height);
            }
        }
    }
}