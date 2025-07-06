import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class RenegociarController extends CalculoControllerBase{

    @FXML
    private ToggleGroup atrasada;

    @FXML
    private RadioButton regular;
    
    @FXML
    private RadioButton irregular;

    @FXML
    private TextField dataParcela;

    @FXML
    private TextField percentualDaEntrada;

    @FXML
    private TextField valorFatura;

    @FXML
    private Spinner<Integer> diasEmAtraso;

    @FXML
    private Label labelDiasEmAtraso;

    double multa = CalculoControllerBase.getMultaPercentual();
    double juros = CalculoControllerBase.getJurosPercentual();

    public void initialize(){
        spinnerFormat(diasEmAtraso, 1, 99, 1);
        configurarCampoNumerico(valorFatura);
        configurarCampoPercentual(percentualDaEntrada);
        configurarCampoData(dataParcela);
        irregular.setOnAction(_ -> mostrarCampos(true, labelDiasEmAtraso, diasEmAtraso));
        regular.setOnAction(_ -> mostrarCampos(false, labelDiasEmAtraso, diasEmAtraso));
    }

    @FXML
    @Override
    public void calcular(ActionEvent event) {
        if (camposValidos()){
            double valorPlano = converterParaDouble(valorFatura.getText());
            double percentual = converterPercentual(percentualDaEntrada);
            int dias = obterDias(true, diasEmAtraso);
            String data = dataParcela.getText();
            double atraso = irregular.isSelected() ? calcularAtraso(valorPlano, dias) : 0.0;
            double entrada = calcularEntrada(valorPlano, percentual);
            double parcela = calcularParcela(valorPlano, entrada);
            if (regular.isSelected()){
                abrirResultado(CaminhosFXML.RESULTADO, "setDados", valorPlano, entrada, parcela, percentual, dias, data, true);
            } else {
                entrada += atraso;
                parcela += atraso;
                abrirResultado(CaminhosFXML.RESULTADO, "setDados", valorPlano, entrada, parcela, percentual, dias, data, false);
            }
            
        }

    }

    private double calcularEntrada(double valorPlano, double percentual){
        double entrada = valorPlano*percentual;
        return entrada;
    }

    private double calcularParcela(double valorPlano, double entrada){
        double parcela = valorPlano-entrada;
        return parcela;
    }

    private double calcularAtraso(double valorPlano, int dias){
        double valorMulta = valorPlano*multa;
        double valorJuros = valorPlano*juros*dias;
        double atraso = (valorMulta+valorJuros)/2;
        return atraso;
    }

    @Override
    protected boolean camposValidos(){
        boolean valido = true;

        limparEstilos(
            valorFatura,
            percentualDaEntrada,
            dataParcela,
            regular,
            irregular,
            diasEmAtraso
        );

        if (campoVazio(valorFatura)) valido = false;
        if (campoVazio(percentualDaEntrada)) valido = false;
        if (campoVazio(dataParcela)) valido = false;
        if (validarToggle(atrasada, regular, irregular)) valido = false;
        if (irregular.isSelected()){
            if (campoSpinnerVazio(diasEmAtraso)) valido = false;
        }

        return valido;
    }

}
