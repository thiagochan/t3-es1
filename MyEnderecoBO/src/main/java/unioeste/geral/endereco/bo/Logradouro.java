package unioeste.geral.endereco.bo;

import java.io.Serializable;

/**
 * Logradouro (nome da rua/avenida) e o seu tipo.
 */
public class Logradouro implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idLogradouro;
    /** Nome sem o tipo (ex.: Brasil, para Avenida Brasil). */
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
