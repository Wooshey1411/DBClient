package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetEmployees implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("emp_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("emp_firstname", false, TokenType.VARCHAR, null, true, true, "Имя"),
            new Token("emp_lastname", false, TokenType.VARCHAR, null, true, true, "Фамилия"),
            new Token("emp_fathername", false, TokenType.VARCHAR, null, true, true, "Отчество"),
            new Token("emp_gender", false, TokenType.VARCHAR, null, true, true, "Пол"),
            new Token("emp_passport", false, TokenType.VARCHAR, null, true, true, "Паспорт"),
            new Token("emp_hire_date", false, TokenType.DATE, null, true, true, "Дата найма"),
            new Token("emp_birthday", false, TokenType.DATE, null, true, true, "Дата рождения"),
            new Token("emp_salary", false, TokenType.INTEGER, null, true, true, "Зарплата"),
            new Token("emp_children_count", false, TokenType.INTEGER, null, true, true, "Количество детей")
    );

    public final static List<String> CONDITIONS = Arrays.asList(
            "date_part('month', age(Emp_hire_date)) + date_part('year', age(Emp_hire_date))*12 > ?",
            "Emp_gender = ?",
            "date_part('year', age(Emp_birthday)) >= ?",
            "Emp_children_count >= ?",
            "Emp_salary >= ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = Arrays.asList(
            new LinkedToken("Experience", "Стаж (месяцы)", true, TokenType.INTEGER, 0, null, null),
            new LinkedToken("emp_gender","Пол", true, TokenType.VARCHAR, 1, "select gender_name as emp_gender from genders",
                    List.of(new Token("emp_gender", true, TokenType.VARCHAR, null, true, true, "Пол"))),
            new LinkedToken("Age", "Возраст", true, TokenType.INTEGER, 0, null, null),
            new LinkedToken("Children count", "Количество детей", true, TokenType.INTEGER, 0, null, null),
            new LinkedToken("Salary", "Зарплата", true, TokenType.INTEGER, 0, null, null)
    );

    public static String ALIAS = "Работники аэропорта";

    @Override
    public String getRequestName() {
        return "get_employees";
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
        return "SELECT * FROM Employees";
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
