package br.com.araujoabreu.sgi.tesouraria.util;

import br.com.araujoabreu.sgi.tesouraria.lang.LinhaDigitavelException;
import br.com.araujoabreu.sgi.tesouraria.lang.LinhaDigitavelValidateException;
import br.com.araujoabreu.sgi.tesouraria.util.boletoUtil.ObjectUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;


public class GuiaUtil {

	private static final String MSG_CODIGO_DE_BARRAS = "Código de barras inválido!";
	private static final String MSG_LINHA_INVALIDA = "A linha digitável não é válida.";

	public static String getDigitoOrBlocoDaLinhaDigitavel(String linhaNumerica, int posicaoInicial, int posicaoFinal) {
		return linhaNumerica.substring(posicaoInicial, posicaoFinal);
	}

	public static String linhaDigitavelFormatadaEmNumerica(String linhaDigitavel) {
		return linhaDigitavel.replace("-", "").replace(" ", "");
	}

	public static String linhaDigitavelNumericaEmCodigoDeBarras(String linhaDigitavel) {
		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);

		final StringBuilder codigoDeBarras = new StringBuilder();

		final String l = StringUtils.trim(linhaNumerica);

		codigoDeBarras.append(l.substring(0, 11));
		codigoDeBarras.append(l.substring(12, 23));
		codigoDeBarras.append(l.substring(24, 35));
		codigoDeBarras.append(l.substring(36, 47));

