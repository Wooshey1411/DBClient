package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetAirplanesThatFlightBeforeRepair implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("airplane_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Название")
    );

    public final static List<String> CONDITIONS = List.of(
            "(select repairs_count FROM Rep_count\n" +
                    "                                                        WHERE Rep_count.airplane_id = airplanes.airplane_id) >= ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = List.of(
            new LinkedToken("Experience", "Количество рейсов", false, TokenType.INTEGER, 0, null, null)
    );

    public static String ALIAS = "Самолеты, соверщившие рейсы заданное кол-во раз перед ремонтом";

    @Override
    public String getRequestName() {
        return "GetAirplanesThatFlightBeforeRepair";
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
        return "\n" +
                "\n" +
                "    WITH Last_repair_time AS(\n" +
                "    SELECT Airplanes.airplane_id, COALESCE(max_time, '1980-01-01 00:00') AS rep_time\n" +
                "    FROM Airplanes\n" +
                "    left join\n" +
                "    (SELECT airplane_id, MAX(airplanes_check_log_date) over(partition by airplane_id) as max_time\n" +
                "     FROM (\n" +
                "    SELECT * FROM airplanes_check_logs\n" +
                "    left join Airplanes a on a.airplane_id = airplanes_check_logs.airplanes_check_log_airplane\n" +
                "    where Airplanes_chech_log_is_faulty = false\n" +
                "    order by airplanes_check_log_date)) db on Airplanes.airplane_id = db.airplane_id),\n" +
                "\n" +
                "    Rep_count AS (\n" +
                "    SELECT distinct count(*) over(partition by airplane_id) as repairs_count, airplane_id FROM (\n" +
                "    SELECT airplane_id, airplane_arrival_date, airplane_technican_brigade,\n" +
                "           airplane_service_brigade, airplane_pilots_brigade, airplane_model_id FROM airplanes_check_logs\n" +
                "    left join Airplanes a on a.airplane_id = airplanes_check_logs.airplanes_check_log_airplane\n" +
                "    WHERE (airplanes_check_log_date > (SELECT rep_time FROM Last_repair_time\n" +
                "                                                       WHERE Last_repair_time.airplane_id = a.airplane_id))))\n" +
                "\n" +
                "    SELECT airplane_id, airplane_name FROM airplanes";
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
