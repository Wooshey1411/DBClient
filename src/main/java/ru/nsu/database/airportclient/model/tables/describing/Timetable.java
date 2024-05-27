package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Timetable implements ITable {

    public final static String TABLE_NAME = "timetable";

    public final static String TABLE_NAME_ALIAS = "Расписание";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("timetable_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("flight_id", false, TokenType.VARCHAR, null, false, true, "Рейс"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("flight_starting_destination_name", false, TokenType.VARCHAR, null, true, true, "Пункт вылета"),
            new Token("flight_ending_destination_name", false, TokenType.VARCHAR, null, true, true, "Пункт прилета"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время посадки"),
            new Token("minimum_tickets", false, TokenType.INTEGER, null, true, true, "Минимальное кол-во билетов"),
            new Token("airplane_id", false, TokenType.INTEGER, null, false, true, "Самолёт"),
            new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Название самолёта")
    );


    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<Token> getAllTokens() {
        return TOKENS;
    }

    @Override
    public String getTableAlias() {
        return TABLE_NAME_ALIAS;
    }

    @Override
    public List<Token> getDataTokens() {
        return TOKENS;
    }

    @Override
    public String getRequestForGetData() {
        return "select timetable_id, flight_id, flight_code, flight_starting_destination_name, destination_name as flight_ending_destination_name, leave_time, arrival_time, minimum_tickets, airplane_id, airplane_name from (select timetable_id, f.flight_id, flight_code, a.airplane_id, destination_name as flight_starting_destination_name, flight_ending_destination_id, leave_time, arrival_time, minimum_tickets, airplane_name  from (timetable join public.flights f on f.flight_id = timetable.flight_id join public.airplanes a on a.airplane_id = timetable.airplane_id\n" +
                "join public.destinations d on f.flight_starting_destination_id = d.destination_id)) join destinations on flight_ending_destination_id = destinations.destination_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return null;
    }

    @Override
    public String getSQLForAddData() {
        return null;
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return null;
    }

    @Override
    public String getSQLForUpdate() {
        return null;
    }

    @Override
    public List<LinkedToken> getTokensForUpdate() {
        return null;
    }

    @Override
    public String getSQLForDelete() {
        return null;
    }
}
