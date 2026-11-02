package unioeste.geral.endereco.teste;

import org.junit.jupiter.api.Test;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Testes do serviço obterEnderecoPorID. */
class ObterEnderecoPorIDTest {

    private final UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();

    @Test
    void retornaEnderecoCompleto() throws Exception {
        Endereco endereco = servicos.obterEnderecoPorID(comId(1));

        assertEquals(1, endereco.getIdEndereco());
        assertEquals("85851000", endereco.getCep());
        assertEquals("Brasil", endereco.getLogradouro().getNome());
        assertEquals("AV", endereco.getLogradouro().getTipoLogradouro().getSigla());
        assertEquals("Centro", endereco.getBairro().getNome());
        assertEquals("Foz do Iguaçu", endereco.getCidade().getNome());
        assertEquals("PR", endereco.getCidade().getEstado().getSigla());
    }

    @Test
    void retornaEnderecoRecemCadastrado() throws Exception {
        Endereco gravado = servicos.cadastrarEndereco(Dados.endereco(Dados.cepUnico(), 5, "Sé", "PC", "da Sé"));

        Endereco lido = servicos.obterEnderecoPorID(comId(gravado.getIdEndereco()));

        assertEquals(gravado.getCep(), lido.getCep());
        assertEquals("São Paulo", lido.getCidade().getNome());
    }

    @Test
    void rejeitaIdInvalido() {
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoPorID(null));
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoPorID(comId(0)));
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoPorID(comId(-5)));
    }

    @Test
    void rejeitaIdInexistente() {
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoPorID(comId(999999)));
    }

    private static Endereco comId(int id) {
        Endereco endereco = new Endereco();
        endereco.setIdEndereco(id);
        return endereco;
    }
}
