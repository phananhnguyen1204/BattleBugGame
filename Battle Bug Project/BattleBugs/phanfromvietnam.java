package BattleBugs;
import java.util.ArrayList;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import java.awt.Color;
import info.gridworld.actor.Actor;
import info.gridworld.actor.Rock;
import javax.swing.JOptionPane;


//TASK1: Caluculating the minimum numActs and go to the shortestPath
//Task2: Avoiding the rock
//Task3: Attacking enemy
//Task4: Running Away from enemy
//Task5: Getting around the obstacles
//Task6: Dont get the Blue Power Up when getSpeed()>=10

public class phanfromvietnam extends BattleBug2012
{
    private int countRock=0;
    private boolean isChasing=false;
    private boolean runAwayMode=false;
    private boolean  isTrapped=false;
    private Location previousLoc=null;
    private int previousDir=-1;
    //Location of the Power Up that ur bug shouldnt go
   
public phanfromvietnam(int str, int def, int spd, String name, Color col)
    {
        super(str, def, spd, name, col, "killsong.wav", "victorysong.wav");
    }
    
    public phanfromvietnam(int str, int def, int spd, String name, Color col, String ks, String vm)
    {
        super(str, def, spd, name, col, ks, vm);
    }
   //===========================================================================
    //Phase 1: Shortest Path
    //get the closest PowerUp with minimum numActs
    public Location getClosestPowerUp2(int bugDir,Location bugLoc,ArrayList<Location>puLocs){
        Location output=null;
        if(puLocs.size()==0) return output;
        double min=Integer.MAX_VALUE;
        for(Location puLoc:puLocs){
            if(min>countNumActs(bugLoc,bugDir,puLoc)){
                min=countNumActs(bugLoc,bugDir,puLoc);
                output=puLoc;
            }
        }
        return output;
    }
    //Comparte 2 Locations
    
    //return list of Locations of all Power Ups except Blue
    public ArrayList<Location> removeBluePowerUp(ArrayList<PowerUp>allPowerUp){
        ArrayList<Location>output=new ArrayList<Location>();
        for(int i=0;i<allPowerUp.size();i++){
            PowerUp currentPowerUp=allPowerUp.get(i);
            if(!currentPowerUp.getColor().equals(Color.BLUE)){
                output.add(currentPowerUp.getLocation());
                
            }
        }
        return output;
    }
    public ArrayList<Location> filterPowerUp(ArrayList<PowerUp>allPowerUp,ArrayList<Location>puLocs){
        ArrayList<Location> output=new ArrayList<>();
        
        if(getSpeed()>=10){
            puLocs=removeBluePowerUp(allPowerUp);
            return puLocs;
        }
        for(int i=0;i<puLocs.size();i++){
            Location currentPowerUp=puLocs.get(i);
            if(!giveUpPowerUp(currentPowerUp)){
                output.add(currentPowerUp);
            }
        }
        return output;
    }
    //dont chase the powerUps close to the borders
    public boolean giveUpPowerUp(Location loc){
        countRock=getNumAct()/40;
        int num1=countRock;
        int num2=26-countRock;
        if(getNumAct()%40>35){
            if(loc.getRow()==num1||loc.getRow()==num2||loc.getCol()==num1||loc.getCol()==num2){
                return true;
            }
        }
        return false;
    }
    //Compare two Locations
    public boolean equals(Location loc1,Location loc2){
        return(loc1.getRow()==loc2.getRow()&&loc1.getCol()==loc2.getCol());
    }
    
    //Distance between 2 Location
    public double getDistanceBetween(Location loc1,Location loc2){//getting the distance square
        return (Math.pow(loc1.getRow()-loc2.getRow(),2)+Math.pow(loc1.getCol()-loc2.getCol(),2));
    }
    
    //get the distance between the bug and the diagonal
    //r could be not in the grid
    //r could be positive and negative
    //for Row
    public double getDistanceBetween2A(int r,Location bugLoc){
        return Math.abs(r-bugLoc.getRow());
    }
    
    //get the distance between the bug and the diagonal
    //c could be not in the grid
    //c could be positive and negative
    //for Column
    public double getDistanceBetween2B(int c,Location bugLoc){
        return Math.abs(c-bugLoc.getCol());
    }
   
    //Checking only inside of the grid
    public boolean onTheSameDiagonal(Location loc1,Location loc2){
        return Math.abs(loc1.getRow()-loc2.getRow())==Math.abs(loc1.getCol()-loc2.getCol());
    }
   
    //Checking in and outside of the grid
    //r and c could be not valid row and column in the grid
    //this method will find the nearest diagonal
    public boolean onTheSameDiagonal2(int r,int c,Location loc){//loc is location of the bug
        return Math.abs(loc.getRow()-r)==Math.abs(loc.getCol()-c);
    }
   
    public boolean onTheSameRow(Location loc1,Location loc2){
        return loc1.getRow()==loc2.getRow();
    }
    public boolean onTheSameCol(Location loc1, Location loc2){
        return loc1.getCol()==loc2.getCol();
    }
    //checking if the bug is above Diagonal
    public boolean aboveDiagonal(Location bugLoc,Location loc){//loc is the Location we want to go
        return bugLoc.getRow()<getYCoordinateOnClosestDiagonal(bugLoc,loc);
    }
    
