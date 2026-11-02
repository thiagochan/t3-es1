package unioeste.geral.pessoa.bo;

import java.io.Serializable;

/**
 * E-mail de uma pessoa.
 */
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idEmail;
    private String email;

    public int getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(int idEmail) {
        this.idEmail = idEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
