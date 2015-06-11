package Display;

import Simulation.Agent;
import Simulation.Simulation;
import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Alex Mulkerrin
 */
public class SelectedDetailPanel extends JPanel{
    JTextArea details;
    public Agent targetAgent;
    Simulation targetSim;
    
    public SelectedDetailPanel(Simulation sim) {
        
        targetSim = sim;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200,150));
        
        targetAgent=null;
        
        add (new JLabel("Current Selection"));
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
        String contents = "Selected Agent: ";
        if (targetAgent==null) {
            contents+="none\n";
        } else {
            contents += "name: "+targetAgent.name+"\n";
            //contents += "population: "+targetAgent.population+"\n";
            contents += "aged: "+targetAgent.age+"\n";
            if (targetAgent.isNomadic) {
                contents += "Are currently nomads.\n";
//                contents +="There is enough food for: ";
//            contents +=50*targetSim.map.getCityFertility(targetAgent.x,targetAgent.y);
//            contents +=" nearby.\n";
            } else {
                contents += "Have settled down.\n";
                            contents +="Have enough food for: ";
            contents +=50*targetSim.map.getCityFertility(targetAgent.x,targetAgent.y);
            }

        }
        details.setText(contents);
    }
}
