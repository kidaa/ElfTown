package Display;

import Controller.Mouse;
import Simulation.Agent;
import Simulation.Simulation;
import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Alex Mulkerrin
 */
public class MouseDetailPanel extends JPanel {
    JTextArea details;
    Simulation targetSim;
    Mouse targetPlayer;
    
    public MouseDetailPanel(Simulation sim, Mouse player) {
        targetSim = sim;
        targetPlayer = player;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200,150));
        
        add (new JLabel("Current Moused Over"));
        details = new JTextArea();
        details.setEditable(false);
        add(details);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateDetails();
    }
    
    public void updateDetails() {
        int x=targetPlayer.mouseX;
        int y=targetPlayer.mouseY;
        
        String contents = "Mouse position: "+x+","+y+"\n";
        contents += "Current power: ";
        switch(targetPlayer.brushType) {
            case 0: contents += "sink land.\n";
                break;
            case 1: contents += "raise land.\n";
                break;
            case 2: contents += "raise hill.\n";
                break;
            case 3: contents += "raise mountain.\n";
                break;
            case 4: contents += "Goto command.\n";
                break;
        }
        contents += "Power magnitute: "+targetPlayer.brushSize+"\n";
        contents += "Tile properties:\n";
        contents += "Elevation: "+targetSim.map.getElevation(x,y)+"\n";
        contents += "Temperature: "+targetSim.map.getTemperature(x,y)+"\n";
        contents += "Rainfall: "+targetSim.map.getRainfall(x,y)+"\n";
        contents += "Fertility: "+targetSim.map.getFertility(x,y)+"\n";
//        if (targetSim.map.occupier[x][y]!=null) {
//            Agent hovered = targetSim.map.occupier[x][y];
//            contents += "Tribe: "+hovered.name+"\n";
//            contents += "population: "+hovered.population+"\n";
//        } else {
//            contents +="Unoccupied\n";
//        }
        details.setText(contents);
    }
}
