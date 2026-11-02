package unioeste.geral.endereco.teste;

import org.junit.jupiter.api.Test;
import unioeste.apoio.http.ClienteHttp;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.endereco.bo.EnderecoEspecifico;
import unioeste.geral.endereco.dao.EnderecoExternoDAO;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Testes do serviço obterEnderecoExterno.
 *
 * <p>A maioria usa uma fonte falsa, que devolve um JSON fixo no formato do ViaCEP, para
 * o teste não depender da internet. O último teste chama o ViaCEP de verdade e só roda
 * se houver conexão.
 */
class ObterEnderecoExternoTest {

    private static final String JSON_AVENIDA_BRASIL = """
            {"cep": "85851-000", "logradouro": "Avenida Brasil", "complemento": "", "bairro": "Centro",
             "localidade": "Foz do Iguaçu", "uf": "PR", "estado": "Paraná", "ibge": "4108304"}
            """;

    private static final String JSON_TIPO_DESCONHECIDO = """
            {"cep": "05433-000", "logradouro": "Beco do Batman", "bairro": "Vila Madalena",
             "localidade": "São Paulo", "uf": "SP"}
            """;

    private static final String JSON_CEP_INEXISTENTE = "{\"erro\": \"true\"}";

    @Test
    void completaComIdsDaBaseLocal() throws Exception {
        UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos(fonteFalsa(JSON_AVENIDA_BRASIL));

        EnderecoEspecifico resultado = servicos.obterEnderecoExterno("85851-000");
        Endereco endereco = resultado.getEndereco();

        assertEquals("85851000", endereco.getCep());
        assertEquals(1, endereco.getCidade().getIdCidade());
        assertTrue(endereco.getBairro().getIdBairro() > 0);
        assertEquals("AV", endereco.getLogradouro().getTipoLogradouro().getSigla());
        assertEquals("Brasil", endereco.getLogradouro().getNome());
        assertTrue(endereco.getLogradouro().getIdLogradouro() > 0);
    }

    @Test
    void tipoDeLogradouroDesconhecidoFicaNoNome() throws Exception {
        UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos(fonteFalsa(JSON_TIPO_DESCONHECIDO));

        Endereco endereco = servicos.obterEnderecoExterno("05433000").getEndereco();

        assertEquals("Beco do Batman", endereco.getLogradouro().getNome());
        assertNull(endereco.getLogradouro().getTipoLogradouro());
        assertEquals("São Paulo", endereco.getCidade().getNome());
        assertEquals("São Paulo", endereco.getCidade().getEstado().getNome());
        assertEquals(0, endereco.getBairro().getIdBairro());
    }

    @Test
    void enderecoExternoPodeSerCadastradoDepois() throws Exception {
        UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos(fonteFalsa(JSON_AVENIDA_BRASIL));
        Endereco endereco = servicos.obterEnderecoExterno("85851000").getEndereco();

        String cepNovo = Dados.cepUnico();
        endereco.setCep(cepNovo);
        Endereco gravado = servicos.cadastrarEndereco(endereco);

        assertTrue(gravado.getIdEndereco() > 0);
        assertEquals(cepNovo, gravado.getCep());
    }

    @Test
    void rejeitaCepInexistenteNoServicoExterno() {
        UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos(fonteFalsa(JSON_CEP_INEXISTENTE));

        EnderecoException erro = assertThrows(EnderecoException.class, () -> servicos.obterEnderecoExterno("99999-999"));
        assertTrue(erro.getMessage().contains("não encontrado"));
    }

    @Test
    void rejeitaCepInvalidoSemConsultarOServico() {
        UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos(fonteFalsa(JSON_AVENIDA_BRASIL));

        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoExterno("123"));
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoExterno(""));
    }

    @Test
    void consultaOViaCepDeVerdade() throws Exception {
        assumeTrue(internetDisponivel(), "Sem acesso ao ViaCEP: teste ignorado");

        Endereco endereco = new UCEnderecoGeralServicos().obterEnderecoExterno("01001-000").getEndereco();

        assertEquals("São Paulo", endereco.getCidade().getNome());
        assertEquals("SP", endereco.getCidade().getEstado().getSigla());
        assertEquals("PC", endereco.getLogradouro().getTipoLogradouro().getSigla());
    }

    /** Fonte externa falsa: em vez de ir à internet, converte o JSON fixo. */
    private static EnderecoExternoDAO fonteFalsa(String json) {
        return new EnderecoExternoDAO() {
            @Override
            public Endereco obterPorCep(String cep) {
                return converterJson(cep, json);
            }
        };
    }

    private static boolean internetDisponivel() {
        try {
            return ClienteHttp.get("https://viacep.com.br/ws/01001000/json/").status() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
