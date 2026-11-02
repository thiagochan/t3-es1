package unioeste.geral.endereco.dao;

import unioeste.geral.endereco.bo.TipoLogradouro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** SQL da tabela TipoLogradouro. */
public class TipoLogradouroDAO {

    /** Busca o tipo pela sigla (ex.: AV). Retorna null se não existir. */
    public TipoLogradouro obterPorSigla(String sigla, Connection con) throws SQLException {
        String sql = "SELECT siglaLogradouro, nomeLogradouro FROM TipoLogradouro WHERE siglaLogradouro = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sigla);
            return obterUm(ps);
        }
    }

    /** Busca o tipo pelo nome (ex.: Avenida), sem diferenciar maiúsculas. Retorna null se não existir. */
    public TipoLogradouro obterPorNome(String nome, Connection con) throws SQLException {
        String sql = "SELECT siglaLogradouro, nomeLogradouro FROM TipoLogradouro WHERE UPPER(nomeLogradouro) = UPPER(?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            return obterUm(ps);
        }
    }

    private TipoLogradouro obterUm(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            TipoLogradouro tipo = new TipoLogradouro();
            tipo.setSigla(rs.getString("siglaLogradouro"));
            tipo.setNome(rs.getString("nomeLogradouro"));
            return tipo;
        }
    }
}
