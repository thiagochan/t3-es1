package unioeste.geral.endereco.bo;

import java.io.Serializable;

/**
 * Endereço genérico identificado pelo CEP. Número e complemento ficam em {@link EnderecoEspecifico}.
 */
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idEndereco;
    /** CEP só com os 8 dígitos (String para não perder o zero à esquerda). */
    private String cep;
    private Cidade cidade;
    private Bairro bairro;
    private Logradouro logradouro;

    public int getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public Logradouro getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(Logradouro logradouro) {
        this.logradouro = logradouro;
    }
}
