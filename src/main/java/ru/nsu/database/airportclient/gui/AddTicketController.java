package ru.nsu.database.airportclient.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.WindowEvent;
import org.postgresql.util.PSQLException;
import ru.nsu.database.airportclient.gui.infonodes.CheckBoxInfo;
import ru.nsu.database.airportclient.gui.infonodes.ComboBoxInfo;
import ru.nsu.database.airportclient.gui.infonodes.InfoNode;
import ru.nsu.database.airportclient.gui.infonodes.TextFieldInfo;
import ru.nsu.database.airportclient.model.Context;
import ru.nsu.database.airportclient.model.connection.DBConnection;
import ru.nsu.database.airportclient.model.tables.utils.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;

public class AddTicketController {

    private Context context;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField passportTextField;

    private List<InfoNode> nodes;

    private TextField priceField;

    private HashMap<String, HashMap<String, Token>> linkedTokens;

    private HashMap<String, String> CONSTRAINTS = new HashMap<>(Map.ofEntries(
            Map.entry("place not free", "Место уже занято"),
            Map.entry("passengers_passenger_passport_key", "Человек с таким паспортом уже есть"),
            Map.entry("passengers_passenger_international_passport_key", "Человек с таким заграничным паспортом уже есть"),
            Map.entry("passengers_passenger_birthday_check", "Дата рождения не должна быть меньше 1920 года или не может быть больше чем сегодня")
    ));

    private ComboBox<String> placesCB = new ComboBox<>();

    private List<LinkedToken> tokensForAdd = Arrays.asList(
            new LinkedToken("passenger_firstname", "Имя", false, TokenType.VARCHAR, 30, null, null),
            new LinkedToken("passenger_lastname", "Фамилия", false, TokenType.VARCHAR, 30, null, null),
            new LinkedToken("passenger_fathername", "Отчество", true, TokenType.VARCHAR, 30, null, null),
            new LinkedToken("passenger_passport", "Паспорт", false, TokenType.VARCHAR, 20, null, null),
            new LinkedToken("passenger_international_passport", "Загран. паспорт", true, TokenType.VARCHAR, 20, null, null),
            new LinkedToken("passenger_birthday", "Дата рождения", false, TokenType.DATE, 0, null, null),
            new LinkedToken("passenger_gender","Пол", false, TokenType.VARCHAR, 1, "select gender_name as passenger_gender from genders",
                    List.of(new Token("passenger_gender", true, TokenType.VARCHAR, null, true, true, "Пол"))),
            new LinkedToken("timetable_id", "Рейс", false, TokenType.INTEGER, 0 , "select timetable_id, flight_code, leave_time, arrival_time  from timetable join public.flights f on f.flight_id = timetable.flight_id\n" +
                    "where leave_time > CURRENT_TIMESTAMP and timetable_id not in (select * from canceled_flights)",
                    List.of(
                            new Token("timetable_id", true, TokenType.INTEGER, null, false, true, "1"),
                            new Token("flight_code", true, TokenType.VARCHAR, null, true, true, "1"),
                            new Token("leave_time", true, TokenType.TIMESTAMP, null, true, true, "1"),
                            new Token("arrival_time", true, TokenType.TIMESTAMP, null, true, true, "1")
                    )),
            new LinkedToken("passenger_place", "Место", false, TokenType.INTEGER, 0, null, null),
            new LinkedToken("ticket_price", "Цена", false, TokenType.INTEGER, 0, null, null)
    );
    public void setContext(Context context) {
        this.context = context;
    }

