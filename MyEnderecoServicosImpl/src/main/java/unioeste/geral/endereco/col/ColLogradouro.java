package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.Logradouro;
import unioeste.geral.endereco.bo.TipoLogradouro;
import unioeste.geral.endereco.dao.LogradouroDAO;
import unioeste.geral.endereco.dao.TipoLogradouroDAO;
import unioeste.geral.endereco.exception.EnderecoException;

import java.sql.Connection;
import java.sql.SQLException;

/** Regras de negócio do Logradouro e do seu tipo. */
public class ColLogradouro {

    private final LogradouroDAO logradouroDAO = new LogradouroDAO();
    private final TipoLogradouroDAO tipoLogradouroDAO = new TipoLogradouroDAO();

    /** O logradouro precisa vir com o id ou com o nome e a sigla do tipo. Não acessa o banco. */
    public void validarDados(Logradouro logradouro) throws EnderecoException {
        if (logradouro == null) {
            throw new EnderecoException("Logradouro não informado");
        }
        if (logradouro.getIdLogradouro() > 0) {
            return;
        }
        if (Validacao.vazio(logradouro.getNome())) {
            throw new EnderecoException("Nome do logradouro não informado");
        }
        Validacao.tamanhoMaximo(logradouro.getNome(), 150, "Nome do logradouro");
        TipoLogradouro tipo = logradouro.getTipoLogradouro();
        if (tipo == null || Validacao.vazio(tipo.getSigla())) {
            throw new EnderecoException("Tipo de logradouro não informado (ex.: R, AV)");
        }
    }

    /**
     * Usado no cadastro de endereço.
     * Com id: o logradouro precisa existir. Sem id: o tipo precisa existir; reaproveita
     * o logradouro de mesmo nome e tipo ou cadastra um novo.
     */
    public Logradouro obterOuInserir(Logradouro logradouro, Connection con) throws EnderecoException, SQLException {
        if (logradouro.getIdLogradouro() > 0) {
            Logradouro existente = logradouroDAO.obterPorId(logradouro.getIdLogradouro(), con);
            if (existente == null) {
                throw new EnderecoException("Logradouro não encontrado: id " + logradouro.getIdLogradouro());
            }
            return existente;
        }
        String sigla = logradouro.getTipoLogradouro().getSigla().trim().toUpperCase();
        TipoLogradouro tipo = tipoLogradouroDAO.obterPorSigla(sigla, con);
        if (tipo == null) {
            throw new EnderecoException("Tipo de logradouro inválido: " + sigla);
        }
        String nome = logradouro.getNome().trim();
        Logradouro existente = logradouroDAO.obterPorNomeETipo(nome, sigla, con);
        if (existente != null) {
            return existente;
        }
        Logradouro novo = new Logradouro();
        novo.setNome(nome);
        novo.setTipoLogradouro(tipo);
        logradouroDAO.inserir(novo, con);
        return novo;
    }

    /**
     * Usado no endereço externo. O ViaCEP só informa o nome do tipo ("Avenida"):
     * aqui se procura a sigla na base local. Se o tipo não for conhecido, o nome do tipo
     * volta para o nome do logradouro e o usuário escolhe o tipo na tela.
     * Se o logradouro já existir na base, preenche o id.
     */
    public void completarComBase(Logradouro logradouro, Connection con) throws SQLException {
        if (logradouro == null) {
            return;
        }
        TipoLogradouro tipo = logradouro.getTipoLogradouro();
        if (tipo != null && Validacao.vazio(tipo.getSigla())) {
            TipoLogradouro conhecido = Validacao.vazio(tipo.getNome()) ? null : tipoLogradouroDAO.obterPorNome(tipo.getNome(), con);
            if (conhecido != null) {
                logradouro.setTipoLogradouro(conhecido);
            } else {
                String nomeTipo = Validacao.vazio(tipo.getNome()) ? "" : tipo.getNome() + " ";
                logradouro.setNome(nomeTipo + logradouro.getNome());
                logradouro.setTipoLogradouro(null);
            }
        }
        if (logradouro.getTipoLogradouro() != null && !Validacao.vazio(logradouro.getNome())) {
            Logradouro existente = logradouroDAO.obterPorNomeETipo(logradouro.getNome(), logradouro.getTipoLogradouro().getSigla(), con);
            if (existente != null) {
                logradouro.setIdLogradouro(existente.getIdLogradouro());
            }
        }
    }
}
