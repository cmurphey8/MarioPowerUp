//*******************************************************************
//
//   File: MarioPowerUpSol.java          
//
//   Class: MarioPowerUpSol
// 
//   We Do: Extract all marios from the spriteSheet
//
//   You Do: 1) Extract all "flowers" from the spriteSheet
//           2) Fix the broken stop condition in the while loop of main
//           3) Complete the methods newIndex and updateProbs
//
//*******************************************************************

import java.awt.Graphics2D;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.File; 
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class MarioPowerUp
{
    // global variables defining the size of our DrawingPanel
    static final int width = 575;
    static final int height = 400;

    static final int mariox = 210;
    static final int marioy = 275;

    // init panel && graphics
    static DrawingPanel panel = new DrawingPanel(width, height);
    static Graphics2D g = panel.getGraphics();

    // set up off screen scene and off screen graphics object to enable double buffering
    static BufferedImage offscreen = new BufferedImage(575, 400, BufferedImage.TYPE_INT_RGB);
    static Graphics2D osg = offscreen.createGraphics();

    // background images
    static BufferedImage pb;
    static BufferedImage brick;

    // spritesheet images
    static BufferedImage[] marios;
    static BufferedImage[] flowers;

    // likelihood of getting a powerup
    static double[] probs;
    
    // main routine tests helper method printPyramid
    public static void main(String[] args) throws IOException
    {
        // init Scanner for terminal inputs
        Scanner console = new Scanner(System.in);

        // load bg and spritesheet images
        loadImages();

        // init probabilities



        // init powerup index for mario
        int indexM = 0;
        String response;
        do {
            // run through animation 1 time
            indexM = animateLoop(indexM);

            // prompt for user input
            System.out.print("Try Again? y/n ");
            response = console.next();

        // YOU DO: fix this stop condition
        } while (response.equals("y"));      
        
        // sign off
        System.out.println("Thanks for playing!");
        console.close();
    }

    // animate one sequence of MarioPowerUp
    public static int animateLoop(int indexM)
    {
        // init flower pos
        int flowerX = 30;
        int flowerY = 200;

        // init new random powerup index for flower
        int indexF = (int) (Math.random() * 24); // newIndex();
        flowerY = growFlower(indexM, indexF, flowerX, flowerY);
        flowerX = slideFlower(indexM, indexF, flowerX, flowerY);
        indexM = dropFlower(indexM, indexF, flowerX, flowerY);

        return indexM;
    }

    // find the next powerup index
    public static int newIndex()
    {
        // find a new random number in range [0, 1)
        double next = Math.random();

        // YOU DO: find the index that corresponds with the value of next (recall Barnsley leaf example from lecture)
        for (int i = 0; i < probs.length; i++){
            // once we find our index, update probs and return the index
            updateProbs(i);
            return i;
        }
        // default value
        return 0;
    }

    // reduce prob of getting powerup at index, redistribute prob across all other powerups
    public static void updateProbs(int index)
    {
        // we will reduce prob of powerup at index by half
        double reduce = probs[index] / 2;

        // redistribute probs across all other powerups
        for (int i = 0; i < probs.length; i++){
            // YOU DO: update probs
        }

        // make the update for the powerup at index
        probs[index] = reduce;
    }

    // drop the powerup flower on mario and display new powerup
    public static int dropFlower(int iM, int iF, int fX, int fY)
    {
        // a frame time of 17 ms corresponds to about 60 frames per second
        final int FRAME_T = 17;

        // animate flower grow-up in one second
        for (double t = 0; t < .87; t += FRAME_T/1000.0) 
        {
            // draw background from bg image file onto off screen image
            drawBackground();
            drawBricks();

            // draw mario with current powerup index
            osg.drawImage(marios[iM], mariox, marioy, null);

            // draw flower at each time step
            osg.drawImage(flowers[iF], pos(fX, 63, 0, t), pos(fY, 60, 100, t), null);

            // copy off screen image onto DrawingPanel
            g.drawImage(offscreen, 0, 0, null);
        
            // sleep for FRAME_T milliseconds
            panel.sleep(FRAME_T);
        }     
        // draw background from bg image file onto off screen image
        drawBackground();
        drawBricks();

        // draw mario with current powerup index
        osg.drawImage(marios[iF], mariox, marioy, null);

        // copy off screen image onto DrawingPanel
        g.drawImage(offscreen, 0, 0, null);

        panel.sleep(1000);
        return iF;
    }
    
    // slide the powerup flower 126 px across the bricks in 2s 
    public static int slideFlower(int iM, int iF, int fX, int fY)
    {
        // frame time of 17 ms corresponds to about 60 frames per second
        final int FRAME_T = 17;

        // animate flower grow-up in one second
        for (double t = 0; t < 2; t += FRAME_T/1000.0) 
        {
            // draw background and bricks onto off screen image
            drawBackground();
            drawBricks();

            // draw mario with current powerup index onto off screen image
            osg.drawImage(marios[iM], mariox, marioy, null);

            // draw new powerup flower with latest position onto off screen image
            osg.drawImage(flowers[iF], pos(fX, 63, 0, t), fY, null);

            // copy off screen image onto DrawingPanel
            g.drawImage(offscreen, 0, 0, null);
        
            // sleep for FRAME_T milliseconds
            panel.sleep(FRAME_T);
        }     
        // return the new x position of the flower after the slide
        return (int) pos(fX, 63, 0, 2);
    }

    // grow a new powerup flower
    public static int growFlower(int iM, int iF, int fX, int fY)
    {
        // a frame time of 17 ms corresponds to about 60 frames per second
        final int FRAME_T = 17;

        // animate flower grow-up in one second
        for (double t = 0; t < 1; t += FRAME_T/1000.0) 
        {
            // draw background from bg image file onto off screen image
            drawBackground();

            // draw mario with current powerup index
            osg.drawImage(marios[iM], mariox, marioy, null);

            // grow the flower at each time step
            osg.drawImage(flowers[iF], fX, pos(fY, -35, 0, t), null);

            // draw bricks after flower to hide flower as it grows
            drawBricks();

            // copy off screen image onto DrawingPanel
            g.drawImage(offscreen, 0, 0, null);
        

            // sleep for FRAME_T milliseconds
            panel.sleep(FRAME_T);
        }     
        panel.sleep(1000);
        return (int) pos(fY, -35, 0, 1);
    }
    
    // displacement formula to find current position
    public static int pos(double p0, double v, double a, double t) {
        return (int) (p0 + v * t + .5 * a * t * t);
    }

    // add bricks over background image
    public static void drawBricks()
    {
        // draw bricks
        osg.drawImage(brick, 0, 200, null);
        osg.drawImage(pb, 35, 200, null);
        osg.drawImage(brick, 70, 200, null);
        osg.drawImage(brick, 105, 200, null);
        osg.drawImage(brick, 140, 200, null);
        osg.drawImage(brick, 245, 200, null);
        osg.drawImage(brick, 280, 200, null);
    }

    // load background pyramid figure
    public static void drawBackground()
    {
        // draw background
        osg.setColor(Color.CYAN);
        osg.fillRect(0, 0, 575, 400);
        osg.setColor(Color.GREEN);
        osg.fillRect(0, 325, 575, 400);
    }

    // load images from spritesheet
    public static void loadImages() throws IOException
    {
        // load backgruond images
        pb = ImageIO.read(new File("Power_Block.png"));
        brick = ImageIO.read(new File("Brick.png"));

        // load sprite sheet
        BufferedImage spriteSheet = ImageIO.read(new File("Marios.png"));
        
        // load mario sprites
        final int width = 45;
        final int heightM = 55;
        final int heightF = 37;
        final int rows = 3;
        final int cols = 8;

        // WE DO: load mario sprites
       
        
        // YOU DO: load flower sprites
        

        // initialize probabilities of getting each powerup
        probs = new double[marios.length];
        for (int i = 0; i < marios.length; i++)
        {
            // start with uniform probability
            probs[i] = 1.0 / marios.length;
        }
    }
}
