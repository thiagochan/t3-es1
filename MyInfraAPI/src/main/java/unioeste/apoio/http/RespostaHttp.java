package unioeste.apoio.http;

/**
 * Resposta de uma requisição HTTP: o código de status e o corpo em texto.
 */
public record RespostaHttp(int status, String corpo) {
}
