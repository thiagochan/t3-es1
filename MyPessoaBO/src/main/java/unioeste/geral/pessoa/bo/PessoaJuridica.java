package unioeste.geral.pessoa.bo;

import java.io.Serializable;

/**
 * Pessoa jurídica.
 */
public class PessoaJuridica extends Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    /** CNPJ só com os 14 dígitos. */
    private String cnpj;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
