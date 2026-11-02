package unioeste.geral.pessoa.bo;

import java.io.Serializable;
import java.time.LocalDate;
import unioeste.geral.endereco.bo.UnidadeFederacao;

/**
 * Documento de identidade (RG) de uma pessoa física.
 */
public class RGIdentidadePessoal implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nroRG;
    private LocalDate dataEmissao;
    private UnidadeFederacao ufEmissao;

    public String getNroRG() {
        return nroRG;
    }

    public void setNroRG(String nroRG) {
        this.nroRG = nroRG;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public UnidadeFederacao getUfEmissao() {
        return ufEmissao;
    }

    public void setUfEmissao(UnidadeFederacao ufEmissao) {
        this.ufEmissao = ufEmissao;
    }
}
