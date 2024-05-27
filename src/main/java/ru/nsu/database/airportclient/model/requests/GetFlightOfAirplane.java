package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetFlightOfAirplane implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("timetable_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса")
    );

    public final static List<String> CONDITIONS = List.of(
            "airplane_model_id = ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = List.of(
            new LinkedToken("airplane_model", "Модель самолёта", true, TokenType.INTEGER, 1, "select * from airplane_models",
                    List.of(new Token("airplane_model_id", true, TokenType.INTEGER, null, false, true, "Пол"),
                            new Token("airplane_model_name", false, TokenType.VARCHAR, null, true, true, "ПН")))
    );

    public static String ALIAS = "Рейсы, по котором летает самолёт опредленной модели";

    @Override
    public String getRequestName() {
        return "GetFlightOfAirplane";
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
        return "select timetable_id, leave_time, arrival_time, flight_code, minimum_tickets\n" +
                "from timetable\n" +
                "left join public.airplanes a on a.airplane_id = timetable.airplane_id join public.flights f on f.flight_id = timetable.flight_id";
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
