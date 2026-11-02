# t3-es1 – Engenharia de Software I (T3)

Serviços de Endereço (T3B) e classes de negócio do Imposto de Renda (T3A), em Java 17 com
Maven multi-módulo, banco H2 em memória e SQL direto (JDBC) com pool de conexões (HikariCP).

## Módulos

| Módulo | Pacotes | O que tem |
|---|---|---|
| MyEnderecoBO | `unioeste.geral.endereco.bo` | Classes de entidade de endereço |
| MyPessoaBO | `unioeste.geral.pessoa.bo` | Classes genéricas de pessoa (T3A) |
| MyImpostoRendaBO | `unioeste.ir.bo` | Classes específicas do IR (T3A) |
| MyInfraAPI | `unioeste.apoio.bd`, `unioeste.apoio.http` | Pool de conexões e cliente HTTP |
| MyEnderecoServicosImpl | `unioeste.geral.endereco.manager`, `.col`, `.dao`, `.exception` | `UCEnderecoGeralServicos`, regras, SQL e scripts do H2 |
| MyEnderecoServicos | `unioeste`, `unioeste.geral.endereco.ws` | Aplicação Spring Boot que expõe os serviços via REST/JSON |
| MyEnderecoTeste | `unioeste.geral.endereco.teste` | Um teste JUnit por serviço |

## Como rodar

No IntelliJ: abra o `pom.xml` da raiz e execute a classe `unioeste.Application` (módulo MyEnderecoServicos).

Pelo terminal (na raiz do repositório):

```
mvnw.cmd clean package
java -jar MyEnderecoServicos\target\MyEnderecoServicos-0.0.1-SNAPSHOT.jar
```

No Linux/macOS, use `./mvnw` e `/` no caminho do jar. O `package` também roda todos os testes.

## Serviços

| Serviço | Método | Endereço HTTP |
|---|---|---|
| cadastrarEndereco | POST | `http://localhost:8080/endereco/cadastrarEndereco` |
| obterEnderecoPorCEP | GET | `http://localhost:8080/endereco/obterEnderecoPorCEP/{cep}` |
| obterEnderecoPorID | GET | `http://localhost:8080/endereco/obterEnderecoPorID/{id}` |
| obterEnderecoExterno | GET | `http://localhost:8080/endereco/obterEnderecoExterno/{cep}` |
| obterCidade | GET | `http://localhost:8080/endereco/obterCidade/{id}` |

Erros voltam como JSON `{"erro": "mensagem"}`: 400 para validação, 500 para infraestrutura.

## Banco H2

O banco é criado do zero a cada execução pelos scripts em
`MyEnderecoServicosImpl/src/main/resources/sql/`. Para consultar as tabelas com a aplicação rodando:
`http://localhost:8080/h2-console`, JDBC URL `jdbc:h2:mem:t3es1`, usuário `sa`, senha vazia.
