package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnedTickets implements ITable {

    public final static String TABLE_NAME = "returned_tickers";

    public final static String TABLE_NAME_ALIAS = "Возвращенные билеты";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("returned_ticket_id", true, TokenType.INTEGER, null, false, true, "Идентификатор")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("returned_ticket_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта"),
            new Token("ticket_place", false, TokenType.INTEGER, null, true, true, "Место")
    );

    public final static List<LinkedToken> ADD_TOKENS = List.of(
            new LinkedToken("ticket_place", "Номер билета", false, TokenType.INTEGER, 0, null, null)
    );

    public final static HashMap<String, String> CONSTRAINTS_TEXTS = new HashMap<>(Map.of(
            "ticket free", "По данному билету место не занято"

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
        return "select returned_ticket_id, flight_code, leave_time, arrival_time, ticket_place from returned_ticket join public.tickets t on t.ticket_id = returned_ticket.returned_ticket_id join public.timetable t2 on t2.timetable_id = t.ticket_flight_id join public.flights f on f.flight_id = t2.flight_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "insert into returned_ticket(returned_ticket_id, returned_ticket_date) values (? , current_timestamp)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return CONSTRAINTS_TEXTS;
    }

    @Override
    public String getSQLForUpdate() {
        return null;
    }

    @Override
    public List<LinkedToken> getTokensForUpdate() {
        return null;
    }

    @Override
    public String getSQLForDelete() {
        return null;
    }
}
