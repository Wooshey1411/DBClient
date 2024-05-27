package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;

import java.util.List;

public interface IRequest {
    String getRequestName();
    String getRequestAlias();

    List<Token> getDataTokens();
    String getRequestBody();

    List<String> getRequestCond();
    List<LinkedToken> getTokensForRequest();

}
