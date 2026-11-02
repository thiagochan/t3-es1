package unioeste.ir.bo;

import java.io.Serializable;
import unioeste.geral.pessoa.bo.PessoaJuridica;

public class Empresa extends PessoaJuridica implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idEmpresa;

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
}
