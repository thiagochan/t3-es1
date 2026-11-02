package unioeste.apoio.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public final class ClienteHttp {

    private static final Duration TEMPO_LIMITE = Duration.ofSeconds(10);

    private static final HttpClient CLIENTE = HttpClient.newBuilder()
            .connectTimeout(TEMPO_LIMITE)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private ClienteHttp() {
    }

    public static RespostaHttp get(String url) throws IOException, InterruptedException {
        HttpRequest requisicao = HttpRequest.newBuilder(URI.create(url))
                .timeout(TEMPO_LIMITE)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> resposta = CLIENTE.send(requisicao, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        return new RespostaHttp(resposta.statusCode(), resposta.body());
    }
}
