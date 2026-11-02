package unioeste.ir.bo;

import java.io.Serializable;
import unioeste.geral.pessoa.bo.PessoaFisica;

public class Dependente extends PessoaFisica implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idDependente;

    private TipoDependente tipoDependencia;

    public int getIdDependente() {
        return idDependente;
    }

    public void setIdDependente(int idDependente) {
        this.idDependente = idDependente;
    }

    public TipoDependente getTipoDependencia() {
        return tipoDependencia;
    }

    public void setTipoDependencia(TipoDependente tipoDependencia) {
        this.tipoDependencia = tipoDependencia;
    }
}
