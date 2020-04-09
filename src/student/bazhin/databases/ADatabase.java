package student.bazhin.databases;


import java.sql.Connection;

public abstract class ADatabase {

    protected Connection dbConnection = null;

    public abstract void init(String login, String password, String databaseName);

}
