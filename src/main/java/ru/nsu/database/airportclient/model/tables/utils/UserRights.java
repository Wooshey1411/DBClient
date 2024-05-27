package ru.nsu.database.airportclient.model.tables.utils;

public class UserRights {
    private String read;
    private String add;
    private String edit;
    private String delete;

    public UserRights(String read, String add, String edit, String delete) {
        this.read = read;
        this.add = add;
        this.edit = edit;
        this.delete = delete;
    }

    public String getRead() {
        return read;
    }

    public String getAdd() {
        return add;
    }

    public String getEdit() {
        return edit;
    }

    public String getDelete() {
        return delete;
    }
}
