package unioeste.ir.bo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Bem ou direito declarado.
 */
public class Bem implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idBem;
    private String nomeBem;
    /** Valor do bem em 31/12 do ano da declaração (valorAtual no MER). */
    private BigDecimal valorBem;
    /** Valor do bem em 31/12 do ano anterior. */
    private BigDecimal valorAnoAnterior;
    private TipoBem tipoBem;

    public int getIdBem() {
        return idBem;
    }

    public void setIdBem(int idBem) {
        this.idBem = idBem;
    }

    public String getNomeBem() {
        return nomeBem;
    }

    public void setNomeBem(String nomeBem) {
        this.nomeBem = nomeBem;
    }

    public BigDecimal getValorBem() {
        return valorBem;
    }

    public void setValorBem(BigDecimal valorBem) {
        this.valorBem = valorBem;
    }

    public BigDecimal getValorAnoAnterior() {
        return valorAnoAnterior;
    }

    public void setValorAnoAnterior(BigDecimal valorAnoAnterior) {
        this.valorAnoAnterior = valorAnoAnterior;
    }

    public TipoBem getTipoBem() {
        return tipoBem;
    }

    public void setTipoBem(TipoBem tipoBem) {
        this.tipoBem = tipoBem;
    }
}
