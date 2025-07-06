import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;


public class MultaController extends CalculoControllerBase{

    @FXML
    private RadioButton antigoContrato;

    @FXML
    private ToggleGroup contrato;

    @FXML
    private Spinner<Integer> diasConsumo;

    @FXML
    private Label labelDias;

    @FXML
    private Label labelPlano;

    @FXML
    private Spinner<Integer> mesesAtivos;

    @FXML
    private RadioButton novoContrato;

    @FXML
    private RadioButton possuiProporcional;

    @FXML
    private ToggleGroup proporcional;

    @FXML
    private RadioButton semProporcional;

    @FXML
    private TextField valorPlano;

    public void initialize() {
        configurarCampoNumerico(valorPlano);
        spinnerFormat(mesesAtivos, 1, 11, 1);
        spinnerFormat(diasConsumo, 1, 29, 1);
        possuiProporcional.setOnAction(_ -> mostrarCampos(true, valorPlano, diasConsumo, labelDias, labelPlano));
        semProporcional.setOnAction(_ -> mostrarCampos(false, valorPlano, diasConsumo, labelDias, labelPlano));
    }

    @FXML
    @Override
    public void calcular(ActionEvent event) {
        if (camposValidos()) {

            int dias = obterDias(possuiProporcional.isSelected(), diasConsumo);
            int meses = obterDias(true, mesesAtivos);
            double multaIntegral = getUserDataAsDouble(contrato);
            double multaRescisoria = processarDados(multaIntegral, meses);
            double plano = converterParaDouble(valorPlano.getText());
            double valorProporcional = possuiProporcional.isSelected() ?
                calcularValorProporcional(dias, plano)
                : 0.0;

            abrirResultado(CaminhosFXML.RESULTADO, "setDados",
            multaRescisoria, valorProporcional, plano, dias, meses, multaIntegral
            );
        } 
    }

    @Override
    protected boolean camposValidos() {
        boolean valido = true;
        
        limparEstilos(
            antigoContrato,
            novoContrato,
            possuiProporcional,
            semProporcional,
            mesesAtivos.getEditor(),
            valorPlano,
            diasConsumo
        );
        
        if (validarToggle(contrato, antigoContrato, novoContrato)) valido = false;
        if (validarToggle(proporcional, possuiProporcional, semProporcional)) valido = false;
        if (campoSpinnerVazio(mesesAtivos)) valido = false;
        
        if (possuiProporcional.isSelected()) {
            if (campoVazio(valorPlano)) valido = false;
            if (campoSpinnerVazio(diasConsumo)) valido = false;
        }
        
        return valido;
    }

    private double processarDados(double multaIntegral, int meses){
        int aux = 12-meses;
        double multaMensal = multaIntegral/12;
        return multaMensal * aux;
    }

}