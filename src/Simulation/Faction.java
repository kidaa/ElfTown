package Simulation;

/**
 *
 * @author Alex Mulkerrin
 */
public class Faction {
    public String name;
    
    public Faction(Simulation sim) {
        int nameLength = sim.random.integer(4)+4;
        name = sim.random.name(nameLength);
    }
    
}
