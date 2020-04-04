package student.bazhin.factory;

import student.bazhin.core.Core;
import student.bazhin.data.AScadaProject;
import student.bazhin.data.MasterScada3Project;

public class MasterScada3Factory implements IScadaFactory {
    @Override
    public AScadaProject createScadaProject() {
        int scadaProjectId = Core.getInstance().getStorage().getNewId();
        return new MasterScada3Project(scadaProjectId);
    }
}
