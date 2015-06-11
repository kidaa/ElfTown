package Display;

import Controller.Mouse;
import java.awt.*;
import javax.swing.*;
import Simulation.Simulation;
import Simulation.Agent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

/**
 *
 * @author Alex Mulkerrin
 */
public class MapPanel extends JPanel {
//                                    //ocean     grass   shrubs      trees       field    desert
//    String[] palette = new String[]{"#478CC1","#D8D8D8","#DAFF7F","#63E22D","#7F6A00","#FFE46D",
//                                    //jungle    plain    swamp     forest    tundra   heath     taiga     arctic              
//                                    "#C6E256","#DAFF7F","#007F7F","#267F00","#7C5F4C","#9992A3","#005D52","#EAEAEA",
//                                    //ice?      coast
//                                    "#ffffff","#B4DAF7"
//    };
    String[] palette = new String[]{
        //biome         //barren    //average   //fertile
        /*ocean*/       "#478CC1",  "#B4DAF7",  "#000000",     
        /*desert*/      "#FFE97F",  "#B7A65B",  "#406600",
        /*savannah*/    "#DAFF7F",  "#89A050",  "#16BC00",
        /*temperate*/   "#72C462",  "#4B7F3F",  "#006329",
        /*tundra*/      "#ABAF69",  "#707244",  "#123700",
        /*artic*/       "#EAEAEA",  "#ffffff",  "#B4DAF7",
        /*farmland*/    "#998F64",  "#7F6A00",  "#564800",      
    };

    BufferedImage hillSprite;
    BufferedImage mountainSprite;
    BufferedImage treeSprite;
    BufferedImage shrubSprite;
    BufferedImage settlementSprite;
    BufferedImage nomadSprite;
    BufferedImage skullSprite;
    BufferedImage ruinSprite;
    BufferedImage cropSprite;
    BufferedImage powerSprite;
    BufferedImage noPowerSprite;
    BufferedImage fightSprite;
    
    Simulation targetSim;
    Mouse targetPlayer;
    public int sqSize=16;

    public MapPanel(Simulation sim, Mouse player) {
        targetSim = sim;
        targetPlayer = player;
        
        addMouseListener(player);
        addMouseMotionListener(player);
        addMouseWheelListener(player);
        
        try {
            hillSprite = ImageIO.read(getClass().getResource("/Resources/hill.png"));
            mountainSprite = ImageIO.read(getClass().getResource("/Resources/mountain.png"));
            treeSprite = ImageIO.read(getClass().getResource("/Resources/trees.png"));
            shrubSprite = ImageIO.read(getClass().getResource("/Resources/shrubs.png"));
            settlementSprite = ImageIO.read(getClass().getResource("/Resources/settlement.png"));
            nomadSprite = ImageIO.read(getClass().getResource("/Resources/nomad.png"));
            skullSprite = ImageIO.read(getClass().getResource("/Resources/skull.png"));
            ruinSprite = ImageIO.read(getClass().getResource("/Resources/ruin.png"));
            cropSprite = ImageIO.read(getClass().getResource("/Resources/crops.png"));
            powerSprite = ImageIO.read(getClass().getResource("/Resources/icon.png"));
            noPowerSprite = ImageIO.read(getClass().getResource("/Resources/noPower.png"));
            fightSprite = ImageIO.read(getClass().getResource("/Resources/fighting.png"));
        } catch (IOException ex) {
        }


        
        setPreferredSize(
                new Dimension(
                sqSize*targetSim.map.getWidth(),
                sqSize*targetSim.map.getHeight())
        );
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintTerrain(g);
        paintAgents(g);
        paintMouseHover(g);
    }
    
