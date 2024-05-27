package ru.nsu.database.airportclient;

import org.postgresql.util.ServerErrorMessage;
import ru.nsu.database.airportclient.gui.FirstWindowStarter;
import ru.nsu.database.airportclient.model.connection.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.postgresql.util.PSQLException;

public class EntryPoint {
    public static void main(String[] args) {
        /*try {
            Connection connection = DBConnection.INSTANCE.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select \"add_emp_to_brigade\"(13, 1)");

        } catch (PSQLException ex){
            ServerErrorMessage serverErrorMessage = ex.getServerErrorMessage();
            String s = serverErrorMessage.getConstraint();
            String M = serverErrorMessage.getMessage();
            System.out.println(s);
        } catch (SQLException ex){
            ex.printStackTrace();
        }*/

        FirstWindowStarter.init(args);
    }
}
