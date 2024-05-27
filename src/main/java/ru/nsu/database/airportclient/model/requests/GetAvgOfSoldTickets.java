package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetAvgOfSoldTickets implements IRequest {

    public final static List<Token> TOKENS = List.of(
            new Token("avg", true, TokenType.INTEGER, null, true, true, "Среднее")
    );

    public final static List<String> CONDITIONS = List.of(
            "flight_basic_price > ?",
            "extract(epoch FROM age(arrival_time, leave_time))/60 > ?",
            "leave_time::time > ?",
            "destination_id = ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = List.of(
            new LinkedToken("flight_basic_price", "Цена билета", true, TokenType.INTEGER, 0, null, null),
            new LinkedToken("flight_basic_price", "Длительность перелета (часы)", true, TokenType.INTEGER, 0, null, null),
            new LinkedToken("flight_basic_price", "Время вылета", true, TokenType.TIMESTAMP, 0, null, null),
            new LinkedToken("destination_name","Пункт прилёта", true, TokenType.INTEGER, 1, "select * from destinations",
                    List.of(new Token("destination_id", true, TokenType.VARCHAR, null, false, true, "Пол"),
                            new Token("destination_name", false, TokenType.VARCHAR, null, true, true, "ПН")))
    );

    public static String ALIAS = "Среднее количество проданных билетов на рейсы";

    @Override
    public String getRequestName() {
        return "GetAvgOfSoldTickets";
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
        return "WITH all_selled_tickets AS (select distinct timetable_id,\n" +
                "                                            coalesce(count(*) over(partition by timetable_id), 0) as selled,\n" +
                "                                            flight_basic_price, leave_time, arrival_time, destination_id from timetable\n" +
                "join public.flights f on f.flight_id = timetable.flight_id\n" +
                "join public.destinations d on d.destination_id = f.flight_ending_destination_id\n" +
                "join public.tickets t on timetable.timetable_id = t.ticket_flight_id \n" +
                "where ticket_passanger_id is not null\n" +
                "and (flight_category_id = 1 or flight_category_id = 2))\n" +
                "select avg(selled) from all_selled_tickets";
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
