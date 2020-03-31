package student.bazhin.data;

import student.bazhin.core.View;
import student.bazhin.interfaces.IData;
import student.bazhin.interfaces.IScadaProjectData;

import javax.swing.*;
import java.io.Serializable;

public class MasterScada3ProjectData implements IScadaProjectData, Serializable {

    protected int id;
    protected String path;
    protected String scadaName;
    protected AuthData authData;
    protected String scadaProjectName;
    protected boolean status;

    public MasterScada3ProjectData(int id) {
        this.id = id;
        this.scadaName = "Master Scada 3.x";
        this.scadaProjectName = "Волочильный стан";
    }

    @Override
    public IData perform() {
        return null;
    }

    @Override
    public void render(View view) {
        if (view != null) {
            view.addButton(new JButton(scadaName + " (" + scadaProjectName + ")"), view.getTopPanel());
        }
    }
}
