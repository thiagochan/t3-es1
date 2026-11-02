package unioeste.geral.pessoa.bo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Pessoa física.
 */
public class PessoaFisica extends Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String primeiroNome;
    private String sobreNome;
    private String cpf;
    private LocalDate dataNascimento;
    private RGIdentidadePessoal rg;
    private Sexo sexo;

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public RGIdentidadePessoal getRg() {
        return rg;
    }

    public void setRg(RGIdentidadePessoal rg) {
        this.rg = rg;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }
}
