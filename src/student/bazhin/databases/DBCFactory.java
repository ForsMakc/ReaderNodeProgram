package student.bazhin.databases;


import student.bazhin.factory.database.FirebirdDBCFactory;

public class DBCFactory {

    public static final String[] dbTypeList = {
        "Firebird"
    };

    public static ADatabase createScadaProject(String dbType) {
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
