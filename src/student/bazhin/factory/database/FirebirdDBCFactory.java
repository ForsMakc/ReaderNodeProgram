package student.bazhin.factory.database;

import student.bazhin.databases.ADatabase;
import student.bazhin.databases.FirebirdDatabase;

public class FirebirdDBCFactory implements IDBConnectorFactory {

    @Override
    public ADatabase createDBConnection() {
        return new FirebirdDatabase();
    }

}
