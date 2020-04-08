package student.bazhin.factory.scada;

import student.bazhin.components.scadaProject.AScadaProject;
import student.bazhin.factory.scada.MasterScada3Factory;

public class ScadaFactory {

    public static final String[] scadaSystemsList = {
        "Master Scada 3.x"
    };

    public static AScadaProject createScadaProject(String scadaSystemName) {
        switch (scadaSystemName) {
            case "Master Scada 3.x": {
                return new MasterScada3Factory().createScadaProject(scadaSystemName);
            }
            default: {
                return null;
            }
        }
    }

}
