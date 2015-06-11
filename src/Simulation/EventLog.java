package Simulation;

/**
 *
 * @author Alex Mulkerrin
 */
public class EventLog {
    private String log="";
    
    public String getLog() {
        return log;
    }
    
    public void clear() {
        log = "";
    }
    
    public void add(String addition) {
        log += addition+"\n";
    }
}
