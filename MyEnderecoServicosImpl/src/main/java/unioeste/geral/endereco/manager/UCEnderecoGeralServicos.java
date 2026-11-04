package unioeste.geral.endereco.manager;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.endereco.bo.EnderecoEspecifico;
import unioeste.geral.endereco.col.ColBairro;
import unioeste.geral.endereco.col.ColCidade;
import unioeste.geral.endereco.col.ColEndereco;
import unioeste.geral.endereco.col.ColLogradouro;
import unioeste.geral.endereco.dao.EnderecoDAO;
import unioeste.geral.endereco.dao.EnderecoExternoDAO;
import unioeste.geral.endereco.exception.EnderecoException;

import java.sql.Connection;
import java.util.List;

public class UCEnderecoGeralServicos {

    private final ColEndereco colEndereco = new ColEndereco();
    private final ColCidade colCidade = new ColCidade();
    private final ColBairro colBairro = new ColBairro();
    private final ColLogradouro colLogradouro = new ColLogradouro();
    private final EnderecoDAO enderecoDAO = new EnderecoDAO();
    private final EnderecoExternoDAO enderecoExternoDAO;

    public UCEnderecoGeralServicos() {
        this(new EnderecoExternoDAO());
    }

    /** Permite trocar a fonte externa de CEP (usado nos testes para não depender da internet). */
    public UCEnderecoGeralServicos(EnderecoExternoDAO enderecoExternoDAO) {
        this.enderecoExternoDAO = enderecoExternoDAO;
    }

    /**
     * Inclui um novo endereço.
     *
     * Valida CEP e dados em branco; a cidade, o bairro e o logradouro podem vir pelo id
     *  ou pelo nome
     * @return o endereço gravado, com o id gerado e os dados completos
     */
    public Endereco cadastrarEndereco(Endereco endereco) throws EnderecoException, Exception {
        colEndereco.validarDados(endereco);
        try (Connection con = ConexaoBD.getConnection()) {
            con.setAutoCommit(false);
            try {
                endereco.setCidade(colCidade.obterOuInserir(endereco.getCidade(), con));
                endereco.setBairro(colBairro.obterOuInserir(endereco.getBairro(), con));
                endereco.setLogradouro(colLogradouro.obterOuInserir(endereco.getLogradouro(), con));
                colEndereco.validarNaoCadastrado(endereco, con);
                enderecoDAO.inserir(endereco, con);
                con.commit();
                return endereco;
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }

    /** Dado um CEP, retorna a lista de endereços cadastrados com ele (vazia se não houver). */
    public List<Endereco> obterEnderecoPorCEP(String cep) throws EnderecoException, Exception {
        String digitos = colEndereco.validarCep(cep);
        try (Connection con = ConexaoBD.getConnection()) {
            return enderecoDAO.obterPorCep(digitos, con);
        }
    }

    /** Dado um objeto Endereco com o id preenchido, retorna o endereço completo. */
    public Endereco obterEnderecoPorID(Endereco endereco) throws EnderecoException, Exception {
        colEndereco.validarId(endereco);
        try (Connection con = ConexaoBD.getConnection()) {
            return colEndereco.obterPorId(endereco.getIdEndereco(), con);
        }
    }

    /**
     * Dado um CEP, busca o endereço no webservice ViaCEP.
     *
     * O resultado é completado com a base local: se a cidade, o bairro ou o logradouro já
     * existirem, os ids são preenchidos. O EnderecoEspecifico volta sem número e complemento,
     * que o usuário informa na tela antes de cadastrar.
     */
    public EnderecoEspecifico obterEnderecoExterno(String cep) throws EnderecoException, Exception {
        String digitos = colEndereco.validarCep(cep);
        Endereco endereco = enderecoExternoDAO.obterPorCep(digitos);
        if (endereco == null) {
            throw new EnderecoException("CEP " + cep.trim() + " não encontrado no serviço externo");
        }
        try (Connection con = ConexaoBD.getConnection()) {
            colCidade.completarComBase(endereco.getCidade(), con);
            colBairro.completarComBase(endereco.getBairro(), con);
            colLogradouro.completarComBase(endereco.getLogradouro(), con);
        }
        EnderecoEspecifico enderecoEspecifico = new EnderecoEspecifico();
        enderecoEspecifico.setEndereco(endereco);
        return enderecoEspecifico;
    }

    /** Dado um objeto Cidade com o id preenchido, retorna a cidade com a sua UF. */
    public Cidade obterCidade(Cidade cidade) throws EnderecoException, Exception {
        colCidade.validarId(cidade);
        try (Connection con = ConexaoBD.getConnection()) {
            return colCidade.obterPorId(cidade.getIdCidade(), con);
        }
    }
}
