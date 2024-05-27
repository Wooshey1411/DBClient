package ru.nsu.database.airportclient.model.tables;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;

import java.util.HashMap;
import java.util.List;

public interface ITable {
    String getTableName();
    List<Token> getAllTokens();
    String getTableAlias();
    List<Token> getDataTokens();
    String getRequestForGetData();
    List<LinkedToken> getTokensForAdd();
    String getSQLForAddData();
    HashMap<String, String> getConstraintTexts();
    String getSQLForUpdate();
    List<LinkedToken> getTokensForUpdate();
    String getSQLForDelete();

}
