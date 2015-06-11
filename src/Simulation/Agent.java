package Simulation;

/**
 *
 * @author Alex Mulkerrin
 */
public class Agent {
    public int x;
    public int y;
    public int oldx;
    public int oldy;
    public int targx;
    public int targy;
    public Boolean isGoing=false;
    public Boolean isFighting=false;
    
    public int age;
    public int index;
    public String name;
    
    public Boolean isNomadic;
    
    private Simulation sim;
    private Terrain map;

    
    public Agent(int placeX, int placeY, Terrain location, Simulation containingSim , Boolean isColonist) {
        x = placeX;
        y = placeY;
        oldx = x;
        oldy = y;
        if (isColonist) {
        targx=x+((int)(Math.random()*3)-1)*5;
        targy=y+((int)(Math.random()*3)-1)*5;
        isGoing=true;
        }
        
        
        map = location;
        sim = containingSim;
        name = randomTribeName();
        isNomadic=true;
 
        age=0;
        
    }
    
    public void update() {
        age++;
//        if (isFighting) isFighting=false;
//        if (isNomadic) {
//            if (isGoing) {
//                gotoLocation();
//            } else {
//                // very slight chance to settle on their own
//                if (map.getCityFertility(x, y)>30) {
//                    this.settle();
//                    
//                }
//            
//                //wander();
//            }
//                 
//                    
//                    switch (map.getFertility(x,y)) {
//                        case 0: {
//                            map.drainFertility(x,y);
//                            population-=1;
//                            break;
//                        }
//                        case 1: {
//                            population+=1;
//                            map.drainFertility(x,y);
//                            break;
//                        }
//                        case 2: {
//                            population+=2;
//                            map.drainFertility(x,y);
//                            if (population>150) population=150;
//                            break;
//                        }
//                    }
//                
//            
//        } else {
//            int food = 100*map.getCityFertility(x,y); 
//            if (food>population) {
//                population+=5;
//            } else if (food==population) {
//                //population unchanged;
//            } else if (food<population) {
//                population-=5;  
//            }
//            map.drainCityFertility(x,y,population/100);
//        }
    }
    
    public void wander() {
        int nx=x;
        int ny=y;
        int choice = (int)(Math.random()*4);
        if (choice==0) ny--;
        if (choice==1) nx++;
        if (choice==2) ny++;
        if (choice==3) nx--;
        if (nx>0 && nx<map.getWidth() && ny>0 && ny<map.getHeight()) {
            if (map.checkValidMove(nx,ny,this)) {

                oldx=x;
                oldy=y;
                x=nx;
                y=ny;
                map.occupier[x][y] = this;
                
            }  
        }
    }
    
    public void gotoLocation() {
        int nx=x;
        int ny=y;
        Boolean moving=true;
        if (targx>x) {
            nx++;
        } else if(targx<x) {
            nx--;
        } else if (targy>y) {
            ny++;
        } else if (targy<y) {
            ny--;
        } else {
            moving=false;
        }
        Boolean success=false;
        if (nx>0 && nx<map.getWidth() && ny>0 && ny<map.getHeight()) {
            if (moving) {
                if (map.checkValidMove(nx,ny,this)) {
                    success=true;   
                    oldx=x;
                    oldy=y;
                    x=nx;
                    y=ny;
                    map.occupier[x][y] = this;

                }   
            }
            if (!success) {
                isGoing=false;
            }
        }
    }
    
    public void settle() {
        isNomadic=false;
        sim.log.add(sim.turn+": Tribe "+name+" has settled.");
    }
    
    private String randomTribeName() {
        char[] vowels = new char[]{'a','e','i','o','u'};
        char[] consonants = new char[]{'w','l','f','s','y'};
        String text="", result="";


            text="";
            int wordLength= sim.random.nextInt(4)+4;
            int letterType = sim.random.nextInt(2);
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
            result += text;
        return result;
    }
    
    public char randomChoice(char[] choices) {
        int pick = sim.random.nextInt(choices.length);
        return choices[pick];
    }
    
    
}
