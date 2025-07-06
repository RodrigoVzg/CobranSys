import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MenuController{

    @FXML
    private StackPane areaDeConteudo;

    @FXML
    private StackPane areaDeResultado;

    @FXML
    private AnchorPane barraSuperior;

    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    public void initialize() {
        barraSuperior.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        barraSuperior.setOnMouseDragged(e -> {
            Stage stage = (Stage) barraSuperior.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
    }
    

    @FXML
    void btnClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    @FXML
    void btnMinimizar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    

    @FXML
    protected void Multa(ActionEvent event) {
        carregarConteudo(CaminhosFXML.MULTA);
    }

    @FXML
    protected void Proporcional(ActionEvent event) {
        carregarConteudo(CaminhosFXML.PROPORCIONAL);
    }

    @FXML
    protected void Renegociar(ActionEvent event) {
        carregarConteudo(CaminhosFXML.RENEGOCIAR);
    }

    private void carregarConteudo(String caminhoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent conteudo = loader.load();
            Object controller = loader.getController();
    
            if (controller instanceof CalculoControllerBase base) {
                base.setAreaDeResultado(areaDeResultado);
            }
    
            areaDeConteudo.getChildren().setAll(conteudo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}