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

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AddFormController {

    private Context context;

    @FXML
    private ScrollPane scrollPane;

    private List<InfoNode> nodes;

    private HashMap<String, HashMap<String, Token>> linkedTokens;

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

        for (LinkedToken linkedToken : context.getCurrentTable().getTokensForAdd()){
            Label name = new Label(linkedToken.alias() + ":");
            name.setFont(new Font("Go", 16));
            name.setTextFill(new Color(1, 1, 1, 1));
            name.setMinHeight(50);
            GridPane.setHalignment(name, HPos.RIGHT);
            gridPane.add(name, 0, pos);
            pos++;

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
        for (LinkedToken linkedToken : context.getCurrentTable().getTokensForAdd()){
            if(!checkFields(nodes.get(pos).getInfo(), linkedToken)){
                return;
            }

            pos++;
        }

        try(PreparedStatement statement = DBConnection.INSTANCE.getConnection().prepareStatement(context.getCurrentTable().getSQLForAddData())) {

            pos = 0;

            for (LinkedToken linkedToken : context.getCurrentTable().getTokensForAdd()){

                String data = nodes.get(pos).getInfo();

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
                    String alert = context.getCurrentTable().getConstraintTexts().get(msg);
                    if(alert != null){
                        showWarningAlert(alert);
                        return;
                    }
                } catch (NullPointerException ignored){}

                String constraint = Objects.requireNonNull(ex.getServerErrorMessage()).getConstraint();
                showWarningAlert(context.getCurrentTable().getConstraintTexts().get(constraint));
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


}
