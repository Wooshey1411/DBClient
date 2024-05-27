package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetFlights implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("timetable_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта"),
            new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Название самолёта"),
            new Token("destination_name", false, TokenType.VARCHAR, null, true, true, "Пункт вылета"),
            new Token("ending_destination_name", false, TokenType.VARCHAR, null, true, true, "Пункт прилёта")
    );

    public final static List<String> CONDITIONS = Arrays.asList(
            "flight_ending_destination_id = ?",
            "extract(epoch FROM age(arrival_time, leave_time))/3600 >= ?",
            "flight_basic_price >= ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = Arrays.asList(
            new LinkedToken("destination_name","Пункт прилёта", true, TokenType.INTEGER, 1, "select * from destinations",
                    List.of(new Token("destination_id", true, TokenType.VARCHAR, null, false, true, "Пол"),
                    new Token("destination_name", false, TokenType.VARCHAR, null, true, true, "ПН"))),
            new LinkedToken("Age", "Длительность полёта (часы)", true, TokenType.INTEGER, 0, null, null),
            new LinkedToken("Children count", "Цена билета", true, TokenType.INTEGER, 0, null, null)
    );

    public static String ALIAS = "Рейсы";

    @Override
    public String getRequestName() {
        return "GetFlights";
    }

    @Override
    public String getRequestAlias() {
        return ALIAS;
    }

    @Override
    public List<Token> getDataTokens() {
        return TOKENS;
    }

    @Override
    public String getRequestBody() {
        return "select timetable_id, flight_code, leave_time, arrival_time, airplane_name,destination_name, ending_destination_name\n" +
                "from (SELECT f.flight_ending_destination_id, timetable_id, flight_code, leave_time, arrival_time, airplane_name, destination_name as ending_destination_name, flight_starting_destination_id  from timetable\n" +
                "    join Flights f on f.flight_id = timetable.flight_id\n" +
                "    join destinations on f.flight_ending_destination_id = destinations.destination_id\n" +
                "    join public.airplanes a on a.airplane_id = timetable.airplane_id) join destinations on flight_starting_destination_id = destinations.destination_id";
    }

    @Override
    public List<String> getRequestCond() {
        return CONDITIONS;
    }

    @Override
    public List<LinkedToken> getTokensForRequest() {
        return REQUEST_TOKENS;
    }
}
