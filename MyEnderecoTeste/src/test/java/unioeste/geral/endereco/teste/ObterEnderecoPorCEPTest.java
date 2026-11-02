package unioeste.geral.endereco.teste;

import org.junit.jupiter.api.Test;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObterEnderecoPorCEPTest {

    private final UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();

    @Test
    void retornaTodosOsEnderecosDoCep() throws Exception {
        List<Endereco> enderecos = servicos.obterEnderecoPorCEP("85851-000");

        assertEquals(2, enderecos.size());
        for (Endereco endereco : enderecos) {
            assertEquals("85851000", endereco.getCep());
            assertEquals("Foz do Iguaçu", endereco.getCidade().getNome());
        }
    }

    @Test
    void encontraEnderecoRecemCadastrado() throws Exception {
        String cep = Dados.cepUnico();
        servicos.cadastrarEndereco(Dados.endereco(cep, 4, "Batel", "AV", "Sete de Setembro"));

        List<Endereco> enderecos = servicos.obterEnderecoPorCEP(cep);

        assertEquals(1, enderecos.size());
        assertEquals("Sete de Setembro", enderecos.get(0).getLogradouro().getNome());
        assertEquals("Curitiba", enderecos.get(0).getCidade().getNome());
    }

    @Test
    void cepSemEnderecoRetornaListaVazia() throws Exception {
        assertTrue(servicos.obterEnderecoPorCEP("01001000").isEmpty());
    }

    @Test
    void rejeitaCepInvalido() {
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoPorCEP(null));
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoPorCEP("abc"));
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoPorCEP("858510000"));
    }
}
