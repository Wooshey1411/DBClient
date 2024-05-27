module ru.nsu.database.airportclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens ru.nsu.database.airportclient to javafx.fxml;
    exports ru.nsu.database.airportclient;
    exports ru.nsu.database.airportclient.gui;
    opens ru.nsu.database.airportclient.gui to javafx.fxml;
    exports ru.nsu.database.airportclient.model;
    opens ru.nsu.database.airportclient.model to javafx.fxml;
    exports ru.nsu.database.airportclient.model.connection;
    opens ru.nsu.database.airportclient.model.connection to javafx.fxml;
    exports ru.nsu.database.airportclient.model.tables.utils;
    opens ru.nsu.database.airportclient.model.tables.utils to javafx.fxml;
    exports ru.nsu.database.airportclient.gui.infonodes;
    opens ru.nsu.database.airportclient.gui.infonodes to javafx.fxml;
}