package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.endereco.dao.EnderecoDAO;
import unioeste.geral.endereco.exception.EnderecoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

/** Regras de negócio do Endereco. */
public class ColEndereco {

    /** 8 dígitos, com ou sem hífen: 85851000 ou 85851-000. */
    private static final Pattern FORMATO_CEP = Pattern.compile("\\d{5}-?\\d{3}");

    private final EnderecoDAO enderecoDAO = new EnderecoDAO();
    private final ColCidade colCidade = new ColCidade();
    private final ColBairro colBairro = new ColBairro();
    private final ColLogradouro colLogradouro = new ColLogradouro();

    /**
     * Valida o formato do CEP.
     *
     * @return o CEP só com os 8 dígitos, que é como ele fica gravado no banco
     */
    public String validarCep(String cep) throws EnderecoException {
        if (Validacao.vazio(cep)) {
            throw new EnderecoException("CEP não informado");
        }
        String cepLimpo = cep.trim();
        if (!FORMATO_CEP.matcher(cepLimpo).matches()) {
            throw new EnderecoException("CEP inválido: " + cepLimpo + ". Informe 8 dígitos, ex.: 85851-000");
        }
        String digitos = cepLimpo.replace("-", "");
        if (digitos.equals("00000000")) {
            throw new EnderecoException("CEP inválido: " + cepLimpo);
        }
        return digitos;
    }

    /**
     * Valida todos os dados do endereço antes de ir ao banco: CEP, cidade, bairro e logradouro.
     * Deixa o CEP do objeto só com os dígitos.
     */
    public void validarDados(Endereco endereco) throws EnderecoException {
        if (endereco == null) {
            throw new EnderecoException("Endereço não informado");
        }
        endereco.setCep(validarCep(endereco.getCep()));
        colCidade.validarDados(endereco.getCidade());
        colBairro.validarDados(endereco.getBairro());
        colLogradouro.validarDados(endereco.getLogradouro());
    }

    /** Impede cadastrar duas vezes o mesmo CEP com a mesma cidade, bairro e logradouro. */
    public void validarNaoCadastrado(Endereco endereco, Connection con) throws EnderecoException, SQLException {
        if (enderecoDAO.existe(endereco, con)) {
            throw new EnderecoException("Endereço já cadastrado para o CEP " + endereco.getCep());
        }
    }

    /** Validação do parâmetro do serviço obterEnderecoPorID: objeto Endereco com o id preenchido. */
    public void validarId(Endereco endereco) throws EnderecoException {
        if (endereco == null || endereco.getIdEndereco() <= 0) {
            throw new EnderecoException("Informe um id de endereço válido");
        }
    }

    /** Busca o endereço completo. Lança EnderecoException se não existir. */
    public Endereco obterPorId(int idEndereco, Connection con) throws EnderecoException, SQLException {
        Endereco endereco = enderecoDAO.obterPorId(idEndereco, con);
        if (endereco == null) {
            throw new EnderecoException("Endereço não encontrado: id " + idEndereco);
        }
        return endereco;
    }
}
