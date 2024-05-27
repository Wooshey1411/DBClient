package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanceledFlights implements ITable {

    public final static String TABLE_NAME = "canceled_flights";

    public final static String TABLE_NAME_ALIAS = "Отмененные рейсы";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("canceled_flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("canceled_flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта")
    );

    public final static List<LinkedToken> ADD_TOKENS = Arrays.asList(
            new LinkedToken("canceled_flight_id", "Рейс", false, TokenType.INTEGER, 0, "select timetable_id, flight_code, leave_time, arrival_time from timetable join public.flights f on f.flight_id = timetable.flight_id\n" +
                    "where leave_time > CURRENT_TIMESTAMP and timetable_id not in (select * from canceled_flights)", List.of(
                    new Token("timetable_id", true, TokenType.INTEGER, null, false, true, "?"),
                    new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
                    new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
                    new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта")
            ))
    );

    public final static HashMap<String, String> CONSTRAINTS_TEXTS = new HashMap<>(Map.of(
            "delayed_flights_delay_time_minutes_check", "Время задержки не может быть меньше 0"

    ));


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
        return DATA_TOKENS;
    }

    @Override
    public String getRequestForGetData() {
        return "select canceled_flight_id, flight_code ,leave_time, arrival_time from canceled_flights join public.timetable t on t.timetable_id = canceled_flights.canceled_flight_id join public.flights f on f.flight_id = t.flight_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "insert into canceled_flights(canceled_flight_id) values (?)";
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
