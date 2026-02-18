package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

public class PoliceCar extends Unit{
    public PoliceCar(int id, int x, int y, int homeStationId){
        super(id,UnitType.POLICE_CAR, x,y,homeStationId,x,y);
    }

    @Override
    public boolean canHandle(IncidentType type){
        return type == IncidentType.CRIME;
    }

    @Override
    public int getTicksToResolve(int severity){
        return 3;
    }
}
