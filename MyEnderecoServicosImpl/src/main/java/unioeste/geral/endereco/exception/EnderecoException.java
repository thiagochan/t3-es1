package unioeste.geral.endereco.exception;

/**
 * Erro de validação ou de regra de negócio dos serviços de endereço
 * (CEP inválido, dado em branco, cidade inexistente...).
 */
public class EnderecoException extends Exception {

    private static final long serialVersionUID = 1L;

    public EnderecoException(String mensagem) {
        super(mensagem);
    }
}
