package student.bazhin.components.scadaProject;

import student.bazhin.data.ScadaData;
import student.bazhin.interfaces.IData;

import java.io.Serializable;

public class MasterScada3Project extends AScadaProject implements Serializable {

    public MasterScada3Project(int id) {
        this.id = id;
    }

    @Override
    public IData perform() {
        //todo получать данные scada проекта
        if (status) {
            return new ScadaData(id + " " + path + " "  + scadaName + " "  + scadaProjectName);
        } else {
            return null;
        }
    }

    @Override
    protected void updateScadaSource() {
        validateScadaData();
        //todo обновить scada директорию и ресурсы
    }

    @Override
    public boolean validateScadaData() {
        status = true;
        //todo обработка проверки scada директории
        return status;
    }
}
