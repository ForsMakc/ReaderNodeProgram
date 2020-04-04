package student.bazhin.data;

import student.bazhin.interfaces.IData;

import java.io.Serializable;

public class MasterScada3Project extends AScadaProject implements Serializable {

    public MasterScada3Project(int id) {
        this.id = id;
    }

    @Override
    public IData perform() {
        return new ScadaData(id + " " + path + " "  + scadaName + " "  + scadaProjectName);
    }

    @Override
    protected void updateScadaSource() {

    }

    @Override
    public boolean validateScadaData() {
        status = true;
        return status;
    }
}
