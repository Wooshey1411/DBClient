package ru.nsu.database.airportclient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nsu.database.airportclient.model.Context;
import ru.nsu.database.airportclient.model.connection.DBAuthentication;
import ru.nsu.database.airportclient.model.connection.DBConnection;
import ru.nsu.database.airportclient.model.connection.SQLConnectionException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class AuthWindowController {

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onEnterBtnClick(){
        try {
            DBAuthentication.AuthAnswer response = DBAuthentication.authUser(loginField.getText(), passwordField.getText());

            if(response.isSuccess()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Успешное соединение с базой данных", ButtonType.OK);
                alert.showAndWait();
                Stage currStage = (Stage)passwordField.getScene().getWindow();
                currStage.close();

                context.setCurrUserRights(response.userRights());

                FXMLLoader newWindowLoader = new FXMLLoader(Objects.requireNonNull(MainWindowController.class.getResource("main-window.fxml")));
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
                newWindowStage.setMinWidth(640);
                newWindowStage.setTitle("Супер крутой аэропорт");
                newWindowStage.setScene(new Scene(root,640,480));
                MainWindowController controller = newWindowLoader.getController();
                controller.setContext(context);
                controller.init();
                newWindowStage.show();

            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Неправильный логин или пароль", ButtonType.CANCEL);
                passwordField.clear();
                alert.showAndWait();
            }

        } catch (SQLConnectionException | SQLException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Нет соединения с базой данных.\nПопробуйте позже", ButtonType.CLOSE);
            alert.showAndWait();
            System.exit(0);
        }
    }
}