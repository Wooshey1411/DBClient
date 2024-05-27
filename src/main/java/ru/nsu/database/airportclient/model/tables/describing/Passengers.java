package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Passengers implements ITable {

    public final static String TABLE_NAME = "passengers";

    public final static String TABLE_NAME_ALIAS = "Пассажиры";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("passenger_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("passenger_firstname", false,TokenType.VARCHAR, null, true, true, "Имя"),
            new Token("passenger_lastname", false,TokenType.VARCHAR, null, true, true, "Фамилия"),
            new Token("passenger_fathername", false,TokenType.VARCHAR, null, true, true, "Отчество"),
            new Token("passenger_passport", false,TokenType.VARCHAR, null, true, true, "Паспорт"),
            new Token("passenger_international_passport", false,TokenType.VARCHAR, null, true, true, "Загран. паспорт"),
            new Token("passenger_birthday", false,TokenType.DATE, null, true, true, "Дата рождения"),
            new Token("passenger_gender", false,TokenType.VARCHAR, null, true, true, "Пол")
    );



    public final static HashMap<String, String> CONSTRAINTS_TEXTS = new HashMap<>(Map.of(
            "passengers_passenger_passport_key", "Человек с таким паспортом уже есть",
            "passengers_passenger_international_passport_key", "Человек с таким заграничным паспортом уже есть",
            "passengers_passenger_birthday_check", "Дата рождения не должна быть меньше 1920 года или не может быть больше чем сегодня"

    ));

    public final static List<LinkedToken> UPDATE_TOKENS = List.of(
            new LinkedToken("passenger_firstname", "Имя", false, TokenType.VARCHAR, 30, null, null),
            new LinkedToken("passenger_lastname", "Фамилия", false, TokenType.VARCHAR, 30, null, null),
            new LinkedToken("passenger_fathername", "Отчество", true, TokenType.VARCHAR, 30, null, null),
            new LinkedToken("passenger_passport", "Паспорт", false, TokenType.VARCHAR, 20, null, null),
            new LinkedToken("passenger_international_passport", "Загран. паспорт", true, TokenType.VARCHAR, 20, null, null),
            new LinkedToken("passenger_birthday", "Дата рождения", false, TokenType.DATE, 0, null, null),
            new LinkedToken("passenger_gender","Пол", false, TokenType.VARCHAR, 1, "select gender_name as passenger_gender from genders",
                    List.of(new Token("passenger_gender", true, TokenType.VARCHAR, null, true, true, "Пол")))
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
        return TOKENS;
    }

    @Override
    public String getRequestForGetData() {
        return "select * from passengers";
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
        return CONSTRAINTS_TEXTS;
    }

    @Override
    public String getSQLForUpdate() {
        return "update passengers set passenger_firstname = ? , passenger_lastname = ? , passenger_fathername = ? , passenger_passport = ? , passenger_international_passport = ? , passenger_birthday = ? , passenger_gender = ? where passenger_id = ?";
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