    public int getYCoordinateOnClosestDiagonal(Location bugLoc, Location loc){//loc is the location of where you want to go
        int row=bugLoc.getRow();
        int col=bugLoc.getCol();
        //bug in Quadrant I or Quadrant II
        if((row<loc.getRow()&&col<loc.getCol())||(row<loc.getRow()&&col>loc.getCol())){
            for(int r=-27;r<54;r++){
               
                if(onTheSameDiagonal2(r,bugLoc.getCol(),loc)){
                    return r;
                }
            }
        }
        //bug in Quadrant III or Quadrant IV
        else
            for(int r=53;r>=-27;r--){
               
                if(onTheSameDiagonal2(r,bugLoc.getCol(),loc)){
                    return r;
                }
            }
        //this will happen when there is no powerUp or loc is null
        return -1;  
    }
    
    public ArrayList<Location> getPowerUpWhenThereIsNoEmptyList(ArrayList<Actor>list){
        ArrayList<Location> output=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Actor actor=list.get(i);
            if(actor instanceof PowerUp){
                output.add(actor.getLocation());
            }
        }
        return output;
    }
    //Caculating numActs
    //precondition: toGo is not null
    public double countNumActs(Location bugLoc,int bugDir,Location toGo){
        double count=0;
        double r=bugLoc.getRow();
        double c=bugLoc.getCol();
        double x=Math.abs(toGo.getCol()-c);
        double y=Math.abs(toGo.getRow()-r);
        int step=1;
        if(getSpeed()>=10){
            step=2;
        }
        //x==y
        if(onTheSameDiagonal(bugLoc,toGo)){
            if(bugDir==getDirectionToward(toGo)){
                count=x/step;
            }
            else count=x/step+1;            
        }
        //y=0; x!=0
        else if(onTheSameRow(bugLoc,toGo)){
            if(bugDir==getDirectionToward(toGo)){
                count=x/step;
            }
            else count=x/step+1;

        }
        //x=0; y!=0
        else if(onTheSameCol(bugLoc,toGo)){
            if(bugDir==getDirectionToward(toGo)){
                count=y/step;
            }
            else count=1+y/step;
        }
        else{
         //Case 1: Quadrant II:
            if(bugLoc.getRow()<toGo.getRow()&&bugLoc.getCol()<toGo.getCol()){
                 
                if(bugDir==90){//East
                    if(!aboveDiagonal(bugLoc,toGo)){
                        count=x/step+1;
                    }
                    else{
                        //turnTo(135)
                        count=2+y/step;
                    }
                }
                else if(bugDir==135){//SouthEast
                    if(aboveDiagonal(bugLoc,toGo)){
                        count=1+y/step;
                    }
                    else{
                        count=1+x/step;
                    }

                }
                else if(bugDir==180){//South
                    if(aboveDiagonal(bugLoc,toGo)){
                        count=y/step+1;
                    }
                    else{
                        //turnTo(135)
                        count=x/step+2;
                    }

                }
                //turnTo(135)
                else{
                    if(aboveDiagonal(bugLoc,toGo)){
                        count=2+y/step;
                    }
                    else{
                        count=2+x/step;
                    }
                    //turntTo(135)//Need to check the obstacles to find the fastest path
                }
            }
            //Case 2: Quadrant I:
                if(bugLoc.getRow()<toGo.getRow()&&bugLoc.getCol()>toGo.getCol()){
                    if(bugDir==270){
                        if(!aboveDiagonal(bugLoc,toGo)){
                            count=1+x/step;
                        }
                        else{
                            //turnTo(225)
                            count=y/step+2;
                        }
                    }
                    else if(bugDir==180){
                        if(aboveDiagonal(bugLoc,toGo)){
                            count=y/step+1;
                        }
                        else{
                            //turnTo(225)
                            count=x/step+2;
                        }
                    }
                    else if(bugDir==225){
                        if(aboveDiagonal(bugLoc,toGo)){
                            count=1+y/step;
                        }
                        else{
                            count=1+x/step;
                        }
                    }
                    else{
                        //turnTo(225)
                        if(aboveDiagonal(bugLoc,toGo)){
                            count=2+y/step;
                        }
                        else{
                            count=2+x/step;
                        }
                    }
                }
                //Case 3: Quadrant III:
                if(bugLoc.getRow()>toGo.getRow()&&bugLoc.getCol()<toGo.getCol()){
                    if(bugDir==45){
                        if(aboveDiagonal(bugLoc,toGo)){
                            count=x/step+1;
                        }
                        else{
                            count=y/step+1;
                        }
                                           
                    }
                    else if(bugDir==90){
                        if(aboveDiagonal(bugLoc,toGo)){
                            count=x/step+1;
                        }
                        else{
                            //turnTo(45)
                            count=y/step+2;
                        }
                    }
                    else if(bugDir==0){
                        if(!aboveDiagonal(bugLoc,toGo)){
                            count=y/step+1;
                        }
                        else{
                            //turnTo(45)
                            count=x/step+2;
                        }
                    }
                    else{
                        //turnTo(45)
                        if(aboveDiagonal(bugLoc,toGo)){
                            count=x/step+2;
                        }
                        else{
                            count=y/step+2;
                        }
                    }
                }
                //Case 4: Quadrant IV:
                if(bugLoc.getRow()>toGo.getRow()&&bugLoc.getCol()>toGo.getCol()){
                    if(bugDir==315){
                        if(!aboveDiagonal(bugLoc,toGo)){
                            count=y/step+1;
                        }
                        else{
                            count=x/step+1;
                        }
                    }
                    else if(bugDir==270){
                        if(aboveDiagonal(bugLoc,toGo)){
                            count=x/step+1;
                        }
                        else{
                            //turnTo(315)
                            count=y/step+2;
                        }
                    }
                    else if(bugDir==0){
                        if(!aboveDiagonal(bugLoc,toGo)){
                            count=y/step+1;
                        }
                        else{
                            //turnTo(315)
                            count=x/step+2;
                        }
                    }
                    else{
                        //turnTo(315)
                        if(aboveDiagonal(bugLoc,toGo)){
                            count=x/step+2;
                        }
                        else{
                            count=y/step+2;
                        }
                    }
            }
        }
        return count;
    } 
    public void nearestPath(Location bugLoc,int bugDir,Location toGo){ //toGo is the location of PowerUp or enermy or anyLocation you want to go
        //getting all the empty adjacent Location to your bug
        ArrayList<Location> emptyList=getEmptyAdjacentLocations();
        ArrayList<Actor> occupiedList=getNeighbors();

        //meaning there is no empty location but PowerUp around
        if(emptyList.size()==0){
            emptyList=getPowerUpWhenThereIsNoEmptyList(occupiedList);
        }
        if(isTrapped==false){
            if(onTheSameDiagonal(bugLoc,toGo)){
//                if(getDistanceBetween(bugLoc,toGo)>=8){
//                    go2(toGo);
//                }
//                else  go(toGo);
                go2(toGo);
            }
            else if(onTheSameRow(bugLoc,toGo)){
//                if(getDistanceBetween(bugLoc,toGo)>=4){
//                    go2(toGo);
//                }
//                else go(toGo);
                  go2(toGo);

            }
            else if(onTheSameCol(bugLoc,toGo)){
//                if(getDistanceBetween(bugLoc,toGo)>=4){
//                    go2(toGo);
//                }
//                else go(toGo);
                go2(toGo);
            }
            else{
                //Case 1: Quadrant II:
                if(bugLoc.getRow()<toGo.getRow()&&bugLoc.getCol()<toGo.getCol()){
                    //Case 1): bugDirection: 0
                    if(bugDir==90){//East
                        if(!aboveDiagonal(bugLoc,toGo)){
                            if(getDistanceBetween2A(getYCoordinateOnClosestDiagonal(bugLoc,toGo),bugLoc)<2){
                                Location goTo=new Location(bugLoc.getRow(),bugLoc.getCol()+1);
                                go(goTo);
                            }
                            else{
                                Location goTo=new Location(bugLoc.getRow(),bugLoc.getCol()+2);
                                go2(goTo);
                            }
                        }
                        else{
                            turnTo(135);
                        }
                    }
                    else if(bugDir==135){//SouthEast
                        if(getDistanceBetween2A(toGo.getRow(),bugLoc)>=2&&getDistanceBetween2B(toGo.getCol(),bugLoc)>=2){
                            Location goTo=new Location(bugLoc.getRow()+2,bugLoc.getCol()+2);
                            go2(goTo);
                        }
                        else{
                            Location goTo=new Location(bugLoc.getRow()+1,bugLoc.getCol()+1);
                            go(goTo);
                        }

                    }
                    else if(bugDir==180){//South
                        if(aboveDiagonal(bugLoc,toGo)){
                            if(getDistanceBetween2A(getYCoordinateOnClosestDiagonal(bugLoc,toGo),bugLoc)>=2){
                                Location goTo=new Location(bugLoc.getRow()+2,bugLoc.getCol());
                                go2(goTo);
                            }
                            else{
                                Location goTo=new Location(bugLoc.getRow()+1,bugLoc.getCol());
                                go(goTo);
                            }
                        }
                        else{
                            turnTo(135);
                        }

                    }
                    else{
                        turnTo(135); //Need to check the obstacles to find the fastest path
                    }
                }
              //Case 2: Quadrant I:
                if(bugLoc.getRow()<toGo.getRow()&&bugLoc.getCol()>toGo.getCol()){
                    if(bugDir==270){
                        if(!aboveDiagonal(bugLoc,toGo)){
                            if(getDistanceBetween2A(getYCoordinateOnClosestDiagonal(bugLoc,toGo),bugLoc)<2){
                                Location goTo=new Location(bugLoc.getRow(),bugLoc.getCol()-1);
                                go(goTo);
                            }
                            else{
                                Location goTo=new Location(bugLoc.getRow(),bugLoc.getCol()-2);
                                go2(goTo);
                            }
                        }
                        else{
                            turnTo(225);
                        }
                    }
                    else if(bugDir==180){
                        if(aboveDiagonal(bugLoc,toGo)){
                            if(getDistanceBetween2A(getYCoordinateOnClosestDiagonal(bugLoc,toGo),bugLoc)<2){
                                Location goTo=new Location(bugLoc.getRow()+1,bugLoc.getCol());
                                go(goTo);
                            }
                            else{
                                Location goTo=new Location(bugLoc.getRow()+2,bugLoc.getCol());
                                go2(goTo);
                            }
                        }
                        else{
                            turnTo(225);
                        }
                    }
                    else if(bugDir==225){
                        if(getDistanceBetween2A(toGo.getRow(),bugLoc)>=2&&getDistanceBetween2B(toGo.getCol(),bugLoc)>=2){
                            Location goTo=new Location(bugLoc.getRow()+2,bugLoc.getCol()-2);
                            go2(goTo);
                        }
                        else{
                            Location goTo=new Location(bugLoc.getRow()+1,bugLoc.getCol()-1);
                            go(goTo);
                        }
                    }
                    else{
                        turnTo(225);
                    }
                }
                //Case 3: Quadrant III:
                if(bugLoc.getRow()>toGo.getRow()&&bugLoc.getCol()<toGo.getCol()){
                    if(bugDir==45){
                        if(getDistanceBetween2A(toGo.getRow(),bugLoc)>=2&&getDistanceBetween2B(toGo.getCol(),bugLoc)>=2){
                            Location goTo=new Location(bugLoc.getRow()-2,bugLoc.getCol()+2);
                            go2(goTo);
                        }
                        else{
                            Location goTo=new Location(bugLoc.getRow()-1,bugLoc.getCol()+1);
                            go(goTo);
                        }                   
                    }
                    else if(bugDir==90){
                        if(aboveDiagonal(bugLoc,toGo)){
                            if(getDistanceBetween2A(getYCoordinateOnClosestDiagonal(bugLoc,toGo),bugLoc)<2){
                                Location goTo=new Location(bugLoc.getRow(),bugLoc.getCol()+1);
                                go(goTo);
                            }
                            else{
                                Location goTo=new Location(bugLoc.getRow(),bugLoc.getCol()+2);
                                go2(goTo);
                            }
                        }
                        else{
                            turnTo(45);
                        }
                    }
                    else if(bugDir==0){
                        if(!aboveDiagonal(bugLoc,toGo)){
                            if(getDistanceBetween2A(getYCoordinateOnClosestDiagonal(bugLoc,toGo),bugLoc)<2){
                                Location goTo=new Location(bugLoc.getRow()-1,bugLoc.getCol());
                                go(goTo);
                            }
                            else{
                                Location goTo=new Location(bugLoc.getRow()-2,bugLoc.getCol());
                                go2(goTo);
                            }
                        }
                        else{
                            turnTo(45);
                        }
                    }
                    else{
                        turnTo(45);
                    }
                }
                //Case 4: Quadrant IV:
                if(bugLoc.getRow()>toGo.getRow()&&bugLoc.getCol()>toGo.getCol()){
                    if(bugDir==315){
                        if(getDistanceBetween2A(toGo.getRow(),bugLoc)>=2&&getDistanceBetween2B(toGo.getCol(),bugLoc)>=2){                      
                            Location goTo=new Location(bugLoc.getRow()-2,bugLoc.getCol()-2);
                            go2(goTo);
                        }
                        else{
                            Location goTo=new Location(bugLoc.getRow()-1,bugLoc.getCol()-1);
                            go(goTo);
                        }
                    }
                    else if(bugDir==270){
                        if(aboveDiagonal(bugLoc,toGo)){
                            if(getDistanceBetween2A(getYCoordinateOnClosestDiagonal(bugLoc,toGo),bugLoc)<2){
                                Location goTo=new Location(bugLoc.getRow(),bugLoc.getCol()-1);
                                go(goTo);
                            }
                            else{
                                Location goTo=new Location(bugLoc.getRow(),bugLoc.getCol()-2);
                                go2(goTo);
                            }
                        }
                        else{
                            turnTo(315);
                        }
                    }
                    else if(bugDir==0){
                        if(!aboveDiagonal(bugLoc,toGo)){
                            if(getDistanceBetween2A(getYCoordinateOnClosestDiagonal(bugLoc,toGo),bugLoc)<2){
                                Location goTo=new Location(bugLoc.getRow()-1,bugLoc.getCol());
                                go(goTo);
                            }
                            else{
                                Location goTo=new Location(bugLoc.getRow()-2,bugLoc.getCol());
                                go2(goTo);
                            }
                        }
                        else{
                            turnTo(315);
                        }
                    }
                    else{
                        turnTo(315);
                    }
                }
            }
        }
        else{
            if(!canMove()){
                turnTo(getDirectionToward(closestEmpty(bugLoc,toGo,emptyList)));
            }
            else{
                go(closestEmpty(bugLoc,toGo,emptyList));
                isTrapped=false;
            }
            
        }
    }
    
    //move and turn to the loc with calling move()
    public void go(Location loc){ 
        if(getDirection()==getDirectionToward(loc)){
            move();
        }
        else{
            turnTo(getDirectionToward(loc));
        }
    }
    //move and turn to the loc with calling move2()
    public void go2(Location loc){
        if(getDirection()==getDirectionToward(loc)){
            move2();
        }
        else{
            turnTo(getDirectionToward(loc));
        }
    }

    
    
    
    //==========================================================================
   //Phase 2: Avoid rock failing
    public void avoidRock(Location bugLoc,int bugDir){
        
        //Another way: go to the center of the grid and dont care about Quadrant
//     Case1: Quadrant I:
       if(bugLoc.getRow()<13&&bugLoc.getCol()>13){
           Location toGo=new Location(bugLoc.getRow()+1,bugLoc.getCol()-1);
           nearestPath(bugLoc,bugDir,toGo);
       }
       
       //Case 2: Quadrant II:
       else if(bugLoc.getRow()<13&&bugLoc.getCol()<13){
           Location toGo=new Location(bugLoc.getRow()+1,bugLoc.getCol()+1);
           nearestPath(bugLoc,bugDir,toGo);
       }
       
       //Case 3: Quadrant III:
       else if (bugLoc.getRow()>13&&bugLoc.getCol()<13){
           Location toGo=new Location(bugLoc.getRow()-1,bugLoc.getCol()+1);
           nearestPath(bugLoc,bugDir,toGo);
       }
       //Case 4: Quadrant IV:
       else if(bugLoc.getRow()>13&&bugLoc.getCol()>13){
           Location toGo=new Location(bugLoc.getRow()-1,bugLoc.getCol()-1);
           nearestPath(bugLoc,bugDir,toGo);
       }
       
       //Case 5:in the middle of the top:
       else if(bugLoc.getCol()==13&&bugLoc.getRow()<13){
           Location toGo=new Location(bugLoc.getRow()+1,13);
           nearestPath(bugLoc,bugDir,toGo);
       }
       //Case 6: in the middle of the bottom
       else if(bugLoc.getCol()==13&&bugLoc.getRow()>13){
           Location toGo=new Location(bugLoc.getRow()-1,13);
           nearestPath(bugLoc,bugDir,toGo);
       }
       //Case 7:int the middle of the left
       else if(bugLoc.getRow()==13&&bugLoc.getCol()<13){
           Location toGo=new Location(13,bugLoc.getCol()+1);
           nearestPath(bugLoc,bugDir,toGo);
       }
       //Case 8: int the middle of the right
       else if(bugLoc.getRow()==13&&bugLoc.getCol()>13){
           Location toGo=new Location(13,bugLoc.getCol()-1);
           nearestPath(bugLoc,bugDir,toGo);
       }
       //in case there is an error
       else{
           Location toGo=new Location(13,13);
           nearestPath(bugLoc,bugDir,toGo);
       }
       
    }
   
   //Checking if the bug is valid in new grid so the bug dont have to avoid the Rock failing
    public boolean isValidInNewGrid(Location bugLoc){
        int bugDir=getDirection();
        countRock=getNumAct()/40;
        int num=countRock+2;
        if(getSpeed()>=10){
            num=countRock+3;
        }
        int row=bugLoc.getRow();
        int col=bugLoc.getCol();

         
        
        return((row>=num)&&row<=(26-num)&&col>=num&&col<=(26-num));
    }

    
    
    
    
    //==========================================================================
    //Phase 3: Get around obstacles:
    //get closestEmpty adjacent to your bug:
    //return null if there is no empty locaion
    public Location closestEmpty(Location bugLoc,Location toGo,ArrayList<Location> emptyList){
        if(emptyList.size()==0) return null;
        Location output=null;
        double minDistance=Integer.MAX_VALUE;
        for(int i=0;i<emptyList.size();i++){
            if(minDistance>getDistanceBetween(toGo,emptyList.get(i))){
                output=emptyList.get(i);
                minDistance=getDistanceBetween(toGo,emptyList.get(i));
            }
        }
        return output;
    }
    
    
    
    
    //==========================================================================
    //Phase 4: Attack and chase enemies
    //a) Chasing Enemy: not the closest one but row major order (need to find the closest one)
    //return a index of an enemy
    public Location chasingEnemy(ArrayList<BattleBug>enemies){
        Location bugLoc=getLocation();
        int bugDir=getDirection();
        Location output=null;
        isChasing=false;
        
        if(enemies.size()==0){
            isChasing=false;
            return output;
        }
        else{
            for(int i=0;i<enemies.size();i++){
                BattleBug enemy=enemies.get(i);
                Location enemyLoc=enemy.getLocation();
                if(getStrength()-enemy.getDefense()>=3){
                    //if enemy cant kill phan
//                    if(getDefense()>enemy.getStrength()-3){
                        output=enemy.getLocation();
                        isChasing=true;
//                    }
//                    //if enemy can kill phan
//                    //don't chase enemy
//                    //need smarter method to attack enemy
//                    else{
//                        
//                    }
                }                
            }            
            return output;
        }
    }
    //get Location of enemy that can kill phan but phan can kill him too
    public Location getDangerousEnemy(ArrayList<BattleBug>enemies){
        Location bugLoc=getLocation();
        int bugDir=getDirection();
        Location output=null;
        if(enemies.size()==0){
            return output;
        }
        else{
            for(int i=0;i<enemies.size();i++){
                BattleBug enemy=enemies.get(i);
                Location enemyLoc=enemy.getLocation();
                if((getStrength()-enemy.getDefense())>=3 && getDefense()<=(enemy.getStrength()-3)){
                        output=enemy.getLocation();
                }                
            }            
            return output;
        }        
    }
    
