package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrigadesDistribution implements ITable {

    public final static String TABLE_NAME = "brigades_distribution";

    public final static String TABLE_NAME_ALIAS = "Распределение бригад";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("brigade_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("employee_id", false,TokenType.INTEGER, null, true, true, "Работник")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("brigade_id", true, TokenType.INTEGER, null, true, true, "Номер бригады"),
            new Token("specialties_name", false, TokenType.VARCHAR, null, true, true, "Специализация"),
            new Token("specialties_id", false, TokenType.INTEGER, null, false, true, "Специализация ИД"),
            new Token("emp_id", true, TokenType.INTEGER, null, false, true, "Работник ИД"),
            new Token("emp_firstname", false, TokenType.VARCHAR, null, true, true, "Имя"),
            new Token("emp_lastname", false, TokenType.VARCHAR, null, true, true, "Фамилия"),
            new Token("emp_fathername", false, TokenType.VARCHAR, null, true, true, "Отчество"),
            new Token("emp_passport", false, TokenType.VARCHAR, null, true, true, "Паспорт")
    );

    public final static List<LinkedToken> ADD_TOKENS = Arrays.asList(
            new LinkedToken("emp_id", "Работник", false, TokenType.INTEGER, 0, "select emp_id, emp_firstname, emp_lastname, emp_fathername, emp_passport, specialties_name from pilots join employees on pilot_id = emp_id cross join specialties sp where sp.specialties_id = 1\n" +
                    "union\n" +
                    "select emp_id, emp_firstname, emp_lastname, emp_fathername, emp_passport, specialties_name from technicans join employees on technican_id = emp_id cross join specialties sp where sp.specialties_id = 3\n" +
                    "union\n" +
                    "select emp_id, emp_firstname, emp_lastname, emp_fathername, emp_passport, specialties_name from service_workers join employees on service_worker_id = emp_id cross join specialties sp where sp.specialties_id = 4", List.of(
                    new Token("emp_id", true, TokenType.INTEGER, null, false, true, "Работник ИД"),
                    new Token("emp_firstname", false, TokenType.VARCHAR, null, true, true, "Имя"),
                    new Token("emp_lastname", false, TokenType.VARCHAR, null, true, true, "Фамилия"),
                    new Token("emp_fathername", false, TokenType.VARCHAR, null, true, true, "Отчество"),
                    new Token("emp_passport", false, TokenType.VARCHAR, null, true, true, "Паспорт"),
                    new Token("specialties_name", false, TokenType.VARCHAR, null, true, true, "Специализация"))),
            new LinkedToken("brigade_id", "Бригада", false, TokenType.INTEGER, 0, "select brigade_id, specialties_name from brigades join departments on brigare_department_id = department_id join specialties on department_specialization = specialties_id", List.of(
                    new Token("brigade_id", true, TokenType.INTEGER, null, true, true, "Номер бригады"),
                    new Token("specialties_name", false, TokenType.VARCHAR, null, true, true, "Специализация")
            ))
    );

    public final static HashMap<String, String> CONSTRAINTS_TEXTS = new HashMap<>(Map.of(
        "bad_brigade", "В бригаду такой специализации нельзя записывать работников",
            "bad_speciality", "Работник не соответствует записываемой бригаде",
            "brigades_distribution_pkey", "Работик уже находится в данной бригаде"
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
        return "select bd.brigade_id, specialties_name, specialties_id, emp_id, emp_firstname, emp_lastname, emp_fathername, emp_passport from brigades_distribution bd join employees on employee_id = emp_id\n" +
                "join brigades b on b.brigade_id = bd.brigade_id join departments on department_id = b.brigare_department_id\n" +
                "join specialties on department_specialization = specialties_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "select \"add_emp_to_brigade\"(?, ?)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return CONSTRAINTS_TEXTS;
    }

    @Override
    public String getSQLForUpdate() {
        return null;
    }

    @Override
    public List<LinkedToken> getTokensForUpdate() {
        return null;
    }

    @Override
    public String getSQLForDelete() {
        return "delete from brigades_distribution where brigade_id = ? and employee_id = ?";
    }
}
