package unioeste.ir.bo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Rendimento tributável recebido de pessoa jurídica.
 */
public class Rendimento implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idRendimento;
    private BigDecimal valorTotalRecebido;
    /** Contribuição previdenciária oficial. */
    private BigDecimal inss;
    /** Imposto de renda retido na fonte. */
    private BigDecimal irrfPago;
    /** Valor do 13º salário. */
    private BigDecimal valorDecimoTerceiro;
    private TipoRendimento tipoRendimento;
    /** Empresa (fonte pagadora) que pagou o rendimento. */
    private Empresa empresa;

    public int getIdRendimento() {
        return idRendimento;
    }

    public void setIdRendimento(int idRendimento) {
        this.idRendimento = idRendimento;
    }

    public BigDecimal getValorTotalRecebido() {
        return valorTotalRecebido;
    }

    public void setValorTotalRecebido(BigDecimal valorTotalRecebido) {
        this.valorTotalRecebido = valorTotalRecebido;
    }

    public BigDecimal getInss() {
        return inss;
    }

    public void setInss(BigDecimal inss) {
        this.inss = inss;
    }

    public BigDecimal getIrrfPago() {
        return irrfPago;
    }

    public void setIrrfPago(BigDecimal irrfPago) {
        this.irrfPago = irrfPago;
    }

    public BigDecimal getValorDecimoTerceiro() {
        return valorDecimoTerceiro;
    }

    public void setValorDecimoTerceiro(BigDecimal valorDecimoTerceiro) {
        this.valorDecimoTerceiro = valorDecimoTerceiro;
    }

    public TipoRendimento getTipoRendimento() {
        return tipoRendimento;
    }

    public void setTipoRendimento(TipoRendimento tipoRendimento) {
        this.tipoRendimento = tipoRendimento;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
