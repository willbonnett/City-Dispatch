package cityrescue;


import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;
import cityrescue.exceptions.InvalidGridException;

public class OwnTest {
     
    public static void main(String[] args) throws cityrescue.exceptions.IDNotRecognisedException,cityrescue.exceptions.InvalidLocationException,cityrescue.exceptions.InvalidNameException,cityrescue.exceptions.InvalidUnitException,cityrescue.exceptions.InvalidSeverityException{
        CityRescueImpl cr = new CityRescueImpl();
        
        try{
            cr.initialise(5, 5);
        }
        catch(InvalidGridException e){
            System.out.println("Grid exception called");
        }
        
        int s = cr.addStation("A", 0, 0);
        int u = cr.addUnit(s, UnitType.AMBULANCE);

        int i = cr.reportIncident(IncidentType.MEDICAL, 1, 0, 1);
        cr.dispatch();

        System.out.println(cr.getStatus());
        cr.tick();
        System.out.println(cr.getStatus());
        cr.tick();
        System.out.println(cr.getStatus());
        cr.tick();
        System.out.println(cr.getStatus());

    }
}
