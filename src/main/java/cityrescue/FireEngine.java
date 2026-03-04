package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

/**
 * Represents the Fire Engine units that respond to only FIRE events
 */

public class FireEngine extends Unit{
    /**
     * Constructor of the FireEngine Class
     * @param id The unique ID given to the unit
     * @param x The current x position of the unit
     * @param y The current y position of the unit
     * @param homeStationId The ID of the home station
     */
    public FireEngine(int id, int x, int y, int homeStationId){
        super(id,UnitType.FIRE_ENGINE,x,y,homeStationId,x,y);
    }

    /** 
     * Funtion that will return True if this unit can respond to the passed in IncidentType
     * @param type Passed in IncidentType
     * @return boolean True if this unit can resolve that incident
     */
    @Override
    public boolean canHandle(IncidentType type){
        return type == IncidentType.FIRE;
    }

    /** 
     * Function that returns the number of ticks to resolve the incident
     * @param severity Passed in integer of the incident severity
     * @return int Returns the number of ticks needed
     */
    @Override
    public int getTicksToResolve(int severity){
        return 4;
    }
}
