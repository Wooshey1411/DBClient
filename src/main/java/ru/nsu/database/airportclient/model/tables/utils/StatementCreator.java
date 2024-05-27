package ru.nsu.database.airportclient.model.tables.utils;

import java.sql.*;

public class StatementCreator {

    public static void fillStatementField(String data, TokenType tokenType, PreparedStatement statement, int position) throws SQLException {
        switch (tokenType){
            case VARCHAR -> statement.setString(position, data);
            case INTEGER -> {
                try {
                    statement.setInt(position, Integer.parseInt(data));
                } catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
            }
            case BOOLEAN -> statement.setBoolean(position, data.equals("T"));
            case DATE -> {
                try {
                    statement.setDate(position, Date.valueOf(data));
                } catch (IllegalArgumentException ex){
                    ex.printStackTrace();
                }
            }
            case TIMESTAMP -> {
                try {
                    statement.setTimestamp(position, Timestamp.valueOf(data));
                } catch (IllegalArgumentException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void fillNullStatementField(TokenType tokenType, PreparedStatement statement, int position) throws SQLException {
        int type = 0;
        switch (tokenType){
            case DATE -> type = Types.DATE;
            case TIMESTAMP -> type = Types.TIMESTAMP;
            case VARCHAR -> type = Types.VARCHAR;
            case BOOLEAN -> type = Types.BOOLEAN;
            case INTEGER -> type = Types.INTEGER;
        }
        statement.setNull(position, type);
    }

}
