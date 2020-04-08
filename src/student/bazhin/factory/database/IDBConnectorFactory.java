package student.bazhin.factory.database;

import student.bazhin.databases.ADatabase;

public interface IDBConnectorFactory {

    public ADatabase createDBConnection();

}
