package crud.clinica.view.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;

/**
 * Classe utilitária final para validações de dados da aplicação.
 * Contém apenas métodos estáticos e não pode ser instanciada.
 */
public final class ValidacaoUtil {

    /**
     * Construtor privado para impedir a instanciação da classe.
     */
    private ValidacaoUtil() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /**
     * Valida um CPF brasileiro. Verifica o formato, se todos os dígitos são iguais
     * e os dígitos verificadores.
     *
     * @param cpf O CPF a ser validado, pode conter máscara (pontos e traço).
     * @return {@code true} se o CPF for válido, {@code false} caso contrário.
     */
    public static boolean validarCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            // Cálculo do 1º Dígito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (cpf.charAt(i) - 48); // Converte o char para número
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48);
            }

            // Cálculo do 2º Dígito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            // Verifica se os dígitos calculados conferem com os dígitos informados
            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
            
        } catch (InputMismatchException erro) {
            return false;
        }
    }

    /**
     * Valida uma data no formato "dd/MM/yyyy".
     *
     * @param data A string da data a ser validada.
     * @return {@code true} se a data for válida, {@code false} caso contrário.
     */
    public static boolean validarData(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // Impede que datas como 32/01/2023 sejam convertidas para 01/02/2023
        try {
            sdf.parse(data);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    /**
     * Valida um nome. Verifica se contém apenas letras, espaços e acentos,
     * e se o comprimento está entre 3 e 100 caracteres.
     *
     * @param nome O nome a ser validado.
     * @return {@code true} se o nome for válido, {@code false} caso contrário.
     */
    public static boolean validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3 || nome.trim().length() > 100) {
            return false;
        }
        // Regex permite letras (incluindo acentuadas), espaços e apóstrofos.
        return nome.matches("^[\\p{L} ']+$");
    }
    
    // Os métodos formatarCPF e formatarData não foram pedidos no início,
    // mas podem ser adicionados aqui se necessário. Geralmente,
    // o componente JFormattedTextField é usado para isso na própria view.
}