    public void paintTerrain(Graphics g) {
        g.setColor(Color.decode(palette[0]));
        g.fillRect(0, 0, sqSize*targetSim.map.getWidth(), sqSize*targetSim.map.getHeight());
        g.setColor(Color.BLACK);
        for (int i=0; i<targetSim.map.getWidth(); i++) {
            for (int j=0; j<targetSim.map.getHeight(); j++) {
                
                int elev = targetSim.map.getElevation(i, j);
                //land tiles
                if (elev>0) {
                    // select colour depending on temperature and fertility
                    int n = targetSim.map.getMaxFertility(i,j);
                    int t = targetSim.map.getTemperature(i, j);
                    g.setColor(Color.decode(palette[t*3+n+3]));
                    if (targetSim.map.isExploited(i,j)) {
                        g.setColor(Color.decode(palette[18]));
                    }
                    g.fillRect(i*sqSize, j*sqSize, sqSize, sqSize);
                    
                    //field
                    int f = targetSim.map.getFertility(i,j);
                    if (targetSim.map.isExploited(i,j)) {
                        g.setColor(Color.decode(palette[18+n]));
                        g.fillRect(i*sqSize+1, j*sqSize+1, sqSize-2, sqSize-2);
                        if (f>0) g.drawImage(cropSprite, i*sqSize, j*sqSize, this);
                    }
                    // shrubland
                    if (f==1 && !targetSim.map.isExploited(i,j)) g.drawImage(shrubSprite, i*sqSize, j*sqSize, this);
                    // forests
                    if (f==2 && !targetSim.map.isExploited(i,j)) g.drawImage(treeSprite, i*sqSize, j*sqSize, this);
                    // show higher elevations
                    if (elev==2) g.drawImage(hillSprite, i*sqSize, j*sqSize, this); 
                    if (elev==3)g.drawImage(mountainSprite, i*sqSize, j*sqSize, this); 
                    
                    // display remains and ruins
                    int r=targetSim.map.getRemains(i, j);
                    if (r==1) g.drawImage(skullSprite, i*sqSize, j*sqSize, this);
                    if (r==2) g.drawImage(ruinSprite, i*sqSize, j*sqSize, this);
                    
                // ocean tiles    
                } else {
                    int adj =targetSim.map.getEdge(i,j);
                    if (adj>0) {
                        g.setColor(Color.decode(palette[1]));
                        if (adj%2>0) g.fillRect(i*sqSize+sqSize-1, j*sqSize, 1, sqSize);
                        if (adj%4>1) g.fillRect(i*sqSize, j*sqSize+sqSize-1, sqSize, 1);
                        if (adj%8>3) g.fillRect(i*sqSize, j*sqSize, 1, sqSize);
                        if (adj%16>7) g.fillRect(i*sqSize, j*sqSize, sqSize, 1);  
                    }
                }    
            }
        }
    }
    
    
    public void paintAgents(Graphics g) {
        g.setColor(Color.red);
        for (int i=0; i<targetSim.unit.size(); i++) {
            Agent toPaint = (Agent)targetSim.unit.get(i);
            int x = toPaint.x;
            int y = toPaint.y;
            if (toPaint.isNomadic) {
                if (toPaint.isGoing) {
                    g.setColor(Color.cyan);
                    g.fillRect(x*sqSize+1, y*sqSize+1, sqSize-2, sqSize-2);
                }
                g.drawImage(nomadSprite, x*sqSize, y*sqSize, this);
                if (toPaint.isFighting) {
                    g.drawImage(fightSprite, x*sqSize, y*sqSize, this);
                }
            } else {
                //g.setColor(Color.red);
                g.drawImage(settlementSprite, x*sqSize, y*sqSize, this);
            }
            
        }
    }
    
    public void paintMouseHover(Graphics g) {
        g.setColor(Color.red);
        
        g.drawRect(targetPlayer.mouseX*sqSize, targetPlayer.mouseY*sqSize,
                sqSize, sqSize);
        
        if (targetPlayer.selectedAgent!=null) {
            g.setColor(Color.green);
            g.drawRect(targetPlayer.selectedAgent.x*sqSize, 
                    targetPlayer.selectedAgent.y*sqSize,sqSize, sqSize);
        }
        
        if (targetPlayer.brushType!=4) {
            if (targetSim.mana>0) {
                g.drawImage(powerSprite, targetPlayer.mouseX*sqSize+sqSize, targetPlayer.mouseY*sqSize-sqSize, this);
            } else {
                g.drawImage(noPowerSprite, targetPlayer.mouseX*sqSize+sqSize, targetPlayer.mouseY*sqSize-sqSize, this);
            }
        }
    }
    
}
