package Simulation;

import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author Alex Mulkerrin
 */
public class Terrain {
    // ordered area around city to exploit
    private int[][] cityStencil = new int[][]{
        {0,0},{0,-1},{1,0},{0,1},{-1,0},
        {1,-1},{1,1},{-1,1},{-1,-1},{0,-2},
        {2,0},{0,2},{-2,0},{1,-2},{2,-1},{2,1},
        {1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},
    };
    private int width;
    private int height;
    private Tile tile[][];
    public Agent[][] occupier;
    
    public int totalLand;
    public int totalFertile, totalAverage, totalBarren;
    
    public int mapTempTendency;
    public int mapTemperature;
    
    private Simulation sim;
    
    public Terrain(int w, int h, Simulation containingSim) {
        width = w;
        height = h;
        sim = containingSim;
        tile = new Tile[width][height];
        occupier = new Agent[width][height];
    }

    
    
    private class Tile {
        int elevation = 0;
        int edge = 0;
        int temperature = 0;
        int rainfall = 0;
        int fertility = 0;
        int maxFertility = 0;
        Boolean exploited = false;
        int remains = 0;
        
        public int calculateFertility() {
            int value=3-elevation;
            
                    if (temperature==0) value=0;
                    if (temperature==1) value--;
                    if (temperature==3) value-=2;
                    if (rainfall>0) value++;
                    if (temperature==4) value=0;
                    if (value>2) value=2;
                    if (value<0) value=0;
            return value;
        }
    }
    
