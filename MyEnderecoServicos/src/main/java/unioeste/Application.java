package unioeste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import unioeste.apoio.bd.ConexaoBD;

import java.sql.SQLException;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/** Cria o pool de conexões e as tabelas do H2 assim que a aplicação sobe. */
	@EventListener(ApplicationReadyEvent.class)
	public void inicializarBanco() throws SQLException {
		ConexaoBD.inicializar();
	}

}
