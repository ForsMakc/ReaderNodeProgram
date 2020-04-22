package student.bazhin.factory.scada;

import student.bazhin.components.scadaProject.AScadaProject;
import student.bazhin.factory.scada.MasterScada3Factory;

public class ScadaFactory {

    public static final String[] scadaSystemsList = {
        "MasterScada 3.x"
    };

    public static AScadaProject createScadaProject(String scadaSystemName) {
        switch (scadaSystemName) {
            case "MasterScada 3.x": {
                return new MasterScada3Factory().createScadaProject(scadaSystemName);
            }
            default: {
                return null;
            }
        }
    }

}
