import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;

public abstract class CalculoControllerBase {

    public abstract void calcular(ActionEvent event);

    protected abstract boolean camposValidos();

    protected StackPane areaDeResultado;

    private static final double MULTA = 0.02;
    private static final double JUROS = 0.0003;
    private static final double DESCONTO = 10;

    public static double getMultaPercentual() {
        return MULTA;
    }
    
    public static double getJurosPercentual() {
        return JUROS;
    }

    public static double getDesconto() {
        return DESCONTO;
    }

    public void setAreaDeResultado(StackPane areaDeResultado) {
        this.areaDeResultado = areaDeResultado;
    }

    protected void configurarCampoNumerico(TextField campo) {
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String novoTexto = change.getControlNewText();
            return novoTexto.matches("[0-9,.]*") ? change : null;
        };
        campo.setTextFormatter(new TextFormatter<>(filtro));
        campo.setPromptText("000,00");
    }

    protected void configurarCampoPercentual(TextField campo) {
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String novoTexto = change.getControlNewText().replaceAll("%", "");
            if (!novoTexto.matches("\\d{0,3}")) return null;
            if (novoTexto.isEmpty()) {
                return change;
            }
            change.setText(novoTexto + "%");
            change.setRange(0, campo.getText().length());
            return change;
        };
        campo.setTextFormatter(new TextFormatter<>(filtro));
        campo.setPromptText("Ex: 25%");
    }
    
    protected void configurarCampoData(TextField campo) {
        campo.textProperty().addListener((_, _, newVal) -> {
            String numeros = newVal.replaceAll("[^\\d]", "");
    
            if (numeros.length() > 8) numeros = numeros.substring(0, 8);
    
            StringBuilder sb = new StringBuilder();
            if (numeros.length() >= 2) sb.append(numeros, 0, 2).append('/');
            else {
                sb.append(numeros);
                campo.setText(sb.toString());
                campo.positionCaret(sb.length());
                return;
            }
            if (numeros.length() >= 4) sb.append(numeros, 2, 4).append('/');
            else {
                sb.append(numeros.substring(2));
                campo.setText(sb.toString());
                campo.positionCaret(sb.length());
                return;
            }
            if (numeros.length() > 4) sb.append(numeros.substring(4));
            campo.setText(sb.toString());
            campo.positionCaret(sb.length());
        });
        campo.setPromptText("DD/MM/AAAA");
    }
    
    protected void mostrarCampos(boolean visivel, javafx.scene.Node... elementos) {
        for (javafx.scene.Node n : elementos) {
            n.setVisible(visivel);
            n.setDisable(!visivel);
        }
    }

    protected void spinnerFormat(Spinner<Integer> spinner, int min, int max, int ini){
        SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, ini);
        spinner.setValueFactory(factory);
    }

    protected void limparEstilos(javafx.scene.Node... campos) {
        for (javafx.scene.Node campo : campos) {
            campo.getStyleClass().remove("alerta-visivel");
        }
    }
    
    protected int obterDias(boolean condicao, Spinner<Integer> spinner) {
        return condicao ? spinner.getValue() : 0;
    }

    protected double converterParaDouble(String texto) {
        try {
            return Double.parseDouble(texto.replace(",", "."));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    protected double converterPercentual(TextField campo) {
        String texto = campo.getText().replace("%", "").trim();
    
        if (texto.isEmpty()) return 0.0;
    
        try {
            return Double.parseDouble(texto) / 100.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    protected double getUserDataAsDouble(ToggleGroup grupo) {
        Toggle selecionado = grupo.getSelectedToggle();
        return Double.parseDouble(selecionado.getUserData().toString());
    }

    protected double calcularValorProporcional(int dias, double plano){
        double valorDiario = (plano-DESCONTO)/30;
        return dias * valorDiario;
    }

    protected double calcularValorProporcionalVencido(int dias, double plano){
        double multa = MULTA * plano;
        double juros = JUROS * plano;
        double valorDiario = (plano-DESCONTO)/30;
        int diasComJuros = Math.max(0, dias - 30);
        double valorProporcional = valorDiario * dias;
        double valorFinal = valorProporcional + multa + (juros * diasComJuros);
        return valorFinal;
    }

    protected boolean validarToggle(ToggleGroup grupo, javafx.scene.Node... avisosVisuais) {
        if (grupo.getSelectedToggle() == null) {
            for (javafx.scene.Node n : avisosVisuais) {
                n.getStyleClass().add("alerta-visivel");
            }
            return true;
        }
        return false;
    }
    
    protected boolean campoSpinnerVazio(Spinner<?> spinner) {
        String texto = spinner.getEditor().getText();
        if (texto == null || texto.trim().isEmpty()){
            spinner.getStyleClass().add("alerta-visivel");
            return true;
        } else {
            return false;
        }
    }
    
    protected boolean campoVazio(TextField campo) {
        String texto = campo.getText();
        if (texto == null || texto.trim().isEmpty()){
            campo.getStyleClass().add("alerta-visivel");
            return true;
        } else {
            return false;
        }
        
    }

    protected void abrirResultado(String caminhoFXML, String metodoComDados, Object... parametros) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent conteudo = loader.load();
            Object controller = loader.getController();

            if (metodoComDados != null && parametros.length > 0) {

                Class<?>[] tipos = new Class<?>[parametros.length];
                for (int i = 0; i < parametros.length; i++) {
                    Class<?> tipo = parametros[i].getClass();
                    if (tipo == Integer.class) tipos[i] = int.class;
                    else if (tipo == Double.class) tipos[i] = double.class;
                    else if (tipo == Boolean.class) tipos[i] = boolean.class;
                    else tipos[i] = tipo;
                }
                controller.getClass()
                          .getMethod(metodoComDados, tipos)
                          .invoke(controller, parametros);
            }

            areaDeResultado.getChildren().setAll(conteudo);
        } catch (Exception e) {
            System.out.println("Falha ao abrir resultado:");
            e.printStackTrace();
        }
    }
}