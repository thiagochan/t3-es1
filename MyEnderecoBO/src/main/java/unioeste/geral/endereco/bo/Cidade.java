package unioeste.geral.endereco.bo;

import java.io.Serializable;

/**
 * Cidade de uma Unidade da Federação.
 */
public class Cidade implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idCidade;
    private String nome;
    /** UF da cidade (associação "estado" do diagrama de classes). */
    private UnidadeFederacao estado;

    public int getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(int idCidade) {
        this.idCidade = idCidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UnidadeFederacao getEstado() {
        return estado;
    }

    public void setEstado(UnidadeFederacao estado) {
        this.estado = estado;
    }
}
