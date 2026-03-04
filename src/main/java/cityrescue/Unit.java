package cityrescue;


import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

/**
 * Abstract class for each of the different unit types
 */
public abstract class Unit{
    /**
     * Unique ID of the class
     */
    protected final int unitId;
    /**UnitType of the desired unit */
    protected final UnitType type;
    /**Current x position of unit */
    protected int x;
    /**Current y position of unit */
    protected int y;
    /**Unique ID of the home station */
    protected int homeStationId;
    /**X position of the home station */
    protected int homeStationX;
    /**Y position of the home station */
    protected int homeStationY;

    /**Current status of the unit */
    protected UnitStatus status;
    /**Currently assigned unit, Null if no incident assigned */
    protected Incident assignedIncident;
    /**Unique ID of assigned incident */
    protected int assignedIncidentId;
    /**X position of assigned incident */
    protected int incidentX;
    /**Y position of assigned indident */
    protected int incidentY;

    /**Number of ticks of work left */
    protected int workremaining;

    public abstract boolean canHandle(IncidentType type);
    public abstract int getTicksToResolve(int severity);

    /**
     * Constructor of the Abstract Unit class
     * @param id The unique ID given to the unit
     * @param type The given UnitType of the unit
     * @param x The current x position of the unit
     * @param y the current y position of the unit
     * @param homeStationId the ID of the home station
     * @param homeStationX the x position of the home station
     * @param homeStationY the y position of the home station
     */
    protected Unit(int id, UnitType type, int x,int y, int homeStationId, int homeStationX, int homeStationY){
        this.unitId = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.homeStationId = homeStationId;
        this.homeStationX = homeStationX;
        this.homeStationY = homeStationY;

        this.status = UnitStatus.IDLE;
        this.assignedIncidentId = -1;
    }

    /**
     * Function that is run each tick to decide what the unit does that tick
     * 
     * If Unit is IDLE, then nothing happens
     * IF Unit is EN_ROUTE, then the unit is moved to the first legal move
     * IF Unit is ATSCENE, Then the units work remaining is reduced until 0
     * 
     * @param blocked Blocked grid for checking if the move is legal
     */
    public void Tick(boolean[][] blocked){
        if(this.status == UnitStatus.IDLE){
        return; 
        }
        if(this.status == UnitStatus.EN_ROUTE){
            int[][] candidates = new int[][]{{0,-1},{1,0},{0,1},{-1,0}};
            int firstValidX = -1;
            int firstValidY = -1;
            boolean foundValidMove = false;
            for(int[] direc : candidates){
                int nextX = this.x + direc[0];
                int nextY = this.y + direc[1];
                
                try {
                    if(blocked[nextX][nextY]){
                        continue;
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    continue;
                }
                if(!foundValidMove){
                    firstValidX = nextX;
                    firstValidY = nextY;
                    foundValidMove = true;
                }
          
                if(this.getDistanceFrom(nextX, nextY, this.incidentX, this.incidentY) < this.getDistanceFrom(this.incidentX, this.incidentY)){
         
                    this.x = nextX;
                    this.y = nextY;

           
                    if(this.x == this.incidentX && this.y == this.incidentY){
             
                        this.status = UnitStatus.AT_SCENE;
                        this.workremaining = this.getTicksToResolve(this.assignedIncident.getSeverity());
                        this.assignedIncident.setStatus(IncidentStatus.IN_PROGRESS);
                    }
                    return; 
                }
            }
            if(foundValidMove){
                this.x = firstValidX;
                this.y = firstValidY;
}
        }

  
        if(this.status == UnitStatus.AT_SCENE){
            this.workremaining--;

            if (this.workremaining <= 0){ 
  
                this.assignedIncident.resolve();
                this.status = UnitStatus.IDLE;
                this.assignedIncident = null;
                this.assignedIncidentId = -1;
                this.incidentX = -1; 
                this.incidentY = -1; 
            }
        }
    }



    /**
     * Function used to return the distance of this unit from the passed in coordinate
     * @param x Target x coordinate
     * @param y Target y coordinate
     * @return int The calculated Distance
     */
    protected int getDistanceFrom(int x, int y){
        return (Math.abs(this.x-x)+Math.abs(this.y-y));
    }
    /**
     * Function used to return the distance between two points
     * @param x1 Source x position
     * @param y1 Source y position
     * @param x2 Target x position
     * @param y2 Target y position
     * @return int The calculated Distance
     */
    protected int getDistanceFrom(int x1,int y1, int x2, int y2){
        return (Math.abs(x1-x2)+Math.abs(y1-y2));
    }

    /**
     * Returns a String representation of the Unit at that current tick
     * @return String, returns one large string of the unit status.
     */
    protected String viewUnit(){
        String incString = (this.assignedIncidentId == -1) ? "-" : String.valueOf(this.assignedIncidentId);
        String baseString = "U#"+this.unitId+" TYPE="+this.type+" HOME="+this.homeStationId+
                        " LOC=("+this.x+","+this.y+") STATUS="+this.status+" INCIDENT="+incString;
        if(this.status == UnitStatus.AT_SCENE){
            return baseString + " WORK=" + this.workremaining;
        }
    return baseString;
    }

    /**
     * Assigns the unit to the passed in Incident
     * @param incident
     */
    public void assign(Incident incident){
        this.assignedIncident = incident; 
        this.assignedIncidentId = incident.getId();
        this.incidentX = incident.getLoc()[0];
        this.incidentY = incident.getLoc()[1];
        this.status = UnitStatus.EN_ROUTE;
    }

    /**
     * Unassigns the unit and sets status to IDLE
     */
    protected void unassign(){
        this.assignedIncidentId=-1;
        this.status = UnitStatus.IDLE;
    }

    /**
     * Returns the current Status of the unit
     * @return UnitStatus current status
     */
    protected UnitStatus returnStatus(){
        return this.status;
    }

    /**
     * Updates home station ID to passed ID
     * @param newId ID of new station
     */
    protected void setStationId(int newId){
        this.homeStationId = newId;
    }

    /**
     * Updates the unit status to passed status
     * @param status New status
     */
    protected void setStatus(UnitStatus status){
        this.status = status;
    }
    
    /**
     *  Returns the ID of this Unit
     */
    public int getId(){
        return this.unitId;
    }
}
