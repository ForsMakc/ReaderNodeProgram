package student.bazhin.factory;

import student.bazhin.core.Core;
import student.bazhin.data.AScadaProjectData;
import student.bazhin.data.MasterScada3ProjectData;

public class MasterScada3Factory implements IScadaFactory {
    @Override
    public AScadaProjectData createScadaProject() {
        int scadaProjectId = Core.getInstance().getStorage().getNewId();
        return new MasterScada3ProjectData(scadaProjectId);
    }
}
