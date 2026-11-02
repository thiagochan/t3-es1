package unioeste.geral.endereco.bo;

import java.io.Serializable;

public class EnderecoEspecifico implements Serializable {

    private static final long serialVersionUID = 1L;

    private int nroEndereco;
    private String complemento;
    private Endereco endereco;

    public int getNroEndereco() {
        return nroEndereco;
    }

    public void setNroEndereco(int nroEndereco) {
        this.nroEndereco = nroEndereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
