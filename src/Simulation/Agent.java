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
        int nameLength = sim.random.integer(4)+4;
        name = sim.random.name(nameLength);
        isNomadic=true;
 
        age=0;
        
    }
    
    public void update() {
        age++;
        wander();
    }
    
    public void wander() {
        int nx=x;
        int ny=y;
        int choice = sim.random.integer(4);
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
  
}
