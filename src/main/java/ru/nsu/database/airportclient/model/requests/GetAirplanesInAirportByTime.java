package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetAirplanesInAirportByTime implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("airplane_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplane_model_id", false, TokenType.INTEGER, null, false, true, "Имя"),
            new Token("airplane_pilots_brigade", false, TokenType.INTEGER, null, true, true, "Бригада пилотов"),
            new Token("airplane_technican_brigade", false, TokenType.INTEGER, null, true, true, "Бригада техников"),
            new Token("airplane_service_brigade", false, TokenType.INTEGER, null, true, true, "Бригада обслуживания"),
            new Token("airplane_arrival_date", false, TokenType.DATE, null, true, true, "Дата поступления"),
            new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Название")
    );

    public final static List<String> CONDITIONS = Arrays.asList(
            "airplane_id IN (SELECT Flight_data.airplane_id FROM Flight_data WHERE destination_name IS NULL)\n" +
                    "\n" +
                    "         OR airplane_id IN (SELECT * FROM NEAR_MIN_FLIGHT_EVENT)\n" +
                    "         OR ? < (SELECT MIN_TIME FROM MIN_LEAVE_TIMES\n" +
                    "                                  WHERE MIN_LEAVE_TIMES.airplane_id = airplanes.airplane_id)"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = Arrays.asList(
            new LinkedToken("Experience", "Дата", false, TokenType.TIMESTAMP, 0, null, null)
    );

    public static String ALIAS = "Самолеты, находящиеся в аэропорту";

    @Override
    public String getRequestName() {
        return "get_airplanes_in_airport";
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
        return "WITH Flight_data AS\n" +
                "(SELECT Airplanes.Airplane_id, Leave_time, Arrival_time, flight_ending_destination_id\n" +
                " FROM Airplanes\n" +
                "    JOIN Timetable t on Airplanes.Airplane_id = t.Airplane_id\n" +
                "    JOIN Flights f on f.Flight_id = t.flight_id\n" +
                "    WHERE timetable_id NOT IN (SELECT canceled_flight_id FROM Canceled_flights)\n" +
                "    ORDER BY Airplane_id, Leave_time),\n" +
                "Nearest_flight_event_time AS (\n" +
                "    select distinct airplane_id, max(arrival_time) over(partition by airplane_id) as ar_time from Flight_data where arrival_time is not null and arrival_time < ?\n" +
                "),\n" +
                "Nearest_flight_event_dest AS (\n" +
                "    select t.airplane_id, flight_ending_destination_id from Nearest_flight_event_time n join timetable t on t.airplane_id = n.airplane_id\n" +
                "             join public.flights f2 on f2.flight_id = t.flight_id   where ar_time = t.arrival_time\n" +
                ")\n" +
                "\n" +
                "SELECT * from airplanes\n" +
                "where (airplane_id not in (select airplane_id from Flight_data) or airplane_id in (select airplane_id from Nearest_flight_event_dest where flight_ending_destination_id = 1))\n" +
                "and (airplane_arrival_date < ?)\n";
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
