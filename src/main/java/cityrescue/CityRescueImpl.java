package cityrescue;

import java.util.ArrayList;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;
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
    private ArrayList<Incident> incidents;
    private int stationId;
    private int unitId;
    private int incidentId;
    private int globalUnitCount;
    private int obstacleCount;

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
        this.incidents = new ArrayList<Incident>();
        this.stationId = 1;
        this.unitId = 1;
        this.globalUnitCount = 0;
        this.obstacleCount=0;
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
        this.obstacleCount++;
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
        this.obstacleCount--;
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
                    this.globalUnitCount++;
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
                        this.globalUnitCount--;
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
        
        Unit unit = null;
        Station targetStation = null;
        Station oldStation = null;

        for(int i = 0;i<this.stations.size();i++){
            if(this.stations.get(i).getStationID() == newStationId){
                targetStation = this.stations.get(i);
            }
            for(int j=0;i<this.stations.get(i).getUnits().size();j++){
                if(this.stations.get(i).getUnits().get(j).unitId == unitId){
                    unit = this.stations.get(i).getUnits().get(j);
                    oldStation = this.stations.get(i);
                }
            }
        }
        
        if(unit == null || targetStation == null || oldStation == null){
            throw new IDNotRecognisedException("ID not found");
        }

        if(unit.returnStatus() == UnitStatus.IDLE && targetStation.hasSpareCapacity()){
            oldStation.removeUnit(unitId);
            targetStation.addUnit(unit);
            return;
        }

        throw new IllegalStateException();
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        
        Unit unit = null;

        // Finding the unit
        for(int i = 0;i<this.stations.size();i++){
            for(int j = 0;j<this.stations.get(i).getUnits().size();j++){
                if(unitId == this.stations.get(i).getUnits().get(j).unitId){
                    unit = this.stations.get(i).getUnits().get(j);
                }
            }
        }

        if(unit == null){
            throw new IDNotRecognisedException("ID not recognised ");
        }

        if(unit.returnStatus() == UnitStatus.IDLE || unit.returnStatus()==UnitStatus.OUT_OF_SERVICE){
            if(outOfService){
                unit.setStatus(UnitStatus.OUT_OF_SERVICE);
                return;
            }
            unit.setStatus(UnitStatus.IDLE);
            return;
        }
        throw new IllegalStateException("Illegal Status");
    }

    @Override
    public int[] getUnitIds() {
        
        int[] ids = new int[this.globalUnitCount];
        int pointer = 0;

        for(int i=0;i<this.stations.size();i++){
            for(int j=0;j<this.stations.get(i).getUnits().size();j++){
                ids[pointer] = this.stations.get(i).getUnits().get(j).unitId;
                pointer++;
            }
        }
        return ids;
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        Unit unit = null;
        
        for(int i=0;i<this.stations.size();i++){
            for(int j=0;j<this.stations.get(i).getUnits().size();j++){
                if(this.stations.get(i).getUnits().get(j).unitId == unitId){
                    unit = this.stations.get(i).getUnits().get(j);
                }
            }
        }

        if(unit == null){
            throw new IDNotRecognisedException("ID Not Found");
        }
        return unit.viewUnit();
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        
        if(x < 0 || x > this.map.length){
            throw new InvalidLocationException("Invalid Location");  
        }
        if(y < 0 || y > this.map[0].length){
            throw new InvalidLocationException("Invalid Location");
        }
        if(severity < 1 || severity > 5){
            throw new InvalidSeverityException("Invalid Severity");
        }

        this.incidents.add(new Incident(
            this.incidentId,
            type,
            severity,
            x,y
        ));
        return this.incidentId++;
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        
        Incident incident = null;

        for(int i=0;i<this.incidents.size();i++){
            if(this.incidents.get(i).getId()==incidentId){
                incident = this.incidents.get(i);
            }
        }

        if(incident == null){
            throw new IDNotRecognisedException("Incident ID not Recognised");
        }
        if(incident.getStatus()== IncidentStatus.RESOLVED || incident.getStatus()==IncidentStatus.CANCELLED || incident.getStatus()==IncidentStatus.IN_PROGRESS){
            throw new IllegalStateException("Incident is either RESOLVED or CANCELLED or IN_PROGRESS");
        }

        incident.cancelIncident();
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        
        Incident incident = null;

        if(newSeverity<1||newSeverity>5){
            throw new InvalidSeverityException("Invalid Severity");
        }

        for(int i=0;i<this.incidents.size();i++){
            if(this.incidents.get(i).getId()==incidentId){
                incident = this.incidents.get(i);
            }
        }

        if(incident==null){
            throw new IDNotRecognisedException("ID not found");
        }

        if(incident.getStatus()==IncidentStatus.CANCELLED||incident.getStatus()==IncidentStatus.RESOLVED){
            throw new InvalidSeverityException("Illegal Incident Status");
        }

        incident.setSeverity(newSeverity);
    }

    @Override
    public int[] getIncidentIds() {
        
        int[] ids = new int[this.incidents.size()];

        for(int i=0;i<this.incidents.size();i++){
            ids[i]=this.incidents.get(i).getId();
        }
        return ids;
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        
        for(int i=0;i<this.incidents.size();i++){
            if(this.incidents.get(i).getId()==incidentId){
                return this.incidents.get(i).viewIncident();
            }
        }
        throw new IDNotRecognisedException("Id Not Found");
    }

    @Override
    public void dispatch() {
        
        for (int i=0;i<this.incidents.size();i++){
            if(this.incidents.get(i).getStatus() == IncidentStatus.REPORTED){
                Unit closestUnit= null;
                int x = this.incidents.get(i).getLoc()[0];
                int y = this.incidents.get(i).getLoc()[1];

                for (int j=0;j<this.stations.size();j++){
                    for (int k=0;k<this.stations.get(j).getUnits().size();k++){
                        Unit unit = this.stations.get(j).getUnits().get(k);

                        if(unit.canHandle(this.incidents.get(i).getType())&& unit.returnStatus() == UnitStatus.IDLE){
                            if(closestUnit == null){
                                closestUnit = unit;
                            }
                            if(closestUnit.getDistanceFrom(x, y) > unit.getDistanceFrom(x, y)){
                                closestUnit = unit;
                            }
                            if(closestUnit.getDistanceFrom(x, y) == unit.getDistanceFrom(x, y)){
                                if(closestUnit.getId() > unit.getId()){
                                    closestUnit = unit;
                                }
                            }
                        }
                    }
                }

                // Found Best Unit, now Assign the unit
                closestUnit.assign(this.incidents.get(i));
                this.incidents.get(i).setAssignedUnit(closestUnit);
            }
        }
    }

    @Override
    public void tick() {

        for(int i=0;i<this.stations.size();i++){
            for(int j=0;j<this.stations.get(i).getUnits().size();j++){
                this.stations.get(i).getUnits().get(j).Tick(this.blocked);
            }
        }
        
        this.tick++;
    }

    @Override
    public String getStatus() {
        StringBuilder sb = new StringBuilder();

        sb.append("TICK=").append(this.tick).append("\n");

        sb.append("STATIONS=").append(this.stations.size());
        sb.append(" UNITS=").append(this.globalUnitCount);
        sb.append(" INCIDENTS=").append(this.incidents.size());
        sb.append(" OBSTACLES=").append(this.obstacleCount).append("\n");
        
        sb.append("INCIDENTS\n");
        for(int i=0;i<this.incidents.size();i++){
            sb.append(this.incidents.get(i).viewIncident()).append("\n");
        }

        sb.append("UNITS\n");
        for(int i=0;i<this.stations.size();i++){
            for(int j=0;j<this.stations.get(i).getUnits().size();j++){
                sb.append(this.stations.get(i).getUnits().get(j).viewUnit()).append("\n");
            }
        }

        return sb.toString();
    }
}
