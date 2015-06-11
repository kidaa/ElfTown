package Display;

import Controller.Mouse;
import java.awt.*;
import javax.swing.*;
import Simulation.Simulation;
import Program.ElfTown;
/**
 *
 * @author Alex Mulkerrin
 */
public class DisplayFrame extends JFrame{
    public MapPanel mapDisplay;
    public StatPanel statDisplay;
    public LogPanel logDisplay;
    public MouseDetailPanel mouseDetailDisplay;
    public SelectedDetailPanel selectedDetailDisplay;
    ButtonPanel buttonDisplay;
    
    Simulation targetSim;
    Mouse targetPlayer;
    ElfTown targetProgram;
    
    public int animationTicks;
    
    public DisplayFrame(Simulation sim, Mouse player, ElfTown program) {
        super("Elf Town");
        //setIconImage(new ImageIcon(getClass().getResource("/Resources/icon.png")).getImage());
        targetSim = sim;
        targetPlayer = player;
        targetProgram = program;
        
        setLayout(new BorderLayout());
        
        mapDisplay = new MapPanel(sim, player);
        JScrollPane scroller = new JScrollPane(mapDisplay);
        getContentPane().add(scroller,BorderLayout.CENTER);
        
        statDisplay = new StatPanel(sim);
        getContentPane().add(statDisplay,BorderLayout.EAST);
        
        logDisplay = new LogPanel(sim);
        mouseDetailDisplay = new MouseDetailPanel(sim, player);
        selectedDetailDisplay = new SelectedDetailPanel(targetSim);
        buttonDisplay = new ButtonPanel(targetProgram,targetPlayer);
        
        JPanel lowerPanel = new JPanel();
        lowerPanel.add(logDisplay);
        lowerPanel.add(mouseDetailDisplay);
        lowerPanel.add(selectedDetailDisplay);
        lowerPanel.add(buttonDisplay);
        getContentPane().add(lowerPanel,BorderLayout.SOUTH);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,720);
        setVisible(true);
        setLocationRelativeTo(null);
    }
    
    public void update() {
        mapDisplay.repaint();
        statDisplay.repaint();
        logDisplay.repaint();
        mouseDetailDisplay.repaint();
        selectedDetailDisplay.repaint();
    }
    
}
