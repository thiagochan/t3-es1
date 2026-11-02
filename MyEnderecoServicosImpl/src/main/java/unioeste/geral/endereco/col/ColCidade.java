package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.bo.UnidadeFederacao;
import unioeste.geral.endereco.dao.CidadeDAO;
import unioeste.geral.endereco.dao.UnidadeFederacaoDAO;
import unioeste.geral.endereco.exception.EnderecoException;

import java.sql.Connection;
import java.sql.SQLException;

public class ColCidade {

    private final CidadeDAO cidadeDAO = new CidadeDAO();
    private final UnidadeFederacaoDAO unidadeFederacaoDAO = new UnidadeFederacaoDAO();

    /** A cidade precisa vir com o id ou com o nome e a sigla da UF.  */
    public void validarDados(Cidade cidade) throws EnderecoException {
        if (cidade == null) {
            throw new EnderecoException("Cidade não informada");
        }
        if (cidade.getIdCidade() > 0) {
            return;
        }
        if (Validacao.vazio(cidade.getNome())) {
            throw new EnderecoException("Informe o id da cidade ou o seu nome e UF");
        }
        Validacao.tamanhoMaximo(cidade.getNome(), 100, "Nome da cidade");
        if (cidade.getEstado() == null || Validacao.vazio(cidade.getEstado().getSigla())) {
            throw new EnderecoException("UF da cidade não informada");
        }
    }

    /** objeto Cidade com o id preenchido. */
    public void validarId(Cidade cidade) throws EnderecoException {
        if (cidade == null || cidade.getIdCidade() <= 0) {
            throw new EnderecoException("Informe um id de cidade válido");
        }
    }

    /** Busca a cidade com a UF. Lança EnderecoException se não existir. */
    public Cidade obterPorId(int idCidade, Connection con) throws EnderecoException, SQLException {
        Cidade cidade = cidadeDAO.obterPorId(idCidade, con);
        if (cidade == null) {
            throw new EnderecoException("Cidade não encontrada: id " + idCidade);
        }
        return cidade;
    }

    /**
     * Com id: a cidade precisa existir. Sem id: reaproveita a cidade de mesmo nome e UF
     * ou cadastra uma nova (a UF precisa existir).
     */
    public Cidade obterOuInserir(Cidade cidade, Connection con) throws EnderecoException, SQLException {
        if (cidade.getIdCidade() > 0) {
            return obterPorId(cidade.getIdCidade(), con);
        }
        String nome = cidade.getNome().trim();
        String sigla = cidade.getEstado().getSigla().trim().toUpperCase();

        Cidade existente = cidadeDAO.obterPorNomeEUf(nome, sigla, con);
        if (existente != null) {
            return existente;
        }
        UnidadeFederacao uf = unidadeFederacaoDAO.obterPorSigla(sigla, con);
        if (uf == null) {
            throw new EnderecoException("UF inválida: " + sigla);
        }
        Cidade nova = new Cidade();
        nova.setNome(nome);
        nova.setEstado(uf);
        cidadeDAO.inserir(nova, con);
        return nova;
    }

    /**
     * Usado no endereço externo: se a cidade já existe na base local, preenche o id
     * (assim o endereço pode ser enviado direto para cadastrarEndereco). Completa o nome da UF.
     */
    public void completarComBase(Cidade cidade, Connection con) throws SQLException {
        if (cidade == null || Validacao.vazio(cidade.getNome())
                || cidade.getEstado() == null || Validacao.vazio(cidade.getEstado().getSigla())) {
            return;
        }
        Cidade existente = cidadeDAO.obterPorNomeEUf(cidade.getNome(), cidade.getEstado().getSigla(), con);
        if (existente != null) {
            cidade.setIdCidade(existente.getIdCidade());
            cidade.setEstado(existente.getEstado());
            return;
        }
        UnidadeFederacao uf = unidadeFederacaoDAO.obterPorSigla(cidade.getEstado().getSigla(), con);
        if (uf != null) {
            cidade.setEstado(uf);
        }
    }
}
