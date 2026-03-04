package cityrescue;

/**
 * Representation of the Stations that hold the Units.
 */
public class Station {

    /**Unique ID given to station */
    private final int id;
    /**Final name passed in at instanciation */
    private final String name;
    /**X position of station */
    private final int x;
    /**Y position of station */
    private final int y;

    /**Capacity of the station */
    private int capacity;
    /**Number of units currently held in station */
    private int unitCount;
    /**Array of the units */
    private Unit[] units;



    /**
     * Constructor for the Station
     * @param id Unique ID given to station
     * @param name Name of station
     * @param x X position of station
     * @param y Y position of station
     */
    public Station(int id, String name, int x, int y){

        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = 50;
        this.units = new Unit[this.capacity];
    }

    /**
     * Adds the passed in unit to the station
     * @param unit The Unit to be added
     */
    public void addUnit(Unit unit){
        if (this.unitCount < this.capacity) {
            this.units[this.unitCount] = unit;
            this.unitCount++;
        }
    }

    /**
     * Removed the unit from the station based on the ID
     * @param unitId ID of the unit to be removed
     */
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

    /**
     * Return the station ID
     * @return int Station ID
     */
    public int getStationID(){
        return this.id;
    }

    /**
     * Returns the capacity of station
     * @return int Station Capacity
     */
    public int getCapacity(){
        return this.capacity;
    }

    /**
     * Sets the new capacity of the station
     * @param capacity the new capacity of the station 
     */
    public void setCapacity(int capacity){ 
        this.capacity = capacity;
        Unit[] newUnits = new Unit[capacity];
        for(int i = 0; i < this.unitCount; i++) {
            newUnits[i] = this.units[i];
        }
        this.units = newUnits;
    }

    /**
     * Return the current number of units in the station
     * @return int number of unit in station
     */
    public int getUnitCount(){
        return this.unitCount;
    }

    /**
     * Returns the location of the station
     * @return int[] coordinates of station
     */
    public int[] getLocation(){
        return new int[] {this.x,this.y};
    }

    /**
     * Returns if the station has any available capacity
     * @return boolean True if the station has available capacity
     */
    public boolean hasSpareCapacity(){
        return this.capacity > this.unitCount;
    }
    /**
     * Returns the array of the units
     * @return Unit[] Current units
     */
    public Unit[] getUnits() {
        Unit[] activeUnits = new Unit[this.unitCount];
        for (int i = 0; i < this.unitCount; i++) {
            activeUnits[i] = this.units[i];
        }
    
        return activeUnits;
    }
}
