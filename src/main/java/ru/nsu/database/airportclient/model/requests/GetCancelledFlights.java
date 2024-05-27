package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetCancelledFlights implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("canceled_flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта")
    );

    public final static List<String> CONDITIONS = Arrays.asList(
            "flight_ending_destination_id = ?",
            "(select free_places from Free_places_cnt\n" +
                    "                           where Flight_info.canceled_flight_id = Free_places_cnt.canceled_flight_id) > ?",

            "(100 * (select free_places from Free_places_cnt\n" +
                    "                                  where Flight_info.canceled_flight_id = Free_places_cnt.canceled_flight_id)\n" +
                    "            / (select all_places from All_places_cnt\n" +
                    "                                 where Flight_info.canceled_flight_id = All_places_cnt.canceled_flight_id)) > ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = Arrays.asList(
            new LinkedToken("destination_name","Пункт прилёта", true, TokenType.INTEGER, 1, "select * from destinations",
                    List.of(new Token("destination_id", true, TokenType.VARCHAR, null, false, true, "Пол"),
                            new Token("destination_name", false, TokenType.VARCHAR, null, true, true, "ПН"))),
            new LinkedToken("Age", "Количество свободных мест", true, TokenType.INTEGER, 0, null, null),
            new LinkedToken("Children count", "Доля свободных мест в %", true, TokenType.INTEGER, 0, null, null)
    );

    public static String ALIAS = "Отмененные рейсы";

    @Override
    public String getRequestName() {
        return "GetCancelledFlights";
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
        return "    WITH Flight_info AS (\n" +
                "    SELECT canceled_flight_id, destination_name, ticket_passanger_id FROM canceled_flights\n" +
                "        join Timetable t on t.timetable_id = canceled_flights.canceled_flight_id\n" +
                "        join flights f on f.flight_id = t.flight_id\n" +
                "        join destinations d on f.flight_ending_destination_id = d.destination_id\n" +
                "        join public.tickets t2 on t.timetable_id = t2.ticket_flight_id),\n" +
                "\n" +
                "    Free_places_cnt AS (\n" +
                "        SELECT distinct count(*) over(partition by canceled_flight_id) AS free_places,\n" +
                "                        canceled_flight_id FROM Flight_info\n" +
                "        where ticket_passanger_id is NULL\n" +
                "    ),\n" +
                "    All_places_cnt AS (\n" +
                "        SELECT distinct count(*) over(partition by canceled_flight_id) AS all_places,\n" +
                "                        canceled_flight_id FROM Flight_info\n" +
                "    )\n" +
                "\n" +
                "    SELECT distinct canceled_flight_id, flight_code, leave_time, arrival_time  FROM Flight_info join timetable t on canceled_flight_id = t.timetable_id join public.flights f2 on f2.flight_id = t.flight_id";
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
