package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelayedFlights implements ITable {

    public final static String TABLE_NAME = "delayed_flight";

    public final static String TABLE_NAME_ALIAS = "Задержанные рейсы";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("delayed_flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("delayed_flight_time", true, TokenType.TIMESTAMP, null, false, true, "Идентификатор"),
            new Token("delayed_flight_reason", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("delay_time_minutes", true, TokenType.INTEGER, null, false, true, "Идентификатор")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("delayed_flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("delayed_flight_time", false, TokenType.TIMESTAMP, null, true, true, "Время задержания"),
            new Token("delayed_flight_reason", false, TokenType.INTEGER, null, false, true, "Причина"),
            new Token("delay_reason_name", false, TokenType.INTEGER, null, true, true, "Причина"),
            new Token("delay_time_minutes", false, TokenType.INTEGER, null, true, true, "Время задержка (минут)"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта")
    );

    public final static List<LinkedToken> ADD_TOKENS = Arrays.asList(
           new LinkedToken("delayed_flight_time", "Время задержания", false, TokenType.TIMESTAMP, 0, null, null),
            new LinkedToken("delayed_flight_reason", "Причина", false, TokenType.INTEGER, 0, "select * from delay_reasons",
                    List.of(
                            new Token("delay_reason_id", true, TokenType.INTEGER, null, false, true, "?"),
                            new Token("delay_reason_name", true, TokenType.VARCHAR, null, true, true, "?")
                    )),
            new LinkedToken("delay_time_minutes", "Время задежки (минуты)", false, TokenType.INTEGER, 0, null, null),
            new LinkedToken("delayed_flight_id", "Рейс", false, TokenType.INTEGER, 0, "select timetable_id, leave_time, arrival_time, flight_code from timetable join public.flights f on f.flight_id = timetable.flight_id\n" +
                    "where leave_time > current_timestamp and timetable_id not in (select * from canceled_flights)", List.of(
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
        return "select delayed_flight_id, delayed_flight_time, delayed_flight_reason, delay_reason_name,delay_time_minutes, flight_code, leave_time, arrival_time from delayed_flights join public.delay_reasons dr on dr.delay_reason_id = delayed_flights.delayed_flight_reason\n" +
                "join public.timetable t on t.timetable_id = delayed_flights.delayed_flight_id join public.flights f on f.flight_id = t.flight_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "insert into delayed_flights(delayed_flight_time, delayed_flight_reason, delay_time_minutes, delayed_flight_id) values (? , ? , ? , ?)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return CONSTRAINTS_TEXTS;
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
