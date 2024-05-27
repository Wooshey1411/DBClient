package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetCheckingAirplanesByTime implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("airplane_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Название")
    );

    public final static List<String> CONDITIONS = List.of(
            "airplanes_check_log_date > ?",
            "airplanes_check_log_date < ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = List.of(
            new LinkedToken("Experience", "От", false, TokenType.TIMESTAMP, 0, null, null),
            new LinkedToken("Experience", "До", false, TokenType.TIMESTAMP, 0, null, null)
    );

    public static String ALIAS = "Самолёты осматриваемы в промежутке времени";

    @Override
    public String getRequestName() {
        return "GetCheckingAirplanesByTime";
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
        return "WITH Logs_with_airplanes AS(\n" +
                "SELECT distinct airplane_id, airplanes_check_log_date FROM airplanes_check_logs\n" +
                "left join public.airplanes a on a.airplane_id = airplanes_check_logs.airplanes_check_log_airplane\n" +
                "order by airplanes_check_log_date)\n" +
                "\n" +
                "SELECT distinct l.airplane_id, airplane_name FROM Logs_with_airplanes l join airplanes a2 on a2.airplane_id = l.airplane_id";
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
