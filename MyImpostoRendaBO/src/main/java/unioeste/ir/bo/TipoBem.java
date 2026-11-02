package unioeste.ir.bo;

import java.io.Serializable;

/**
 * Tipo de bem (imóvel, veículo, aplicação financeira...).
 */
public class TipoBem implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idTipoBem;
    private String nomeTipoBem;

    public int getIdTipoBem() {
        return idTipoBem;
    }

    public void setIdTipoBem(int idTipoBem) {
        this.idTipoBem = idTipoBem;
    }

    public String getNomeTipoBem() {
        return nomeTipoBem;
    }

    public void setNomeTipoBem(String nomeTipoBem) {
        this.nomeTipoBem = nomeTipoBem;
    }
}
