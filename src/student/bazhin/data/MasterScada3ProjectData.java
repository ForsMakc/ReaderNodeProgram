package student.bazhin.data;

import student.bazhin.interfaces.IData;

import java.io.Serializable;

public class MasterScada3ProjectData extends AScadaProjectData implements Serializable {

    public MasterScada3ProjectData(int id, boolean status) {
        this.id = id;
        this.scadaName = "Master Scada 3.x";
        this.scadaProjectName = "Волочильный стан";
        this.status = status;
        this.authData = new AuthData("user1","1234");
    }

    @Override
    public IData perform() {
        return null;
    }

}
