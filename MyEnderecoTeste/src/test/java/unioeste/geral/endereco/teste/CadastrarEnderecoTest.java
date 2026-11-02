package unioeste.geral.endereco.teste;

import org.junit.jupiter.api.Test;
import unioeste.apoio.bd.ConexaoBD;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.endereco.dao.BairroDAO;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CadastrarEnderecoTest {

    private final UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();

    @Test
    void cadastraEnderecoERetornaComIdEDadosCompletos() throws Exception {
        Endereco gravado = servicos.cadastrarEndereco(Dados.endereco(Dados.cepUnico(), 1, "Jardim das Flores", "R", "das Palmeiras"));

        assertTrue(gravado.getIdEndereco() > 0);
        assertEquals("Foz do Iguaçu", gravado.getCidade().getNome());
        assertEquals("PR", gravado.getCidade().getEstado().getSigla());
        assertTrue(gravado.getBairro().getIdBairro() > 0);
        assertTrue(gravado.getLogradouro().getIdLogradouro() > 0);
        assertEquals("Rua", gravado.getLogradouro().getTipoLogradouro().getNome());
    }

    @Test
    void aceitaCepComHifenEGravaSoOsDigitos() throws Exception {
        String cep = Dados.cepUnico();
        String cepComHifen = cep.substring(0, 5) + "-" + cep.substring(5);

        Endereco gravado = servicos.cadastrarEndereco(Dados.endereco(cepComHifen, 2, "Centro", "AV", "Brasília"));

        assertEquals(cep, gravado.getCep());
    }

    @Test
    void reaproveitaBairroELogradouroQueJaExistem() throws Exception {
        Endereco exemplo = servicos.obterEnderecoPorCEP("85851000").get(0);

        Endereco gravado = servicos.cadastrarEndereco(Dados.endereco(Dados.cepUnico(), 1, "centro", "AV", "Brasil"));

        assertEquals(exemplo.getBairro().getIdBairro(), gravado.getBairro().getIdBairro());
        assertEquals(exemplo.getLogradouro().getIdLogradouro(), gravado.getLogradouro().getIdLogradouro());
    }

    @Test
    void cadastraCidadeNovaQuandoVemPeloNomeEUf() throws Exception {
        Endereco gravado = servicos.cadastrarEndereco(
                Dados.enderecoComCidadePeloNome(Dados.cepUnico(), "Santa Terezinha de Itaipu", "pr", "Centro", "R", "Tiradentes"));

        assertTrue(gravado.getCidade().getIdCidade() > 0);
        assertEquals("PR", gravado.getCidade().getEstado().getSigla());
        assertEquals("Paraná", gravado.getCidade().getEstado().getNome());
    }

    @Test
    void rejeitaEnderecoNulo() {
        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(null));
    }

    @Test
    void rejeitaCepEmBrancoComTamanhoErradoOuComLetras() {
        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(Dados.endereco("  ", 1, "Centro", "R", "A")));
        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(Dados.endereco("8585100", 1, "Centro", "R", "A")));
        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(Dados.endereco("8585A-000", 1, "Centro", "R", "A")));
        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(Dados.endereco("00000-000", 1, "Centro", "R", "A")));
    }

    @Test
    void rejeitaBairroOuLogradouroEmBranco() {
        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(Dados.endereco(Dados.cepUnico(), 1, "   ", "R", "A")));
        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(Dados.endereco(Dados.cepUnico(), 1, "Centro", "R", "")));
        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(Dados.endereco(Dados.cepUnico(), 1, "Centro", "", "A")));
    }

    @Test
    void rejeitaCidadeInexistenteUfInvalidaETipoInvalido() {
        EnderecoException cidade = assertThrows(EnderecoException.class,
                () -> servicos.cadastrarEndereco(Dados.endereco(Dados.cepUnico(), 99999, "Centro", "R", "A")));
        assertTrue(cidade.getMessage().contains("Cidade não encontrada"));

        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(
                Dados.enderecoComCidadePeloNome(Dados.cepUnico(), "Lugar Nenhum", "ZZ", "Centro", "R", "A")));
        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(
                Dados.endereco(Dados.cepUnico(), 1, "Centro", "XYZ", "A")));
    }

    @Test
    void rejeitaEnderecoDuplicado() throws Exception {
        String cep = Dados.cepUnico();
        servicos.cadastrarEndereco(Dados.endereco(cep, 3, "Centro", "R", "Paraná"));

        EnderecoException erro = assertThrows(EnderecoException.class,
                () -> servicos.cadastrarEndereco(Dados.endereco(cep, 3, "Centro", "R", "Paraná")));
        assertTrue(erro.getMessage().contains("já cadastrado"));
    }

    @Test
    void naoGravaNadaQuandoUmaEtapaFalha() throws Exception {
        String bairroNovo = "Bairro Que Nao Deve Ficar Gravado";

        // o bairro é inserido antes de o tipo de logradouro inválido ser detectado: o rollback precisa desfazer
        assertThrows(EnderecoException.class,
                () -> servicos.cadastrarEndereco(Dados.endereco(Dados.cepUnico(), 1, bairroNovo, "XYZ", "A")));

        try (Connection con = ConexaoBD.getConnection()) {
            assertNull(new BairroDAO().obterPorNome(bairroNovo, con));
        }
    }
}
