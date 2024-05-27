package ru.nsu.database.airportclient.model.requests;

import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.List;

public class GetReturnedTickets implements IRequest {

    public final static List<Token> TOKENS = Arrays.asList(
            new Token("returned_ticket_date", true, TokenType.TIMESTAMP, null, true, true, "Время сдачи"),
            new Token("ticket_place", true, TokenType.INTEGER, null, true, true, "Номер места"),
            new Token("passenger_firstname", false,TokenType.VARCHAR, null, true, true, "Имя"),
            new Token("passenger_lastname", false,TokenType.VARCHAR, null, true, true, "Фамилия"),
            new Token("passenger_fathername", false,TokenType.VARCHAR, null, true, true, "Отчество"),
            new Token("passenger_passport", false,TokenType.VARCHAR, null, true, true, "Паспорт"),
            new Token("passenger_international_passport", false,TokenType.VARCHAR, null, true, true, "Загран. паспорт"),
            new Token("passenger_birthday", false,TokenType.DATE, null, true, true, "Дата рождения"),
            new Token("passenger_gender", false,TokenType.VARCHAR, null, true, true, "Пол"),
            new Token("flight_code", false, TokenType.VARCHAR, null, true, true, "Код рейса"),
            new Token("leave_time", false, TokenType.TIMESTAMP, null, true, true, "Время вылета"),
            new Token("arrival_time", false, TokenType.TIMESTAMP, null, true, true, "Время прилёта")
    );

    public final static List<String> CONDITIONS = Arrays.asList(
            "returned_ticket_date = ?",
            "flight_ending_destination_id = ?",
            "flight_basic_price >= ?",
            "date_part('year', age(passenger_birthday)) >= ?",
            "passenger_gender = ?"
    );

    public final static List<LinkedToken> REQUEST_TOKENS = Arrays.asList(
            new LinkedToken("1", "День возврата", true, TokenType.DATE, 0, null, null),
            new LinkedToken("destination_name","Пункт прилёта", true, TokenType.INTEGER, 1, "select * from destinations",
                    List.of(new Token("destination_id", true, TokenType.VARCHAR, null, false, true, "Пол"),
                            new Token("destination_name", false, TokenType.VARCHAR, null, true, true, "ПН"))),
            new LinkedToken("1", "Цена билета", true, TokenType.INTEGER, 0, null, null),
            new LinkedToken("1", "Возраст", true, TokenType.INTEGER, 0, null, null),
            new LinkedToken("passenger_gender","Пол", true, TokenType.VARCHAR, 1, "select gender_name as passenger_gender from genders",
                    List.of(new Token("passenger_gender", true, TokenType.VARCHAR, null, true, true, "Пол")))
    );

    public static String ALIAS = "Возвращенные билеты";

    @Override
    public String getRequestName() {
        return "GetReturnedTickets";
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
        return "select returned_ticket_date, ticket_place,\n" +
                "       passenger_firstname, passenger_lastname, passenger_fathername, passenger_international_passport, passenger_passport, passenger_birthday,\n" +
                "       passenger_gender, flight_code, leave_time, arrival_time\n" +
                "from timetable\n" +
                "join public.tickets t on timetable.timetable_id = t.ticket_flight_id\n" +
                "join public.returned_ticket rt on t.ticket_id = rt.returned_ticket_id\n" +
                "join public.flights f on f.flight_id = timetable.flight_id\n" +
                "join public.passengers p on p.passenger_id = rt.returned_ticket_passenger_id\n" +
                "join public.tickets_distribution td on t.ticket_id = td.ticket_id\n" +
                "and rt.returned_ticket_passenger_id = td.passenger_id";
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
