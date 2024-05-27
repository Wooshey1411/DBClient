package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirplanePreparationLogs implements ITable {

    public final static String TABLE_NAME = "airplane_preparation_logs";

    public final static String TABLE_NAME_ALIAS = "Подготовка самолётов";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("airplanes_preparation_log_flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplanes_preparation_log_work_id", false,TokenType.INTEGER, null, true, true, "ИД работы"),
            new Token("airplanes_preparation_log_date", false,TokenType.TIMESTAMP, null, true, true, "Дата осмотра")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("airplanes_preparation_log_flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplane_id", false, TokenType.INTEGER, null, false, true, "id"),
            new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Название самолёта"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("airplanes_preparation_log_work_id", true,TokenType.INTEGER, null, false, true, "ИД работы"),
            new Token("preparation_work_name", false,TokenType.INTEGER, null, true, true, "Работа"),
            new Token("airplanes_preparation_log_date", false,TokenType.TIMESTAMP, null, true, true, "Дата работ")

    );

    public final static List<LinkedToken> ADD_TOKENS = Arrays.asList(
            new LinkedToken("airplanes_preparation_log_flight_id", "Рейс", false, TokenType.INTEGER, 0, "select timetable_id as airplanes_preparation_log_flight_id, flight_code, leave_time from timetable join public.flights f on f.flight_id = timetable.flight_id", List.of(
                    new Token("airplanes_preparation_log_flight_id", true, TokenType.INTEGER, null, false, true, "Работник ИД"),
                    new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код"),
                    new Token("leave_time", false, TokenType.VARCHAR, null, true, true, "Время вылета"))),
            new LinkedToken("airplanes_preparation_log_work_id", "Работа", false, TokenType.INTEGER, 0, "select * from preparation_works", List.of(
                    new Token("preparation_work_id", true, TokenType.INTEGER, null, false, true, "id"),
                    new Token("preparation_work_name", false, TokenType.VARCHAR, null, true, true, "Название самолёта")
            )),
            new LinkedToken("airplanes_check_log_date", "Дата работ", false, TokenType.TIMESTAMP, 0, null, null)
    );
    public final static List<LinkedToken> UPDATE_TOKENS = Arrays.asList(
            new LinkedToken("airplanes_preparation_log_flight_id", "Рейс", false, TokenType.INTEGER, 0, "select timetable_id as airplanes_preparation_log_flight_id, flight_code, leave_time from timetable join public.flights f on f.flight_id = timetable.flight_id", List.of(
                    new Token("airplanes_preparation_log_flight_id", true, TokenType.INTEGER, null, false, true, "Работник ИД"),
                    new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код"),
                    new Token("leave_time", false, TokenType.VARCHAR, null, true, true, "Время вылета"))),
            new LinkedToken("airplanes_preparation_log_work_id", "Работа", false, TokenType.INTEGER, 0, "select preparation_work_id as airplanes_preparation_log_work_id, preparation_work_name from preparation_works", List.of(
                    new Token("airplanes_preparation_log_work_id", true, TokenType.INTEGER, null, false, true, "id"),
                    new Token("preparation_work_name", false, TokenType.VARCHAR, null, true, true, "Название самолёта")
            )),
            new LinkedToken("airplanes_check_log_date", "Дата работ", false, TokenType.TIMESTAMP, 0, null, null)
    );

    public final static HashMap<String, String> CONSTRAINTS = new HashMap<>(Map.of(
            "airplanes_preparation_logs_airplanes_preparation_log_date_check", "Дата осмотра не может быть больше, чем сейчас",
            "airplanes_preparation_logs_pkey", "Такое приготовление уже было совершено для данного рейса",
            "wrong time", "Дата подготовления не может быть позже, чем вылет самолёта"
    ));


    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<Token> getAllTokens() {
        return TOKENS;
    }

    @Override
    public String getTableAlias() {
        return TABLE_NAME_ALIAS;
    }

    @Override
    public List<Token> getDataTokens() {
        return DATA_TOKENS;
    }

    @Override
    public String getRequestForGetData() {
        return "select airplanes_preparation_log_flight_id, a.airplane_id, airplane_name, flight_code, leave_time, airplanes_preparation_log_work_id, preparation_work_name, airplanes_preparation_log_date from airplanes_preparation_logs join preparation_works pw on pw.preparation_work_id = airplanes_preparation_logs.airplanes_preparation_log_work_id\n" +
                "join public.timetable t on t.timetable_id = airplanes_preparation_logs.airplanes_preparation_log_flight_id join public.flights f on f.flight_id = t.flight_id join public.airplanes a on a.airplane_id = t.airplane_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "insert into airplanes_preparation_logs(airplanes_preparation_log_flight_id, airplanes_preparation_log_work_id, airplanes_preparation_log_date) VALUES (? , ? , ?)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return CONSTRAINTS;
    }

    @Override
    public String getSQLForUpdate() {
        return "update airplanes_preparation_logs set airplanes_preparation_log_flight_id = ? , airplanes_preparation_log_work_id = ? , airplanes_preparation_log_date = ? where airplanes_preparation_log_flight_id = ? and airplanes_preparation_log_work_id = ?";
    }

    @Override
    public List<LinkedToken> getTokensForUpdate() {
        return UPDATE_TOKENS;
    }

    @Override
    public String getSQLForDelete() {
        return "delete from airplanes_preparation_logs where airplanes_preparation_log_flight_id = ? and airplanes_preparation_log_work_id = ?";
    }
}
