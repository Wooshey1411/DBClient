package ru.nsu.database.airportclient.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.database.airportclient.model.Context;

import java.io.IOException;

public class FirstWindowStarter extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(FirstWindowStarter.class.getResource("auth-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 330);
        stage.setResizable(false);
        stage.setTitle("Супер крутой аэропорт");
        stage.setScene(scene);
        AuthWindowController controller = fxmlLoader.getController();
        Context context = new Context();
        controller.setContext(context);
        stage.show();
    }

    public static void init(String[] args) {
        launch();
    }
}