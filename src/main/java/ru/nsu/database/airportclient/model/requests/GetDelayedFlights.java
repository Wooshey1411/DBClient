package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetDelayedFlights implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("delayed_flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("delayed_flight_time", false, TokenType.TIMESTAMP, null, true, true, "Время задержки"),
            new Token("tickets_returned", false, TokenType.INTEGER, null, true, true, "Билетов возвращено"),
            new Token("delay_reason_name", false, TokenType.VARCHAR, null, true, true, "Причина задержки"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта")
    );

    public final static List<String> CONDITIONS = Arrays.asList(
            "flight_ending_destination_id = ?",
            "delayed_flight_reason = ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = Arrays.asList(
            new LinkedToken("destination_name","Пункт прилёта", true, TokenType.INTEGER, 1, "select * from destinations",
                    List.of(new Token("destination_id", true, TokenType.VARCHAR, null, false, true, "Пол"),
                            new Token("destination_name", false, TokenType.VARCHAR, null, true, true, "ПН"))),
            new LinkedToken("delayed_flight_reason","Причина задержки", true, TokenType.INTEGER, 1, "select * from delay_reasons",
                    List.of(new Token("delay_reason_id", true, TokenType.INTEGER, null, false, true, "Пол"),
                            new Token("delay_reason_name", false, TokenType.VARCHAR, null, true, true, "ПН")))
    );

    public static String ALIAS = "Задержанные рейсы";

    @Override
    public String getRequestName() {
        return "GetDelayedFlights";
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
        return "SELECT delayed_flights.delayed_flight_id, delayed_flights.delayed_flight_time,\n" +
                "       coalesce(returned_tickets_count, 0) as tickets_returned, delay_reason_name, flight_code, leave_time, arrival_time\n" +
                "FROM delayed_flights\n" +
                "left join\n" +
                "(SELECT distinct in_req.delayed_flight_id,\n" +
                "                 count(*) over(partition by in_req.delayed_flight_id) as returned_tickets_count\n" +
                "FROM (select distinct delayed_flight_id, returned_ticket_id from delayed_flights\n" +
                "join delay_reasons dr on dr.delay_reason_id = delayed_flights.delayed_flight_reason\n" +
                "join timetable t on t.timetable_id = delayed_flights.delayed_flight_id\n" +
                "join public.flights f on f.flight_id = t.flight_id\n" +
                "join public.destinations d on d.destination_id = f.flight_ending_destination_id\n" +
                "join public.tickets t2 on t.timetable_id = t2.ticket_flight_id\n" +
                "join public.returned_ticket rt on t2.ticket_id = rt.returned_ticket_id\n" +
                "where returned_ticket_date between delayed_flight_time and t.leave_time) in_req) req on delayed_flights.delayed_flight_id = req.delayed_flight_id\n" +
                "join public.timetable t3 on t3.timetable_id = delayed_flights.delayed_flight_id join public.flights f2 on f2.flight_id = t3.flight_id\n" +
                "join public.delay_reasons r on r.delay_reason_id = delayed_flights.delayed_flight_reason";
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
