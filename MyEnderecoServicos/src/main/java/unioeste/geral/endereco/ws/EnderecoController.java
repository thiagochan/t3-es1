package unioeste.geral.endereco.ws;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.endereco.bo.EnderecoEspecifico;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import java.util.List;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    private final UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();

    /** POST /endereco/cadastrarEndereco, com o Endereco em JSON no corpo. */
    @PostMapping("/cadastrarEndereco")
    public Endereco cadastrarEndereco(@RequestBody Endereco endereco) throws Exception {
        return servicos.cadastrarEndereco(endereco);
    }

    /** GET /endereco/obterEnderecoPorCEP/{cep} */
    @GetMapping("/obterEnderecoPorCEP/{cep}")
    public List<Endereco> obterEnderecoPorCEP(@PathVariable("cep") String cep) throws Exception {
        return servicos.obterEnderecoPorCEP(cep);
    }

    /** GET /endereco/obterEnderecoPorID/{id} */
    @GetMapping("/obterEnderecoPorID/{id}")
    public Endereco obterEnderecoPorID(@PathVariable("id") int id) throws Exception {
        Endereco endereco = new Endereco();
        endereco.setIdEndereco(id); // o serviço recebe um objeto Endereco com o id preenchido
        return servicos.obterEnderecoPorID(endereco);
    }

    /** GET /endereco/obterEnderecoExterno/{cep} (consulta o ViaCEP) */
    @GetMapping("/obterEnderecoExterno/{cep}")
    public EnderecoEspecifico obterEnderecoExterno(@PathVariable("cep") String cep) throws Exception {
        return servicos.obterEnderecoExterno(cep);
    }

    /** GET /endereco/obterCidade/{id} */
    @GetMapping("/obterCidade/{id}")
    public Cidade obterCidade(@PathVariable("id") int id) throws Exception {
        Cidade cidade = new Cidade();
        cidade.setIdCidade(id); // o serviço recebe um objeto Cidade com o id preenchido
        return servicos.obterCidade(cidade);
    }
}
