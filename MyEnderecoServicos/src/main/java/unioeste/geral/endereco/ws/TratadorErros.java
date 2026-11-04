package unioeste.geral.endereco.ws;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import unioeste.geral.endereco.exception.EnderecoException;

import java.util.Map;

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
