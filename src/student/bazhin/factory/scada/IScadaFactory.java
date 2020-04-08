package student.bazhin.factory.scada;

import student.bazhin.components.scadaProject.AScadaProject;

public interface IScadaFactory {

    public AScadaProject createScadaProject(String scadaSystemName);

}
