package student.bazhin.factory;

import student.bazhin.core.Core;
import student.bazhin.components.scadaProject.AScadaProject;
import student.bazhin.components.scadaProject.MasterScada3Project;

public class MasterScada3Factory implements IScadaFactory {

    @Override
    public AScadaProject createScadaProject(String scadaSystemName) {
        int scadaProjectId = Core.getInstance().getStorage().getNewId();
        return new MasterScada3Project(scadaProjectId,scadaSystemName);
    }

}
