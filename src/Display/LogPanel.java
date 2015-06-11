package Display;

import java.awt.*;
import javax.swing.*;
import Simulation.Simulation;
/**
 *
 * @author Alex Mulkerrin
 */
public class LogPanel extends JPanel {
    JTextArea eventLog;
    Simulation targetSim;
    
    public LogPanel(Simulation sim) {
        targetSim = sim;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        setPreferredSize(new Dimension(300,150));
        
        add (new JLabel("Event Log"));
        eventLog = new JTextArea();
        eventLog.setEditable(false);
        JScrollPane scroller = new JScrollPane(eventLog);
        add(scroller);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateEventLog();
    }
    
    public void updateEventLog() {
        String contents = targetSim.log.getLog();
        eventLog.setText(contents);
    }
}
