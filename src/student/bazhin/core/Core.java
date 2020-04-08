package student.bazhin.core;

import student.bazhin.components.Connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Core{

    protected View view;
    protected Storage storage;
    protected Connector connector;
    protected boolean working;
    protected static Core instance;

    protected Core() {}

    protected void init() {
        working = true;
        view = new View();
        storage = new Storage();
        connector = new Connector();
        connector.perform();
    }

    public static Core getInstance() {
        if (instance == null) {
            instance = new Core();
            instance.init();
            try {

                String DRIVER = "org.firebirdsql.jdbc.FBDriver";
                String url = "jdbc:firebirdsql:localhost/3050:C:\\MasterSCADA Projects\\Projects\\Test\\ARCHIVE.FDB";
                String login = "SYSDBA";
                String password = "masterkey";
                Class.forName(DRIVER);
                Connection dbConnection = DriverManager.getConnection(url,login,password);

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public View getView(){
        return view;
    }

    public Storage getStorage() {
        return storage;
    }

    public Connector getConnector() {
        return connector;
    }

    public String getNodeId() {
        return null;
    }
}
