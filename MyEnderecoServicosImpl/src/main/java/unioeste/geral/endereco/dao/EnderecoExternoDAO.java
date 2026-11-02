package unioeste.geral.endereco.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.json.JsonMapper;
import unioeste.apoio.http.ClienteHttp;
import unioeste.apoio.http.RespostaHttp;
import unioeste.geral.endereco.bo.Bairro;
import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.endereco.bo.Logradouro;
import unioeste.geral.endereco.bo.TipoLogradouro;
import unioeste.geral.endereco.bo.UnidadeFederacao;

import java.io.IOException;

/**
 * Acesso a dados de endereço numa fonte externa: o webservice ViaCEP.
 * É um DAO como os outros, só que a fonte dos dados é um serviço na web em vez do banco.
 */
public class EnderecoExternoDAO {

    private static final String URL_VIACEP = "https://viacep.com.br/ws/%s/json/";

    private static final JsonMapper JSON = JsonMapper.builder().build();

    /**
     * Consulta um CEP (8 dígitos) no ViaCEP.
     *
     * @return o endereço encontrado, ou null se o CEP não existe na base do ViaCEP
     * @throws IOException se o serviço estiver fora do ar ou responder com erro
     */
    public Endereco obterPorCep(String cep) throws IOException, InterruptedException {
        RespostaHttp resposta = ClienteHttp.get(String.format(URL_VIACEP, cep));
        if (resposta.status() != 200) {
            throw new IOException("O serviço externo de CEP respondeu HTTP " + resposta.status());
        }
        return converterJson(cep, resposta.corpo());
    }

    /**
     * Converte o JSON do ViaCEP num Endereco. Fica público para poder ser testado sem internet.
     *
     * <p>Exemplo de JSON: {"cep":"01001-000","logradouro":"Praça da Sé","bairro":"Sé",
     * "localidade":"São Paulo","uf":"SP","estado":"São Paulo", ...}.
     * Quando o CEP não existe, o ViaCEP devolve {"erro": "true"}.
     *
     * @return o endereço, ou null se o JSON indicar CEP inexistente
     */
    public Endereco converterJson(String cep, String json) {
        RespostaViaCep dados = JSON.readValue(json, RespostaViaCep.class);
        if (dados.erro != null) {
            return null;
        }

        UnidadeFederacao uf = new UnidadeFederacao();
        uf.setSigla(dados.uf);
        uf.setNome(dados.estado);

        Cidade cidade = new Cidade();
        cidade.setNome(dados.localidade);
        cidade.setEstado(uf);

        Endereco endereco = new Endereco();
        endereco.setCep(cep);
        endereco.setCidade(cidade);

        // CEPs gerais de cidade vêm sem bairro e sem logradouro: o usuário completa na tela
        if (temTexto(dados.bairro)) {
            Bairro bairro = new Bairro();
            bairro.setNome(dados.bairro.trim());
            endereco.setBairro(bairro);
        }
        if (temTexto(dados.logradouro)) {
            endereco.setLogradouro(separarTipo(dados.logradouro.trim()));
        }
        return endereco;
    }

    /**
     * O ViaCEP devolve o logradouro inteiro ("Avenida Brasil"). Separa a primeira palavra
     * como nome do tipo ("Avenida") e o resto como nome do logradouro ("Brasil").
     * A sigla do tipo (AV) é procurada depois na base local, pelo ColLogradouro.
     */
    private Logradouro separarTipo(String logradouroCompleto) {
        Logradouro logradouro = new Logradouro();
        int espaco = logradouroCompleto.indexOf(' ');
        if (espaco > 0) {
            TipoLogradouro tipo = new TipoLogradouro();
            tipo.setNome(logradouroCompleto.substring(0, espaco));
            logradouro.setTipoLogradouro(tipo);
            logradouro.setNome(logradouroCompleto.substring(espaco + 1).trim());
        } else {
            logradouro.setNome(logradouroCompleto);
        }
        return logradouro;
    }

    private static boolean temTexto(String valor) {
        return valor != null && !valor.isBlank();
    }

    /** Campos do JSON do ViaCEP que o sistema usa; os demais são ignorados. */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RespostaViaCep {
        public String cep;
        public String logradouro;
        public String bairro;
        public String localidade;
        public String uf;
        public String estado;
        /** Só aparece quando o CEP não existe ("erro": "true"). */
        public Object erro;
    }
}