//    //precondition: enemy bug can kill you and you can kill enemy
//    // return true if phan can kill a dangerous enemy
    //enemyLoc is a Location of dangerous Enemy
    public boolean canKill(Location enemyLoc,Location bugLoc,int bugDir){
        int r=bugLoc.getRow();
        int c=bugLoc.getCol();
        int enemyRow=enemyLoc.getRow();
        int enemyCol=enemyLoc.getCol();
        int x=Math.abs(c-enemyCol);
        int y=Math.abs(r-enemyRow);
        //Case A: Phan can turn and attack instantly
        //I. Strength<10
        if(getStrength()<10){
            if(onTheSameCol(bugLoc,enemyLoc)&&y==1){
                return true;
            }
            if(onTheSameRow(bugLoc,enemyLoc)&&x==1){
                return true;
            }
            if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==1){
                return true;
            }
        }
        else if(getStrength()>=10&&getStrength()<20){
            if(onTheSameCol(bugLoc,enemyLoc)&&y==2){
                return true;
            }
            if(onTheSameRow(bugLoc,enemyLoc)&&x==2){
                return true;
            }
            if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==2){
                return true;
            }            
        }
        else{
            if(onTheSameCol(bugLoc,enemyLoc)&&y==3){
                return true;
            }
            if(onTheSameRow(bugLoc,enemyLoc)&&x==3){
                return true;
            }
            if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==3){
                return true;
            }                 
        }
        
        //Case B: Phan can move 1 and attack instantly
        if(getSpeed()<10){
            if(bugDir==getDirectionToward(enemyLoc)){
                if(getStrength()<10){
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==3){
                        return true;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==3){
                        return true;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==3){
                        return true;
                    }
                }
                else if(getStrength()>=10&&getStrength()<20){
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==4){
                        return true;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==4){
                        return true;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==4){
                        return true;
                    }            
                }
                else{
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==5){
                        return true;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==5){
                        return true;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==5){
                        return true;
                    }                 
                }
            }
        }
        //Case C; phan can move 2 and kill enemy instantly
        if(getSpeed()>=10){
            if(bugDir==getDirectionToward(enemyLoc)){
                if(getStrength()<10){
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==2){
                        return true;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==2){
                        return true;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==2){
                        return true;
                    }
                }
                else if(getStrength()>=10&&getStrength()<20){
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==3){
                        return true;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==3){
                        return true;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==3){
                        return true;
                    }            
                }
                else{
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==4){
                        return true;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==4){
                        return true;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==4){
                        return true;
                    }                 
                }
            }
        }        
        return false;
    }
   //THere is something wrong with my attacking method but I still didn't figure out why 
    public void smartKill(Location enemyLoc,Location bugLoc,int bugDir){
        int r=bugLoc.getRow();
        int c=bugLoc.getCol();
        int enemyRow=enemyLoc.getRow();
        int enemyCol=enemyLoc.getCol();
        int x=Math.abs(c-enemyCol);
        int y=Math.abs(r-enemyRow);
        //Case A: Phan can turn and attack instantly
        //I. Strength<10
        if(getStrength()<10){
            if(onTheSameCol(bugLoc,enemyLoc)&&y==1){
                turnTo(getDirectionToward(enemyLoc));
                attack();
                return;
            }
            if(onTheSameRow(bugLoc,enemyLoc)&&x==1){
                turnTo(getDirectionToward(enemyLoc));
                attack();
                return;
            }
            if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==1){
                turnTo(getDirectionToward(enemyLoc));
                attack();
                return;
            }
        }
        else if(getStrength()>=10&&getStrength()<20){
            if(onTheSameCol(bugLoc,enemyLoc)&&y==2){
                turnTo(getDirectionToward(enemyLoc));
                attack();
                return;
            }
            if(onTheSameRow(bugLoc,enemyLoc)&&x==2){
                turnTo(getDirectionToward(enemyLoc));
                attack();
                return;
            }
            if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==2){
                turnTo(getDirectionToward(enemyLoc));
                attack();
                return;
            }            
        }
        else{
            if(onTheSameCol(bugLoc,enemyLoc)&&y==3){
                turnTo(getDirectionToward(enemyLoc));
                attack();
                return;
            }
            if(onTheSameRow(bugLoc,enemyLoc)&&x==3){
                turnTo(getDirectionToward(enemyLoc));
                attack();
                return;
            }
            if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==3){
                turnTo(getDirectionToward(enemyLoc));
                attack();
                return;
            }                 
        }
        
        //Case B: Phan can move 1 and attack instantly
        if(getSpeed()<10){
            if(bugDir==getDirectionToward(enemyLoc)){
                if(getStrength()<10){
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==3){
                        move();
                        attack();
                        return;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==3){
                        move();
                        attack();
                        return;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==3){
                        move();
                        attack();
                        return;
                    }
                }
                else if(getStrength()>=10&&getStrength()<20){
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==4){
                        move();
                        attack();
                        return;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==4){
                        move();
                        attack();
                        return;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==4){
                        move();
                        attack();
                        return;
                    }            
                }
                else{
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==5){
                        move();
                        attack();
                        return;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==5){
                        move();
                        attack();
                        return;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==5){
                        move();
                        attack();
                        return;
                    }                 
                }
            }
        }
        //Case C; phan can move 2 and kill enemy instantly
        if(getSpeed()>=10){
            if(bugDir==getDirectionToward(enemyLoc)){
                if(getStrength()<10){
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==2){
                        move2();
                        attack();
                        return;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==2){
                        move2();
                        attack();
                        return;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==2){
                        move2();
                        attack();
                        return;
                    }
                }
                else if(getStrength()>=10&&getStrength()<20){
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==3){
                        move2();
                        attack();
                        return;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==3){
                        move2();
                        attack();
                        return;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==3){
                        move2();
                        attack();
                        return;
                    }            
                }
                else{
                    if(onTheSameCol(bugLoc,enemyLoc)&&y==4){
                        move2();
                        attack();
                        return;
                    }
                    if(onTheSameRow(bugLoc,enemyLoc)&&x==4){
                        move2();
                        attack();
                        return;
                    }
                    if(onTheSameDiagonal(bugLoc,enemyLoc)&&x==4){
                        move2();
                        attack();
                        return;
                    }                 
                }
            }
        }        
        System.out.println("phan is not attacking");
    }
    
