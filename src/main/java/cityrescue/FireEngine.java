package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

public class FireEngine extends Unit{
    public FireEngine(int id, int x, int y, int homeStationId){
        super(id,UnitType.FIRE_ENGINE,x,y,homeStationId);
    }

    @Override
    public boolean canHandle(IncidentType type){
        return type == IncidentType.FIRE;
    }

    @Override
    public int getTicksToResolve(int severity){
        return 4;
    }
}
