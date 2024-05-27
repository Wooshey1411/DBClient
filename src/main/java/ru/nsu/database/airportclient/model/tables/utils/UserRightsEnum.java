package ru.nsu.database.airportclient.model.tables.utils;

public enum UserRightsEnum {
    ADMIN ("A"),
    CASHIER ("C"),
    HR ("H");

    private String rights;

    UserRightsEnum(String rights) {
        this.rights = rights;
    }

    public String getRights() {
        return rights;
    }
}
