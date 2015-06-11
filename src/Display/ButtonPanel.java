package Display;

import Controller.Mouse;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import Program.ElfTown;
/**
 *
 * @author Alex Mulkerrin
 */
public class ButtonPanel extends JPanel{
    ElfTown targetProgram;
    Mouse targetPlayer;
    
    public ButtonPanel(ElfTown program, Mouse player) {
        
        targetProgram = program;
        targetPlayer= player;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250,150));
        
        add(new JLabel("Player Actions"));
        
        JPanel buttonBox = new JPanel(new GridLayout(5,2));
        
        JButton button = new JButton("End Turn");
        button.addActionListener(new endTurnButton());
        buttonBox.add(button);
        
        button = new JButton("select Elf");
        button.addActionListener(new selectPower(4));
        buttonBox.add(button);
        
        button = new JButton("Auto End Turn");
        button.addActionListener(new autoTurnButton());
        buttonBox.add(button);
        
        button = new JButton("sink land");
        button.addActionListener(new selectPower(0));
        buttonBox.add(button);
        
        button = new JButton("Settle");
        button.addActionListener(new settleButton());
        buttonBox.add(button);
        
        button = new JButton("raise land");
        button.addActionListener(new selectPower(1));
        buttonBox.add(button);
        
        button = new JButton("next Elf");
        button.addActionListener(new nextTribe());
        buttonBox.add(button);

        button = new JButton("raise hill");
        button.addActionListener(new selectPower(2));
        buttonBox.add(button);
        
        button = new JButton("go to...");
        button.addActionListener(new selectPower(5));
        buttonBox.add(button);
        
        button = new JButton("raise mountain");
        button.addActionListener(new selectPower(3));
        buttonBox.add(button);
        
        add(buttonBox);
    }

    private class endTurnButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            targetProgram.update();        
        }
    }

    private class autoTurnButton implements ActionListener {
        Boolean isOn=false;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isOn) {
                targetProgram.timer.cancel();
                JButton button =(JButton)e.getSource();
                button.setText("Auto End Turn");
                isOn=false;
            } else {
                targetProgram.run();
                JButton button =(JButton)e.getSource();
                button.setText("Pause");
                isOn=true;
            }
        }
    }
    
    private class settleButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            targetPlayer.commandToSettle();        
        }
    }
    
    private class nextTribe implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            targetPlayer.nextAgent();
            targetProgram.display.repaint();
        }
    }
    
    private class selectPower implements ActionListener {
        int triggeredSelection;
        
        selectPower(int type) {
            triggeredSelection=type;
        }
        
        @Override
        public void actionPerformed(ActionEvent ev) {
            targetPlayer.brushType=triggeredSelection;
            targetProgram.display.update();
        }
    }
    
}