    public void init(){
        nodes = new ArrayList<>();
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #f08132");
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        linkedTokens = new HashMap<>();
        int pos = 0;

        for (LinkedToken linkedToken : tokensForAdd){
            Label name = new Label(linkedToken.alias() + ":");
            name.setFont(new Font("Go", 16));
            name.setTextFill(new Color(1, 1, 1, 1));
            name.setMinHeight(50);
            GridPane.setHalignment(name, HPos.RIGHT);
            gridPane.add(name, 0, pos);
            pos++;

            if(linkedToken.name().equals("passenger_place")){
                placesCB.setStyle("-fx-background-insets: 0");
                GridPane.setMargin(placesCB, new Insets(0, 20, 0, 50));
                gridPane.add(placesCB, 1, pos-1);
                nodes.add(new ComboBoxInfo(placesCB));
                continue;
            }

            if(linkedToken.name().equals("ticket_price")){
                priceField = new TextField();
                priceField.setPrefWidth(150);
                GridPane.setMargin(priceField, new Insets(0, 20, 0, 50));
                gridPane.add(priceField, 1, pos-1);
                nodes.add(new TextFieldInfo(priceField));
                continue;
            }

            if(linkedToken.request() != null){

                try{
                    List<List<Token>> reqTokens = TableParser.parse(linkedToken.request(), linkedToken.requestTokens());
                    HashMap<String, Token> tokenHashMap = new HashMap<>();

                    Token primaryToken = null;
                    int primaryTokenPos = 0;
                    for (Token token : linkedToken.requestTokens()){
                        if (token.isPK()){
                            primaryToken = token;
                            break;
                        }
                        primaryTokenPos++;
                    }

                    if(primaryToken == null){
                        throw new RuntimeException("NO PRIMARY TOKEN");
                    }


                    ComboBox<String> comboBox = new ComboBox<>();
                    ObservableList<String> values = FXCollections.observableArrayList();
                    int currRow = 0;
                    for (List<Token> row : reqTokens){
                        StringBuilder res = new StringBuilder();
                        for (Token token : row){
                            if(token.isShowing()) {
                                res.append(token.data()).append(" | ");
                            }
                        }
                        res.delete(res.length()-3, res.length());
                        tokenHashMap.put(res.toString(), new Token(primaryToken.name(), true,
                                primaryToken.tokenType(), reqTokens.get(currRow).get(primaryTokenPos).data(), false, true, primaryToken.alias()));

                        values.add(res.toString());
                        currRow++;
                    }

                    linkedTokens.put(linkedToken.alias(), tokenHashMap);

                    comboBox.setItems(values);
                    comboBox.setStyle("-fx-background-insets: 0");
                    GridPane.setMargin(comboBox, new Insets(0, 20, 0, 50));
                    gridPane.add(comboBox, 1, pos-1);
                    nodes.add(new ComboBoxInfo(comboBox));

                } catch (SQLException ex){
                    System.out.println(ex.getLocalizedMessage());
                    DBConnection.INSTANCE.close();
                    System.exit(0);
                }
            } else{
                switch (linkedToken.type()){
                    case VARCHAR, INTEGER -> {
                        TextField textField = new TextField();
                        textField.setPrefWidth(150);
                        GridPane.setMargin(textField, new Insets(0, 20, 0, 50));
                        gridPane.add(textField, 1, pos-1);
                        nodes.add(new TextFieldInfo(textField));
                    }
                    case BOOLEAN -> {
                        CheckBox checkBox = new CheckBox();
                        checkBox.setText("");
                        checkBox.setStyle("-fx-border-color: #f08132");
                        GridPane.setMargin(checkBox, new Insets(0, 20, 0, 50));
                        gridPane.add(checkBox, 1, pos-1);
                        nodes.add(new CheckBoxInfo(checkBox));
                    }
                    case DATE -> {
                        MaskedTextField textField = new MaskedTextField();
                        long millis=System.currentTimeMillis();

                        String date = new java.sql.Date(millis).toString();

                        textField.setMask("####-##-##");
                        textField.setText(date.replace('/', '-'));
                        textField.setMaxWidth(100);
                        GridPane.setMargin(textField, new Insets(0, 20, 0, 50));
                        gridPane.add(textField, 1, pos-1);
                        nodes.add(new TextFieldInfo(textField));
                    }
                    case TIMESTAMP -> {
                        MaskedTextField textField = new MaskedTextField();

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();

                        textField.setMask("####-##-## ##:##:##");
                        textField.setText(dtf.format(now));
                        textField.setMaxWidth(180);
                        GridPane.setMargin(textField, new Insets(0, 20, 0, 50));
                        gridPane.add(textField, 1, pos-1);
                        nodes.add(new TextFieldInfo(textField));
                    }
                }
            }

        }

        scrollPane.setContent(gridPane);

        scrollPane.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, windowEvent -> {
            if (linkedTokens != null){
                linkedTokens.clear();
            }
        });

    }

    @FXML
    private void onAddBtnClick(){
        int pos = 0;
        for (LinkedToken linkedToken : tokensForAdd){
            if(!checkFields(nodes.get(pos).getInfo(), linkedToken)){
                return;
            }

            pos++;
        }

        try(PreparedStatement statement = DBConnection.INSTANCE.getConnection().prepareStatement("select \"sell_ticket\"(? , ? , ? , ? , ? , ? , ? , ? , ? , ?)")) {

            pos = 0;

            for (LinkedToken linkedToken : tokensForAdd){

                String data = nodes.get(pos).getInfo();

                if(linkedToken.name().equals("passenger_place")){
                    StatementCreator.fillStatementField(data, TokenType.INTEGER, statement, pos+1);
                    pos++;
                    continue;
                }

                if(data == null || data.isEmpty()){
                    StatementCreator.fillNullStatementField(linkedToken.type(), statement, pos+1);
                    pos++;
                    continue;
                }

                if(linkedToken.request() != null){
                    data = linkedTokens.get(linkedToken.alias()).get(data).data();
                }

                StatementCreator.fillStatementField(data, linkedToken.type(), statement, pos+1);

                pos++;
            }

            statement.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Успешно добавлено", ButtonType.OK);
            alert.showAndWait();

        } catch (PSQLException ex){

            try{

                try{
                    String msg = Objects.requireNonNull(ex.getServerErrorMessage()).getMessage();
                    String alert = CONSTRAINTS.get(msg);
                    if(alert != null){
                        showWarningAlert(alert);
                        return;
                    }
                } catch (NullPointerException ignored){}

                String constraint = Objects.requireNonNull(ex.getServerErrorMessage()).getConstraint();
                showWarningAlert(CONSTRAINTS.get(constraint));
            } catch (NullPointerException e){
                showWarningAlert("Произошла неизвестная ошибка");
                System.out.println(ex.getLocalizedMessage());
            }

        } catch (SQLException ex){
            showWarningAlert("Нет соединения с базой данных");
        }

    }

    private static void showWarningAlert(String text){
        if(text == null){
            throw new NullPointerException();
        }
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
        alert.showAndWait();
    }

    private boolean checkFields(String text, LinkedToken linkedToken){
        if (!linkedToken.isCanBeNone() && (text == null || text.isEmpty())){
            showWarningAlert("Поле '" + linkedToken.alias() + "' не может быть пустым!");
            return false;
        }

        if(linkedToken.request() != null){
            return true;
        }

        if(linkedToken.type() == TokenType.VARCHAR && text.length() > linkedToken.maxLength()){
            showWarningAlert("Поле '" + linkedToken.alias() + "' слишком длинное!");
            return false;
        }

        if(linkedToken.type() == TokenType.INTEGER){
            try {
                Integer.parseInt(text);
            } catch (NumberFormatException ex){
                showWarningAlert("Поле '" + linkedToken.alias() + "' должно быть целым числом!");
                return false;
            }
        }

        if(linkedToken.type() == TokenType.DATE){
            if (text.length() != 10){
                showWarningAlert("Поле '" + linkedToken.alias() + "' является неккоректной датой!");
                return false;
            }
            try {
                LocalDate.parse(text, DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT));
            } catch (DateTimeParseException ignored){
                showWarningAlert("В поле '" + linkedToken.alias() + "' введена несуществующая дата!");
                return false;
            }
        }

        if(linkedToken.type() == TokenType.TIMESTAMP){
            if (text.length() != 19){
                showWarningAlert("Поле '" + linkedToken.alias() + "' является неккоректной датой с временем!");
                return false;
            }

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                sdf.parse(text.substring(0,10));
            } catch (ParseException e) {
                showWarningAlert("В поле '" + linkedToken.alias() + "' введена несуществующая дата!");
                return false;
            }

            DateTimeFormatter strictTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                    .withResolverStyle(ResolverStyle.STRICT);
            try {
                LocalTime.parse(text.substring(11), strictTimeFormatter);
            } catch (DateTimeParseException ex){
                showWarningAlert("В поле '" + linkedToken.alias() + "' введено некорректное время!");
                return false;
            }
        }

        return true;

    }






    @FXML
    private void onCheckPassportBtnClick(){
        if (passportTextField.getText() == null || passportTextField.getText().isEmpty()){
            return;
        }

        try(PreparedStatement statement = DBConnection.INSTANCE.getConnection().prepareStatement("select * from passengers where passenger_passport = ?")) {

            statement.setString(1, passportTextField.getText());

            ResultSet rs = statement.executeQuery();
            if(!rs.next()){
                showWarningAlert("Ничего не найдено");
                return;
            }
            for (int pos = 2; pos <= 8; pos++){
                nodes.get(pos-2).setInfo(rs.getString(pos));
            }

        } catch (SQLException ex){
            System.out.println(ex.getLocalizedMessage());
        }

    }

    @FXML
    private void onCheckPlacesBtnClick(){
        if (nodes.get(7).getInfo() == null || nodes.get(7).getInfo().isEmpty()){
            return;
        }

        String data = linkedTokens.get("Рейс").get(nodes.get(7).getInfo()).data();

        try(PreparedStatement statement = DBConnection.INSTANCE.getConnection().prepareStatement("select ticket_place from tickets where ticket_flight_id = ? and ticket_passanger_id is null ")) {
            StatementCreator.fillStatementField(data, TokenType.INTEGER, statement, 1);
            ResultSet rs = statement.executeQuery();

            if(!rs.isBeforeFirst()){
                showWarningAlert("Нет мест");
                return;
            }

            ObservableList<String> values = FXCollections.observableArrayList();
            while (rs.next()){
                values.add(rs.getInt(1) + "");
            }

            if (values.isEmpty()){
                showWarningAlert("Нет мест");
                return;
            }
            placesCB.setItems(values);
            placesCB.setValue(values.get(0));

        } catch (SQLException ex){
            System.out.println(ex.getLocalizedMessage());
        }

        try(PreparedStatement statement = DBConnection.INSTANCE.getConnection().prepareStatement("select flight_basic_price from flights join public.timetable t on flights.flight_id = t.flight_id where timetable_id = ?")) {
            StatementCreator.fillStatementField(data, TokenType.INTEGER, statement, 1);
            ResultSet rs = statement.executeQuery();

            if(!rs.isBeforeFirst()){
                return;
            }

            rs.next();
            priceField.setText(rs.getInt(1) + "");

        } catch (SQLException ex){
            System.out.println(ex.getLocalizedMessage());
        }


    }



}
