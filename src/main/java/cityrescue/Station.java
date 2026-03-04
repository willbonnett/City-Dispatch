package cityrescue;

public class Station {

    private final int id;
    private final String name;
    private final int x;
    private final int y;

    private int capacity;
    private int unitCount;
    private Unit[] units;




    public Station(int id, String name, int x, int y){

        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = 50;
        this.units = new Unit[this.capacity];
    }

    public void addUnit(Unit unit){
        if (this.unitCount < this.capacity) {
            this.units[this.unitCount] = unit;
            this.unitCount++;
        }
    }

    public void removeUnit(int unitId){
        for(int i = 0; i < this.unitCount; i++){
            if(this.units[i].getId() == unitId){ 
                for(int j = i; j < this.unitCount - 1; j++){
                    this.units[j] = this.units[j+1];
                }
                this.units[this.unitCount - 1] = null;
                this.unitCount--;
                return;
            }
        }
    }

    public int getStationID(){
        return this.id;
    }

    public int getCapacity(){
        return this.capacity;
    }

    public void setCapacity(int capacity){ 
        this.capacity = capacity;
        Unit[] newUnits = new Unit[capacity];
        for(int i = 0; i < this.unitCount; i++) {
            newUnits[i] = this.units[i];
        }
        this.units = newUnits;
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
    public Unit[] getUnits() {
    Unit[] activeUnits = new Unit[this.unitCount];
    for (int i = 0; i < this.unitCount; i++) {
        activeUnits[i] = this.units[i];
    }
    
    return activeUnits;
}
}
