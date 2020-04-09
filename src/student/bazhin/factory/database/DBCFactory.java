package student.bazhin.factory.database;


import student.bazhin.databases.ADatabase;

public class DBCFactory {

    public static final String[] dbTypeList = {
        "Firebird"
    };

    public static ADatabase createDBConnection(String dbType) {
        switch (dbType) {
            case "Firebird": {
                return new FirebirdDBCFactory().createDBConnection();
            }
            default: {
                return null;
            }
        }
    }

}
