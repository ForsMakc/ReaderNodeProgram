package student.bazhin.data;

import student.bazhin.interfaces.IData;

import java.io.Serializable;

public class MasterScada3ProjectData extends AScadaProjectData implements Serializable {

    public MasterScada3ProjectData(int id) {
        this.id = id;
        this.status = true;
    }

    @Override
    public IData perform() {
        return null;
    }

    @Override
    protected void updateScadaSource() {

    }

    @Override
    public boolean validateScadaData() {
        return true;
    }
}
