package student.bazhin.factory;

import student.bazhin.components.scadaProject.AScadaProject;

public class ScadaFactory {

    public static final String[] scadaSystemsList = {
        "Master Scada 3.x"
    };

    public static AScadaProject createScadaProject(String scadaSystemName) {
        switch (scadaSystemName) {
            case "Master Scada 3.x": {
                return new MasterScada3Factory().createScadaProject();
            }
            default: {
                return null;
            }
        }
    }

}
