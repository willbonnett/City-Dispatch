package cityrescue;

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



    private int[][] map;
    private boolean[][] blocked;

    private int tick;

    private static final int maxStations = 20;
    private static final int maxIncidents = 200;

    // Counters
    private Station[] stations;
    private int stationCount;
    private Incident[] incidents;
    private int incidentCount;

    private int stationId;
    private int unitId;
    private int incidentId;
    private int globalUnitCount;
    private int obstacleCount;

    /** 
    * @param width the width of the grid
    * @param y the height of the grid
    * @throws InvalidGridException if either the x or y values are equal to or less than 0
    */
    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        
        if(width <= 0 || height <= 0){
            throw new InvalidGridException("Invalid Width/Height");
        }

        //Create Grid
        this.map = new int[width][height];
        this.blocked = new boolean[width][height];


        //Reset Ticks and ID values
        this.tick = 0;
        this.stations = new Station[maxStations];
        this.stationCount = 0;
        this.incidents = new Incident[maxIncidents];
        this.incidentCount = 0;
        this.stationId = 1;
        this.unitId = 1;
        this.incidentId = 1;
        this.globalUnitCount = 0;
        this.obstacleCount=0;
    }

    /** 
    * @return an interger array with the height and width of the grid
    * @throws ArrayIndexOutOfBoundsException if the grid is not initalised
    */
    @Override
    public int[] getGridSize() {
        
        return new int[] {this.map.length, this.map[0].length};
    }

    /** 
    * @param x the x value for obstacle
    * @param y the y value for obstacle
    * @throws InvalidLocationException if the x and y values give a location that is invalid 
    */
    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {

        if(x < 0 || x >= this.map.length){
            throw new InvalidLocationException("Invalid Location");  
        }
        if(y < 0 || y >= this.map[0].length){
            throw new InvalidLocationException("Invalid Location");
        }
        this.obstacleCount++;
        this.blocked[x][y] = true;
    }
    /**
     * @param x the x value for the obstacle to be removed
     * @param y the y value for the obstacle to be removed
     * @throws InvalidLocationException if the x or y coordinates given are outside the grid
     */
    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        
        if(x < 0 || x >= this.map.length){
            throw new InvalidLocationException("Invalid Location");  
        }
        if(y < 0 || y >= this.map[0].length){
            throw new InvalidLocationException("Invalid Location");
        }
        this.obstacleCount--;
        this.blocked[x][y] = false;
    }
    /**
     * @param name the name of the station to add
     * @param x the x coordinate of where the station is being added
     * @param y the y coordinate of where the station is being added
     * @throws InvalidNameException if the name is empty or null
     * @throws InvalidLocationException if the location is blocked or outside of the grid
     * @return the station Id
     */
    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        
        if(name == null || name.isEmpty()){
            throw new InvalidNameException("Invalid Name");
        }
        if(x < 0 || x >= this.map.length){
            throw new InvalidLocationException("Invalid Location out of bounds");  
        }
        if(y < 0 || y >= this.map[0].length){
            throw new InvalidLocationException("Invalid Location out of bounds");
        }
        if(this.blocked[x][y] == true){
            throw new InvalidLocationException("Invalid Location is blocked");
        }
        
        this.stations[this.stationCount] = new Station(this.stationId, name, x, y);
        this.stationCount++;
        return this.stationId++;
    }
    /**
     * @param stationId the id for the station that is being removed
     * @throws IDNotRecognisedException if the id is not found in the array of stations
     * @throws IllegalStateException if the station still has units in it
     */
    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        for(int i = 0; i < this.stationCount; i++){
            if(this.stations[i].getStationID() == stationId){
                if(this.stations[i].getUnitCount() != 0){
                    throw new IllegalStateException("Station still contains Units");
                }
                
                for(int j = i; j < this.stationCount - 1; j++){
                    this.stations[j] = this.stations[j+1];
                }
                this.stations[this.stationCount - 1] = null;
                this.stationCount--;
                return;
            }
        }
        throw new IDNotRecognisedException("ID not Found");
    }
    /**
     * @param stationId the station in which we are setting capacity for
     * @param maxUnits the maximum number of units
     * @throws IDNotRecognisedException if the station Id is not found in the array of stations
     * @throws InvalidCapacityException if the new capacity is less than the old one
     */
    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        for(int i = 0; i < this.stationCount; i++){
            if(this.stations[i].getStationID() == stationId){
                if(this.stations[i].getCapacity() > maxUnits){
                    throw new InvalidCapacityException("New Capacity less than old");
                }
                this.stations[i].setCapacity(maxUnits);
                return;
            }
        }
        throw new IDNotRecognisedException("ID not Found");
    }
    /**
     * @return the id of the station
     */
    @Override
    public int[] getStationIds() {
        int[] ids = new int[this.stationCount];
        for(int i = 0; i < this.stationCount; i++){
            ids[i] = this.stations[i].getStationID();
        }
        return ids;
    }
    /**
     * @param stationId the id of the station in which we are adding a unit
     * @param unitType the type of unit being added
     * @throws IDNotRecognisedException if the station Id is not found in the array of stations
     * @throws InvalidUnitException the given unit type is not one of the valid units
     * @throws IllegalStateException if the station is full
     * @return unit Id
     */
    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        for(int i = 0; i < this.stationCount; i++){
            if(this.stations[i].getStationID() == stationId){
                if(this.stations[i].hasSpareCapacity()){
                    int sx = this.stations[i].getLocation()[0];
                    int sy = this.stations[i].getLocation()[1];
                    int sId = this.stations[i].getStationID();

                    switch(type){
                        case POLICE_CAR -> this.stations[i].addUnit(new PoliceCar(this.unitId, sx, sy, sId));
                        case FIRE_ENGINE -> this.stations[i].addUnit(new FireEngine(this.unitId, sx, sy, sId));
                        case AMBULANCE -> this.stations[i].addUnit(new Ambulance(this.unitId, sx, sy, sId));
                        default -> throw new InvalidUnitException("Invalid Unit Type");
                    }
                    this.globalUnitCount++;
                    return this.unitId++;
                }
                throw new IllegalStateException("Station full");
            }
        }
        throw new IDNotRecognisedException("StationId not recognised");
    }
    /**
     * @param unitId the Id of the unit being decommissioned
     * @throws IDNotRecognisedException if the unit Id is not found in array of units
     * @throws IllegalStateException if the unit is still active
     */
    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        for(int i = 0; i < this.stationCount; i++){
            Unit[] currentUnits = this.stations[i].getUnits();
            for(int j = 0; j < currentUnits.length; j++){
                if(currentUnits[j].getId() == unitId){ 
                    if(currentUnits[j].returnStatus() == UnitStatus.IDLE){
                        this.stations[i].removeUnit(unitId);
                        this.globalUnitCount--;
                        return;
                    }
                    throw new IllegalStateException("Unit not in IDLE status");
                }
            }
        }
        throw new IDNotRecognisedException("Unit ID not found");
    }
    /**
     * @param unitId the Id of the unit being transfered
     * @param newStationId the Id of the station the unit is moving to
     * @throws IDNotRecognisedException if the unit Id is not in the array of units
     * @throws IllegalStateException the unit cannot be transfered due to the unit being active or the station being full
     */
    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        Unit unit = null;
        Station targetStation = null;
        Station oldStation = null;

        for(int i = 0; i < this.stationCount; i++){
            if(this.stations[i].getStationID() == newStationId){
                targetStation = this.stations[i];
            }
            Unit[] currentUnits = this.stations[i].getUnits();
            for(int j = 0; j < currentUnits.length; j++){
                if(currentUnits[j].getId() == unitId){
                    unit = currentUnits[j];
                    oldStation = this.stations[i];
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
    /**
     * @param unitId the Id of the unit being put out of service
     * @param outOfService boolean whether the unit is currently out of service
     */
    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        Unit unit = null;

        for(int i = 0; i < this.stationCount; i++){
            Unit[] currentUnits = this.stations[i].getUnits();
            for(int j = 0; j < currentUnits.length; j++){
                if(unitId == currentUnits[j].getId()){
                    unit = currentUnits[j];
                }
            }
        }

        if(unit == null){
            throw new IDNotRecognisedException("ID not recognised ");
        }

        if(unit.returnStatus() == UnitStatus.IDLE || unit.returnStatus() == UnitStatus.OUT_OF_SERVICE){
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

        for(int i = 0; i < this.stationCount; i++){
            Unit[] currentUnits = this.stations[i].getUnits();
            for(int j = 0; j < currentUnits.length; j++){
                ids[pointer] = currentUnits[j].getId();
                pointer++;
            }
        }
        return ids;
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        Unit unit = null;
        
        for(int i = 0; i < this.stationCount; i++){
            Unit[] currentUnits = this.stations[i].getUnits();
            for(int j = 0; j < currentUnits.length; j++){
                if(currentUnits[j].getId() == unitId){
                    unit = currentUnits[j];
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
    
    if(x < 0 || x >= this.map.length || y < 0 || y >= this.map[0].length){
        throw new InvalidLocationException("Invalid Location");  
    }

    if(this.blocked[x][y]) {
        throw new InvalidLocationException("Cannot report an incident on a blocked location");
    }

    if(severity < 1 || severity > 5){
        throw new InvalidSeverityException("Invalid Severity");
    }
    if (this.incidentCount >= maxIncidents) {
        throw new IllegalStateException("Maximum number of incidents reached");
    }
    this.incidents[this.incidentCount] = new Incident(this.incidentId, type, severity, x, y);
    this.incidentCount++;
    return this.incidentId++;
}

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        Incident incident = null;

        for(int i = 0; i < this.incidentCount; i++){
            if(this.incidents[i].getId() == incidentId){
                incident = this.incidents[i];
            }
        }

        if(incident == null){
            throw new IDNotRecognisedException("Incident ID not Recognised");
        }
        if(incident.getStatus() == IncidentStatus.RESOLVED || incident.getStatus() == IncidentStatus.CANCELLED || incident.getStatus() == IncidentStatus.IN_PROGRESS){
            throw new IllegalStateException("Incident is either RESOLVED or CANCELLED or IN_PROGRESS");
        }

        incident.cancelIncident();
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        Incident incident = null;

        if(newSeverity < 1 || newSeverity > 5){
            throw new InvalidSeverityException("Invalid Severity");
        }

        for(int i = 0; i < this.incidentCount; i++){
            if(this.incidents[i].getId() == incidentId){
                incident = this.incidents[i];
            }
        }

        if(incident == null){
            throw new IDNotRecognisedException("ID not found");
        }

        if(incident.getStatus() == IncidentStatus.CANCELLED || incident.getStatus() == IncidentStatus.RESOLVED){
            throw new InvalidSeverityException("Illegal Incident Status");
        }

        incident.setSeverity(newSeverity);
    }

    @Override
    public int[] getIncidentIds() {
        int[] ids = new int[this.incidentCount];
        for(int i = 0; i < this.incidentCount; i++){
            ids[i] = this.incidents[i].getId();
        }
        return ids;
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        for(int i = 0; i < this.incidentCount; i++){
            if(this.incidents[i].getId() == incidentId){
                return this.incidents[i].viewIncident();
            }
        }
        throw new IDNotRecognisedException("Id Not Found");
    }
    @Override
    public void dispatch() {
        for (int i = 0; i < this.incidentCount; i++){
            if(this.incidents[i].getStatus() == IncidentStatus.REPORTED){
                Unit closestUnit = null;
                int x = this.incidents[i].getLoc()[0];
                int y = this.incidents[i].getLoc()[1];

                for (int j = 0; j < this.stationCount; j++){
                    Unit[] currentUnits = this.stations[j].getUnits();
                    for (int k = 0; k < currentUnits.length; k++){
                        Unit unit = currentUnits[k];

                        if(unit.canHandle(this.incidents[i].getType()) && unit.returnStatus() == UnitStatus.IDLE){
                            if(closestUnit == null){
                                closestUnit = unit;
                            } else if(closestUnit.getDistanceFrom(x, y) > unit.getDistanceFrom(x, y)){
                                closestUnit = unit;
                            } else if(closestUnit.getDistanceFrom(x, y) == unit.getDistanceFrom(x, y)){
                                if(closestUnit.getId() > unit.getId()){
                                    closestUnit = unit;
                                }
                            }
                        }
                    }
                }

                if (closestUnit != null) {
                    closestUnit.assign(this.incidents[i]);
                    this.incidents[i].setAssignedUnit(closestUnit);
                }
            }
        }
    }

    @Override
    public void tick() {
        for(int i = 0; i < this.stationCount; i++){
            Unit[] currentUnits = this.stations[i].getUnits();
            for(int j = 0; j < currentUnits.length; j++){
                currentUnits[j].Tick(this.blocked);
            }
        }
        this.tick++;
    }

    @Override
    public String getStatus() {
        StringBuilder sb = new StringBuilder();

        sb.append("TICK=").append(this.tick).append("\n");

        sb.append("STATIONS=").append(this.stationCount);
        sb.append(" UNITS=").append(this.globalUnitCount);
        sb.append(" INCIDENTS=").append(this.incidentCount == -1 ? "-" : this.incidentCount);
        sb.append(" OBSTACLES=").append(this.obstacleCount).append("\n");
        
        sb.append("INCIDENTS\n");
        for(int i=0;i<this.incidentCount;i++){
            sb.append(this.incidents[i].viewIncident()).append("\n");
        }

        sb.append("UNITS\n");
        for(int i=0;i<this.stationCount;i++){
            Unit[] currentUnits = this.stations[i].getUnits();
            for(int j = 0; j < currentUnits.length; j++){
                sb.append(currentUnits[j].viewUnit()).append("\n");
            }
        }

        return sb.toString();
    }
}
