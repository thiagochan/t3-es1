package unioeste.ir.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import unioeste.geral.pessoa.bo.PessoaFisica;

public class Contribuinte extends PessoaFisica implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idContribuinte;

    private List<Dependente> dependentes = new ArrayList<>();

    public int getIdContribuinte() {
        return idContribuinte;
    }

    public void setIdContribuinte(int idContribuinte) {
        this.idContribuinte = idContribuinte;
    }

    public List<Dependente> getDependentes() {
        return dependentes;
    }

    public void setDependentes(List<Dependente> dependentes) {
        this.dependentes = dependentes;
    }
}
