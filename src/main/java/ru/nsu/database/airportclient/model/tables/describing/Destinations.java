package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Destinations implements ITable {

    public final static String TABLE_NAME = "destinations";

    public final static String TABLE_NAME_ALIAS = "Направления";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("destination_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("destination_name", false,TokenType.INTEGER, null, true, true, "Пункт")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("destination_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("destination_name", false,TokenType.INTEGER, null, true, true, "Пункт")
    );
    public final static List<LinkedToken> UPDATE_TOKENS = List.of(
            new LinkedToken("destination_name", "Пункт", false, TokenType.VARCHAR, 40, null, null)
    );

    public final static List<LinkedToken> ADD_TOKENS = List.of(
            new LinkedToken("destination_name", "Пункт", false, TokenType.VARCHAR, 40, null, null)
    );

    public final static HashMap<String, String> CONSTRAINTS = new HashMap<>(Map.of(
            "destinations_destination_name_key", "Пункт с таким названием уже есть"
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
        return "select * from destinations";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "insert into destinations(destination_name) values(?)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return CONSTRAINTS;
    }

    @Override
    public String getSQLForUpdate() {
        return "update destinations set destination_name = ? where destination_id = ?";
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
