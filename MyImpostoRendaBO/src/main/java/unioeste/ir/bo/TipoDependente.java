package unioeste.ir.bo;

import java.io.Serializable;

/**
 * Tipo de dependência (cônjuge, filho(a), enteado(a)...).
 */
public class TipoDependente implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idTipoDependente;
    private String nome;

    public int getIdTipoDependente() {
        return idTipoDependente;
    }

    public void setIdTipoDependente(int idTipoDependente) {
        this.idTipoDependente = idTipoDependente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
