package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirplaneModels implements ITable {

    public final static String TABLE_NAME = "airplane_models";

    public final static String TABLE_NAME_ALIAS = "Модели самолётов";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("airplane_model_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplane_model_name", false, TokenType.VARCHAR, null, true, true, "Название"),
            new Token("airplane_model_capacity", false, TokenType.INTEGER, null, true, true, "Вместимость")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("airplane_model_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplane_model_name", false, TokenType.VARCHAR, null, true, true, "Название"),
            new Token("airplane_model_capacity", false, TokenType.INTEGER, null, true, true, "Вместимость")
    );

    public final static List<LinkedToken> ADD_TOKENS = Arrays.asList(
            new LinkedToken("airplane_model_name", "Модель", false, TokenType.VARCHAR, 40, null, null),
            new LinkedToken("airplane_model_capacity", "Вместимость", false, TokenType.INTEGER, 0, null, null)
    );

    public final static HashMap<String, String> CONSTRAINTS_TEXTS = new HashMap<>(Map.of(
        "airplane_model_capacity", "Вместимость не может быть отрицательной",
            "airplane_models_airplane_model_name_key", "Такая модель уже есть"
    ));

    public final static List<LinkedToken> UPDATE_TOKENS = List.of(
            new LinkedToken("airplane_model_name", "Модель", false, TokenType.VARCHAR, 40, null, null),
            new LinkedToken("airplane_model_capacity", "Вместимость", false, TokenType.INTEGER, 0, null, null)
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
        return "select * from airplane_models";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "insert into airplane_models(airplane_model_name, airplane_model_capacity) VALUES(? , ?)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return CONSTRAINTS_TEXTS;
    }

    @Override
    public String getSQLForUpdate() {
        return "update airplane_models set airplane_model_name = ? , airplane_model_capacity = ?  where airplane_model_id = ?";
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
