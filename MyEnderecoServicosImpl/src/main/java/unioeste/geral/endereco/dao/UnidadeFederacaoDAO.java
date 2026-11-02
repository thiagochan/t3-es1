package unioeste.geral.endereco.dao;

import unioeste.geral.endereco.bo.UnidadeFederacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnidadeFederacaoDAO {

    /** Busca a UF pela sigla. Retorna null se não existir. */
    public UnidadeFederacao obterPorSigla(String sigla, Connection con) throws SQLException {
        String sql = "SELECT siglaUF, nomeUF FROM UnidadeFederativa WHERE siglaUF = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sigla);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                UnidadeFederacao uf = new UnidadeFederacao();
                uf.setSigla(rs.getString("siglaUF"));
                uf.setNome(rs.getString("nomeUF"));
                return uf;
            }
        }
    }
}
