package Program;

import Display.DisplayFrame;
import Simulation.Simulation;
import Controller.Mouse;
import java.util.*;

/**
 *
 * @author Alex Mulkerrin
 */
public class ElfTown {
   // public SplashScreen mainMenu;
    public DisplayFrame display;
    public Simulation sim;
    Mouse player;
    public Timer timer;
    TimerTask updater;
    int seed;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ElfTown program = new ElfTown();
        //program.run();
    }
    
    public ElfTown() {
        seed = (int)(System.nanoTime());
        sim = new Simulation(45,30,10,seed);
        player = new Mouse(sim);
        display = new DisplayFrame(sim,player, this);
       // IntroFrame intro = new IntroFrame(sim.name);
        player.linkObject(display);
    }
    
    public void reset() {
       // mainMenu.dispose();
        seed = (int)(System.nanoTime());
        sim = new Simulation(20,20,10,seed);
        player = new Mouse(sim);
        display = new DisplayFrame(sim,player, this);
        
        player.linkObject(display);
    }
    
    public void resetWithSeed() {
       // mainMenu.dispose();
        sim = new Simulation(80,50,10,seed);
        player = new Mouse(sim);
        display = new DisplayFrame(sim,player, this);
        
        player.linkObject(display);
    }
    
    public void run() {
        
        timer = new Timer();
        updater =new UpdateTask();
        timer.schedule(updater, 0, 100);
    }
    
    public void update() {
        sim.update();
        display.update();
        if (sim.turn>=500 ){
            timer.cancel();
            gameEnd(sim.getTotalPop());
        } 
        if (sim.getTotalPop()<=0) {
            timer.cancel();
            gameEnd(0);
        }
    }
    
    public class UpdateTask extends TimerTask {
        @Override
        public void run() {
            update();
        }
    }
    
    public void gameEnd(int score) {
        //mainMenu = new MenuFrame(this, score);
    }
    
}
