package Simulation;

import java.util.*;
/**
 *
 * @author Alex Mulkerrin
 */
public class Simulation {
    Random random;
    public int seed;
    public EventLog log;
    public String name;
    public Terrain map;
    public ArrayList<Agent> unit;
    
    
    
    public int turn;
    public int[] score;
    public int maxScore;
    public int mana;
    
    public Simulation(int width, int height, int startingFactions, int seeded) {
        
        seed=seeded;
        random = new Random();
        random.setSeed(seed);
        name = randomWorldName();
        
        score=new int[501];
        initializeScore();
        
        turn=0;
        
        mana=1;
        
        log = new EventLog();
        
        map = new Terrain(width,height,this);
        map.generateWorld();
        
        unit = new ArrayList<>();
        for (int i=0; i<startingFactions; i++) {
            Agent toAdd=null;
            while (toAdd==null) {
                int x =random.nextInt(map.getWidth());
                int y =random.nextInt(map.getHeight());
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
        char[] vowels = new char[]{'a','e','i','o','u'};
        char[] consonants = new char[]{'p','t','k','m','n'};
        String text="", result="";
        int numWords = random.nextInt(3)+1;
        for (int i=0; i<numWords; i++) {
            text="";
            int wordLength= random.nextInt(4)+4;
            int letterType = random.nextInt(2);
            for (int j=0; j<wordLength; j++) {
                if (letterType==0) {
                    text += randomChoice(consonants);
                    letterType++;
                } else {
                    text += randomChoice(vowels);
                    letterType=0;
                }
                if (j==0) text = text.toUpperCase();
            }
            result += text+" ";
        }
        return result;
    }
    
    public char randomChoice(char[] choices) {
        int pick = random.nextInt(choices.length);
        return choices[pick];
    }
}
