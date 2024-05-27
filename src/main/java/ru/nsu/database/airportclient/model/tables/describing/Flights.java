package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flights implements ITable {

    public final static String TABLE_NAME = "Flights";

    public final static String TABLE_NAME_ALIAS = "Рейсы";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код"),
            new Token("flight_category_id", false,TokenType.INTEGER, null, true, true, "Категория"),
            new Token("flight_basic_price", false,TokenType.INTEGER, null, true, true, "Базовая цена"),
            new Token("flight_starting_destination_id", false,TokenType.INTEGER, null, true, true, "Пункт вылета"),
            new Token("flight_ending_destination_id", false,TokenType.INTEGER, null, true, true, "Пункт прилета")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("flight_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код"),
            new Token("flight_category_id", false,TokenType.INTEGER, null, false, true, "Категория"),
            new Token("flight_category_name", false,TokenType.VARCHAR, null, true, true, "Категория"),
            new Token("flight_basic_price", false,TokenType.INTEGER, null, true, true, "Базовая цена"),
            new Token("flight_starting_destination_id", false,TokenType.INTEGER, null, false, true, "Пункт вылета"),
            new Token("flight_starting_destination_name", false,TokenType.INTEGER, null, true, true, "Пункт вылета"),
            new Token("flight_ending_destination_id", false,TokenType.INTEGER, null, false, true, "Пункт прилета"),
            new Token("flight_ending_destination_name", false,TokenType.INTEGER, null, true, true, "Пункт прилета")
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
        return "select flight_id, flight_code, flight_category_id, flight_category_name, flight_basic_price, flight_starting_destination_id, flight_starting_destination_name, flight_ending_destination_id, destination_name as flight_ending_destination_name\n" +
                "from (select flight_id, flight_code,  fc.flight_category_id, flight_category_name, flight_basic_price, flight_starting_destination_id, destination_name as flight_starting_destination_name, flight_ending_destination_id from flights\n" +
                "join flight_categories fc on fc.flight_category_id = flights.flight_category_id\n" +
                "join public.destinations d on d.destination_id = flights.flight_starting_destination_id) join destinations on flight_ending_destination_id = destinations.destination_id";
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