    public void clearMap() {
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                tile[i][j] = new Tile();
                occupier[i][j] = null;
            }
        }
    }
    
    public void generateWorld() {
        clearMap();
        Boolean challenge = sim.random.bool();
        if (challenge){
            mapTempTendency=-1;
        } else {
            mapTempTendency=1;
        }
        mapTemperature=3;
        generateHeightMap();
        identifyCoast();
        generateTemperature();
        generateMapRainfall();
        generateFertility();
    }
    
    public void generateHeightMap() {
        totalLand=0;
        int border=2;
        int desiredLand = (width-border*2)*(height-border*2)*2/3;
        int[][] stamp = new int[][]{
            {0,0},{1,0},{0,1},{-1,0},{0,-1}
        };
        int[][] choices = new int[][]{
            {1,0},{0,1},{-1,0},{0,-1}
        };
        int[][] stencil;
        
        while (totalLand<desiredLand) {
            stencil = new int[width][height];
            int sx = sim.random.integer(width-2*border)+border;
            int sy = sim.random.integer(height-2*border)+border;
            int length = sim.random.integer(63)+1;
            Boolean insideBorder = true;
            while (length>0 && insideBorder) {
                for (int e=0; e<stamp.length; e++) {
                    stencil[sx+stamp[e][0]][sy+stamp[e][1]]=1;
                }
                int choice = sim.random.integer(choices.length);
                sx += choices[choice][0];
                sy += choices[choice][1];
                length--;
                if (sx<border || sx>=width-border || sy<border || sy>=height-border) {
                    insideBorder = false;
                }
            }
            
            for (int i=0; i<width; i++) {
                for (int j=0; j<height; j++) {
                    if (stencil[i][j]>0) {
                        if (tile[i][j].elevation<1) {
                            totalLand++;
                        }
                        tile[i][j].elevation += stencil[i][j];
                    }
                }
            }
        }
    }
    
    public void identifyCoast() {
        int[][] adjacent = new int[][]{
            {1,0},{0,1},{-1,0},{0,-1}
        };
        int[] mask = new int[]{1,2,4,8};
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                tile[i][j].edge=0;
                if (tile[i][j].elevation==0) {
                    for (int e=0; e<adjacent.length; e++) {
                        int nx=i+adjacent[e][0];
                        int ny=j+adjacent[e][1];
                        if (nx>0 && nx<width && ny>0 && ny<height) {
                            if (tile[nx][ny].elevation>0) {
                                tile[i][j].edge += mask[e];
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void generateTemperature() {
        int temp;
        int scaling =(mapTemperature+3)*2;
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
//                temp = j-height/2; // latitude
//                temp = scaling*temp/height; //rescale from 0 to scale factor
//                temp = (int)(Math.abs(temp));
//                if (temp>4) temp=4;
                temp=2;
                tile[i][j].temperature=temp;
                //tile[i][j].rainfall=0;
            }
        }
    }
    
    public void generateMapRainfall() {
        for (int j=0; j<height; j++) {
            int wet=0;
            int temp =(Math.abs(height/2-j));
            for (int i=0; i<width; i++) { //easterly winds
                tile[i][j].rainfall=0; // reset rainfall
                if (tile[i][j].elevation==0) {
                    int yield = Math.abs(temp-height/4) + 8;
                    if (yield>wet) wet++;
                } else if (wet>0) {
                    tile[i][j].rainfall=1;
                    wet-=2+tile[i][j].elevation;
                }
            }
            wet=0;
            for (int i=width-1; i>=0; i--) { //westerly winds
                if (tile[i][j].elevation==0) {
                    int yield = Math.abs(temp-height/4) + 8;
                    if (yield>wet) wet++;
                } else if (wet>0) {
                    tile[i][j].rainfall=1;
                    wet-=2+tile[i][j].elevation;
                }
            }
        }
    }
    
    
    public void generateFertility() {
        totalFertile=0;
        totalAverage=0;
        totalBarren=0;
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                if (tile[i][j].elevation>0) {
                    tile[i][j].maxFertility= tile[i][j].calculateFertility();
                    if (tile[i][j].maxFertility==2) {
                        tile[i][j].fertility=2;
                        totalFertile++;
                    } else if (tile[i][j].maxFertility==1) {
                        tile[i][j].fertility=1;
                        totalAverage++;
                    } else {
                        totalBarren++;
                    }
                }
                
            }
        }
    }
    
    public void climateChange() {
        int change = (int)(Math.random()*3)-1;
        if (change==0) change+= (int)(Math.random()*2)*mapTempTendency;
        mapTemperature+= change;
       
       generateTemperature();
       generateMapRainfall();
       recalculateFertility();
       updateTotals(); 
    }
    
    public void recalculateFertility() {
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                if (tile[i][j].elevation>0) {
                    tile[i][j].maxFertility= tile[i][j].calculateFertility();
                } 
            }
        }
    }
    
    public Agent createOccupier(int x, int y) {
        Agent toAdd;
        if (occupier[x][y]==null && tile[x][y].elevation>0) {
            toAdd = new Agent(x, y, this, sim, false);
            occupier[x][y] = toAdd;
            return toAdd;
        }
        return null;
    }
    
    public Agent createNewTribe(int x, int y) {
        Agent toAdd = new Agent(x,y, this, sim, true);
        occupier[x][y] = toAdd;
        return toAdd;

    }
    
    public Boolean checkValidMove(int x, int y, Agent mover) {
        Boolean success = false;
        if (tile[x][y].elevation>0 ) {
            if (occupier[x][y]!=null) {
               // success=fight(mover,occupier[x][y]);
            } else {
                success=true;
            }
        } 
        return success;
    }
    
