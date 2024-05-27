package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PilotMedCheckLogs implements ITable {

    public final static String TABLE_NAME = "pilots_med_check_logs";

    public final static String TABLE_NAME_ALIAS = "Мед. осмотр пилотов";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("pilots_med_check_log_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("pilots_med_check_log_pilot_id", false,TokenType.INTEGER, null, true, true, "ИД пилота"),
            new Token("pilots_med_check_log_date", false,TokenType.TIMESTAMP, null, true, true, "Дата осмотра"),
            new Token("pilots_med_check_log_is_unhealthy", false, TokenType.BOOLEAN, null, true, true, "Здоров")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("pilots_med_check_log_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("emp_id", false, TokenType.INTEGER, null, false, true, "Работник ИД"),
            new Token("emp_firstname", false, TokenType.VARCHAR, null, true, true, "Имя"),
            new Token("emp_lastname", false, TokenType.VARCHAR, null, true, true, "Фамилия"),
            new Token("emp_fathername", false, TokenType.VARCHAR, null, true, true, "Отчество"),
            new Token("emp_passport", false, TokenType.VARCHAR, null, true, true, "Паспорт"),
            new Token("pilots_med_check_log_date", false,TokenType.TIMESTAMP, null, true, true, "Дата осмотра"),
            new Token("pilots_med_check_log_is_unhealthy", false, TokenType.BOOLEAN, null, true, true, "Здоров")

    );

    public final static List<LinkedToken> ADD_TOKENS = Arrays.asList(
            new LinkedToken("emp_id", "Пилот", false, TokenType.INTEGER, 0, "select emp_id, emp_firstname, emp_lastname, emp_fathername, emp_passport from pilots join employees e on e.emp_id = pilots.pilot_id", List.of(
                    new Token("emp_id", true, TokenType.INTEGER, null, false, true, "Работник ИД"),
                    new Token("emp_firstname", false, TokenType.VARCHAR, null, true, true, "Имя"),
                    new Token("emp_lastname", false, TokenType.VARCHAR, null, true, true, "Фамилия"),
                    new Token("emp_fathername", false, TokenType.VARCHAR, null, true, true, "Отчество"),
                    new Token("emp_passport", false, TokenType.VARCHAR, null, true, true, "Паспорт"))),
            new LinkedToken("pilots_med_check_log_date", "Дата осмотра", false, TokenType.TIMESTAMP, 0, null, null),
            new LinkedToken("pilots_med_check_log_is_unhealthy", "Здоров", false, TokenType.BOOLEAN, 0, null, null)
    );
    public final static List<LinkedToken> UPDATE_TOKENS = List.of(
            new LinkedToken("emp_id", "Пилот", false, TokenType.INTEGER, 0, "select emp_id, emp_firstname, emp_lastname, emp_fathername, emp_passport from pilots join employees e on e.emp_id = pilots.pilot_id", List.of(
                    new Token("emp_id", true, TokenType.INTEGER, null, false, true, "Работник ИД"),
                    new Token("emp_firstname", false, TokenType.VARCHAR, null, true, true, "Имя"),
                    new Token("emp_lastname", false, TokenType.VARCHAR, null, true, true, "Фамилия"),
                    new Token("emp_fathername", false, TokenType.VARCHAR, null, true, true, "Отчество"),
                    new Token("emp_passport", false, TokenType.VARCHAR, null, true, true, "Паспорт"))),
            new LinkedToken("pilots_med_check_log_date", "Дата осмотра", false, TokenType.TIMESTAMP, 0, null, null),
            new LinkedToken("pilots_med_check_log_is_unhealthy", "Здоров", false, TokenType.BOOLEAN, 0, null, null)
    );

    public final static HashMap<String, String> CONSTRAINTS = new HashMap<>(Map.of(
            "pilots_med_check_logs_pilots_med_check_log_date_check", "Дата осмотра не может быть больше, чем сейчас",
            "wrong time", "Дата осмотра не может быть раньше, чем пилот был нанят на работу"
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
        return "select pilots_med_check_log_id, emp_id, emp_firstname, emp_lastname, emp_fathername, emp_passport, pilots_med_check_log_date, pilots_med_check_log_is_unhealthy" +
                " from pilots_med_check_logs join pilots on pilots_med_check_log_pilot_id=pilot_id join employees on emp_id=pilot_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "insert into pilots_med_check_logs(pilots_med_check_log_pilot_id, pilots_med_check_log_date, pilots_med_check_log_is_unhealthy) values(? , ? , ?)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return CONSTRAINTS;
    }

    @Override
    public String getSQLForUpdate() {
        return "update pilots_med_check_logs set pilots_med_check_log_pilot_id = ? , pilots_med_check_log_date = ? , pilots_med_check_log_is_unhealthy = ? where pilots_med_check_log_id = ?";
    }

    @Override
    public List<LinkedToken> getTokensForUpdate() {
        return UPDATE_TOKENS;
    }

    @Override
    public String getSQLForDelete() {
        return "delete from pilots_med_check_logs where pilots_med_check_log_id = ?";
    }
}
