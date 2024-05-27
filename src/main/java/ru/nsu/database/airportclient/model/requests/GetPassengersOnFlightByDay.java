package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetPassengersOnFlightByDay implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("passenger_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("passenger_firstname", false,TokenType.VARCHAR, null, true, true, "Имя"),
            new Token("passenger_lastname", false,TokenType.VARCHAR, null, true, true, "Фамилия"),
            new Token("passenger_fathername", false,TokenType.VARCHAR, null, true, true, "Отчество"),
            new Token("passenger_passport", false,TokenType.VARCHAR, null, true, true, "Паспорт"),
            new Token("passenger_international_passport", false,TokenType.VARCHAR, null, true, true, "Загран. паспорт"),
            new Token("passenger_birthday", false,TokenType.DATE, null, true, true, "Дата рождения"),
            new Token("passenger_gender", false,TokenType.VARCHAR, null, true, true, "Пол")
    );

    public final static List<String> CONDITIONS = Arrays.asList(
            "leave_time::date = ?",
            "passenger_gender = ?",
            "date_part('year', age(passenger_birthday)) >= ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = Arrays.asList(
            new LinkedToken("1", "День вылета", false, TokenType.DATE, 0, null, null),
            new LinkedToken("passenger_gender","Пол", true, TokenType.VARCHAR, 1, "select gender_name as passenger_gender from genders",
                    List.of(new Token("passenger_gender", true, TokenType.VARCHAR, null, true, true, "Пол"))),
            new LinkedToken("1", "Возраст", true, TokenType.INTEGER, 0, null, null)
    );

    public static String ALIAS = "Пассажиры вылетевшие в определенный день";

    @Override
    public String getRequestName() {
        return "GetFlights2";
    }

    @Override
    public String getRequestAlias() {
        return ALIAS;
    }

    @Override
    public List<Token> getDataTokens() {
        return TOKENS;
    }

    @Override
    public String getRequestBody() {
        return "select p.passenger_id, passenger_firstname, passenger_lastname, passenger_fathername,\n" +
                "        passenger_international_passport, passenger_passport, passenger_gender, passenger_birthday\n" +
                "from timetable\n" +
                "join public.tickets t on timetable.timetable_id = t.ticket_flight_id\n" +
                "join public.passengers p on p.passenger_id = t.ticket_passanger_id\n" +
                "join public.tickets_distribution td on t.ticket_id = td.ticket_id and p.passenger_id = td.passenger_id\n" +
                "left join public.checked_luggage cl on td.ticket_distribution_id = cl.checked_luggage_ticket";
    }

    @Override
    public List<String> getRequestCond() {
        return CONDITIONS;
    }

    @Override
    public List<LinkedToken> getTokensForRequest() {
        return REQUEST_TOKENS;
    }
}
