package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetFlights2 implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта")
    );

    public final static List<String> CONDITIONS = Arrays.asList(
            "flight_category_id = ?",
            "destination_id = ?",
            "airplane_model_id = ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = Arrays.asList(
            new LinkedToken("destination_name","Категории полётов", true, TokenType.INTEGER, 1, "select * from flight_categories",
                    List.of(new Token("flight_category_id", true, TokenType.VARCHAR, null, false, true, "Пол"),
                            new Token("flight_category_name", false, TokenType.VARCHAR, null, true, true, "ПН"))),
            new LinkedToken("airplane_model", "Модель самолёта", true, TokenType.INTEGER, 1, "select * from airplane_models",
                    List.of(new Token("airplane_model_id", true, TokenType.INTEGER, null, false, true, "Пол"),
                            new Token("airplane_model_name", false, TokenType.VARCHAR, null, true, true, "ПН"))),
            new LinkedToken("destination_name","Пункт прилёта", true, TokenType.INTEGER, 1, "select * from destinations",
                    List.of(new Token("destination_id", true, TokenType.VARCHAR, null, false, true, "Пол"),
                            new Token("destination_name", false, TokenType.VARCHAR, null, true, true, "ПН")))
    );

    public static String ALIAS = "Авиарейсы по параметрам";

    @Override
    public String getRequestName() {
        return "GetFlights2";
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
        return "select flight_code, leave_time, arrival_time from timetable\n" +
                "join public.flights f on f.flight_id = timetable.flight_id\n" +
                "join public.destinations d on f.flight_ending_destination_id = d.destination_id\n" +
                "join public.airplanes a on timetable.airplane_id = a.airplane_id\n";
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
