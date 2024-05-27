package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Departments implements ITable {

    public final static String TABLE_NAME = "departments";

    public final static String TABLE_NAME_ALIAS = "Отделы";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("department_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("department_specialization", false,TokenType.INTEGER, null, true, true, "Специализация"),
            new Token("department_head_id", false,TokenType.INTEGER, null, true, true, "ИД главы отдела")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("department_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("emp_id", false, TokenType.INTEGER, null, false, true, "Работник ИД"),
            new Token("specialties_name", false,TokenType.VARCHAR, null, true, true, "Специализация"),
            new Token("emp_firstname", false, TokenType.VARCHAR, null, true, true, "Имя"),
            new Token("emp_lastname", false, TokenType.VARCHAR, null, true, true, "Фамилия"),
            new Token("emp_fathername", false, TokenType.VARCHAR, null, true, true, "Отчество"),
            new Token("emp_passport", false, TokenType.VARCHAR, null, true, true, "Паспорт")
    );
    public final static List<LinkedToken> UPDATE_TOKENS = List.of(
            new LinkedToken("department_head_id", "Глава отдела", false, TokenType.INTEGER, 0, "select emp_id, emp_firstname, emp_lastname, emp_fathername, emp_passport from administration join employees e on e.emp_id = administration.administrator_id", List.of(
                    new Token("emp_id", true, TokenType.INTEGER, null, false, true, "Работник ИД"),
                    new Token("emp_firstname", false, TokenType.VARCHAR, null, true, true, "Имя"),
                    new Token("emp_lastname", false, TokenType.VARCHAR, null, true, true, "Фамилия"),
                    new Token("emp_fathername", false, TokenType.VARCHAR, null, true, true, "Отчество"),
                    new Token("emp_passport", false, TokenType.VARCHAR, null, true, true, "Паспорт")))
    );


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
        return "select department_id, emp_id, specialties_name, emp_firstname, emp_lastname, emp_fathername, emp_passport" +
                " from departments join specialties s on s.specialties_id = departments.department_specialization" +
                " join administration a on a.administrator_id = departments.department_head_id" +
                " join employees e on e.emp_id = a.administrator_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return null;
    }

    @Override
    public String getSQLForAddData() {
        return null;
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return null;
    }

    @Override
    public String getSQLForUpdate() {
        return "update departments set department_head_id = ? where department_id = ?";
    }

    @Override
    public List<LinkedToken> getTokensForUpdate() {
        return UPDATE_TOKENS;
    }

    @Override
    public String getSQLForDelete() {
        return null;
    }
}