//    public Boolean fight(Agent aggressor, Agent defender) {
//        sim.log.add("Tribe "+aggressor.name+" attacks "+defender.name+"!");
//        int strength = aggressor.population/2;
//        aggressor.isFighting=true;
//        defender.isFighting=true;
//        aggressor.population-=defender.population/2;
//        defender.population-=strength;
//        if (aggressor.population>0) {
//            return true; 
//        }else {
//            return false;
//        }
//    }
    
    public void update(ArrayList<Agent> unit) {
        occupier = new Agent[width][height];
        for (int i=0; i<unit.size(); i++) {
            Agent toSet = unit.get(i);
            occupier[toSet.x][toSet.y]= toSet;
        }
        replenishFertility();
        updateTotals();
    }
    
    public void updateTotals() {
        totalLand=0;
        totalFertile=0;
        totalAverage=0;
        totalBarren=0;
        for (int i=0; i<width; i++) {
                for (int j=0; j<height; j++) {
                    if (tile[i][j].elevation>0) {
                        totalLand++;
                        if (tile[i][j].fertility==2) {
                            totalFertile++;
                        } else if (tile[i][j].fertility==1) {
                            totalAverage++;
                        } else {
                            totalBarren++;
                        }
                    }
                }
            }
    }
    
    public void replenishFertility() {
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                if (tile[i][j].fertility<tile[i][j].maxFertility){//occupier[i][j]==null &&  {
                    int chance= (int)(Math.random()*20);
                    if (chance==0) tile[i][j].fertility++;
                } else if (tile[i][j].fertility>tile[i][j].maxFertility) {
                    //decay!
                    int chance= (int)(Math.random()*2);
                    if (chance==0) tile[i][j].fertility--;
                }
            }
        }
    }
    
   public Boolean setElevation(int x, int y, int brushType) {
       Boolean success=false;
       if (tile[x][y].elevation>brushType) {
           tile[x][y].elevation--;
           calcFertility(x,y);
           calcAdjacent(x,y);
           success=true;
       } else if (tile[x][y].elevation<brushType) {
           tile[x][y].elevation++;
           calcFertility(x,y);
           calcAdjacent(x,y);
           success=true;
       }
       generateMapRainfall();
       recalculateFertility();
       updateTotals(); 
       return success;
   }
   
   public void calcFertility(int x, int y) {
        if (tile[x][y].elevation>0) {
            tile[x][y].maxFertility= tile[x][y].calculateFertility();
            tile[x][y].fertility=0;

        }
   }
   
   public void calcAdjacent(int x, int y) {
       //kludge!
       // I don't need to recalculate the entire maps edges but just the ones near the effect...
       // too much work for now...
       identifyCoast();
       
//       if (tile[x][y].elevation>0) {
//           tile[x][y].edge=0;
//       } else {
//           int[][] adjacent = new int[][]{{1,0},{0,1},{-1,0},{0,-1}};
//            int[] mask = new int[]{1,2,4,8};
//
//            for (int e=0; e<adjacent.length; e++) {
//                int nx=x+adjacent[e][0];
//                int ny=y+adjacent[e][1];
//                if (nx>0 && nx<width && ny>0 && ny<height) {
//                    if (tile[nx][ny].elevation>0) {
//                        tile[x][y].edge += mask[e];
//                    }
//                }
//            }   
//       }
   }
   
   public void setRemains(int x, int y, int type) {
       tile[x][y].remains=type;
   }
    
    
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getElevation(int x, int y) {
        return tile[x][y].elevation;
    }
    
    public int getEdge(int x, int y) {
        return tile[x][y].edge;
    }
    
    public int getTemperature(int x, int y) {
        return tile[x][y].temperature;    
    }
    
    public int getRainfall(int x, int y) {
        return tile[x][y].rainfall;    
    }
    
    public int getMaxFertility(int x, int y) {
        return tile[x][y].maxFertility;    
    }
    
    public int getFertility(int x, int y) {
        return tile[x][y].fertility;    
    }
    
    public int getRemains(int x, int y) {
        return tile[x][y].remains;    
    }
    
    public Boolean isExploited(int x, int y) {
        return tile[x][y].exploited;
    }
    
    public int getCityFertility(int x, int y) {
        int total=0;
//        int range=2;
//        for (int i=-range; i<=range; i++) {
//              for (int j=-range; j<=range; j++) {
        for (int e=0; e<cityStencil.length; e++) {
            int i=cityStencil[e][0];
            int j=cityStencil[e][1];
                  int nx=i+x;
                  int ny=j+y;
                  if (nx>0 && nx<width && ny>0 && ny<height) {
                      if (tile[nx][ny].fertility>0) total+=tile[nx][ny].fertility;
                  }
              }
//          }
        return total;
    }
    
      public void drainFertility(int x, int y) {
          if (tile[x][y].fertility>0) {
            tile[x][y].fertility--;
            tile[x][y].exploited=false;
            
              
          }
      }
      
      public void drainCityFertility(int x, int y, int usage) {
          int drain=usage;
//          int range=2;
//          for (int i=-range; i<=range; i++) {
//              for (int j=-range; j<=range; j++) {
        for (int e=0; e<cityStencil.length; e++) {
            int i=cityStencil[e][0];
            int j=cityStencil[e][1];
                  int nx=i+x;
                  int ny=j+y;
                  if (nx>0 && nx<width && ny>0 && ny<height) {
                      if (tile[nx][ny].fertility>0) {
                          tile[nx][ny].fertility--;
                          tile[nx][ny].exploited=true;
                          drain--;
                          if (drain<1) return;
                      }
                      
                  }
              }
//          }
      }
    
}
