package unioeste.geral.endereco.dao;

import unioeste.geral.endereco.bo.Logradouro;
import unioeste.geral.endereco.bo.TipoLogradouro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** SQL da tabela Logradouro. As consultas já trazem o tipo junto (JOIN). */
public class LogradouroDAO {

    private static final String SELECT_LOGRADOURO =
            "SELECT l.idLogradouro, l.nome, t.siglaLogradouro, t.nomeLogradouro "
            + "FROM Logradouro l "
            + "JOIN TipoLogradouro t ON t.siglaLogradouro = l.siglaLogradouro ";

    /** Busca o logradouro pelo id, com o tipo. Retorna null se não existir. */
    public Logradouro obterPorId(int idLogradouro, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SELECT_LOGRADOURO + "WHERE l.idLogradouro = ?")) {
            ps.setInt(1, idLogradouro);
            return obterUm(ps);
        }
    }

    /** Busca pelo nome (sem diferenciar maiúsculas) e pela sigla do tipo. Retorna null se não existir. */
    public Logradouro obterPorNomeETipo(String nome, String siglaTipo, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SELECT_LOGRADOURO + "WHERE UPPER(l.nome) = UPPER(?) AND l.siglaLogradouro = ?")) {
            ps.setString(1, nome);
            ps.setString(2, siglaTipo);
            return obterUm(ps);
        }
    }

    /** Insere o logradouro e preenche o id gerado pelo banco no próprio objeto. */
    public void inserir(Logradouro logradouro, Connection con) throws SQLException {
        String sql = "INSERT INTO Logradouro (nome, siglaLogradouro) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, logradouro.getNome());
            ps.setString(2, logradouro.getTipoLogradouro().getSigla());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    logradouro.setIdLogradouro(chaves.getInt(1));
                }
            }
        }
    }

    private Logradouro obterUm(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            TipoLogradouro tipo = new TipoLogradouro();
            tipo.setSigla(rs.getString("siglaLogradouro"));
            tipo.setNome(rs.getString("nomeLogradouro"));

            Logradouro logradouro = new Logradouro();
            logradouro.setIdLogradouro(rs.getInt("idLogradouro"));
            logradouro.setNome(rs.getString("nome"));
            logradouro.setTipoLogradouro(tipo);
            return logradouro;
        }
    }
}
