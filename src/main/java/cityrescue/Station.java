package cityrescue;

import java.util.ArrayList;

public class Station {

    private final int id;
    private final String name;
    private final int x;
    private final int y;

    private int capacity;
    private int unitCount;
    private ArrayList<Unit> units;




    public Station(int id, String name, int x, int y){

        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = 50;
    }

    public void addUnit(Unit unit){
        this.units.add(unit);
        this.unitCount++;
    }

    public void removeUnit(int unitId){
        for(int i=0;i<this.units.size();i++){
            if(this.units.get(i).unitId == unitId){
                this.units.remove(i);
                this.unitCount--;
            }
        }
    }

    public int getStationID(){
        return this.id;
    }

    public int getCapacity(){
        return this.capacity;
    }

    public void setCapaity(int capacity){
        this.capacity=capacity;
    }

    public int getUnitCount(){
        return this.unitCount;
    }

    public int[] getLocation(){
        return new int[] {this.x,this.y};
    }

    public boolean hasSpareCapacity(){
        return this.capacity > this.unitCount;
    }
    public ArrayList<Unit> getUnits(){
        return this.units;
    }
}
