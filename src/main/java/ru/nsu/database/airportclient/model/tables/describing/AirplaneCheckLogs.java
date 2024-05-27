package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirplaneCheckLogs implements ITable {

    public final static String TABLE_NAME = "airplane_check_logs";

    public final static String TABLE_NAME_ALIAS = "Осмотр самолётов";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("airplanes_check_log_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplanes_check_log_airplane", false,TokenType.INTEGER, null, true, true, "ИД самолёта"),
            new Token("airplanes_check_log_date", false,TokenType.TIMESTAMP, null, true, true, "Дата осмотра"),
            new Token("airplanes_chech_log_is_faulty", false, TokenType.BOOLEAN, null, true, true, "Исправный")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("airplanes_check_log_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplanes_check_log_airplane", false,TokenType.INTEGER, null, false, true, "ИД самолёта"),
            new Token("airplane_model_name", false, TokenType.VARCHAR, null, true, true, "Модель самолёта"),
            new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Название самолёта"),
            new Token("airplanes_check_log_date", false,TokenType.TIMESTAMP, null, true, true, "Дата осмотра"),
            new Token("airplanes_chech_log_is_faulty", false, TokenType.BOOLEAN, null, true, true, "Исправный")

    );

    public final static List<LinkedToken> ADD_TOKENS = Arrays.asList(
            new LinkedToken("airplanes_check_log_airplane", "Самолёт", false, TokenType.INTEGER, 0, "select airplane_id, airplane_model_name, airplane_name from airplanes join airplane_models am on am.airplane_model_id = airplanes.airplane_model_id", List.of(
                    new Token("airplane_id", true, TokenType.INTEGER, null, false, true, "Работник ИД"),
                    new Token("airplane_model_name", false, TokenType.VARCHAR, null, true, true, "Имя"),
                    new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Фамилия"))),
            new LinkedToken("airplanes_check_log_date", "Дата осмотра", false, TokenType.TIMESTAMP, 0, null, null),
            new LinkedToken("airplanes_chech_log_is_faulty", "Исправен", false, TokenType.BOOLEAN, 0, null, null)
    );
    public final static List<LinkedToken> UPDATE_TOKENS = List.of(
            new LinkedToken("airplanes_check_log_airplane", "Самолёт", false, TokenType.INTEGER, 0, "select airplane_id as airplanes_check_log_airplane, airplane_model_name, airplane_name from airplanes join airplane_models am on am.airplane_model_id = airplanes.airplane_model_id", List.of(
                    new Token("airplanes_check_log_airplane", true, TokenType.INTEGER, null, false, true, "Работник ИД"),
                    new Token("airplane_model_name", false, TokenType.VARCHAR, null, true, true, "Имя"),
                    new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Фамилия"))),
            new LinkedToken("airplanes_check_log_date", "Дата осмотра", false, TokenType.TIMESTAMP, 0, null, null),
            new LinkedToken("airplanes_chech_log_is_faulty", "Исправен", false, TokenType.BOOLEAN, 0, null, null)
    );

    public final static HashMap<String, String> CONSTRAINTS = new HashMap<>(Map.of(
            "airplanes_check_logs_airplanes_check_log_date_check", "Дата осмотра не может быть больше, чем сейчас",
            "wrong time", "Дата осмотра не может быть раньше, чем самолёт поступил на службу"
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
        return "select airplanes_check_log_id, airplanes_check_log_airplane, airplane_model_name, airplane_name, airplanes_check_log_date, airplanes_chech_log_is_faulty" +
                " from airplanes_check_logs join airplanes a on a.airplane_id = airplanes_check_logs.airplanes_check_log_airplane join airplane_models am on am.airplane_model_id = a.airplane_model_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "insert into airplanes_check_logs(airplanes_check_log_airplane, airplanes_check_log_date, airplanes_chech_log_is_faulty) VALUES (? , ? , ?)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return CONSTRAINTS;
    }

    @Override
    public String getSQLForUpdate() {
        return "update airplanes_check_logs set airplanes_check_log_airplane = ? , airplanes_check_log_date = ? , airplanes_chech_log_is_faulty = ? where airplanes_check_log_id = ?";
    }

    @Override
    public List<LinkedToken> getTokensForUpdate() {
        return UPDATE_TOKENS;
    }

    @Override
    public String getSQLForDelete() {
        return "delete from airplanes_check_logs where airplanes_check_log_id = ?";
    }
}
