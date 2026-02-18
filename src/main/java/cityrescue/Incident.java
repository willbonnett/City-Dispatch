package cityrescue;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;

public class Incident{
    private final int incidentId;
    private final IncidentType type;
    private IncidentStatus status;
    private int severity;
    private int x;
    private int y;
    private Unit assignedUnit;

    public Incident(int incidentId, IncidentType type, int severity, int x, int y){
        this.incidentId = incidentId;
        this.type = type;
        this.severity = severity;
        this.x = x;
        this.y = y;
        this.assignedUnit = null;
        this.status = IncidentStatus.REPORTED;
    }

    public void cancelIncident(){
        if(this.assignedUnit!=null){
            this.assignedUnit.unassign();
        }

        this.status = IncidentStatus.CANCELLED;
        this.assignedUnit=null;
    }

    public void resolve(){
        this.status = IncidentStatus.RESOLVED;
        this.assignedUnit = null;
    }

    public String viewIncident(){
        String unit = "-";
        if(this.assignedUnit != null){
            unit = String.valueOf(this.assignedUnit.getId());
        }
        return "I#"+this.incidentId+" TYPE="+this.type+" SEV="+this.severity+" LOC=("+this.x+","+this.y+") STATUS="+this.status+" UNIT="+unit;
    }

    public void setSeverity(int newSeverity){
        this.severity=newSeverity;
    }

    public int getSeverity(){
        return this.severity;
    }

    public int getId(){
        return this.incidentId;
    }

    public IncidentType getType(){
        return this.type;
    }

    public void setAssignedUnit(Unit unit){
        this.assignedUnit = unit;
        this.status = IncidentStatus.DISPATCHED;
    }

    public void setStatus(IncidentStatus status){
        this.status = status;
    }

    public IncidentStatus getStatus(){
        return this.status;
    }

    public int[] getLoc(){
        return new int[]{this.x,this.y};
    }
}
