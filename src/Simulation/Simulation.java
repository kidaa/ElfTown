package Simulation;

import java.util.*;
/**
 *
 * @author Alex Mulkerrin
 */
public class Simulation {
    public EventLog log;
    public String name;
    
    public Terrain map;
    public ArrayList<Agent> unit;
    public RandomGenerator random;
    
    
    
    public int turn;
    public int[] score;
    public int maxScore;
    public int mana;
    
    public Simulation(int width, int height, int startingAgents, int seeded) {
        
        random = new RandomGenerator(seeded);
        name = randomWorldName();
        
        score=new int[501];
        initializeScore();
        
        turn=0;
        
        mana=1;
        
        log = new EventLog();
        
        map = new Terrain(width,height,this);
        map.generateWorld();
        
        unit = new ArrayList<>();
        for (int i=0; i<startingAgents; i++) {
            Agent toAdd=null;
            while (toAdd==null) {
                int x =random.integer(map.getWidth());
                int y =random.integer(map.getHeight());
                toAdd = map.createOccupier(x,y);
                
                
            }
            unit.add(toAdd);
        }
        score[turn]=getTotalPop();
        if (score[turn]>maxScore) maxScore=score[turn];
    }
    
    public void initializeScore() {
        for (int i=0; i<score.length; i++) {
            score[i]=0;
        }
    }
    
    public void update() {
        turn++;
        if (turn%50==0) map.climateChange();
        mana++;
        for (int i=0; i<unit.size(); i++) {
            Agent toUpdate = unit.get(i);
            toUpdate.update();
//            if (toUpdate.population<1) {
//                unit.remove(i);
//                if (toUpdate.isNomadic) {
//                    map.setRemains(toUpdate.x, toUpdate.y, 1);
//                    log.add(turn+": Tribe "+toUpdate.name+" died.");
//                } else {
//                    map.setRemains(toUpdate.x, toUpdate.y, 2);
//                    log.add(turn+": Settlement "+toUpdate.name+" died.");
//                }
//                i--;
//            } else {
//                if (toUpdate.isNomadic && toUpdate.population>100) {
//                unit.add(map.createNewTribe(toUpdate.x, toUpdate.y));
//                toUpdate.population-=50;
//                Agent temp = unit.get(unit.size()-1);
//                log.add(turn+": Tribe "+toUpdate.name+
//                        " created tribe "+temp.name+".");
//                }
//                if ( toUpdate.population>200) {
//                unit.add(map.createNewTribe(toUpdate.x, toUpdate.y));
//                toUpdate.population-=50;
//                Agent temp = unit.get(unit.size()-1);
//                log.add(turn+": Settlement "+toUpdate.name+
//                        " created tribe "+temp.name+".");
//                }
//            }
        }
        map.update(unit);
        score[turn]=getTotalPop();
        if (score[turn]>maxScore) maxScore=score[turn];
        
    }
    
    public int getTotalPop() {
        int totalPop=0;
        for (int i=0; i<unit.size(); i++) {
            Agent toView = unit.get(i);
            totalPop++;//toView.population;
        }
        return totalPop;
    }
    
    public int getAveragePop() {
        int totalPop=0;
        for (int i=0; i<unit.size(); i++) {
            Agent toView = unit.get(i);
            totalPop++;//toView.population;
        }
        if (unit.size()>0){
            return totalPop/unit.size();
        } else {
            return 0;
        }
    }

    private String randomWorldName() {
        String text="", result="";
        int wordLength;
        int numWords = random.integer(3)+1;
        for (int i=0; i<numWords; i++) {
            wordLength = random.integer(4)+4;
            result += random.name(wordLength);
        }
        return result;
    }
}
