package ru.nsu.database.airportclient.model;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.UserRights;

public class MarkedTable {
    private final ITable table;
    private final UserRights rights;

    public MarkedTable(ITable table, UserRights rights) {
        this.table = table;
        this.rights = rights;
    }

    public ITable getTable() {
        return table;
    }

    public UserRights getRights() {
        return rights;
    }
}
