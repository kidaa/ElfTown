package Display;

import java.awt.*;
import javax.swing.*;
import Simulation.Simulation;

/**
 *
 * @author Alex Mulkerrin
 */
public class StatPanel extends JPanel{
    JTextArea worldStats;
    JTextArea landStats;
    JTextArea factionStats;
    Simulation targetSim;

    public StatPanel(Simulation sim) {
        targetSim = sim;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("World Stats "));
        worldStats = new JTextArea();
        worldStats.setEditable(false);
        add(worldStats);
        
        add(new JLabel("Land Stats "));
        landStats = new JTextArea();
        landStats.setEditable(false);
        add(landStats);
        
        add(new JLabel("Faction Stats "));
        factionStats = new JTextArea();
        factionStats.setEditable(false);
        add(factionStats);
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateWorldStats();
        updateLandStats();
        updateFactionStats();
        
    }
    
    private void updateWorldStats() {
        String contents ="World: "+targetSim.name+"\n";
        contents += "Seed: "+targetSim.seed+"\n";
        contents += "Size: "+targetSim.map.getWidth()+","+targetSim.map.getHeight()+"\n";
        contents += "Turn: "+targetSim.turn+"\n";
        contents += "Current Mana: "+targetSim.mana+"\n";
        contents += "Current climate: "+targetSim.map.mapTemperature+"\n";
        worldStats.setText(contents);
    }
    
    private void updateLandStats() {
        String contents = "Total Area: "+targetSim.map.getWidth()*targetSim.map.getHeight()+"\n";
        
        contents += "Total Land: "+targetSim.map.totalLand;
        int percent = getPercentage(targetSim.map.totalLand,(targetSim.map.getWidth()*targetSim.map.getHeight()));
        contents +=" ("+percent+"%)\n";
        
        contents += "of which: \n";
        contents += "Fertile: "+targetSim.map.totalFertile;
        percent = getPercentage(targetSim.map.totalFertile,targetSim.map.totalLand);
        contents +=" ("+percent+"%)\n";
        contents += "Average: "+targetSim.map.totalAverage;
        percent = getPercentage(targetSim.map.totalAverage,targetSim.map.totalLand);
        contents +=" ("+percent+"%)\n";
        contents += "Barren: "+targetSim.map.totalBarren;
        percent = getPercentage(targetSim.map.totalBarren,targetSim.map.totalLand);
        contents +=" ("+percent+"%)\n";
        
        landStats.setText(contents);
    }
    
    private void updateFactionStats() {
        String contents ="Total Agents: "+targetSim.unit.size()+"\n";
        contents += "Hunter Gatherer Elves: "+targetSim.unit.size()+"\n";
        //contents += "Average Faction population: "+targetSim.getAveragePop()+"\n";
        //contents += "World population: "+targetSim.getTotalPop();
        factionStats.setText(contents);
    }
    
    public int getPercentage(int i, int j) {
        if (j!=0) {
            return 100*i/j;
        } else {
            return 0;
        }
    }
}
