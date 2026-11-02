package unioeste.geral.pessoa.bo;

import java.io.Serializable;

/**
 * Telefone de uma pessoa.
 */
public class Fone implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idFone;
    private String nroFone;
    private int ddd;
    private int ddi;

    public int getIdFone() {
        return idFone;
    }

    public void setIdFone(int idFone) {
        this.idFone = idFone;
    }

    public String getNroFone() {
        return nroFone;
    }

    public void setNroFone(String nroFone) {
        this.nroFone = nroFone;
    }

    public int getDdd() {
        return ddd;
    }

    public void setDdd(int ddd) {
        this.ddd = ddd;
    }

    public int getDdi() {
        return ddi;
    }

    public void setDdi(int ddi) {
        this.ddi = ddi;
    }
}
