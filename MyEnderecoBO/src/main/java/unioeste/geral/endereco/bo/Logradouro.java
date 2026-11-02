package unioeste.geral.endereco.bo;

import java.io.Serializable;

public class Logradouro implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idLogradouro;

    private String nome;
    private TipoLogradouro tipoLogradouro;

    public int getIdLogradouro() {
        return idLogradouro;
    }

    public void setIdLogradouro(int idLogradouro) {
        this.idLogradouro = idLogradouro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoLogradouro getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }
}
