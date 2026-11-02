package unioeste.geral.pessoa.bo;

import java.io.Serializable;

/**
 * Sexo de uma pessoa física (ex.: sexo "F", descrição "Feminino").
 */
public class Sexo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idSexo;
    private String sexo;
    private String descricao;

    public int getIdSexo() {
        return idSexo;
    }

    public void setIdSexo(int idSexo) {
        this.idSexo = idSexo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
