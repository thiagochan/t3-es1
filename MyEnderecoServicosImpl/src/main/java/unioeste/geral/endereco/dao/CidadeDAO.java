package unioeste.geral.endereco.dao;

import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.bo.UnidadeFederacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** SQL da tabela Cidade. As consultas já trazem a UF junto (JOIN). */
public class CidadeDAO {

    private static final String SELECT_CIDADE =
            "SELECT c.idCidade, c.nome, uf.siglaUF, uf.nomeUF "
            + "FROM Cidade c "
            + "JOIN UnidadeFederativa uf ON uf.siglaUF = c.siglaUF ";

    /** Busca a cidade pelo id, com a UF. Retorna null se não existir. */
    public Cidade obterPorId(int idCidade, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SELECT_CIDADE + "WHERE c.idCidade = ?")) {
            ps.setInt(1, idCidade);
            return obterUma(ps);
        }
    }

    /** Busca a cidade pelo nome (sem diferenciar maiúsculas) e pela sigla da UF. Retorna null se não existir. */
    public Cidade obterPorNomeEUf(String nome, String siglaUF, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SELECT_CIDADE + "WHERE UPPER(c.nome) = UPPER(?) AND c.siglaUF = ?")) {
            ps.setString(1, nome);
            ps.setString(2, siglaUF);
            return obterUma(ps);
        }
    }

    /** Insere a cidade e preenche o id gerado pelo banco no próprio objeto. */
    public void inserir(Cidade cidade, Connection con) throws SQLException {
        String sql = "INSERT INTO Cidade (nome, siglaUF) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cidade.getNome());
            ps.setString(2, cidade.getEstado().getSigla());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    cidade.setIdCidade(chaves.getInt(1));
                }
            }
        }
    }

    private Cidade obterUma(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            UnidadeFederacao uf = new UnidadeFederacao();
            uf.setSigla(rs.getString("siglaUF"));
            uf.setNome(rs.getString("nomeUF"));

            Cidade cidade = new Cidade();
            cidade.setIdCidade(rs.getInt("idCidade"));
            cidade.setNome(rs.getString("nome"));
            cidade.setEstado(uf);
            return cidade;
        }
    }
}
