package unioeste.ir.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Declaracao implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idDeclaracao;

    private int ano;
    private Contribuinte contribuinte;
    private List<Bem> bens = new ArrayList<>();
    private List<Rendimento> rendimentos = new ArrayList<>();

    public int getIdDeclaracao() {
        return idDeclaracao;
    }

    public void setIdDeclaracao(int idDeclaracao) {
        this.idDeclaracao = idDeclaracao;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Contribuinte getContribuinte() {
        return contribuinte;
    }

    public void setContribuinte(Contribuinte contribuinte) {
        this.contribuinte = contribuinte;
    }

    public List<Bem> getBens() {
        return bens;
    }

    public void setBens(List<Bem> bens) {
        this.bens = bens;
    }

    public List<Rendimento> getRendimentos() {
        return rendimentos;
    }

    public void setRendimentos(List<Rendimento> rendimentos) {
        this.rendimentos = rendimentos;
    }
}
