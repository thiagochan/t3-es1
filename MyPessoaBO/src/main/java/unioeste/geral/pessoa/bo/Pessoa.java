package unioeste.geral.pessoa.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import unioeste.geral.endereco.bo.EnderecoEspecifico;

public abstract class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String nomeSocial;
    private List<Email> emails = new ArrayList<>();
    private List<Fone> fones = new ArrayList<>();
    private EnderecoEspecifico enderecoResidencial;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public List<Fone> getFones() {
        return fones;
    }

    public void setFones(List<Fone> fones) {
        this.fones = fones;
    }

    public EnderecoEspecifico getEnderecoResidencial() {
        return enderecoResidencial;
    }

    public void setEnderecoResidencial(EnderecoEspecifico enderecoResidencial) {
        this.enderecoResidencial = enderecoResidencial;
    }
}
