package unioeste.geral.endereco.dao;

import unioeste.geral.endereco.bo.Bairro;
import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.endereco.bo.Logradouro;
import unioeste.geral.endereco.bo.TipoLogradouro;
import unioeste.geral.endereco.bo.UnidadeFederacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {

    private static final String SELECT_ENDERECO =
            "SELECT e.idEndereco, e.cep, "
            + "c.idCidade, c.nome AS nomeCidade, uf.siglaUF, uf.nomeUF, "
            + "b.idBairro, b.nome AS nomeBairro, "
            + "l.idLogradouro, l.nome AS nomeLogradouro, t.siglaLogradouro, t.nomeLogradouro AS nomeTipoLogradouro "
            + "FROM Endereco e "
            + "JOIN Cidade c ON c.idCidade = e.idCidade "
            + "JOIN UnidadeFederativa uf ON uf.siglaUF = c.siglaUF "
            + "JOIN Bairro b ON b.idBairro = e.idBairro "
            + "JOIN Logradouro l ON l.idLogradouro = e.idLogradouro "
            + "JOIN TipoLogradouro t ON t.siglaLogradouro = l.siglaLogradouro ";

    /** Busca o endereço pelo id. Retorna null se não existir. */
    public Endereco obterPorId(int idEndereco, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SELECT_ENDERECO + "WHERE e.idEndereco = ?")) {
            ps.setInt(1, idEndereco);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    /** Lista os endereços de um CEP (8 dígitos). Lista vazia se não houver nenhum. */
    public List<Endereco> obterPorCep(String cep, Connection con) throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SELECT_ENDERECO + "WHERE e.cep = ? ORDER BY l.nome, e.idEndereco")) {
            ps.setString(1, cep);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enderecos.add(montar(rs));
                }
            }
        }
        return enderecos;
    }

    /** Diz se já existe um endereço com o mesmo CEP, cidade, bairro e logradouro. */
    public boolean existe(Endereco endereco, Connection con) throws SQLException {
        String sql = "SELECT 1 FROM Endereco WHERE cep = ? AND idCidade = ? AND idBairro = ? AND idLogradouro = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, endereco.getCep());
            ps.setInt(2, endereco.getCidade().getIdCidade());
            ps.setInt(3, endereco.getBairro().getIdBairro());
            ps.setInt(4, endereco.getLogradouro().getIdLogradouro());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Insere o endereço e preenche o id gerado pelo banco no próprio objeto.
     * Cidade, bairro e logradouro já precisam ter id.
     */
    public void inserir(Endereco endereco, Connection con) throws SQLException {
        String sql = "INSERT INTO Endereco (cep, idCidade, idBairro, idLogradouro) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, endereco.getCep());
            ps.setInt(2, endereco.getCidade().getIdCidade());
            ps.setInt(3, endereco.getBairro().getIdBairro());
            ps.setInt(4, endereco.getLogradouro().getIdLogradouro());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    endereco.setIdEndereco(chaves.getInt(1));
                }
            }
        }
    }

    /** Converte a linha atual do ResultSet em um Endereco completo. */
    private Endereco montar(ResultSet rs) throws SQLException {
        UnidadeFederacao uf = new UnidadeFederacao();
        uf.setSigla(rs.getString("siglaUF"));
        uf.setNome(rs.getString("nomeUF"));

        Cidade cidade = new Cidade();
        cidade.setIdCidade(rs.getInt("idCidade"));
        cidade.setNome(rs.getString("nomeCidade"));
        cidade.setEstado(uf);

        Bairro bairro = new Bairro();
        bairro.setIdBairro(rs.getInt("idBairro"));
        bairro.setNome(rs.getString("nomeBairro"));

        TipoLogradouro tipo = new TipoLogradouro();
        tipo.setSigla(rs.getString("siglaLogradouro"));
        tipo.setNome(rs.getString("nomeTipoLogradouro"));

        Logradouro logradouro = new Logradouro();
        logradouro.setIdLogradouro(rs.getInt("idLogradouro"));
        logradouro.setNome(rs.getString("nomeLogradouro"));
        logradouro.setTipoLogradouro(tipo);

        Endereco endereco = new Endereco();
        endereco.setIdEndereco(rs.getInt("idEndereco"));
        endereco.setCep(rs.getString("cep"));
        endereco.setCidade(cidade);
        endereco.setBairro(bairro);
        endereco.setLogradouro(logradouro);
        return endereco;
    }
}
