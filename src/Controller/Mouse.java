package Controller;

import java.awt.event.*;
import Simulation.Simulation;
import Display.DisplayFrame;
import Simulation.Agent;
import javax.swing.SwingUtilities;
/**
 *
 * @author Alex Mulkerrin
 */
public class Mouse extends MouseAdapter {
    public int mouseX=0;
    public int mouseY=0;
    public int brushType = 4;
    public int brushSize = 1;
    
    Simulation targetSim;
    DisplayFrame targetDisplay;
    public Agent selectedAgent=null;
    
    public Mouse(Simulation sim) {
        targetSim=sim;   
    }
    
    public void linkObject(DisplayFrame display) {
        targetDisplay=display;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            brushType=4;
            //if (brushType>4) brushType=0;
        } else if (brushType<4) {
            areaEffect();
        } else if (brushType==4) { // select
            changeSelected();
        } else if (brushType==5) { // select
            giveGoto();
        }
        targetDisplay.repaint();
    }
    
    @Override 
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX()/targetDisplay.mapDisplay.sqSize;
        mouseY = e.getY()/targetDisplay.mapDisplay.sqSize;
        if (mouseX<0) mouseX=0;
        if (mouseX>=targetSim.map.getWidth()) mouseX=targetSim.map.getWidth()-1;
        
        if (mouseY<0) mouseY=0;
        if (mouseY>=targetSim.map.getHeight()) mouseY=targetSim.map.getHeight()-1;
        if (brushType<4) {
            areaEffect();
        } else if (brushType==4) { // select
            changeSelected();
        }
        targetDisplay.repaint();
        
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        brushSize += e.getWheelRotation();
        if (brushSize<1) brushSize=1;
        targetDisplay.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX()/targetDisplay.mapDisplay.sqSize;
        mouseY = e.getY()/targetDisplay.mapDisplay.sqSize;
        if (mouseX<0) mouseX=0;
        if (mouseX>=targetSim.map.getWidth()) mouseX=targetSim.map.getWidth()-1;
        
        if (mouseY<0) mouseY=0;
        if (mouseY>=targetSim.map.getHeight()) mouseY=targetSim.map.getHeight()-1;
        targetDisplay.repaint();
    }
    
    public void areaEffect() {
        for (int range=0; range<brushSize; range++) {
            for (int i=-range; i<=range; i++) {
                changeTile(mouseX+range,mouseY+i);
                changeTile(mouseX-range,mouseY+i);
                changeTile(mouseX+i,mouseY+range);
                changeTile(mouseX+i,mouseY-range);
                
            }
        }
    }
    
    public void changeTile(int x, int y) {
        if (targetSim.mana>0) {
            if (x<0) x=0;
            if (x>=targetSim.map.getWidth()) x=targetSim.map.getWidth()-1;
            if (y<0) y=0;
            if (y>=targetSim.map.getHeight()) y=targetSim.map.getHeight()-1;

            if (targetSim.map.setElevation(x,y,brushType)) {
                targetSim.mana--;
            }
        }
    }
    
    public void giveGoto() {
        if (targetSim.mana>0 && selectedAgent!=null) {
            selectedAgent.targx=mouseX;
            selectedAgent.targy=mouseY;
            selectedAgent.isGoing=true;
            targetSim.mana--;
        }
        brushType=4;
    }
    
    public void changeSelected() {
        if (targetSim.map.occupier[mouseX][mouseY]!=null) {
            selectedAgent = targetSim.map.occupier[mouseX][mouseY];
            targetDisplay.selectedDetailDisplay.targetAgent = selectedAgent;
        }
    }
    
    public void nextAgent() {
        int found=-1;
        if (selectedAgent==null) found=0;
        Boolean passed=false;
        for (int i=0; i<targetSim.unit.size(); i++) {
            Agent toCompare =targetSim.unit.get(i);
            if (toCompare.equals(selectedAgent)) {
                passed=true;
                if (i==targetSim.unit.size()-1) found=0;
                
            } else {
                if (found==-1 && passed) {
                    found=i;
                }
            }
        }
        if (found!=-1) {
            selectedAgent = targetSim.unit.get(found);
        }
        targetDisplay.selectedDetailDisplay.targetAgent = selectedAgent;
    }
    
    public void commandToSettle() {
        if (selectedAgent!=null && targetSim.mana>0 && selectedAgent.isNomadic) {
            selectedAgent.settle();
            targetSim.mana--;
            targetDisplay.repaint();
        }
    }

    
}
