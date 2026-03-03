package cityrescue;


import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public abstract class Unit{
    protected final int unitId;
    protected final UnitType type;
    protected int x;
    protected int y;
    protected int homeStationId;
    protected int homeStationX;
    protected int homeStationY;

    protected UnitStatus status;
    protected Incident assignedIncident;
    protected int assignedIncidentId;
    protected int incidentX;
    protected int incidentY;

    protected int workremaining;

    public abstract boolean canHandle(IncidentType type);
    public abstract int getTicksToResolve(int severity);

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

        if(this.status == UnitStatus.IDLE){
            if(this.x == this.homeStationX && this.y == this.homeStationY) {
                return;
            }

            int[][] candidates = new int[][]{{0,-1},{1,0},{0,1},{-1,0}};
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
                if(this.getDistanceFrom(nextX, nextY, this.homeStationX, this.homeStationY) < this.getDistanceFrom(this.homeStationX, this.homeStationY)){
                    this.x = nextX;
                    this.y = nextY;
                    return;
                }
            }
        }
    }



    protected int getDistanceFrom(int x, int y){
        return (Math.abs(this.x-x)+Math.abs(this.y-y));
    }
    protected int getDistanceFrom(int x1,int y1, int x2, int y2){
        return (Math.abs(x1-x2)+Math.abs(y1-y2));
    }

    protected String viewUnit(){
        String incString = (this.assignedIncidentId == -1) ? "-" : String.valueOf(this.assignedIncidentId);
        String baseString = "U#"+this.unitId+" TYPE="+this.type+" HOME="+this.homeStationId+
                        " LOC=("+this.x+","+this.y+") STATUS="+this.status+" INCIDENT="+incString;
        if(this.status == UnitStatus.AT_SCENE){
        return baseString + " WORK=" + this.workremaining;
    }
    
    return baseString;
    }

    public void assign(Incident incident){
        this.assignedIncident = incident; 
        this.assignedIncidentId = incident.getId();
        this.incidentX = incident.getLoc()[0];
        this.incidentY = incident.getLoc()[1];
        this.status = UnitStatus.EN_ROUTE;
    }

    protected void unassign(){
        this.assignedIncidentId=-1;
        this.status = UnitStatus.IDLE;
    }

    protected UnitStatus returnStatus(){
        return this.status;
    }

    protected void setStationId(int newId){
        this.homeStationId = newId;
    }

    protected void setStatus(UnitStatus status){
        this.status = status;
    }
    
    public int getId(){
        return this.unitId;
    }
}
