package student.bazhin.databases;

import java.sql.DriverManager;
import java.sql.SQLException;

public class FirebirdDatabase extends ADatabase {


    String DRIVER = "org.firebirdsql.jdbc.FBDriver";
    String databaseName = "jdbc:firebirdsql:localhost/3050:C:\\MasterSCADA Projects\\Projects\\Test\\ARCHIVE.FDB";
    String login = "SYSDBA";
    String password = "masterkey";

    @Override
    public void init(String login, String password, String databaseName) {
        try {
            Class.forName(DRIVER);
//            this.databaseName = "jdbc:firebirdsql:localhost/3050:" + databaseName;
//            this.login = login;
//            this.password = password;
            dbConnection = DriverManager.getConnection(this.databaseName,this.login,this.password);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
