package edu.twister.malik.core;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import edu.twister.malik.core.Database;

public class SQLCore {

    private static Database database = null;

    public static Connection getConnection() throws SQLException {
        if ( DBStatic.mysqlPooling == false ) { 
            return
                ( DriverManager.getConnection("jdbc:mysql://" +
                                              DBStatic.mysqlHost + "/" +
                                              DBStatic.mysqlDatabase, 
                                              DBStatic.mysqlUserName, 
                                              DBStatic.mysqlPassword) );
        } else {
            if ( database == null )
                database = new Database("jdbc/db");
            return database.getConnection();
        }
    }
}
