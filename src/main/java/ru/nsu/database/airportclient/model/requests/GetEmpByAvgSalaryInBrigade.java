package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetEmpByAvgSalaryInBrigade implements IRequest {

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


    public final static List<LinkedToken> REQUEST_TOKENS = Arrays.asList(
            new LinkedToken("Salary", "Зарплата", false, TokenType.INTEGER, 0, null, null)
    );

    public static String ALIAS = "Работники в бригадах, где зарплата больше среднего по бригадам";

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
        return "WITH All_employees_with_brigades AS(\n" +
                "        SELECT Emp_id, Emp_firstname, Emp_lastname,emp_passport, Emp_fathername, Emp_gender, Emp_hire_date,\n" +
                "               Emp_salary, Emp_children_count, Emp_birthday, b.brigade_id AS Brigade_id FROM Employees\n" +
                "             JOIN Brigades_distribution bd on Emp_id = Employee_id\n" +
                "                JOIN Brigades b on b.Brigade_id = bd.Brigade_id),\n" +
                "     All_brigades_avg_salary AS (\n" +
                "            SELECT DISTINCT Brigade_id, avg(Emp_salary) OVER(PARTITION BY Brigade_id) AS Avg_salary\n" +
                "            FROM All_employees_with_brigades),\n" +
                "    All_brigades_sum_salary AS (\n" +
                "            SELECT DISTINCT Brigade_id, sum(Emp_salary) OVER(PARTITION BY Brigade_id) AS Sum_salary\n" +
                "            FROM All_employees_with_brigades\n" +
                "    )\n" +
                "\n" +
                "\n" +
                "    SELECT Emp_id, Emp_firstname, Emp_lastname, Emp_fathername, Emp_gender, Emp_passport, Emp_hire_date, Emp_birthday, Emp_salary,\n" +
                "           Emp_children_count\n" +
                "    FROM All_employees_with_brigades e\n" +
                "    WHERE e.Emp_salary >= (SELECT Avg_salary FROM All_brigades_avg_salary vs WHERE vs.Brigade_id = e.Brigade_id)";
    }

    @Override
    public List<String> getRequestCond() {
        return null;
    }

    @Override
    public List<LinkedToken> getTokensForRequest() {
        return REQUEST_TOKENS;
    }
}
