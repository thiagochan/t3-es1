package unioeste.geral.endereco.teste;

import org.junit.jupiter.api.Test;
import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObterCidadeTest {

    private final UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();

    @Test
    void retornaCidadeComUf() throws Exception {
        Cidade cidade = servicos.obterCidade(comId(1));

        assertEquals("Foz do Iguaçu", cidade.getNome());
        assertEquals("PR", cidade.getEstado().getSigla());
        assertEquals("Paraná", cidade.getEstado().getNome());
    }

    @Test
    void rejeitaIdInvalido() {
        assertThrows(EnderecoException.class, () -> servicos.obterCidade(null));
        assertThrows(EnderecoException.class, () -> servicos.obterCidade(comId(0)));
    }

    @Test
    void rejeitaCidadeInexistente() {
        assertThrows(EnderecoException.class, () -> servicos.obterCidade(comId(99999)));
    }

    private static Cidade comId(int id) {
        Cidade cidade = new Cidade();
        cidade.setIdCidade(id);
        return cidade;
    }
}
