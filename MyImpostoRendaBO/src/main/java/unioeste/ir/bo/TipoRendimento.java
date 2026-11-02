package unioeste.ir.bo;

import java.io.Serializable;

public class TipoRendimento implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idTipoRendimento;
    private String nomeTipoRendimento;

    public int getIdTipoRendimento() {
        return idTipoRendimento;
    }

    public void setIdTipoRendimento(int idTipoRendimento) {
        this.idTipoRendimento = idTipoRendimento;
    }

    public String getNomeTipoRendimento() {
        return nomeTipoRendimento;
    }

    public void setNomeTipoRendimento(String nomeTipoRendimento) {
        this.nomeTipoRendimento = nomeTipoRendimento;
    }
}
