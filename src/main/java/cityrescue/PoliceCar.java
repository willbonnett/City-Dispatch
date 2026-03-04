package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;


/** 
 * Represents the Police Car units that respond to only CRIME events
 * 
 *
 * 
 */

public class PoliceCar extends Unit{
    /**
     * Constructor of the PoliceCar class
     * @param id The unique Id given to the unit
     * @param x The current x position of the unit
     * @param y The current y position of the unit
     * @param homeStationId The ID of the home station
     */
    public PoliceCar(int id, int x, int y, int homeStationId){
        super(id,UnitType.POLICE_CAR, x,y,homeStationId,x,y);
    }

    /** 
     * Function that will return True if this unit can respond to the passed in IncidentType
     * @param type - Passed in IncidentType
     * @return boolean - True if this unit can resolve that incident
     */
    @Override
    public boolean canHandle(IncidentType type){
        return type == IncidentType.CRIME;
    }

    /** 
     * Function that returns the number of ticks to resolve the incident
     * 
     * @param severity - Passed in integer of the incident severity
     * @return int - Returns the number of ticks needed
     */
    @Override
    public int getTicksToResolve(int severity){
        return 3;
    }
}
