package cityrescue;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;
/**
 * Representation of the Incidents
 */
public class Incident{
    /**Unique ID of the incident */
    private final int incidentId;
    /**Type of the incident */
    private final IncidentType type;
    /**Current status of incident */
    private IncidentStatus status;
    /**Severity of incident */
    private int severity;
    /**X position of incident */
    private int x;
    /**Y position of incident */
    private int y;
    /**Assigned unit to the Incident */
    private Unit assignedUnit;

    /**
     * Constructor for an Incident
     * @param incidentId Unique ID of incident
     * @param type Type of Incident
     * @param severity current severity
     * @param x X position
     * @param y Y position
     */
    public Incident(int incidentId, IncidentType type, int severity, int x, int y){
        this.incidentId = incidentId;
        this.type = type;
        this.severity = severity;
        this.x = x;
        this.y = y;
        this.assignedUnit = null;
        this.status = IncidentStatus.REPORTED;
    }

    /**
     * Cancels the Incident, Unassigning the Unit
     */
    public void cancelIncident(){
        if(this.assignedUnit!=null){
            this.assignedUnit.unassign();
        }

        this.status = IncidentStatus.CANCELLED;
        this.assignedUnit=null;
    }

    /**
     * Resolves incident, updating the Status and unassigns unit
     */
    public void resolve(){
        this.status = IncidentStatus.RESOLVED;
        this.assignedUnit = null;
    }

    /**
     * Returns a String Representation of the Incident
     * @return String String representation of Incident
     */
    public String viewIncident(){
        String unit = "-";
        if(this.assignedUnit != null){
            unit = String.valueOf(this.assignedUnit.getId());
        }
        return "I#"+this.incidentId+" TYPE="+this.type+" SEV="+this.severity+" LOC=("+this.x+","+this.y+") STATUS="+this.status+" UNIT="+unit;
    }

    /**
     * Updates the severity of incident
     * @param newSeverity New severity
     */
    public void setSeverity(int newSeverity){
        this.severity=newSeverity;
    }

    /**
     * Returns the current severity
     * @return Current Severity
     */
    public int getSeverity(){
        return this.severity;
    }

    /**
     * Returns Incident ID
     * @return ID
     */
    public int getId(){
        return this.incidentId;
    }
    
    /**
     * Returns the type of incident
     * @return IncidentType Type
     */
    public IncidentType getType(){
        return this.type;
    }

    /**
     * Sets the Assigned unit
     * @param unit Assigned Unit
     */
    public void setAssignedUnit(Unit unit){
        this.assignedUnit = unit;
        this.status = IncidentStatus.DISPATCHED;
    }

    /**
     * Updates the status
     * @param status New status
     */
    public void setStatus(IncidentStatus status){
        this.status = status;
    }

    /**
     * Returns the current Status
     * @return IncidentStatus Current Status
     */
    public IncidentStatus getStatus(){
        return this.status;
    }

    /**
     * Returns the location of the Incident
     * @return Int[] Coordinate of Incident
     */
    public int[] getLoc(){
        return new int[]{this.x,this.y};
    }
}
