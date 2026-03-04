package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

public class Ambulance extends Unit{
    public Ambulance(int id, int x, int y, int homeStationId){
        super(id,UnitType.AMBULANCE,x,y,homeStationId,x,y);
    }
    
    /** 
     * @param type
     * @return boolean
     */
    @Override
    public boolean canHandle(IncidentType type){
        return type == IncidentType.MEDICAL;
    }

    /** 
     * @param severity
     * @return int
     */
    @Override
    public int getTicksToResolve(int severity){
        return 2;
    }
}
