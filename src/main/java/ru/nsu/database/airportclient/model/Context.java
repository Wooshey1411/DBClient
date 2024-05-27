package ru.nsu.database.airportclient.model;

import javafx.scene.image.Image;
import ru.nsu.database.airportclient.model.requests.*;
import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.describing.*;
import ru.nsu.database.airportclient.model.tables.utils.UserRights;
import ru.nsu.database.airportclient.model.tables.utils.UserRightsEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Context {
    public static final HashMap<String, MarkedTable> ALL_TABLES = new HashMap<>(Map.ofEntries(
            Map.entry(Employees.TABLE_NAME_ALIAS, new MarkedTable(new Employees(), new UserRights(UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), "-", UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), "-"))),
            Map.entry(Pilots.TABLE_NAME_ALIAS, new MarkedTable(new Pilots(), new UserRights(UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.HR.getRights() + UserRightsEnum.ADMIN.getRights()))),
            Map.entry(Cashiers.TABLE_NAME_ALIAS, new MarkedTable(new Cashiers(), new UserRights(UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights()))),
            Map.entry(Dispatchers.TABLE_NAME_ALIAS, new MarkedTable(new Dispatchers(), new UserRights(UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights()))),
            Map.entry(Technicans.TABLE_NAME_ALIAS, new MarkedTable(new Technicans(), new UserRights(UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights()))),
            Map.entry(ServiceWorkers.TABLE_NAME_ALIAS, new MarkedTable(new ServiceWorkers(), new UserRights(UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights()))),
            Map.entry(Administration.TABLE_NAME_ALIAS, new MarkedTable(new Administration(), new UserRights(UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), "-", UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights()))),
            Map.entry(InformationWorkers.TABLE_NAME_ALIAS, new MarkedTable(new InformationWorkers(), new UserRights(UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights()))),
            Map.entry(SecurityWorkers.TABLE_NAME_ALIAS, new MarkedTable(new SecurityWorkers(), new UserRights(UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights()))),
            Map.entry(PilotMedCheckLogs.TABLE_NAME_ALIAS, new MarkedTable(new PilotMedCheckLogs(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights()))),
            Map.entry(Departments.TABLE_NAME_ALIAS, new MarkedTable(new Departments(), new UserRights(UserRightsEnum.ADMIN.getRights(), "-", UserRightsEnum.ADMIN.getRights(), "-"))),
            Map.entry(Brigades.TABLE_NAME_ALIAS, new MarkedTable(new Brigades(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), "-", "-"))),
            Map.entry(BrigadesDistribution.TABLE_NAME_ALIAS, new MarkedTable(new BrigadesDistribution(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), "-", UserRightsEnum.ADMIN.getRights()))),
            Map.entry(AirplaneModels.TABLE_NAME_ALIAS, new MarkedTable(new AirplaneModels(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), "-"))),
            Map.entry(Airplanes.TABLE_NAME_ALIAS, new MarkedTable(new Airplanes(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), "-"))),
            Map.entry(AirplaneCheckLogs.TABLE_NAME_ALIAS, new MarkedTable(new AirplaneCheckLogs(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), "-"))),
            Map.entry(Destinations.TABLE_NAME_ALIAS, new MarkedTable(new Destinations(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), "-"))),
            Map.entry(Agencies.TABLE_NAME_ALIAS, new MarkedTable(new Agencies(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), "-"))),
            Map.entry(Flights.TABLE_NAME_ALIAS, new MarkedTable(new Flights(), new UserRights(UserRightsEnum.ADMIN.getRights(), "-", "-", "-"))),
            Map.entry(Timetable.TABLE_NAME_ALIAS, new MarkedTable(new Timetable(), new UserRights(UserRightsEnum.ADMIN.getRights(), "-", "-", "-"))),
            Map.entry(AirplanePreparationLogs.TABLE_NAME_ALIAS, new MarkedTable(new AirplanePreparationLogs(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights()))),
            Map.entry(CharterFlights.TABLE_NAME_ALIAS, new MarkedTable(new CharterFlights(), new UserRights(UserRightsEnum.ADMIN.getRights(), "-", "-", "-"))),
            Map.entry(Passengers.TABLE_NAME_ALIAS, new MarkedTable(new Passengers(), new UserRights(UserRightsEnum.ADMIN.getRights(), "-", UserRightsEnum.ADMIN.getRights(), "-"))),
            Map.entry(DelayedFlights.TABLE_NAME_ALIAS, new MarkedTable(new DelayedFlights(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), "-", "-"))),
            Map.entry(CanceledFlights.TABLE_NAME_ALIAS, new MarkedTable(new CanceledFlights(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), "-", "-"))),
            Map.entry(ReturnedTickets.TABLE_NAME_ALIAS, new MarkedTable(new ReturnedTickets(), new UserRights(UserRightsEnum.ADMIN.getRights(), UserRightsEnum.ADMIN.getRights(), "-", "-")))

    ));

    public static final HashMap<String, MarkedRequest> ALL_REQUESTS = new HashMap<>(Map.ofEntries(
            Map.entry(GetEmployees.ALIAS, new MarkedRequest(new GetEmployees(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights())),
            Map.entry(GetEmpBySumSalaryInBrigade.ALIAS, new MarkedRequest(new GetEmpBySumSalaryInBrigade(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights())),
            Map.entry(GetEmpByAvgSalaryInBrigade.ALIAS, new MarkedRequest(new GetEmpByAvgSalaryInBrigade(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.HR.getRights())),
            Map.entry(GetPilotsMedcheckLogs.ALIAS, new MarkedRequest(new GetPilotsMedcheckLogs(), UserRightsEnum.ADMIN.getRights())),
            Map.entry(GetAirplanesInAirportByTime.ALIAS, new MarkedRequest(new GetAirplanesInAirportByTime(), UserRightsEnum.ADMIN.getRights())),
            Map.entry(GetAirplanesByFlights.ALIAS, new MarkedRequest(new GetAirplanesByFlights(), UserRightsEnum.ADMIN.getRights())),
            Map.entry(GetCheckingAirplanesByTime.ALIAS, new MarkedRequest(new GetCheckingAirplanesByTime(), UserRightsEnum.ADMIN.getRights())),
            Map.entry(GetBrokenAirplanesOnDay.ALIAS, new MarkedRequest(new GetBrokenAirplanesOnDay(), UserRightsEnum.ADMIN.getRights())),
            Map.entry(GetAirplanesThatFlightBeforeRepair.ALIAS, new MarkedRequest(new GetAirplanesThatFlightBeforeRepair(), UserRightsEnum.ADMIN.getRights())),
            Map.entry(GetFlights.ALIAS, new MarkedRequest(new GetFlights(), UserRightsEnum.ADMIN.getRights())),
            Map.entry(GetCancelledFlights.ALIAS, new MarkedRequest(new GetCancelledFlights(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.CASHIER.getRights())),
            Map.entry(GetDelayedFlights.ALIAS, new MarkedRequest(new GetDelayedFlights(), UserRightsEnum.ADMIN.getRights())),
            Map.entry(GetFlightOfAirplane.ALIAS, new MarkedRequest(new GetFlightOfAirplane(), UserRightsEnum.ADMIN.getRights())),
            Map.entry(GetAvgOfSoldTickets.ALIAS, new MarkedRequest(new GetAvgOfSoldTickets(), UserRightsEnum.ADMIN.getRights())),
            Map.entry(GetFlights2.ALIAS, new MarkedRequest(new GetFlights2(), UserRightsEnum.ADMIN.getRights())),
            Map.entry(GetPassengersOnFlightByDay.ALIAS, new MarkedRequest(new GetPassengersOnFlightByDay(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.CASHIER.getRights())),
            Map.entry(GetPassengersOnFlightByFlight.ALIAS, new MarkedRequest(new GetPassengersOnFlightByFlight(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.CASHIER.getRights())),
            Map.entry(GetFreePlacesOnFlight.ALIAS, new MarkedRequest(new GetFreePlacesOnFlight(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.CASHIER.getRights())),
            Map.entry(GetBookedPlacesOnFlight.ALIAS, new MarkedRequest(new GetBookedPlacesOnFlight(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.CASHIER.getRights())),
            Map.entry(GetReturnedTickets.ALIAS, new MarkedRequest(new GetReturnedTickets(), UserRightsEnum.ADMIN.getRights() + UserRightsEnum.CASHIER.getRights()))

    ));

    public final static String USERS_WHO_CAN_ADD_FLIGHTS = UserRightsEnum.ADMIN.getRights();
    public final static String USERS_WHO_CAN_SELL_TICKETS = UserRightsEnum.ADMIN.getRights() + UserRightsEnum.CASHIER.getRights();

    private final Image editLogo;

    private final Image deleteLogo;
    private String currUserRights;

    private ITable currentTable;

    private IRequest currentRequest;

    public Context() {
        editLogo = new Image(Objects.requireNonNull(Context.class.getResourceAsStream("edit-logo.png")));
        deleteLogo = new Image(Objects.requireNonNull(Context.class.getResourceAsStream("delete-logo.png")));

    }

    public String getCurrUserRights() {
        return currUserRights;
    }

    public void setCurrUserRights(String currUserRights) {
        this.currUserRights = currUserRights;
    }

    public ITable getCurrentTable() {
        return currentTable;
    }

    public void setCurrentTable(ITable currentTable) {
        this.currentTable = currentTable;
    }

    public Image getEditLogo() {
        return editLogo;
    }

    public Image getDeleteLogo() {
        return deleteLogo;
    }

    public IRequest getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(IRequest currentRequest) {
        this.currentRequest = currentRequest;
    }
}
