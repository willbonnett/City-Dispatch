package cityrescue;

public class Station {

    private final int id;
    private final String name;
    private final int x;
    private final int y;

    private int capacity;
    private int unitCount;
    private Unit[] unitIDs;




    public Station(int id, String name, int x, int y){

        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = 50;
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

}
