package ru.nsu.database.airportclient.model.tables.utils;

public record Token(String name, boolean isPK, TokenType tokenType, String data, boolean isShowing, boolean isNotNone, String alias) {}
