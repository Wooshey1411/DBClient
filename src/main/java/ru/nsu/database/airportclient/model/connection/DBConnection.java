package ru.nsu.database.airportclient.model.connection;

import java.sql.*;
import java.util.Locale;
import java.util.TimeZone;

public enum DBConnection implements AutoCloseable {
    INSTANCE;

    private final Connection connection;

     DBConnection(){
        String url = "jdbc:postgresql://localhost:5432/airport";
        TimeZone timeZone = TimeZone.getTimeZone("GMT+7");
        TimeZone.setDefault(timeZone);
        Locale.setDefault(Locale.ENGLISH);
        try {
            connection = DriverManager.getConnection(url, "postgres", "?");
            if (connection == null) {
                throw new SQLConnectionException("NO CONNECTION WITH DB");
            }

        } catch (SQLException ex){
            throw new SQLConnectionException("NO CONNECTION WITH DB",ex);
        }

    }
    public Connection getConnection(){
        return connection;
    }

    @Override
    public void close(){
         try {
             connection.close();
         } catch (SQLException ex){
             System.out.println(ex.getLocalizedMessage());
         }
    }
}
