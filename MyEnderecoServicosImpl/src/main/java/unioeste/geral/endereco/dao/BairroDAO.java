package unioeste.geral.endereco.dao;

import unioeste.geral.endereco.bo.Bairro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** SQL da tabela Bairro. */
public class BairroDAO {

    /** Busca o bairro pelo id. Retorna null se não existir. */
    public Bairro obterPorId(int idBairro, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT idBairro, nome FROM Bairro WHERE idBairro = ?")) {
            ps.setInt(1, idBairro);
            return obterUm(ps);
        }
    }

    /** Busca o bairro pelo nome, sem diferenciar maiúsculas. Retorna null se não existir. */
    public Bairro obterPorNome(String nome, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT idBairro, nome FROM Bairro WHERE UPPER(nome) = UPPER(?)")) {
            ps.setString(1, nome);
            return obterUm(ps);
        }
    }

    /** Insere o bairro e preenche o id gerado pelo banco no próprio objeto. */
    public void inserir(Bairro bairro, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO Bairro (nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, bairro.getNome());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    bairro.setIdBairro(chaves.getInt(1));
                }
            }
        }
    }

    private Bairro obterUm(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            Bairro bairro = new Bairro();
            bairro.setIdBairro(rs.getInt("idBairro"));
            bairro.setNome(rs.getString("nome"));
            return bairro;
        }
    }
}
