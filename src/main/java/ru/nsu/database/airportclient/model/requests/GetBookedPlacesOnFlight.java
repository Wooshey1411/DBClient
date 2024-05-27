package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetBookedPlacesOnFlight implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("ticket_place", true, TokenType.INTEGER, null, true, true, "Номер места"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта")
    );

    public final static List<String> CONDITIONS = Arrays.asList(
            "timetable_id = ?",
            "leave_time::date = ?",
            "flight_basic_price >= ?",
            "leave_time > ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = Arrays.asList(
            new LinkedToken("1", "Рейс", true, TokenType.INTEGER,0 , "select timetable_id, flight_code, leave_time, arrival_time from timetable join public.flights f on f.flight_id = timetable.flight_id",
                    List.of(
                            new Token("timetable_id", true, TokenType.INTEGER, null, false, true, "1"),
                            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "1"),
                            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "1"),
                            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "12")
                    )),
            new LinkedToken("1", "День вылета", true, TokenType.DATE, 0, null, null),
            new LinkedToken("1", "Цена билета", true, TokenType.INTEGER, 0, null, null),
            new LinkedToken("1", "Время вылета", true, TokenType.TIMESTAMP, 0, null, null)
    );

    public static String ALIAS = "Забронированные места";

    @Override
    public String getRequestName() {
        return "GetFreePlacesOnFlight";
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
        return "select ticket_place, flight_code, leave_time, arrival_time from (select * from timetable join public.tickets\n" +
                " t on timetable.timetable_id = t.ticket_flight_id join public.flights f on f.flight_id = timetable.flight_id where t.ticket_passanger_id is not null)\n" +
                "\n";
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
