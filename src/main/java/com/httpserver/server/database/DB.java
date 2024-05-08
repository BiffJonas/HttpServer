package com.httpserver.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DB {
    public static Connection connect() throws SQLException, ClassNotFoundException{
        try {
            var jdbcUrl = DatabaseConfig.getDbUrl();
            var user = DatabaseConfig.getDbUsername();
            var password = DatabaseConfig.getDbPassword();
            // var jdbcUrl = "jdbc:postgresql://localhost:5432/httpserver"
            // var user = "postgres"
            // var password = "biffkaka"

            System.out.println(jdbcUrl);
            System.out.println(user);
            System.out.println(password);
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
            return connection;
        } catch(SQLException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
    
}
