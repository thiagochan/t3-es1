package unioeste.geral.endereco.teste;

import unioeste.geral.endereco.bo.Bairro;
import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.endereco.bo.Logradouro;
import unioeste.geral.endereco.bo.TipoLogradouro;
import unioeste.geral.endereco.bo.UnidadeFederacao;

import java.util.concurrent.atomic.AtomicInteger;

final class Dados {

    private static final AtomicInteger PROXIMO_CEP = new AtomicInteger(99000001);

    private Dados() {
    }

    /** CEP de 8 dígitos ainda não usado por nenhum teste. */
    static String cepUnico() {
        return String.valueOf(PROXIMO_CEP.getAndIncrement());
    }

    /** Endereço com cidade pelo id e bairro/logradouro pelo nome. */
    static Endereco endereco(String cep, int idCidade, String nomeBairro, String siglaTipo, String nomeLogradouro) {
        Cidade cidade = new Cidade();
        cidade.setIdCidade(idCidade);
        return endereco(cep, cidade, nomeBairro, siglaTipo, nomeLogradouro);
    }

    /** Endereço com cidade pelo nome e UF. */
    static Endereco enderecoComCidadePeloNome(String cep, String nomeCidade, String siglaUF,
                                              String nomeBairro, String siglaTipo, String nomeLogradouro) {
        UnidadeFederacao uf = new UnidadeFederacao();
        uf.setSigla(siglaUF);
        Cidade cidade = new Cidade();
        cidade.setNome(nomeCidade);
        cidade.setEstado(uf);
        return endereco(cep, cidade, nomeBairro, siglaTipo, nomeLogradouro);
    }

    private static Endereco endereco(String cep, Cidade cidade, String nomeBairro, String siglaTipo, String nomeLogradouro) {
        Bairro bairro = new Bairro();
        bairro.setNome(nomeBairro);

        TipoLogradouro tipo = new TipoLogradouro();
        tipo.setSigla(siglaTipo);
        Logradouro logradouro = new Logradouro();
        logradouro.setNome(nomeLogradouro);
        logradouro.setTipoLogradouro(tipo);

        Endereco endereco = new Endereco();
        endereco.setCep(cep);
        endereco.setCidade(cidade);
        endereco.setBairro(bairro);
        endereco.setLogradouro(logradouro);
        return endereco;
    }
}
