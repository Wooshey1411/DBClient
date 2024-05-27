package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agencies implements ITable {

    public final static String TABLE_NAME = "agencies";

    public final static String TABLE_NAME_ALIAS = "Агенства";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("agency_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("agency_name", false,TokenType.INTEGER, null, true, true, "Название")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("agency_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("agency_name", false,TokenType.INTEGER, null, true, true, "Название")
    );
    public final static List<LinkedToken> UPDATE_TOKENS = List.of(
            new LinkedToken("agency_name", "Название", false, TokenType.VARCHAR, 40, null, null)
    );

    public final static List<LinkedToken> ADD_TOKENS = List.of(
            new LinkedToken("agency_name", "Название", false, TokenType.VARCHAR, 40, null, null)
    );

    public final static HashMap<String, String> CONSTRAINTS = new HashMap<>(Map.of(
            "agencies_agency_name_key", "Агенство с таким названием уже есть"
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
        return "select * from agencies";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "insert into agencies(agency_name) values(?)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return CONSTRAINTS;
    }

    @Override
    public String getSQLForUpdate() {
        return "update agencies set agency_name = ? where agency_id = ?";
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
