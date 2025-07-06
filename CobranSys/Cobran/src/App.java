import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(CaminhosFXML.MENU));
        Parent root = fxmlLoader.load();
        Scene tela = new Scene(root);
        primaryStage.setTitle("Calculadora");
        InputStream iconStream = getClass().getResourceAsStream(CaminhosFXML.FAVICON);
        if (iconStream == null) {
            throw new RuntimeException("Favicon não encontrado no caminho: " + CaminhosFXML.FAVICON);
        }
        primaryStage.getIcons().add(new Image(iconStream));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(tela);
        primaryStage.show();
    }
}