//    //Attack enemy when in range
//    //precondition: enemyLoc!=null && enemies.size()>0
//    public int getDirectionOfEnemyToAttack(Location enemyLoc,ArrayList<BattleBug> enemies){
//        int output=0;
//        for(BattleBug enemy:enemies){
//            if(enemyLoc.getRow()==enemy.getLocation().getRow()&&enemyLoc.getCol()==enemy.getLocation().getCol()){
//                output=enemy.getDirection();
//            }
//        }
//        return output;
//    }
    public void readyToAttack(int bugDir, Location enemyLoc, Location bugLoc){
        if(getStrength()<10){
            if(bugDir==getDirectionToward(enemyLoc)&&getDistanceBetween(bugLoc,enemyLoc)<=2){
                if(onTheSameDiagonal(enemyLoc,bugLoc)||onTheSameRow(enemyLoc,bugLoc)||onTheSameCol(enemyLoc,bugLoc)){
                    attack();
                    System.out.println("The bug is attacking");
                    
                }
            }
        }
        else if(getStrength()<20){
            if(bugDir==getDirectionToward(enemyLoc)&&getDistanceBetween(bugLoc,enemyLoc)<=8){
                if(onTheSameDiagonal(enemyLoc,bugLoc)||onTheSameRow(enemyLoc,bugLoc)||onTheSameCol(enemyLoc,bugLoc)){
                    attack();
                    System.out.println("The bug is attacking");
                }
            }
        }
        else{
            if(bugDir==getDirectionToward(enemyLoc)&&getDistanceBetween(bugLoc,enemyLoc)<=18){
                if(onTheSameDiagonal(enemyLoc,bugLoc)||onTheSameRow(enemyLoc,bugLoc)||onTheSameCol(enemyLoc,bugLoc)){
                    attack();
                    System.out.println("The bug is attacking");
                }
            }
        }
        System.out.println("The bug is not attacking");
    }
    
    
    
    //==========================================================================
    //Phase 5: Running away from enemy
    //return null if enemies.size()=0 and there is no enemies can attack you
    //return Location of the enemy can attack you (last enemy in row-major order)
    public Location scanning(ArrayList<BattleBug> enemies){
        Location output=null;
        runAwayMode=false;
        if(enemies.size()!=0){
            for(BattleBug enemy:enemies){
                if(enemy.getStrength()-getDefense()>=3&&((BattleBug2012)enemy).getAmmo()>0){
                    runAwayMode=true;
                    output=enemy.getLocation();
                }
            }
        }
        return output;
    }
    
    //precondition: enemyLoc is not null
    //Solution 2:
    public void runAway2(int bugDir,Location bugLoc,Location enemyLoc){
        int beginRow=getNumAct()/40;
        int endRow=26-getNumAct()/40;
        int beginCol=getNumAct()/40;
        int endCol=26-getNumAct()/40;
        //Case 1: Quadrant I:
        if(bugLoc.getRow()<enemyLoc.getRow()&&bugLoc.getCol()>enemyLoc.getCol()){
           Location goTo=new Location(beginRow,endCol);
           nearestPath(bugLoc,bugDir,goTo);
        }
        //Case 2: Quadrant II:
        else if(bugLoc.getRow()<enemyLoc.getRow()&&bugLoc.getCol()<enemyLoc.getCol()){
           Location goTo=new Location(beginRow,beginCol);
           nearestPath(bugLoc,bugDir,goTo);
        }
        //Case 3: Quadrant III:
        else if(bugLoc.getRow()>enemyLoc.getRow()&&bugLoc.getCol()<enemyLoc.getCol()){
           Location goTo=new Location(endRow,beginCol);
           nearestPath(bugLoc,bugDir,goTo);
        }
        //Case 4: Quadrant IV:
        else if(bugLoc.getRow()>enemyLoc.getRow()&&bugLoc.getCol()>enemyLoc.getCol()){
            Location goTo=new Location(endRow,endCol);
            nearestPath(bugLoc,bugDir,goTo);
        }
        //Case 5: in the middle top:
        else if(bugLoc.getRow()<enemyLoc.getRow()&&bugLoc.getCol()==enemyLoc.getCol()){
            if(enemyLoc.getCol()<=13){
                Location goTo=new Location(beginRow,endCol);
                nearestPath(bugLoc,bugDir,goTo);
            }
            else{
                Location goTo=new Location(beginRow,beginCol);
                nearestPath(bugLoc,bugDir,goTo);
            }
            
        }
        //Case 6: in the middle bottom:
        else if(bugLoc.getRow()>enemyLoc.getRow()&&bugLoc.getCol()==enemyLoc.getCol()){
            if(enemyLoc.getCol()<=13){
                Location goTo=new Location(endRow,endCol);
                nearestPath(bugLoc,bugDir,goTo);
            }
            else{
                Location goTo=new Location(endRow,beginCol);
                nearestPath(bugLoc,bugDir,goTo);
            }

        }
        //Case 7:in the middle left
        else if(bugLoc.getRow()==enemyLoc.getRow()&&bugLoc.getCol()<enemyLoc.getCol()){
            if(enemyLoc.getRow()<=13){
                Location goTo=new Location(endRow,beginCol);
                nearestPath(bugLoc,bugDir,goTo);
            }
            else{
                Location goTo=new Location(beginRow,beginCol);
                nearestPath(bugLoc,bugDir,goTo);
            }

        }
        //Case 8: in the middle right
        else{
            if(enemyLoc.getRow()<=13){
                Location goTo=new Location(endRow,endCol);
                nearestPath(bugLoc,bugDir,goTo);
            }
            else{
                Location goTo=new Location(beginRow,endCol);
                nearestPath(bugLoc,bugDir,goTo);
            }

        }
  
    }
    
    //checking if obstacles are TombStone or REgulator
    public boolean checkingObstacles(ArrayList<Actor> obstacles,Location bugLoc,int bugDir){
        if(obstacles.size()==0) return false;
        Location obstacleInfront=bugLoc.getAdjacentLocation(bugDir);
        for(Actor obstacle:obstacles){
            if(equals(obstacle.getLocation(),obstacleInfront)){
                return true;
            }
        }
        return false;
    }
    //==========================================================================
    //ACT Method:
    public void act(                                                                                                                                                                                                                                                                                    )
    {  
        //Declaration:
        //Call the getActors() method to get all the Actors that are within 3 units of your BattleBug, store the result in a variable named actors
        ArrayList<Actor> actors=getActors();
        ArrayList<Actor> obstacles=new ArrayList<>();
        for(int i=0;i<actors.size();i++){
            Actor currentActor=actors.get(i);
            if(currentActor instanceof TombStone||currentActor instanceof Regulator){
                obstacles.add(currentActor);
            }
        }
        int bugDir=getDirection();
        Location bugLoc=getLocation();
        if(!canMove()&&checkingObstacles(obstacles,bugLoc,bugDir)){
            previousDir=bugDir;
            previousLoc=new Location(bugLoc.getRow(),bugLoc.getCol());
        }
        if(previousDir==bugDir&&equals(bugLoc,previousLoc)){
            isTrapped=true;
        }
        previousDir=bugDir;
        previousLoc=new Location(bugLoc.getRow(),bugLoc.getCol());
        //getting all the power Ups
        ArrayList<PowerUp>pu=getPowerUps();
        
        ArrayList<Location> puLocs=getPowerUpLocs();
        
        puLocs=filterPowerUp(pu,puLocs);
 
        //Declare and instantiate a new ArrayList that can only hold objects of type BattleBug
        //Name it enemies, this will store the nearby enemies.
        ArrayList<BattleBug> enemies=new ArrayList<>();
        for(int i=0;i<actors.size();i++){
           Actor curr=actors.get(i);
           if(curr instanceof BattleBug){
               enemies.add((BattleBug)curr);
           }            
        }
        //======================================================================
        //Running away from enemies Phase:
        //null if enemies.size()==0 or there is no enemies can attack you
        //not null if there is an enemy can attack you
        Location enemyChasingYouLoc=scanning(enemies);
        //======================================================================
        //Chasing and Attacking Phase:
        //enemyLoc is null when there is no enemy to attack or no enemy in enemis
        //enemyLoc is not null when there is an enemy to attack;
        Location enemyLoc=chasingEnemy(enemies);
//        int enemyDir=0;
//        //line +++
//        if(enemyLoc!=null){
//            enemyDir=getDirectionOfEnemyToAttack(enemyLoc,enemies);
//        }
        if(isTrapped&&isChasing){
            isTrapped=false;
        }
        //Testing
        Location closestPU=getClosestPowerUp2(bugDir,bugLoc,puLocs);//get closest PU and minimum numAct
        Location centerOfTheGrid=new Location(13,13);
        if(getAmmo()==0){
            isChasing=false;
        }
        if(getNumAct()%40>=38&&!isValidInNewGrid(bugLoc)){
            avoidRock(bugLoc,bugDir);
        }
        else{
            
                //there is an enemy that their defense<= phan's strength-3
                //enemyLoc is not null
               //enemyDir is set
                  //line +++ executed
                if(isChasing){
                    Location dangerousEnemyLoc=getDangerousEnemy(enemies);
                    if(dangerousEnemyLoc==null){
                        //There is something wrong with my attacking method but I still didn't figure out
                        readyToAttack(bugDir,bugLoc,enemyLoc);
                        nearestPath(bugLoc,bugDir,enemyLoc);
                        readyToAttack(bugDir,enemyLoc,bugLoc);
//                        if(bugDir==getDirectionToward(enemyLoc)){
//                            attack();
//                        }
                    }
                    else{
                        if(canKill(dangerousEnemyLoc,bugLoc,bugDir)){
                            smartKill(dangerousEnemyLoc,bugLoc,bugDir);
                            if(bugDir==getDirectionToward(dangerousEnemyLoc)){
                                attack();
                            }
                            

                        }
                        else{

                            turnTo(getDirectionToward(dangerousEnemyLoc));
                            attack();
                            

                        }
                    }
                    //SOlution 2:
                }
                //isChasing=false;
                //enemyLoc could be null 
                //or could be the location of an enemy whose defense>phan's strength-3  
                //or could be the location of an enemy that can kill you but you can't kill him easily
                else{
                    //There is an enemy whose strength()>=ur bug's defense+3
                    //scanning method returns the Location of an enemy
                    if(runAwayMode){
                        runAway2(bugDir,bugLoc,enemyChasingYouLoc);
                    }
                    //scanning method returns null              
                    else{
                            if(closestPU!=null){
                                nearestPath(bugLoc,bugDir,closestPU);
                            }
                                // if there are no PowerUps, the bug will move to the center of the grid to avoid rock
                            else nearestPath(bugLoc,bugDir,centerOfTheGrid);
                        }

                }            
        }
   
    }
    
    @Override
    public String victory(){
        JOptionPane.showMessageDialog(null,"SIUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
        return "SIUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU!";
    }
}