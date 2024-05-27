package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetAirplanesByFlights implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("flight_count", true, TokenType.INTEGER, null, true, true, "Количество вылетов"),
            new Token("airplane_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplane_model_id", false, TokenType.INTEGER, null, false, true, "Имя"),
            new Token("airplane_pilots_brigade", false, TokenType.INTEGER, null, true, true, "Бригада пилотов"),
            new Token("airplane_technican_brigade", false, TokenType.INTEGER, null, true, true, "Бригада техников"),
            new Token("airplane_service_brigade", false, TokenType.INTEGER, null, true, true, "Бригада обслуживания"),
            new Token("airplane_arrival_date", false, TokenType.DATE, null, true, true, "Дата поступления"),
            new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Название")
    );

    public final static List<String> CONDITIONS = List.of(
            "Flight_count >= ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = List.of(
            new LinkedToken("Experience", "Количество вылетов", false, TokenType.INTEGER, 0, null, null)
    );

    public static String ALIAS = "Самолёты по количетсву вылетов";

    @Override
    public String getRequestName() {
        return "get_airplanes_in_airport_by_flights";
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
        return "WITH Count_of_flights AS(\n" +
                "SELECT DISTINCT t.airplane_id, count(*) OVER(PARTITION BY t.airplane_id) AS Flight_count\n" +
                "FROM airplanes JOIN Timetable t on Airplanes.airplane_id = t.Airplane_id \n" +
                "WHERE t.timetable_id NOT IN (SELECT canceled_flight_id FROM canceled_flights)\n" +
                "ORDER BY airplane_id)\n" +
                "\n" +
                "SELECT COALESCE(Flight_count, 0) AS Flight_count,\n" +
                "       airplanes.airplane_id, airplane_model_id, airplane_pilots_brigade,\n" +
                "       airplane_technican_brigade, airplane_service_brigade, airplane_arrival_date,\n" +
                "       airplane_name\n" +
                "FROM airplanes \n" +
                "LEFT JOIN Count_of_flights ON Count_of_flights.airplane_id = airplanes.airplane_id";
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
