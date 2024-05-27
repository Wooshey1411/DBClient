package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharterFlights implements ITable {

    public final static String TABLE_NAME = "charter_flights";

    public final static String TABLE_NAME_ALIAS = "Чартерные рейсы";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("charter_flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("charter_flights_agency_id", false, TokenType.INTEGER, null, true, true, "Агенство"),
            new Token("charter_flights_flight_id", false,TokenType.INTEGER, null, true, true, "Рейс"),
            new Token("charter_flights_flight_price", false,TokenType.INTEGER, null, true, true, "Стоимость")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("charter_flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("charter_flights_agency_id", false, TokenType.INTEGER, null, false, true, "Агенство"),
            new Token("agency_name", false, TokenType.VARCHAR, null, true, true, "Название агенства"),
            new Token("charter_flights_flight_id", false,TokenType.INTEGER, null, false, true, "Рейс"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта"),
            new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Название самолёта"),
            new Token("charter_flights_flight_price", false,TokenType.INTEGER, null, true, true, "Стоимость")
    );


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
        return "select charter_flight_id, charter_flights_agency_id, agency_name, charter_flights_flight_id, flight_code, leave_time, arrival_time, airplane_name, charter_flights_flight_price from charter_flights join public.agencies a on a.agency_id = charter_flights.charter_flights_agency_id\n" +
                "join public.timetable t on t.timetable_id = charter_flights.charter_flights_flight_id\n" +
                "join public.airplanes a2 on a2.airplane_id = t.airplane_id\n" +
                "join public.flights f on t.flight_id = f.flight_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return null;
    }

    @Override
    public String getSQLForAddData() {
        return null;
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return null;
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
