package edu.twister.malik.core;

import java.sql.SQLException;
import java.sql.Connection;
import javax.sql.DataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Database {

    private DataSource source;

    public Database(String jndiname) throws SQLException {
        try {
            source = (DataSource) 
                new InitialContext().lookup("java:comp/env/" + jndiname);
        } catch (NamingException e) {
            throw new SQLException(
                    jndiname + " missing in JNDI - " +
                    e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }

}
