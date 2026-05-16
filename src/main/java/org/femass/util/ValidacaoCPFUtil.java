package org.femass.util;

public class ValidacaoCPFUtil {

    public static boolean validarCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^\\d]", "");

        // Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex.: "111.111.111-11")
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // Cálculo dos dígitos verificadores
            int soma1 = 0, soma2 = 0;
            for (int i = 0; i < 9; i++) {
                int num = Character.getNumericValue(cpf.charAt(i));
                soma1 += num * (10 - i);
                soma2 += num * (11 - i);
            }

            // Primeiro dígito verificador
            int digito1 = (soma1 * 10) % 11;
            if (digito1 == 10) digito1 = 0;

            // Segundo dígito verificador
            soma2 += digito1 * 2;
            int digito2 = (soma2 * 10) % 11;
            if (digito2 == 10) digito2 = 0;

            // Verifica os dígitos validadores no CPF
            return digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                    digito2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }
}