package edu.twister.malik.core;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import edu.twister.malik.core.Database;
import edu.twister.malik.services.ServiceException;

public class SQLCore {

    private static Database database = null;

    public static Connection getConnection() throws ServiceException {
        Connection cnx = null;
        try {
            if ( DBStatic.mysqlPooling == false ) { 
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                }
                catch (ClassNotFoundException e) {
                    throw new ServiceException(
                            "JDBC Driver (ClassNotFoundException)", 
                            ServiceException._CORE_CLASS
                            );
                }
                catch (InstantiationException ie) {
                    throw new ServiceException(
                            "JDBC Driver (InstantiationException)",
                            ServiceException._CORE_CLASS
                            );
                }
                catch (IllegalAccessException iie) {
                    throw new ServiceException(
                            "JDBC Driver (IllegalAccessException)",
                            ServiceException._CORE_CLASS
                            );
                }
                cnx = DriverManager.getConnection(
                        "jdbc:mysql://" +
                        DBStatic.mysqlHost + "/" +
                        DBStatic.mysqlDatabase, 
                        DBStatic.mysqlUserName, 
                        DBStatic.mysqlPassword);
            } else {
                if ( database == null )
                    database = new Database("jdbc/db");
                cnx = database.getConnection();
            }
        }
        catch (SQLException e) {
            throw new ServiceException(e);
        }
        return cnx;
    }
}
