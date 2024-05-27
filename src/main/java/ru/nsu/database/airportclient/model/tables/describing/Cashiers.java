package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cashiers implements ITable {

    public final static String TABLE_NAME = "cashiers";

    public final static String TABLE_NAME_ALIAS = "Кассиры";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("cashier_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("cashier_is_have_access_to_safe", false,TokenType.BOOLEAN, null, true, true, "Наличие доступа к сейфу")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("cashier_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("emp_firstname", false, TokenType.VARCHAR, null, true, true, "Имя"),
            new Token("emp_lastname", false, TokenType.VARCHAR, null, true, true, "Фамилия"),
            new Token("emp_fathername", false, TokenType.VARCHAR, null, true, true, "Отчество"),
            new Token("emp_passport", false, TokenType.VARCHAR, null, true, true, "Паспорт"),
            new Token("emp_salary", false, TokenType.INTEGER, null, true, true, "Зарплата"),
            new Token("cashier_is_have_access_to_safe", false, TokenType.BOOLEAN, null, true, true, "Наличие доступа к сейфу")
    );

    public final static List<LinkedToken> ADD_TOKENS = Arrays.asList(
            new LinkedToken("emp_firstname","Имя", false, TokenType.VARCHAR, 30, null, null),
            new LinkedToken("emp_lastname","Фамилия", false, TokenType.VARCHAR, 30, null, null),
            new LinkedToken("emp_fathername","Отчество", false, TokenType.VARCHAR, 30, null, null),
            new LinkedToken("emp_passport","Паспорт", false, TokenType.VARCHAR, 20, null, null),
            new LinkedToken("emp_birthday","Дата рождения", false, TokenType.DATE, 0, null, null),
            new LinkedToken("emp_gender","Пол", false, TokenType.VARCHAR, 1, "select gender_name from genders",
                    List.of(new Token("gender_name", true, TokenType.VARCHAR, null, true, true, "Пол"))),
            new LinkedToken("emp_salary","Зарплата", false, TokenType.INTEGER, 10, null, null),
            new LinkedToken("emp_children_count","Количество детей", false, TokenType.INTEGER, 3, null, null),
            new LinkedToken("emp_hire_date","Дата найма", false, TokenType.DATE, 0, null, null),
            new LinkedToken("cashier_is_have_access_to_safe","Имеется доступ к сейфу", false, TokenType.BOOLEAN, 0, null, null)
    );

    public final static HashMap<String, String> CONSTRAINTS_TEXTS = new HashMap<>(Map.of(
            "employees_emp_children_count_check", "Количество детей должно быть больше 0",
            "employees_emp_salary_check", "Зарплата должна быть больше 0",
            "employees_emp_birthday_check", "Дата рождения не должна быть меньше 1920 года или человек не должен быть младше 14 лет",
            "employees_emp_hire_date_check", "Дата найма не может быть позже текущего дня",
            "employees_emp_passport_key", "Человек с таким паспортом уже есть"

    ));

    public final static List<LinkedToken> UPDATE_TOKENS = List.of(
            new LinkedToken("cashier_is_have_access_to_safe", "Наличие доступа к сейфу", false, TokenType.BOOLEAN, 0, null, null)
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
        return "select cashier_id, emp_firstname, emp_lastname, emp_fathername, emp_passport, emp_salary, cashier_is_have_access_to_safe" +
                " from cashiers left join employees e on e.emp_id = cashiers.cashier_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "select \"add_cashier\"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return CONSTRAINTS_TEXTS;
    }

    @Override
    public String getSQLForUpdate() {
        return "update cashiers set cashier_is_have_access_to_safe = ? where cashier_id = ?";
    }

    @Override
    public List<LinkedToken> getTokensForUpdate() {
        return UPDATE_TOKENS;
    }

    @Override
    public String getSQLForDelete() {
        return "select \"delete_cashier\"(?)";
    }
}
