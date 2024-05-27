package ru.nsu.database.airportclient.model.tables.utils;

import ru.nsu.database.airportclient.model.connection.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableParser {
    public static List<List<Token>> parse(String request, List<Token> tokens) throws SQLException {
        List<List<Token>> result = new ArrayList<>();
        Connection connection = DBConnection.INSTANCE.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(request);
        while (rs.next()){
            List<Token> row = new ArrayList<>();
            for (Token token : tokens){
                String res = rs.getString(token.name());
                row.add(new Token(token.name(), token.isPK(), token.tokenType(), res, token.isShowing(), token.isNotNone(), token.alias()));
            }
            result.add(row);
        }
        stmt.close();
        return result;
    }
}
