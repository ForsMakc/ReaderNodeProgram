package student.bazhin.data;

import student.bazhin.interfaces.IData;

import java.io.Serializable;

public class NewScadaProjectData extends AScadaProjectData implements Serializable {

    int test;

    public NewScadaProjectData(int id, int test, boolean status) {
        this.test = test;
        this.id = id;
        this.authData = new AuthData("user1","1234");
        this.scadaName = "NewScada" + test;
        this.scadaProjectName = "Что-то ещё";
        this.status = status;
    }

    @Override
    public IData perform() {
        return null;
    }

}