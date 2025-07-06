import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class ProporcionalController extends CalculoControllerBase{

    @FXML
    private ToggleGroup atrasada;

    @FXML
    private Spinner<Integer> diasDeConsumo;

    @FXML
    private RadioButton irregular;

    @FXML
    private RadioButton regular;

    @FXML
    private TextField  valorPlano;

    public void initialize(){
        spinnerFormat(diasDeConsumo, 1, 99, 1);
        configurarCampoNumerico(valorPlano);
    }

    @FXML
    @Override
    public void calcular(ActionEvent event) {
        if(camposValidos()){
            int dias = obterDias(true, diasDeConsumo);
            double plano = converterParaDouble(valorPlano.getText());
            double valorFinal = regular.isSelected() ?
            calcularValorProporcional(dias, plano) 
            : calcularValorProporcionalVencido(dias, plano);
            abrirResultado(CaminhosFXML.RESULTADO, "setDados", 
            valorFinal, plano, dias, regular.isSelected()
            );
        }
    }

    @Override
    protected boolean camposValidos(){
        boolean valido = true;

        limparEstilos(
            diasDeConsumo,
            irregular,
            regular,
            valorPlano
        );

        if (validarToggle(atrasada, irregular, regular)) valido = false;
        if (campoSpinnerVazio(diasDeConsumo)) valido = false;
        if (campoVazio(valorPlano)) valido = false;

        return valido;
    }

}