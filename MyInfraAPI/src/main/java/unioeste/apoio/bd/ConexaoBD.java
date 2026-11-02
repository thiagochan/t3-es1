package unioeste.apoio.bd;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Ponto único de acesso ao SGBD. Cria o pool de conexões (HikariCP) uma única vez
 * e entrega conexões para quem pedir.
 *
 * <p>A configuração é lida do arquivo {@code /db.properties} no classpath:
 * <ul>
 *   <li>{@code db.url}, {@code db.usuario}, {@code db.senha}: conexão JDBC;</li>
 *   <li>{@code db.pool.max} (opcional): tamanho máximo do pool, padrão 10;</li>
 *   <li>{@code db.scripts} (opcional): scripts SQL do classpath, separados por vírgula,
 *       executados uma única vez, logo depois de criar o pool.</li>
 * </ul>
 *
 * <p>Com o H2 em memória o banco começa vazio a cada execução, por isso os scripts
 * criam as tabelas e carregam os dados iniciais. Esta classe não conhece nenhuma
 * tabela: quem define os scripts é o projeto que usa a infraestrutura.
 */
public final class ConexaoBD {

    private static final String ARQUIVO_CONFIGURACAO = "/db.properties";

    private static HikariDataSource pool;

    private ConexaoBD() {
    }

    /**
     * Devolve uma conexão do pool. Use sempre com try-with-resources: o close()
     * não fecha a conexão de verdade, ele a devolve ao pool.
     */
    public static Connection getConnection() throws SQLException {
        return obterPool().getConnection();
    }

    /** Cria o pool e executa os scripts já na subida da aplicação, em vez de na primeira chamada. */
    public static void inicializar() throws SQLException {
        obterPool();
    }

    /** Fecha o pool e todas as suas conexões. */
    public static synchronized void fechar() {
        if (pool != null) {
            pool.close();
            pool = null;
        }
    }

    private static synchronized HikariDataSource obterPool() throws SQLException {
        if (pool == null) {
            Properties configuracao = carregarConfiguracao();
            HikariDataSource novoPool = criarPool(configuracao);
            try {
                executarScripts(novoPool, configuracao.getProperty("db.scripts", ""));
            } catch (SQLException e) {
                novoPool.close();
                throw e;
            }
            pool = novoPool;
        }
        return pool;
    }

    private static Properties carregarConfiguracao() throws SQLException {
        try (InputStream in = ConexaoBD.class.getResourceAsStream(ARQUIVO_CONFIGURACAO)) {
            if (in == null) {
                throw new SQLException("Arquivo " + ARQUIVO_CONFIGURACAO + " não encontrado no classpath");
            }
            Properties propriedades = new Properties();
            propriedades.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            return propriedades;
        } catch (IOException e) {
            throw new SQLException("Erro ao ler " + ARQUIVO_CONFIGURACAO + ": " + e.getMessage(), e);
        }
    }

    private static HikariDataSource criarPool(Properties propriedades) throws SQLException {
        String url = propriedades.getProperty("db.url", "").trim();
        if (url.isEmpty()) {
            throw new SQLException("Propriedade db.url não definida em " + ARQUIVO_CONFIGURACAO);
        }
        HikariConfig config = new HikariConfig();
        config.setPoolName("PoolT3");
        config.setJdbcUrl(url);
        config.setUsername(propriedades.getProperty("db.usuario", "").trim());
        config.setPassword(propriedades.getProperty("db.senha", "").trim());
        config.setMaximumPoolSize(Integer.parseInt(propriedades.getProperty("db.pool.max", "10").trim()));
        try {
            return new HikariDataSource(config);
        } catch (RuntimeException e) {
            throw new SQLException("Não foi possível criar o pool de conexões: " + e.getMessage(), e);
        }
    }

    private static void executarScripts(HikariDataSource dataSource, String scripts) throws SQLException {
        if (scripts.isBlank()) {
            return;
        }
        try (Connection con = dataSource.getConnection(); Statement st = con.createStatement()) {
            for (String script : scripts.split(",")) {
                String recurso = script.trim();
                for (String comando : lerComandos(recurso)) {
                    try {
                        st.execute(comando);
                    } catch (SQLException e) {
                        throw new SQLException("Erro no script " + recurso + ": " + e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * Lê um script SQL do classpath e o separa em comandos pelo ";".
     * Linhas que começam com "--" são comentários e são ignoradas.
     */
    static List<String> lerComandos(String recurso) throws SQLException {
        InputStream in = ConexaoBD.class.getResourceAsStream(recurso);
        if (in == null) {
            throw new SQLException("Script " + recurso + " não encontrado no classpath");
        }
        StringBuilder sql = new StringBuilder();
        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (!linha.trim().startsWith("--")) {
                    sql.append(linha).append('\n');
                }
            }
        } catch (IOException e) {
            throw new SQLException("Erro ao ler o script " + recurso + ": " + e.getMessage(), e);
        }
        List<String> comandos = new ArrayList<>();
        for (String comando : sql.toString().split(";")) {
            if (!comando.isBlank()) {
                comandos.add(comando.trim());
            }
        }
        return comandos;
    }
}
