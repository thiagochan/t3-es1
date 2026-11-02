package unioeste.geral.endereco.col;

import unioeste.geral.endereco.exception.EnderecoException;

/** Verificações simples usadas por todas as Cols. */
final class Validacao {

    private Validacao() {
    }

    /** true se o texto for null, vazio ou só espaços. */
    static boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }

    /** Lança EnderecoException se o texto passar do tamanho da coluna no banco. */
    static void tamanhoMaximo(String valor, int maximo, String campo) throws EnderecoException {
        if (valor != null && valor.trim().length() > maximo) {
            throw new EnderecoException(campo + " deve ter no máximo " + maximo + " caracteres");
        }
    }
}
