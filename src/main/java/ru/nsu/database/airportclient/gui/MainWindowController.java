package ru.nsu.database.airportclient.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.postgresql.util.PSQLException;
import ru.nsu.database.airportclient.model.Context;
import ru.nsu.database.airportclient.model.MarkedRequest;
import ru.nsu.database.airportclient.model.MarkedTable;
import ru.nsu.database.airportclient.model.connection.DBConnection;
import ru.nsu.database.airportclient.model.tables.utils.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainWindowController {

    private Context context;

    @FXML
    private Button addButton;

    public void setContext(Context context) {
        this.context = context;
    }

    @FXML
    private ComboBox<String> tablesComboBox;

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private Label rowsCountLabel;

    @FXML
    private ComboBox<String> requestsComboBox;

    @FXML
    private HBox ButtonsHBOX;

    public void init(){
        ObservableList<String> dataBases = FXCollections.observableArrayList();

        for (Map.Entry<String, MarkedTable> entry : Context.ALL_TABLES.entrySet()){
            if(entry.getValue().getRights().getRead().contains(context.getCurrUserRights())){
                dataBases.add(entry.getKey());
            }
        }

        ObservableList<String> requests = FXCollections.observableArrayList();

        for (Map.Entry<String, MarkedRequest> entry : Context.ALL_REQUESTS.entrySet()){
            if(entry.getValue().getRights().contains(context.getCurrUserRights())){
                requests.add(entry.getKey());
            }
        }
        if (!dataBases.isEmpty()) {
            tablesComboBox.setItems(dataBases);
            tablesComboBox.setValue(dataBases.get(0));
        }

        if (!requests.isEmpty()) {
            requestsComboBox.setItems(requests);
            requestsComboBox.setValue(requests.get(0));
        }

        if(Context.USERS_WHO_CAN_ADD_FLIGHTS.contains(context.getCurrUserRights())){
            ButtonsHBOX.getChildren().add(getButtonForAddFlight());
        }

        if(Context.USERS_WHO_CAN_SELL_TICKETS.contains(context.getCurrUserRights())){
            ButtonsHBOX.getChildren().add(getButtonForCreateTicket());
        }

    }


    @FXML
    private void onGetDataBtnClick(){
        try {
            MarkedTable markedTable = Context.ALL_TABLES.get(tablesComboBox.getValue());

            List<List<Token>> tokens = TableParser.parse(markedTable.getTable().getRequestForGetData(), markedTable.getTable().getDataTokens());
            context.setCurrentTable(Context.ALL_TABLES.get(tablesComboBox.getValue()).getTable());

            addButton.setDisable(!markedTable.getRights().getAdd().contains(context.getCurrUserRights()));

            if(tokens.isEmpty()){
                BorderPane borderPane = new BorderPane();
                Label label = new Label("Нет данных");
                label.setFont(new Font("Go", 24));
                borderPane.setCenter(label);
                mainScrollPane.setContent(borderPane);
                rowsCountLabel.setText("Всего записей: 0");
                return;
            }

            int countOfColumns = 0;
            GridPane gridPane = new GridPane();
            for (Token token : tokens.get(0)){
                if (token.isShowing()) {
                    ColumnConstraints column = new ColumnConstraints();
                    column.setPrefWidth(200);
                    gridPane.getColumnConstraints().add(column);
                    Label label = new Label(token.alias());
                    label.setStyle("-fx-font-weight: bold");
                    GridPane.setHalignment(label, HPos.CENTER);
                    gridPane.add(label, countOfColumns, 0);
                    countOfColumns++;
                }
            }


            if (markedTable.getRights().getEdit().contains(context.getCurrUserRights())) {
                ColumnConstraints updateColumn = new ColumnConstraints();
                updateColumn.setPrefWidth(32);
                gridPane.getColumnConstraints().add(updateColumn);
            }

            if (markedTable.getRights().getDelete().contains(context.getCurrUserRights())) {
                ColumnConstraints updateColumn = new ColumnConstraints();
                updateColumn.setPrefWidth(32);
                gridPane.getColumnConstraints().add(updateColumn);
            }


            int currPos = 1;

            for (List<Token> tokenList : tokens){
                int column = 0;
                for (Token token : tokenList){
                    if (token.isShowing()) {
                        String data;

                        if (token.tokenType() == TokenType.BOOLEAN){
                            if (token.data().equals("t")){
                                data = "Да";
                            } else {
                                data = "Нет";
                            }
                        } else {
                            data = token.data();
                        }

                        Label label = new Label(data);
                        GridPane.setHalignment(label, HPos.CENTER);
                        gridPane.add(label, column, currPos);
                        column++;
                    }
                }
                if (markedTable.getRights().getEdit().contains(context.getCurrUserRights())){
                    Button editButton = getEditButton(tokenList);
                    gridPane.add(editButton, column, currPos);
                    column++;
                }

                if (markedTable.getRights().getDelete().contains(context.getCurrUserRights())){
                    Button geleteButton = getDeleteButton(tokenList);
                    gridPane.add(geleteButton, column, currPos);
                }

                currPos++;
            }

            rowsCountLabel.setText("Всего записей: " + tokens.size());

            gridPane.setGridLinesVisible(true);
            mainScrollPane.setContent(gridPane);


        } catch (SQLException ex){
            System.out.println(ex.getLocalizedMessage());
        }

    }

    private Button getEditButton(List<Token> tokenList) {
        Button button = new Button();
        button.setPrefSize(16, 16);
        ImageView img = new ImageView(context.getEditLogo());
        img.setPreserveRatio(true);
        img.setFitWidth(button.getPrefWidth());
        img.setFitHeight(button.getPrefHeight());
        button.setGraphic(img);
        button.setOnMouseClicked(mouseEvent -> {
            FXMLLoader newWindowLoader = new FXMLLoader(Objects.requireNonNull(MainWindowController.class.getResource("update-form.fxml")));
            Parent root = null;
            try {
                root = newWindowLoader.load();
            } catch (IOException ex){
                ex.printStackTrace();
                DBConnection.INSTANCE.close();
                System.exit(0);
            }
            Stage newWindowStage = new Stage();
            newWindowStage.setMinHeight(480);
            newWindowStage.setMinWidth(620);
            newWindowStage.setTitle("Обновление данных");
            newWindowStage.setScene(new Scene(root,620,480));

            newWindowStage.initOwner(tablesComboBox.getScene().getWindow());
            newWindowStage.initModality(Modality.WINDOW_MODAL);

            UpdateFormController controller = newWindowLoader.getController();
            controller.setContext(context);
            controller.init(tokenList);
            newWindowStage.show();
        });
        return button;
    }

    private Button getDeleteButton(List<Token> tokenList){
        Button button = new Button();
        button.setPrefSize(16, 16);
        ImageView img = new ImageView(context.getDeleteLogo());
        img.setPreserveRatio(true);
        img.setFitWidth(button.getPrefWidth());
        img.setFitHeight(button.getPrefHeight());
        button.setGraphic(img);

        button.setOnMouseClicked(mouseEvent -> {

            try(PreparedStatement statement = DBConnection.INSTANCE.getConnection().prepareStatement(context.getCurrentTable().getSQLForDelete())) {
                int pos = 1;

                for (Token token : tokenList){
                    if(token.isPK()){
                        StatementCreator.fillStatementField(token.data(), token.tokenType(), statement, pos);
                        pos++;
                    }
                }

                statement.execute();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Успешно удалено", ButtonType.OK);
                alert.showAndWait();

                onGetDataBtnClick();

            } catch (PSQLException ex){

                try{
                    String constraint = Objects.requireNonNull(ex.getServerErrorMessage()).getConstraint();
                    showWarningAlert(context.getCurrentTable().getConstraintTexts().get(constraint));
                } catch (NullPointerException e){
                    showWarningAlert("Произошла неизвестная ошибка");
                    System.out.println(ex.getLocalizedMessage());
                }

            } catch (SQLException ex){
                showWarningAlert("Нет соединения с базой данных");
            }

        });


        return button;
    }

    @FXML
    private void onAddDataBtnClick(){

        FXMLLoader newWindowLoader = new FXMLLoader(Objects.requireNonNull(MainWindowController.class.getResource("add-form.fxml")));
        Parent root = null;
        try {
            root = newWindowLoader.load();
        } catch (IOException ex){
            ex.printStackTrace();
            DBConnection.INSTANCE.close();
            System.exit(0);
        }
        Stage newWindowStage = new Stage();
        newWindowStage.setMinHeight(480);
        newWindowStage.setMinWidth(620);
        newWindowStage.setTitle("Добавление данных");
        newWindowStage.setScene(new Scene(root,620,480));

        newWindowStage.initOwner(tablesComboBox.getScene().getWindow());
        newWindowStage.initModality(Modality.WINDOW_MODAL);

        AddFormController controller = newWindowLoader.getController();
        controller.setContext(context);
        controller.init();
        newWindowStage.show();
    }

    @FXML
    private void onExecuteRequestBtnClick(){

        context.setCurrentRequest(Context.ALL_REQUESTS.get(requestsComboBox.getValue()).getRequest());
        addButton.setDisable(true);

        if (context.getCurrentRequest().getRequestCond() == null){


            try(Statement statement = DBConnection.INSTANCE.getConnection().createStatement()) {

                ResultSet rs = statement.executeQuery(context.getCurrentRequest().getRequestBody());

                List<List<Token>> tokens = new ArrayList<>();
                while (rs.next()){
                    List<Token> row = new ArrayList<>();
                    for (Token token : context.getCurrentRequest().getDataTokens()){
                        String res = rs.getString(token.name());
                        row.add(new Token(token.name(), token.isPK(), token.tokenType(), res, token.isShowing(), token.isNotNone(), token.alias()));
                    }
                    tokens.add(row);
                }


                if(tokens.isEmpty()){
                    BorderPane borderPane = new BorderPane();
                    Label label = new Label("Нет данных");
                    label.setFont(new Font("Go", 24));
                    borderPane.setCenter(label);
                    mainScrollPane.setContent(borderPane);
                    rowsCountLabel.setText("Всего записей: 0");
                    return;
                }

                int countOfColumns = 0;
                GridPane gridPane = new GridPane();
                for (Token token : tokens.get(0)){
                    if (token.isShowing()) {
                        ColumnConstraints column = new ColumnConstraints();
                        column.setPrefWidth(200);
                        gridPane.getColumnConstraints().add(column);
                        Label label = new Label(token.alias());
                        label.setStyle("-fx-font-weight: bold");
                        GridPane.setHalignment(label, HPos.CENTER);
                        gridPane.add(label, countOfColumns, 0);
                        countOfColumns++;
                    }
                }

                int currPos = 1;

                for (List<Token> tokenList : tokens){
                    int column = 0;
                    for (Token token : tokenList){
                        if (token.isShowing()) {
                            String data;

                            if (token.tokenType() == TokenType.BOOLEAN){
                                if (token.data().equals("t")){
                                    data = "Да";
                                } else {
                                    data = "Нет";
                                }
                            } else {
                                data = token.data();
                            }

                            Label label = new Label(data);
                            GridPane.setHalignment(label, HPos.CENTER);
                            gridPane.add(label, column, currPos);
                            column++;
                        }
                    }

                    currPos++;
                }

                rowsCountLabel.setText("Всего записей: " + tokens.size());

                gridPane.setGridLinesVisible(true);
                mainScrollPane.setContent(gridPane);

            } catch (SQLException ex){
                showWarningAlert("Нет соединения с базой данных");
            }
            return;
        }


        FXMLLoader newWindowLoader = new FXMLLoader(Objects.requireNonNull(MainWindowController.class.getResource("request-form.fxml")));
        Parent root = null;
        try {
            root = newWindowLoader.load();
        } catch (IOException ex){
            ex.printStackTrace();
            DBConnection.INSTANCE.close();
            System.exit(0);
        }
        Stage newWindowStage = new Stage();
        newWindowStage.setMinHeight(480);
        newWindowStage.setMinWidth(620);
        newWindowStage.setTitle("Запрос");
        newWindowStage.setScene(new Scene(root,620,480));

        newWindowStage.initOwner(tablesComboBox.getScene().getWindow());
        newWindowStage.initModality(Modality.WINDOW_MODAL);

        RequestFormController controller = newWindowLoader.getController();
        controller.setContext(context);
        controller.setMainPane(mainScrollPane);
        controller.setRowsCountLabel(rowsCountLabel);
        controller.init();
        newWindowStage.show();


    }

    private static void showWarningAlert(String text){
        Alert alert = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
        alert.showAndWait();
    }

    private Button getButtonForAddFlight(){
        Button button = new Button("Добавить рейс");
        HBox.setMargin(button, new Insets(5, 0, 5, 5));
        button.setStyle("-fx-background-insets: 0");
        button.setPrefSize(130, 30);
        button.setOnMouseClicked(mouseEvent -> {
            FXMLLoader newWindowLoader = new FXMLLoader(Objects.requireNonNull(MainWindowController.class.getResource("add-flight-form.fxml")));
            Parent root = null;
            try {
                root = newWindowLoader.load();
            } catch (IOException ex){
                ex.printStackTrace();
                DBConnection.INSTANCE.close();
                System.exit(0);
            }
            Stage newWindowStage = new Stage();
            newWindowStage.setMinHeight(480);
            newWindowStage.setMinWidth(620);
            newWindowStage.setTitle("Добавление рейса");
            newWindowStage.setScene(new Scene(root,620,480));
            newWindowStage.setResizable(false);
            newWindowStage.initOwner(tablesComboBox.getScene().getWindow());
            newWindowStage.initModality(Modality.WINDOW_MODAL);

            AddFlightController controller = newWindowLoader.getController();
            controller.setContext(context);
            controller.init();
            newWindowStage.show();
        });
        return button;
    }

    private Button getButtonForCreateTicket(){
        Button button = new Button("Оформить билет");
        HBox.setMargin(button, new Insets(5, 0, 5, 5));
        button.setStyle("-fx-background-insets: 0");
        button.setPrefSize(130, 30);
        button.setOnMouseClicked(mouseEvent -> {
            FXMLLoader newWindowLoader = new FXMLLoader(Objects.requireNonNull(MainWindowController.class.getResource("add-ticket-form.fxml")));
            Parent root = null;
            try {
                root = newWindowLoader.load();
            } catch (IOException ex){
                ex.printStackTrace();
                DBConnection.INSTANCE.close();
                System.exit(0);
            }
            Stage newWindowStage = new Stage();
            newWindowStage.setMinHeight(480);
            newWindowStage.setMinWidth(620);
            newWindowStage.setTitle("Оформление билета");
            newWindowStage.setScene(new Scene(root,800,600));
            newWindowStage.setResizable(false);
            newWindowStage.initOwner(tablesComboBox.getScene().getWindow());
            newWindowStage.initModality(Modality.WINDOW_MODAL);

            AddTicketController controller = newWindowLoader.getController();
            controller.setContext(context);
            controller.init();
            newWindowStage.show();
        });
        return button;
    }

}
