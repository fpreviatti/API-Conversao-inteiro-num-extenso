package com.conversor.conversao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CurrencyWriter {

    private static final BigInteger THOUSAND = new BigInteger("1000");
    private static final BigInteger HUNDRED = new BigInteger("100");
    private static final String CENTO = "cento";
    private static final String CEM = "cem";

    private final Map<Integer, String> grandezasPlural = new HashMap<Integer, String>();
    private final Map<Integer, String> grandezasSingular = new HashMap<Integer, String>();

    private final Map<Integer, String> nomes = new HashMap<Integer, String>();

    private static final String PARTICULA_ADITIVA = "e";
    private static final String PARTICULA_DESCRITIVA = "de";

    private static final BigDecimal MAX_SUPPORTED_VALUE = new BigDecimal("999999999999999999999999999.99");

    private static CurrencyWriter instance = null;

    public static CurrencyWriter getInstance() {
        if (instance == null) {
            instance = new CurrencyWriter();
        }

        return instance;
    }

    private CurrencyWriter() {
        preencherGrandezasPlural();
        preencherGrandezasSingular();
        preencherNomes();
    }

    public String write(final BigDecimal amount) {
        if (null == amount) {throw new IllegalArgumentException();}
        
        BigDecimal value = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        if (value.compareTo(BigDecimal.ZERO) <= 0) {return "";}

        if (MAX_SUPPORTED_VALUE.compareTo(value) < 0) {
            throw new IllegalArgumentException("Valor acima do limite suportado.");
        }

        Stack<Integer> decomposed = decompose(value);

        int expoente = 3 * (decomposed.size() - 2);

        StringBuffer sb = new StringBuffer();
        int lastNonZeroExponent = -1;

        while (!decomposed.empty()) {
            int valor = decomposed.pop();

            if (valor > 0) {
                sb.append(" ").append(PARTICULA_ADITIVA).append(" ");
                sb.append(comporNomeGrupos(valor));
                String nomeGrandeza = obterNomeGrandeza(expoente, valor);
                if (nomeGrandeza.length() > 0) {
                    sb.append(" ");
                }
                sb.append(nomeGrandeza);

                lastNonZeroExponent = expoente;
            }

            switch (expoente) {
                case 0:
                    BigInteger parteInteira = value.toBigInteger();

                    if (BigInteger.ONE.equals(parteInteira)) {
                        sb.append(" ");
                    } else if (parteInteira.compareTo(BigInteger.ZERO) > 0) {
                        if (lastNonZeroExponent >= 6) {
                            sb.append(" ").append(PARTICULA_DESCRITIVA);
                        }
                        sb.append(" ");
                    }
                    break;

                case -3:
                    if (1 == valor) {
                        sb.append(" ");
                    } else if (valor > 1) {
                        sb.append(" ");
                    }
                    break;
            }

            expoente -= 3;
        }

        return sb.substring(3);
    }

    private StringBuffer comporNomeGrupos(int valor) {
        StringBuffer nome = new StringBuffer();

        int centenas = valor - (valor % 100);
        int unidades = valor % 10;
        int dezenas = (valor - centenas) - unidades;
        int duasCasas = dezenas + unidades;

        if (centenas > 0) {
            nome.append(" ").append(PARTICULA_ADITIVA).append(" ");

            if (100 == centenas) {
                if (duasCasas > 0) {
                    nome.append(CENTO);
                } else {
                    nome.append(CEM);
                }
            } else {
                nome.append(nomes.get(centenas));
            }
        }

        if (duasCasas > 0) {
            nome.append(" ").append(PARTICULA_ADITIVA).append(" ");
            if (duasCasas < 20) {
                nome.append(nomes.get(duasCasas));
            } else {
                if (dezenas > 0) {
                    nome.append(nomes.get(dezenas));
                }

                if (unidades > 0) {
                    nome.append(" ").append(PARTICULA_ADITIVA).append(" ");
                    nome.append(nomes.get(unidades));
                }
            }
        }

        return nome.delete(0, 3);
    }

    private String obterNomeGrandeza(int exponent, int value) {
        if (exponent < 3) {return "";}

        if (1 == value) {
            return grandezasSingular.get(exponent);
        } else {
            return grandezasPlural.get(exponent);
        }
    }

    private Stack<Integer> decompose(BigDecimal value) {
        BigInteger intermediate = value.multiply(new BigDecimal(100)).toBigInteger();
        Stack<Integer> decomposed = new Stack<Integer>();

        BigInteger[] result = intermediate.divideAndRemainder(HUNDRED);
        intermediate = result[0];
        decomposed.add(result[1].intValue());

        while (intermediate.compareTo(BigInteger.ZERO) > 0) {
            result = intermediate.divideAndRemainder(THOUSAND);
            intermediate = result[0];
            decomposed.add(result[1].intValue());
        }

        if (decomposed.size() == 1) {
            decomposed.add(0);
        }

        return decomposed;
    }

    private void preencherGrandezasPlural() {
        grandezasPlural.put(3, "mil");
        grandezasPlural.put(6, "milhões");
        grandezasPlural.put(9, "bilhões");
        grandezasPlural.put(12, "trilhões");
        grandezasPlural.put(15, "quatrilhões");
        grandezasPlural.put(18, "quintilhões");
        grandezasPlural.put(21, "sextilhões");
        grandezasPlural.put(24, "setilhões");
    }

    private void preencherGrandezasSingular() {
        grandezasSingular.put(3, "mil");
        grandezasSingular.put(6, "milhão");
        grandezasSingular.put(9, "bilhão");
        grandezasSingular.put(12, "trilhão");
        grandezasSingular.put(15, "quatrilhão");
        grandezasSingular.put(18, "quintilhão");
        grandezasSingular.put(21, "sextilhão");
        grandezasSingular.put(24, "setilhão");
    }

    private void preencherNomes() {
        nomes.put(1, "um");
        nomes.put(2, "dois");
        nomes.put(3, "três");
        nomes.put(4, "quatro");
        nomes.put(5, "cinco");
        nomes.put(6, "seis");
        nomes.put(7, "sete");
        nomes.put(8, "oito");
        nomes.put(9, "nove");
        nomes.put(10, "dez");
        nomes.put(11, "onze");
        nomes.put(12, "doze");
        nomes.put(13, "treze");
        nomes.put(14, "quatorze");
        nomes.put(15, "quinze");
        nomes.put(16, "dezesseis");
        nomes.put(17, "dezessete");
        nomes.put(18, "dezoito");
        nomes.put(19, "dezenove");
        nomes.put(20, "vinte");
        nomes.put(30, "trinta");
        nomes.put(40, "quarenta");
        nomes.put(50, "cinquenta");
        nomes.put(60, "sessenta");
        nomes.put(70, "setenta");
        nomes.put(80, "oitenta");
        nomes.put(90, "noventa");
        nomes.put(200, "duzentos");
        nomes.put(300, "trezentos");
        nomes.put(400, "quatrocentos");
        nomes.put(500, "quinhentos");
        nomes.put(600, "seiscentos");
        nomes.put(700, "setecentos");
        nomes.put(800, "oitocentos");
        nomes.put(900, "novecentos");
    }
}