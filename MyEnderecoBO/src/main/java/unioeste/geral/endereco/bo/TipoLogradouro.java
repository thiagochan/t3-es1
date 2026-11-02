package unioeste.geral.endereco.bo;

import java.io.Serializable;

/**
 * Tipo de logradouro (Rua, Avenida...). Conforme o MER, a sigla (R, AV...) é a chave do tipo.
 */
public class TipoLogradouro implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Sigla do tipo (ex.: AV). Chave da tabela TipoLogradouro (siglaLogradouro). */
    private String sigla;
    /** Nome do tipo (ex.: Avenida). */
    private String nome;

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
