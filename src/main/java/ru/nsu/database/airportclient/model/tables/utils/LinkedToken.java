package ru.nsu.database.airportclient.model.tables.utils;

import java.util.List;

public record LinkedToken(String name, String alias, boolean isCanBeNone, TokenType type, int maxLength, String request, List<Token> requestTokens) {}
