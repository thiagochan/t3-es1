package unioeste.geral.endereco.ws;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import unioeste.geral.endereco.exception.EnderecoException;

import java.util.Map;

/**
 * Converte as exceções dos serviços em respostas HTTP com JSON {"erro": "mensagem"}.
 *
 * <ul>
 *   <li>EnderecoException (validação / regra de negócio) → 400 Bad Request;</li>
 *   <li>qualquer outra Exception (banco, rede) → 500 Internal Server Error.</li>
 * </ul>
 * Estender ResponseEntityExceptionHandler mantém as respostas padrão do Spring para erros
 * de requisição (JSON malformado, rota inexistente...), em vez de virarem 500.
 */
@RestControllerAdvice
public class TratadorErros extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EnderecoException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(EnderecoException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", mensagem(e)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> tratarInfraestrutura(Exception e) {
        logger.error("Erro de infraestrutura", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("erro", "Erro de infraestrutura: " + mensagem(e)));
    }

    private static String mensagem(Exception e) {
        return e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
    }
}
