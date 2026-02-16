package cityrescue;

import java.util.ArrayList;
import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import cityrescue.exceptions.IDNotRecognisedException;
import cityrescue.exceptions.InvalidCapacityException;
import cityrescue.exceptions.InvalidGridException;
import cityrescue.exceptions.InvalidLocationException;
import cityrescue.exceptions.InvalidNameException;
import cityrescue.exceptions.InvalidSeverityException;
import cityrescue.exceptions.InvalidUnitException;

/**
 * CityRescueImpl (Starter)
 *
 * Your task is to implement the full specification.
 * You may add additional classes in any package(s) you like.
 */
public class CityRescueImpl implements CityRescue {

    // TODO: add fields (map, arrays for stations/units/incidents, counters, tick, etc.)

    private int[][] map;
    private boolean[][] blocked;

    private int tick;

    // Counters
    private ArrayList<Station> stations;
    private int stationId;
    private int unitId;

    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        
        if(width <= 0 || height <= 0){
            throw new InvalidGridException("Invalid Width/Height");
        }

        // Create Grid
        this.map = new int[width][height];
        this.blocked = new boolean[width][height];


        // Reset Ticks and ID values
        this.tick = 0;
        this.stations = new ArrayList<Station>();
        this.stationId = 1;
        this.unitId = 1;
    }

    @Override
    public int[] getGridSize() {
        
        return new int[] {this.map.length, this.map[0].length};
    }

    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {

        if(x < 0 || x > this.map.length){
            throw new InvalidLocationException("Invalid Location");  
        }
        if(y < 0 || y > this.map[0].length){
            throw new InvalidLocationException("Invalid Location");
        }
        this.blocked[x][y] = true;
    }

    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        
        if(x < 0 || x > this.map.length){
            throw new InvalidLocationException("Invalid Location");  
        }
        if(y < 0 || y > this.map[0].length){
            throw new InvalidLocationException("Invalid Location");
        }
        this.blocked[x][y] = false;
    }

    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        
        if(name == null || name.isEmpty()){
            throw new InvalidNameException("Invalid Name");
        }
        if(x < 0 || x > this.map.length){
            throw new InvalidLocationException("Invalid Location out of bounds");  
        }
        if(y < 0 || y > this.map[0].length){
            throw new InvalidLocationException("Invalid Location out of bounds");
        }
        if(this.blocked[x][y] == true){
            throw new InvalidLocationException("Invalid Location is blocked");
        }
        
        // Add new Station to ArrayList and initialise value
        this.stations.add(new Station(this.stationId,name,x,y));
        return this.stationId++;
    }

    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        
        for(Station s:this.stations){
            if(s.getStationID()==stationId){
                if(s.getUnitCount()!=0){
                    throw new IllegalStateException("Station still contains Units");
                }
                this.stations.remove(s);
                return;
            }
        }
        throw new IDNotRecognisedException("ID not Found");
    }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {

        for(Station s:this.stations){
            if(s.getStationID()==stationId){
                if(s.getCapacity() > maxUnits){
                    throw new InvalidCapacityException("New Capacity less than old");
                }

                s.setCapaity(maxUnits);
                return;
            }
        }
        throw new IDNotRecognisedException("ID not Found");
    }

    @Override
    public int[] getStationIds() {
        
        int[] ids = new int[this.stations.size()];
        for(int i = 0;i < this.stations.size();i++){
            ids[i] = this.stations.get(i).getStationID();
        }
        return ids;
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {

        for(int i = 0; i<this.stations.size();i++){
            if(this.stations.get(i).getStationID()== stationId){
                if(this.stations.get(i).hasSpareCapacity()){
                    switch(type){
                        case POLICE_CAR ->
                            this.stations.get(i).addUnit(new PoliceCar(
                                this.unitId,
                                this.stations.get(i).getLocation()[0],
                                this.stations.get(i).getLocation()[1],
                                this.stations.get(i).getStationID()
                            ));
                        case FIRE_ENGINE ->
                            this.stations.get(i).addUnit(new FireEngine(
                                this.unitId,
                                this.stations.get(i).getLocation()[0],
                                this.stations.get(i).getLocation()[1],
                                this.stations.get(i).getStationID()
                            ));
                        case AMBULANCE ->
                            this.stations.get(i).addUnit(new Ambulance(
                                this.unitId,
                                this.stations.get(i).getLocation()[0],
                                this.stations.get(i).getLocation()[1],
                                this.stations.get(i).getStationID()
                            ));
                        default -> throw new InvalidUnitException("Invalid Unit Type");
                    }
                    return this.unitId++;
                }
                throw new IllegalStateException("Station full");
            }
            throw new IDNotRecognisedException("StationId not recognised");
        }
        return -1;
    }

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        
        for(int i = 0; i<this.stations.size();i++){
            for(int j=0;j<this.stations.get(i).getUnits().size();j++){
                if(this.stations.get(i).getUnits().get(j).unitId == unitId){
                    if(this.stations.get(i).getUnits().get(j).returnStatus() == UnitStatus.IDLE){
                        this.stations.get(i).removeUnit(unitId);
                        return;
                    }
                    throw new IllegalStateException("Unit not in IDLE status");
                }
            }
            throw new IDNotRecognisedException("Unit ID not found");
        }
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getUnitIds() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getIncidentIds() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void dispatch() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void tick() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getStatus() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