		return codigoDeBarras.toString();
	}

	public static String codigoDeBarrasEmLinhaDigitavelNumerica(String codigoDeBarras) {

		final StringBuilder linhaDigitavel = new StringBuilder();

		final String c = StringUtils.trim(codigoDeBarras);

		String digitoValorEfetivo = getDigitoOrBlocoDaLinhaDigitavel(c, 2, 3);

		/* Não retirar o A, B, C e D pois o calculo do modulo DEZ e ONZE precisa recebe uma string com tamanho 12*/

		//bloco1
		String primeiroBloco = c.substring(0, 11);
		linhaDigitavel.append(primeiroBloco);
		primeiroBloco = primeiroBloco + "A";
		linhaDigitavel.append(calculaDigitoVerificadorBloco(primeiroBloco, digitoValorEfetivo, 2));

		//bloco2
		String segundoBloco = c.substring(11, 22);
		linhaDigitavel.append(segundoBloco);
		segundoBloco = segundoBloco + "B";
		linhaDigitavel.append(calculaDigitoVerificadorBloco(segundoBloco, digitoValorEfetivo, 2));

		//bloco3
		String terceiroBloco = c.substring(22, 33);
		linhaDigitavel.append(terceiroBloco);
		terceiroBloco = terceiroBloco + "C";
		linhaDigitavel.append(calculaDigitoVerificadorBloco(terceiroBloco, digitoValorEfetivo, 2));

		//bloco4
		String quartoBloco = c.substring(33, 44);
		linhaDigitavel.append(quartoBloco);
		quartoBloco = quartoBloco + "D";
		linhaDigitavel.append(calculaDigitoVerificadorBloco(quartoBloco, digitoValorEfetivo, 2));

		return linhaDigitavel.toString();
	}

	public static boolean isLinhaDigitavelValida(String linhaDigitavel) {
		if (StringUtils.isNotBlank(linhaDigitavel)) {
			String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);
			return linhaNumerica.length() == 48;
		} else {
			return Boolean.FALSE;
		}
	}

	public static boolean isCodigoDeBarrasValido(String codigoDeBarras) {
		if (StringUtils.isNotBlank(codigoDeBarras)) {
			return codigoDeBarras.length() == 44;
		} else {
			return Boolean.FALSE;
		}
	}

	public static void checkExistsLinhaDigitavel(String linhaDigitavel) throws NullPointerException, IllegalArgumentException {
		ObjectUtil.isNotNull(linhaDigitavel, MSG_LINHA_INVALIDA);

		if (StringUtils.isBlank(linhaDigitavel)) {
			throw new IllegalArgumentException(MSG_LINHA_INVALIDA);
		}
	}

	public static void checkExistsCodigoBarras(String codigoDeBarras) throws NullPointerException, IllegalArgumentException {
		ObjectUtil.isNotNull(codigoDeBarras, MSG_CODIGO_DE_BARRAS);

		if (StringUtils.isBlank(codigoDeBarras)) {
			throw new IllegalArgumentException(MSG_CODIGO_DE_BARRAS);
		}
	}

	public static void checkFormatoLinhaDigitavel(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkExistsLinhaDigitavel(linhaDigitavel);

		if (!isLinhaDigitavelValida(linhaDigitavel)) {
			throw new LinhaDigitavelException(MSG_LINHA_INVALIDA + "deve conter apenas 48 dígitos númericos [0-9]!");
		}

		for (int x = 0; x < 10; x++) {
			if (linhaDigitavel.equals(TesourariaUtil.obterValoresRepetidos(String.valueOf(x), 47))) {
				throw new LinhaDigitavelException("Linha digitável inválida.");
			}
		}
	}

	public static void checkFormatoCodigoDeBarras(String codigoDeBarras) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkExistsCodigoBarras(codigoDeBarras);

		if (!isCodigoDeBarrasValido(codigoDeBarras)) {
			throw new LinhaDigitavelException(MSG_CODIGO_DE_BARRAS + "deve conter apenas 44 dígitos númericos [0-9]!");
		}

		for (int x = 0; x < 10; x++) {
			if (codigoDeBarras.equals(TesourariaUtil.obterValoresRepetidos(String.valueOf(x), 47))) {
				throw new LinhaDigitavelException("Código de barras inválido.");
			}
		}
	}

	public static void checkDigitoIdentificacaoProduto(String digito) throws LinhaDigitavelValidateException {
		if (!isValidarDigitoIdentificacaoProduto(digito))
			throw new LinhaDigitavelValidateException("o código do produto (1º caracter) é inválido.");
	}

	public static void checkDigitoIdentificacaoSegmento(String digito) throws LinhaDigitavelValidateException {
		if (!isValidarDigitoIdentificacaoSegmento(digito))
			throw new LinhaDigitavelValidateException("o segmento (2º caracter) é inválido (1 ou 5).");
	}

	public static void checkDigitoIdentificacaoValorEfetivo(String digito) throws LinhaDigitavelValidateException {
		if (!isValidarDigitoIdentificacaoValorEfetivo(digito))
			throw new LinhaDigitavelValidateException("o indicador de valor efetivo ou referência (3º caracter) é inválido (6, 7, 8 ou 9).");
	}

	public static void checkValorAPagar(String bloco, BigDecimal valorPagar) throws LinhaDigitavelValidateException {
		if (!isValidarValorAPagar(bloco, valorPagar))
			throw new LinhaDigitavelValidateException("o valor a pagar informado está inválido.");
	}

	public static void checkDigitoVerificadorGeral(String linhaNumerica, String digitoValorEfetivo, String digitoVerificadorGeral, int qtd) throws LinhaDigitavelValidateException {
		if (!isValidarDigitoVerificadorGeral(linhaNumerica, digitoValorEfetivo, digitoVerificadorGeral, qtd))
			throw new LinhaDigitavelValidateException("o dígito verificador geral (4º caracter) é inválido.");
	}


	private static Boolean isValidarDigitoIdentificacaoProduto(String digito) {
		if (digito.equals("8"))
			return true;
		return false;
	}

	private static Boolean isValidarDigitoIdentificacaoSegmento(String digito) {
		if (digito.equals("1") || digito.equals("5"))
			return true;
		return false;
	}

	private static Boolean isValidarDigitoIdentificacaoValorEfetivo(String digito) {
		if (digito.equals("6") || digito.equals("7") || digito.equals("8") || digito.equals("9"))
			return true;
		return false;
	}

	private static Boolean isValidarValorAPagar(String bloco, BigDecimal valorPagar) {
		bloco = bloco.substring(0, bloco.length() - 2) + "." + bloco.substring(bloco.length() - 2, bloco.length());
		BigDecimal valorPagarBloco = new BigDecimal(bloco);
		if (valorPagarBloco.equals(valorPagar))
			return true;
		return false;
	}

	private static Boolean isValidarDigitoVerificadorGeral(String linhaNumerica, String digitoValorEfetivo, String digitoVerificadorGeral, int qtd) {
		Integer digitoCalculado = calculaDigitoVerificadorBloco(linhaNumerica, digitoValorEfetivo, qtd);

		if (digitoCalculado == Integer.parseInt(digitoVerificadorGeral) || digitoCalculado == 0)
			return true;

		return false;
	}

	private static String montarMensagem(int posicao) {
		String mensagem = "";
		if (posicao == 0) {
			mensagem = "o dígito verificador do 1º bloco é inválido.";
		} else if (posicao == 12) {
			mensagem = "o dígito verificador do 2º bloco é inválido.";
		} else if (posicao == 24) {
			mensagem = "o dígito verificador do 3º bloco é inválido.";
		} else if (posicao == 36) {
			mensagem = "o dígito verificador do 4º bloco é inválido.";
		}

		return mensagem;
	}

	public static String retirarDigitoVerificadorGeral(String linhaNumerica) {
		return linhaNumerica.substring(0, 3) + linhaNumerica.substring(4, linhaNumerica.length());
	}

	public static String linhaDigitavelFormatada(String linhaNumerica) {
		final StringBuilder linhaDigitavel = new StringBuilder();


		linhaDigitavel.append(linhaNumerica.substring(0, 11));
		linhaDigitavel.append("-");
		linhaDigitavel.append(linhaNumerica.substring(11, 12));
		linhaDigitavel.append(" ");
		linhaDigitavel.append(linhaNumerica.substring(12, 23));
		linhaDigitavel.append("-");
		linhaDigitavel.append(linhaNumerica.substring(23, 24));
		linhaDigitavel.append(" ");
		linhaDigitavel.append(linhaNumerica.substring(24, 35));
		linhaDigitavel.append("-");
		linhaDigitavel.append(linhaNumerica.substring(35, 36));
		linhaDigitavel.append(" ");
		linhaDigitavel.append(linhaNumerica.substring(36, 47));
		linhaDigitavel.append("-");
		linhaDigitavel.append(linhaNumerica.substring(47, 48));

		return linhaDigitavel.toString();
	}

	public static void validarLinhaDigitavel(String linhaDigitavel, BigDecimal valorPagar) throws LinhaDigitavelValidateException, NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);
		String digitoProduto = getDigitoOrBlocoDaLinhaDigitavel(linhaNumerica, 0, 1);
		String digitoSegmento = getDigitoOrBlocoDaLinhaDigitavel(linhaNumerica, 1, 2);
		String digitoValorEfetivo = getDigitoOrBlocoDaLinhaDigitavel(linhaNumerica, 2, 3);
		String digitoGeral = getDigitoOrBlocoDaLinhaDigitavel(linhaNumerica, 3, 4);
		String codigoDeBarras = linhaDigitavelNumericaEmCodigoDeBarras(linhaNumerica);

		checkFormatoLinhaDigitavel(linhaDigitavel);
		checkDigitoIdentificacaoProduto(digitoProduto);
		checkDigitoIdentificacaoSegmento(digitoSegmento);
		checkDigitoIdentificacaoValorEfetivo(digitoValorEfetivo);

		String bloco = "";
		String digito = "";

		for (int i = 0; i < linhaNumerica.length(); i = i + 12) {

			bloco = getDigitoOrBlocoDaLinhaDigitavel(linhaNumerica, i, i + 12);

			digito = getDigitoOrBlocoDaLinhaDigitavel(bloco, bloco.length() - 1, bloco.length());

			if (calculaDigitoVerificadorBloco(bloco, digitoValorEfetivo, 2) != Integer.parseInt(digito))
				throw new LinhaDigitavelValidateException(montarMensagem(i));
		}

		codigoDeBarras = retirarDigitoVerificadorGeral(codigoDeBarras);
		checkDigitoVerificadorGeral(codigoDeBarras, digitoValorEfetivo, digitoGeral, 1);
	}

	public static Integer calculaDigitoVerificadorBloco(String bloco, String digitoValorEfetivo, int qtd) {
		if (digitoValorEfetivo.equals("6") || digitoValorEfetivo.equals("7"))
			return calculaModuloDezArrecadacoes(bloco, qtd);
		else
			return calculaModuloOnzeArrecadacoes(bloco, qtd);
	}

	public static void validarCodigoBarras(String codigoDeBarras, BigDecimal valorPagar) throws LinhaDigitavelValidateException, NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		String digitoProduto = getDigitoOrBlocoDaLinhaDigitavel(codigoDeBarras, 0, 1);
		String digitoSegmento = getDigitoOrBlocoDaLinhaDigitavel(codigoDeBarras, 1, 2);
		String digitoValorEfetivo = getDigitoOrBlocoDaLinhaDigitavel(codigoDeBarras, 2, 3);
		String digitoGeral = getDigitoOrBlocoDaLinhaDigitavel(codigoDeBarras, 3, 4);

		checkFormatoCodigoDeBarras(codigoDeBarras);
		checkDigitoIdentificacaoProduto(digitoProduto);
		checkDigitoIdentificacaoSegmento(digitoSegmento);
		checkDigitoIdentificacaoValorEfetivo(digitoValorEfetivo);
		codigoDeBarras = retirarDigitoVerificadorGeral(codigoDeBarras);
		checkDigitoVerificadorGeral(codigoDeBarras, digitoValorEfetivo, digitoGeral, 1);
	}

	public static Integer calculaModuloDezArrecadacoes(String bloco, int qtd) {
		Integer nProduto = 0;
		Integer nSoma = 0;


		int j = 2;
		for (int i = bloco.length() - qtd; i > -1; i--) {
			nProduto = Integer.parseInt("" + bloco.charAt(i)) * j;

			if (nProduto.toString().length() == 1) {
				nSoma = (nSoma + nProduto);
			} else {
				nSoma = (nProduto % 10) + 1 + nSoma;
			}

			if (j == 2)
				j = 1;
			else
				j = 2;
		}

		int resto = (nSoma % 10);

		if (resto == 0)
			resto = 0;
		else
			resto = (10 - resto);

		return resto;
	}

	public static Integer calculaModuloOnzeArrecadacoes(String bloco, int qtd) {
		int nSoma = 0;

		int j = 2;
		for (int i = bloco.length() - qtd; i > -1; i--) {
			nSoma = nSoma + (Integer.parseInt("" + bloco.charAt(i)) * j);

			if (++j == 10)
				j = 2;
		}

		int resto = (nSoma % 11);

		if (resto <= 1)
			resto = 0;
		else
			resto = (11 - resto);

		return resto;
	}
}
