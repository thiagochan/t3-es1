package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.Bairro;
import unioeste.geral.endereco.dao.BairroDAO;
import unioeste.geral.endereco.exception.EnderecoException;

import java.sql.Connection;
import java.sql.SQLException;

/** Regras de negócio do Bairro. */
public class ColBairro {

    private final BairroDAO bairroDAO = new BairroDAO();

    /** O bairro precisa vir com o id ou com o nome. Não acessa o banco. */
    public void validarDados(Bairro bairro) throws EnderecoException {
        if (bairro == null) {
            throw new EnderecoException("Bairro não informado");
        }
        if (bairro.getIdBairro() > 0) {
            return;
        }
        if (Validacao.vazio(bairro.getNome())) {
            throw new EnderecoException("Nome do bairro não informado");
        }
        Validacao.tamanhoMaximo(bairro.getNome(), 100, "Nome do bairro");
    }

    /**
     * Usado no cadastro de endereço.
     * Com id: o bairro precisa existir. Sem id: reaproveita o bairro de mesmo nome ou cadastra um novo.
     */
    public Bairro obterOuInserir(Bairro bairro, Connection con) throws EnderecoException, SQLException {
        if (bairro.getIdBairro() > 0) {
            Bairro existente = bairroDAO.obterPorId(bairro.getIdBairro(), con);
            if (existente == null) {
                throw new EnderecoException("Bairro não encontrado: id " + bairro.getIdBairro());
            }
            return existente;
        }
        String nome = bairro.getNome().trim();
        Bairro existente = bairroDAO.obterPorNome(nome, con);
        if (existente != null) {
            return existente;
        }
        Bairro novo = new Bairro();
        novo.setNome(nome);
        bairroDAO.inserir(novo, con);
        return novo;
    }

    /** Usado no endereço externo: se o bairro já existe na base local, preenche o id. */
    public void completarComBase(Bairro bairro, Connection con) throws SQLException {
        if (bairro == null || Validacao.vazio(bairro.getNome())) {
            return;
        }
        Bairro existente = bairroDAO.obterPorNome(bairro.getNome().trim(), con);
        if (existente != null) {
            bairro.setIdBairro(existente.getIdBairro());
        }
    }
}
