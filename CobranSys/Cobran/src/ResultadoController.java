import java.text.NumberFormat;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ResultadoController {

    @FXML
    private TextArea resultado;

    double multa = CalculoControllerBase.getMultaPercentual() * 100;
    double juros = CalculoControllerBase.getJurosPercentual() * 100;
    double desconto = CalculoControllerBase.getDesconto();
    String descontoFormatado = textoFormatado(desconto);

    public void setDados(double multaRescisoria, double valorProporcional, double plano, int dias, int meses, double multaIntegral) {

        String multaRescisoriaFormatada = textoFormatado(multaRescisoria);
        String multaIntegralFormatada = textoFormatado(multaIntegral);
        String proporcionalFormatado = textoFormatado(valorProporcional);
        String planoFormatado = textoFormatado(plano);
        
        int mesesRestantes = 12 - meses;

        if (multaRescisoria != 0 && valorProporcional != 0) {
            resultado.setText(
                String.format("""
                    O contrato firmado previa isenção da taxa de instalação no valor de %s,
                    a qual é cobrada em caso de rescisão antecipada. Essa taxa é proporcionalmente
                    diluída nos primeiros doze meses de vigência contratual.

                    Considerando que o cancelamento do contrato foi solicitado neste mês e que
                    o mesmo permaneceu ativo por %d meses, restam %d meses para o cumprimento
                    integral do prazo acordado. Dessa forma, aplica-se a cobrança proporcional
                    referente à taxa de rescisão:

                    Multa Rescisória: %s

                    Esse valor corresponde à parte proporcional da taxa de instalação isenta,
                    devida em função da rescisão antecipada do contrato.

                    Além disso, será cobrado o valor proporcional aos dias de utilização do
                    serviço no mês do cancelamento, referentes a %d dias de uso do plano no
                    valor mensal de %s, totalizando %s. Caso o pagamento seja efetuado até a
                    data de vencimento da fatura, será aplicado o desconto de pontualidade
                    previsto em seu contrato, no valor de: %s.

                    Em caso de atraso no pagamento, haverá a perda do desconto de pontualidade
                    (%s), além da incidência de multa de %.2f%% sobre o valor integral do plano
                    e juros de mora de %.2f%% ao dia de atraso.
                    """,
                    multaIntegralFormatada,
                    meses, mesesRestantes,
                    multaRescisoriaFormatada,
                    dias,
                    planoFormatado,
                    proporcionalFormatado,
                    descontoFormatado,
                    descontoFormatado,
                    multa,
                    juros            
                )
            );
        } else if (multaRescisoria != 0 && valorProporcional == 0) {
            resultado.setText(
                String.format("""
                    O contrato firmado previa isenção da taxa de instalação no valor de %s,
                    a qual é cobrada em caso de rescisão antecipada. Essa taxa é proporcionalmente
                    diluída nos primeiros doze meses de vigência contratual.

                    Considerando que o cancelamento do contrato foi solicitado neste mês e que
                    o mesmo permaneceu ativo por %d meses, restam %d meses para o cumprimento
                    integral do prazo acordado. Dessa forma, aplica-se a cobrança proporcional
                    referente à taxa de rescisão:

                    Multa Rescisória: %s

                    Esse valor corresponde à parte proporcional da taxa de instalação isenta,
                    devida em função da rescisão antecipada do contrato.

                    Em caso de atraso no pagamento, haverá a perda do desconto de pontualidade
                    (%s), além da incidência de multa de %.2f%% sobre o valor integral do plano
                    e juros de mora de %.2f%% ao dia de atraso.
                    """,
                    multaIntegralFormatada,
                    meses, mesesRestantes,
                    multaRescisoriaFormatada,
                    descontoFormatado,
                    multa,
                    juros
                )
            );
        }
    }

    public void setDados(double valorFinal, double plano, int dias, boolean regular) {

        String valorFinalFormatado = textoFormatado(valorFinal);
        String planoFormatado = textoFormatado(plano);
        String semDescontoFormatado = textoFormatado(valorFinal + desconto);
        

        if (regular) {
            resultado.setText(
                String.format("""
                    Informação sobre valor proporcional e encargos por atraso

                    O valor proporcional referente a %d dias de uso do plano no valor de %s,
                    considerando o desconto de pontualidade de %s, será de %s, desde que o
                    pagamento seja efetuado até a data de vencimento da fatura.

                    Em caso de atraso no pagamento, haverá a perda do desconto de pontualidade
                    (%s), além da incidência de multa de %.2f%% sobre o valor integral do plano
                    e juros de mora de %.2f%% ao dia de atraso.
                    """,
                    dias,
                    planoFormatado,
                    descontoFormatado,
                    valorFinalFormatado,
                    descontoFormatado,
                    multa,
                    juros
                )
            );
        } else {
            resultado.setText(
                String.format("""
                    Valor proporcional com encargos por atraso

                    O valor proporcional referente a %d dias de uso do plano no valor de %s,
                    acrescido da multa de %.2f%% pelo atraso e dos juros de mora de %.2f%% ao
                    dia, totaliza %s.

                    Ressaltamos que, por se tratar de uma fatura vencida, não se aplica o
                    desconto de pontualidade de %s, concedido apenas para pagamentos efetuados
                    dentro do prazo.
                    """,
                    dias,
                    planoFormatado,
                    multa,
                    juros,
                    semDescontoFormatado,
                    descontoFormatado
                )
            );
        }
    }

    public void setDados(double valorPlano, double entrada, double parcela, double percentual, int dias, String data, boolean regular) {
        String valorTotalFormatado = textoFormatado(valorPlano);
        String entradaFormatada = textoFormatado(entrada);
        String parcelaFormatada = textoFormatado(parcela);
        Double porcento = percentual * 100;
    
        if (regular) {
            resultado.setText(
                String.format("""
                    A renegociação do valor total de %s será realizada com entrada de %.0f%%, conforme as condições abaixo:
        
                    1ª parcela (entrada), com desconto de pontualidade: %s — com vencimento hoje;
        
                    2ª parcela, com desconto de pontualidade: %s — com vencimento em %s.
        
                    Observações importantes:
        
                    Em caso de atraso no pagamento da 2ª parcela, o desconto de pontualidade no valor de %s será perdido, e incidirá multa de %.2f%% sobre o valor da parcela, além de juros de %.2f%% ao dia até a regularização.
        
                    Após a formalização desta renegociação, uma nova renegociação só poderá ser realizada após 90 dias contados a partir da data de pagamento da última parcela.
                    """,
                    valorTotalFormatado,
                    porcento,
                    entradaFormatada,
                    parcelaFormatada,
                    data,
                    descontoFormatado,
                    multa,
                    juros
                )
            );
        } else {
            resultado.setText(
                String.format("""
                    A renegociação do valor de %s, acrescido dos encargos correspondentes a juros e multa pelo atraso de %d dias, será realizada com entrada de %.0f%%, conforme as condições a seguir:
        
                    1ª parcela (entrada): %s — com vencimento hoje;
        
                    2ª parcela: %s — com vencimento em %s, desde que paga até a data de vencimento.
        
                    Observações importantes:
        
                    Em caso de atraso no pagamento da 2ª parcela, será cancelado o desconto de pontualidade no valor de %s, e incidirão:
                    - Multa de %.2f%% sobre o valor da parcela;
                    - Juros de %.2f%% ao dia até a regularização do pagamento.
        
                    Após a formalização desta renegociação, uma nova renegociação somente poderá ser solicitada após 90 dias, contados a partir da data de pagamento da última parcela.
                    """,
                    valorTotalFormatado,
                    dias,
                    porcento,
                    entradaFormatada,
                    parcelaFormatada,
                    data,
                    descontoFormatado,
                    multa,
                    juros
                )
            );
        }
    }

    private String textoFormatado(double valor) {
        Locale moeda = Locale.of("pt", "BR");
        NumberFormat formato = NumberFormat.getCurrencyInstance(moeda);
        return formato.format(valor);
    }
}
