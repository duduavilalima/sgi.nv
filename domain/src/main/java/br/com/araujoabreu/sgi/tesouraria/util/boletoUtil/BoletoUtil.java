package br.com.araujoabreu.sgi.tesouraria.util.boletoUtil;

import br.com.araujoabreu.sgi.tesouraria.lang.CodigoDeBarrasException;
import br.com.araujoabreu.sgi.tesouraria.lang.LinhaDigitavelException;
import br.com.araujoabreu.sgi.tesouraria.lang.LinhaDigitavelValidateException;
import br.com.araujoabreu.sgi.tesouraria.util.TesourariaUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BoletoUtil {

	public static final String DATA_BASE_FEBRABAN = "07/10/1997";
	/*
	 * Regras REGEX.
	 */
	public static final String CODIGO_DE_BARRAS_REGEX = "\\d{44}";
	public static final String LINHA_DIGITAVEL_NUMERICA_REGEX = "\\d{47}";
	public static final String LINHA_DIGITAVEL_FORMATADA_REGEX = "\\d{5}\\.\\d{5} \\d{5}\\.\\d{6} \\d{5}\\.\\d{6} \\d{1} \\d{14}";
	/*
	 * Mensagens.
	 */
	private static final String MSG_CODIGO_DE_BARRAS = "Código de barras inválido!";
	private static final String MSG_LINHA_INVALIDA = "Linha digitável inválida.";
	private static final String MSG_STR_VAZIA = "String vazia [ \"%s\" ] tamanho [ %d ].";
	//    private static String MSG_STR_NUMERICA = "String numérica [ \"%s\" ] de tamanho [ %d ] está fora do padrão [ \"ddddddddddddddddddddddddddddddddddddddddddddddd\" ] tamanho = 47.";

	public static String formatarCodigoDeBarras(String codigoBarraNaoFormatada) {
		//23794590300000550262518090000000231900407770
		//23794.59030 00005.502625 18090.000000 2 31900407770
		//23794.59030 00005.502625 18090.000000 2 31900407770
		return codigoBarraNaoFormatada.substring(0, 5) +
			"." + codigoBarraNaoFormatada.substring(5, 10) +
			" " + codigoBarraNaoFormatada.substring(10, 15) +
			"." + codigoBarraNaoFormatada.substring(15, 21) +
			" " + codigoBarraNaoFormatada.substring(21, 26) +
			"." + codigoBarraNaoFormatada.substring(26, 32) +
			" " + codigoBarraNaoFormatada.substring(32, 33) +
			" " + codigoBarraNaoFormatada.substring(33, 44);
	}

	/**
	 * <p>
	 * Retorna o campo livre de uma dada linha digitável.
	 * </p>
	 *
	 * @param linhaDigitavel string no formato FEBRABAN
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @see #checkFormatoLinhaDigitavelFormatada(String)
	 */
	public static String getCampoLivreDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {
		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);

		return new StringBuilder().append(linhaNumerica.substring(4, 9)).append(linhaNumerica.substring(10, 20)).append(linhaNumerica.substring(21, 31))
			.toString();
	}

	public static String getPrimeiroCampoDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);

		return new StringBuilder().append(linhaNumerica.substring(0, 9)).toString();
	}

	public static String getDigitoPrimeiroCampoDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);

		return new StringBuilder().append(linhaNumerica.substring(9, 10)).toString();
	}

	public static String getSegundoCampoLivreDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);

		return new StringBuilder().append(linhaNumerica.substring(10, 20)).toString();
	}

	public static String getDigitoSegundoCampoLivreDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);

		return new StringBuilder().append(linhaNumerica.substring(20, 21)).toString();
	}

	public static String getTerceiroCampoLivreDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);

		return new StringBuilder().append(linhaNumerica.substring(21, 31)).toString();
	}

	public static String getDigitoTerceiroCampoLivreDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);

		return new StringBuilder().append(linhaNumerica.substring(31, 32)).toString();
	}

	public static String getQuartoCampoDigitoVerificadorDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);

		return new StringBuilder().append(linhaNumerica.substring(32, 33)).toString();
	}

	public static String getQuintoCampoLivreDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);

		return new StringBuilder().append(linhaNumerica.substring(33, 37)).toString();
	}

	public static String getValorNominalTituloDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		String linhaNumerica = linhaDigitavelFormatadaEmNumerica(linhaDigitavel);

		return new StringBuilder().append(linhaNumerica.substring(37, 47)).toString();
	}

	/**
	 * <p>
	 * Retorna o valor do título de uma dada linha digitável.
	 * </p>
	 *
	 * @param linhaDigitavel string no formato FEBRABAN
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @see #checkFormatoLinhaDigitavelFormatada(String)
	 */
	public static String getValorDoTituloDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {

		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		return linhaDigitavelFormatadaEmNumerica(linhaDigitavel).substring(37, 47);
	}

	public static BigDecimal getValorBigDecimalDoTituloDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException {
		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		String valorLinha = getValorDoTituloDaLinhaDigitavelFormatada(linhaDigitavel);

		return new BigDecimal(formatValorTitulo(valorLinha));
	}

	/**
	 * <p>
	 * Retorna o fator de vencimento de uma dada linha digitável.
	 * </p>
	 *
	 * @param linhaDigitavel string no formato FEBRABAN
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @see #checkFormatoLinhaDigitavelFormatada(String)
	 */
	public static String getFatorDeVencimentoDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {

		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		return linhaDigitavelFormatadaEmNumerica(linhaDigitavel).substring(33, 37);
	}

	/**
	 * Obter o fator de vencimento da linha digitável.
	 *
	 * @param linha A linha digitável não formatada.
	 * @return O fator de vencimento em {@link Integer}.
	 */
	public static int getFatorDeVencimentoLinhaDigitavelEmInteiro(String linha) {
		String data_boleto = linha.substring(33, 37);
		return Integer.parseInt(data_boleto);
	}

	/**
	 * <p>
	 * Retorna o dígito verificador geral de uma dada linha digitável.
	 * </p>
	 *
	 * @param linhaDigitavel string no formato FEBRABAN
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @see #checkFormatoLinhaDigitavelFormatada(String)
	 */
	public static String getDigitoVerificadorGeralDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {

		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		return linhaDigitavelFormatadaEmNumerica(linhaDigitavel).substring(32, 33);
	}

	/**
	 * <p>
	 * Retorna o código da moeda de uma dada linha digitável.
	 * </p>
	 *
	 * @param linhaDigitavel string no formato FEBRABAN
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @see #checkFormatoLinhaDigitavelFormatada(String)
	 */
	public static String getCodigoDaMoedaDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {

		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		return linhaDigitavelFormatadaEmNumerica(linhaDigitavel).substring(3, 4);
	}

	/**
	 * <p>
	 * Retorna o código do banco de uma dada linha digitável.
	 * </p>
	 *
	 * @param linhaDigitavel string no formato FEBRABAN
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @see #checkFormatoLinhaDigitavelFormatada(String)
	 */
	public static String getCodigoDoBancoDaLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {

		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		return linhaDigitavelFormatadaEmNumerica(linhaDigitavel).substring(0, 3);
	}

	/**
	 * <p>
	 * Retorna o campo livre de um dado código de barras.
	 * </p>
	 *
	 * @param codigoDeBarras string contendo somente números
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws CodigoDeBarrasException  quando a string não está no formato válido
	 * @see #checkFormatoCodigoDeBarras(String)
	 */
	public static String getCampoLivreDoCodigoDeBarras(String codigoDeBarras) throws NullPointerException, IllegalArgumentException,
		CodigoDeBarrasException {

		checkFormatoCodigoDeBarras(codigoDeBarras);

		return StringUtils.trim(codigoDeBarras).substring(19, 44);
	}

	/**
	 * <p>
	 * Retorna o valor do título de um dado código de barras.
	 * </p>
	 *
	 * @param codigoDeBarras string contendo somente números
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws CodigoDeBarrasException  quando a string não está no formato válido
	 * @see #checkFormatoCodigoDeBarras(String)
	 */
	public static String getValorDoTituloDoCodigoDeBarras(String codigoDeBarras) throws NullPointerException, IllegalArgumentException,
		CodigoDeBarrasException {

		checkFormatoCodigoDeBarras(codigoDeBarras);

		return StringUtils.trim(codigoDeBarras).substring(9, 19);
	}

	public static BigDecimal getValorBigDecimalDoTituloDoCodigoDeBarras(String codigoDeBarras) throws NullPointerException, IllegalArgumentException,
		CodigoDeBarrasException {

		checkFormatoCodigoDeBarras(codigoDeBarras);

		String valorBarra = StringUtils.trim(codigoDeBarras).substring(9, 19);

		return new BigDecimal(formatValorTitulo(valorBarra));
	}

	public static String formatValorTitulo(String valor) {
		return valor.substring(0, 8) + "." + valor.substring(8, 10);
	}

	/**
	 * <p>
	 * Retorna o fator de vencimento de um dado código de barras.
	 * </p>
	 *
	 * @param codigoDeBarras string contendo somente números
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws CodigoDeBarrasException  quando a string não está no formato válido
	 * @see #checkFormatoCodigoDeBarras(String)
	 */
	public static String getFatorDeVencimentoDoCodigoDeBarras(String codigoDeBarras) throws NullPointerException, IllegalArgumentException,
		CodigoDeBarrasException {

		checkFormatoCodigoDeBarras(codigoDeBarras);

		return StringUtils.trim(codigoDeBarras).substring(5, 9);
	}

	/**
	 * Obter o fator de vencimento do código de barras.
	 *
	 * @param codigoDeBarras O código de barras.
	 * @return O fator de vencimento em {@link Integer}.
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws CodigoDeBarrasException
	 */
	public static int getFatorDeVencimentoDoCodigoDeBarrasEmInteiro(String codigoDeBarras) throws NullPointerException, IllegalArgumentException,
		CodigoDeBarrasException {

		checkFormatoCodigoDeBarras(codigoDeBarras);

		return Integer.parseInt(StringUtils.trim(codigoDeBarras).substring(5, 9));
	}

	/**
	 * <p>
	 * Retorna o dígito verificador de um dado código de barras.
	 * </p>
	 *
	 * @param codigoDeBarras string contendo somente números
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws CodigoDeBarrasException  quando a string não está no formato válido
	 * @see #checkFormatoCodigoDeBarras(String)
	 */
	public static String getDigitoVerificadorGeralDoCodigoDeBarras(String codigoDeBarras) throws NullPointerException, IllegalArgumentException,
		CodigoDeBarrasException {

		checkFormatoCodigoDeBarras(codigoDeBarras);

		return StringUtils.trim(codigoDeBarras).substring(4, 5);
	}

	/**
	 * <p>
	 * Retorna o código da moeda de um dado código de barras.
	 * </p>
	 *
	 * @param codigoDeBarras string contendo somente números
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws CodigoDeBarrasException  quando a string não está no formato válido
	 * @see #checkFormatoCodigoDeBarras(String)
	 */
	public static String getCodigoDaMoedaDoCodigoDeBarras(String codigoDeBarras) throws NullPointerException, IllegalArgumentException,
		CodigoDeBarrasException {

		checkFormatoCodigoDeBarras(codigoDeBarras);

		return StringUtils.trim(codigoDeBarras).substring(3, 4);
	}

	/**
	 * <p>
	 * Retorna o código do banco de um dado código de barras.
	 * </p>
	 *
	 * @param codigoDeBarras string contendo somente números
	 * @return código do banco em string
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws CodigoDeBarrasException  quando a string não está no formato válido
	 * @see #checkFormatoCodigoDeBarras(String)
	 */
	public static String getCodigoDoBancoDoCodigoDeBarras(String codigoDeBarras) throws NullPointerException, IllegalArgumentException,
		CodigoDeBarrasException {

		checkFormatoCodigoDeBarras(codigoDeBarras);

		return StringUtils.trim(codigoDeBarras).substring(0, 3);
	}

	/**
	 * <p>
	 * Transforma um código de barras em uma linha digitável no formato FEBRABAN.
	 * </p>
	 * <p>
	 * "<strong>dddddddddddddddddddddddddddddddddddddddddddd</strong>" <br />
	 * <b>&rArr;</b> <br />
	 * "<strong>ddddd.ddddd ddddd.dddddd ddddd.dddddd d dddddddddddddd</strong>"
	 * </p>
	 *
	 * @param codigoDeBarras string contendo somente números
	 * @return linha digitável contendo somente números
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws CodigoDeBarrasException  quando a string não está no formato válido
	 * @throws LinhaDigitavelException
	 * @see #checkFormatoCodigoDeBarras(String)
	 */
	public static String codigoDeBarrasEmLinhaDigitavelFormatada(String codigoDeBarras) throws NullPointerException, IllegalArgumentException,
		CodigoDeBarrasException, LinhaDigitavelException {
		return linhaDigitavelNumericaEmFormatada(codigoDeBarrasEmLinhaDigitavelNumerica(codigoDeBarras));
	}

	public static void validarLinhaDigitavelNumerica(String linhaDigitavel) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException, LinhaDigitavelValidateException {
		validarLinhaDigitavelFormatada(BoletoUtil.linhaDigitavelNumericaEmFormatada(linhaDigitavel));
	}

//    public static void main(String[] args) {
//        try {
//            String digitos[] = new String[6];
//            String formatada = BoletoUtil.linhaDigitavelNumericaEmFormatada("00194592705307962234500044034189656640000069800");
//            digitos[0] = BoletoUtil.getDigitoPrimeiroCampoDaLinhaDigitavelFormatada(formatada);
//            digitos[1] = BoletoUtil.getDigitoSegundoCampoLivreDaLinhaDigitavelFormatada(formatada);
//            digitos[2] = BoletoUtil.getDigitoTerceiroCampoLivreDaLinhaDigitavelFormatada(formatada);
//            digitos[3] = BoletoUtil.getQuartoCampoDigitoVerificadorDaLinhaDigitavelFormatada(formatada);
//            digitos[4] = BoletoUtil.getQuintoCampoLivreDaLinhaDigitavelFormatada(formatada);
//            digitos[5] = BoletoUtil.getValorNominalTituloDaLinhaDigitavelFormatada(formatada);
//            for (int i = 0; i < digitos.length; i++) {
//                System.out.println(digitos[i]);
//            }
//
//            Modulo modulo11 = new Modulo(TipoDeModulo.MODULO11);
//
//            //Na verdade estamos transformando em boleto sem o digito verificador geral.
//            String digitoVerificadorGeral = BoletoUtil.getCodigoDoBancoDaLinhaDigitavelFormatada(formatada)
//                    + BoletoUtil.getCodigoDaMoedaDaLinhaDigitavelFormatada(formatada)
//                    + BoletoUtil.getQuintoCampoLivreDaLinhaDigitavelFormatada(formatada)
//                    + BoletoUtil.getValorNominalTituloDaLinhaDigitavelFormatada(formatada)
//                    + BoletoUtil.getPrimeiroCampoDaLinhaDigitavelFormatada(formatada).substring(4)
//                    + BoletoUtil.getSegundoCampoLivreDaLinhaDigitavelFormatada(formatada)
//                    + BoletoUtil.getTerceiroCampoLivreDaLinhaDigitavelFormatada(formatada);
//
//            System.out.println(digitoVerificadorGeral);
//            System.out.println("00196566400000698004592753079622340004403419");
//            System.out.println(modulo11.calcule(digitoVerificadorGeral));
//
//            if (modulo11.calcule(digitoVerificadorGeral) != Integer.parseInt(BoletoUtil.getDigitoVerificadorGeralDaLinhaDigitavelFormatada(formatada))) {
//                throw new LinhaDigitavelValidateException("Dígito verificador geral inválido.");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

	public static void validarLinhaDigitavelFormatada(String formatada) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException, LinhaDigitavelValidateException {
		Modulo modulo10 = new Modulo(TipoDeModulo.MODULO10);

		//00194592705307962234500044034189656640000069800
		//         0          5          96
		//00194592705307952234700044034189157920000069800
		//         0          7          91

		checkFormatoLinhaDigitavelFormatada(formatada);

		String[] digitos = new String[3];

		digitos[0] = BoletoUtil.getDigitoPrimeiroCampoDaLinhaDigitavelFormatada(formatada);
		digitos[1] = BoletoUtil.getDigitoSegundoCampoLivreDaLinhaDigitavelFormatada(formatada);
		digitos[2] = BoletoUtil.getDigitoTerceiroCampoLivreDaLinhaDigitavelFormatada(formatada);

		if (modulo10.calcule(BoletoUtil.getPrimeiroCampoDaLinhaDigitavelFormatada(formatada)) != Integer.parseInt(digitos[0])) {
			throw new LinhaDigitavelValidateException("Dígito verificador do primeiro campo inválido.");
		} else if (modulo10.calcule(BoletoUtil.getSegundoCampoLivreDaLinhaDigitavelFormatada(formatada)) != Integer.parseInt(digitos[1])) {
			throw new LinhaDigitavelValidateException("Dígito verificador do segundo campo inválido.");
		} else if (modulo10.calcule(BoletoUtil.getTerceiroCampoLivreDaLinhaDigitavelFormatada(formatada)) != Integer.parseInt(digitos[2])) {
			throw new LinhaDigitavelValidateException("Dígito verificador do terceiro campo inválido.");
		}

		//Na verdade estamos transformando em boleto sem o digito verificador geral.
		String digitoVerificadorGeral = BoletoUtil.getCodigoDoBancoDaLinhaDigitavelFormatada(formatada)
			+ BoletoUtil.getCodigoDaMoedaDaLinhaDigitavelFormatada(formatada)
			+ BoletoUtil.getQuintoCampoLivreDaLinhaDigitavelFormatada(formatada)
			+ BoletoUtil.getValorNominalTituloDaLinhaDigitavelFormatada(formatada)
			+ BoletoUtil.getPrimeiroCampoDaLinhaDigitavelFormatada(formatada).substring(4)
			+ BoletoUtil.getSegundoCampoLivreDaLinhaDigitavelFormatada(formatada)
			+ BoletoUtil.getTerceiroCampoLivreDaLinhaDigitavelFormatada(formatada);

		Modulo modulo11 = new Modulo(TipoDeModulo.MODULO11);

		if (modulo11.calcule(digitoVerificadorGeral) != Integer.parseInt(BoletoUtil.getDigitoVerificadorGeralDaLinhaDigitavelFormatada(formatada))) {
			throw new LinhaDigitavelValidateException("Dígito verificador geral inválido.");
		}
	}

	/**
	 * <p>
	 * Transforma um código de barras em uma linha digitável numérica.
	 * </p>
	 * <p>
	 * "<strong>dddddddddddddddddddddddddddddddddddddddddddd</strong>" <br />
	 * <b>&rArr;</b> <br />
	 * "<strong>ddddddddddddddddddddddddddddddddddddddddddddddd</strong>"
	 * </p>
	 *
	 * @param codigoDeBarras string contendo somente números
	 * @return linha digitável contendo somente números
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws CodigoDeBarrasException  quando a string não está no formato válido
	 * @see #checkFormatoCodigoDeBarras(String)
	 */
	public static String codigoDeBarrasEmLinhaDigitavelNumerica(String codigoDeBarras) throws NullPointerException, IllegalArgumentException,
		CodigoDeBarrasException {
		checkFormatoCodigoDeBarras(codigoDeBarras);

		final BoletoLinhaDigitavelDV calculadorDV = new BoletoLinhaDigitavelDV();

		final StringBuilder linhaDigitavel = new StringBuilder();

		final String c = StringUtils.trim(codigoDeBarras);

		// campo1
		// banco
		linhaDigitavel.append(c.substring(0, 3));

		// moeda
		linhaDigitavel.append(c.substring(3, 4));
		linhaDigitavel.append(c.substring(19, 24));
		linhaDigitavel.append(calculadorDV.calcule(linhaDigitavel.toString()));

		// campo2
		linhaDigitavel.append(c.substring(24, 34));
		linhaDigitavel.append(calculadorDV.calcule(c.substring(24, 34)));

		// campo3
		linhaDigitavel.append(c.substring(34, 44));
		linhaDigitavel.append(calculadorDV.calcule(c.substring(34, 44)));

		// campo4=DV_Geral
		linhaDigitavel.append(c.substring(4, 5));

		// campo5
		linhaDigitavel.append(c.substring(5, 19));

		return linhaDigitavel.toString();
	}

	/**
	 * <p>
	 * Transforma uma linha digitável no formato FEBRABAN em um código de barras.
	 * </p>
	 * <p>
	 * "<strong>ddddd.ddddd ddddd.dddddd ddddd.dddddd d dddddddddddddd</strong>" <br />
	 * <b>&rArr;</b> <br />
	 * "<strong>dddddddddddddddddddddddddddddddddddddddddddd</strong>"
	 * </p>
	 *
	 * @param linhaDigitavel string no formato FEBRABAN
	 * @return linha digitável contendo somente números
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @see #checkExistsLinhaDigitavel(String)
	 * @see #checkFormatoLinhaDigitavelFormatada(String)
	 */
	public static String linhaDigitavelFormatadaEmCodigoDeBarras(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {

		return linhaDigitavelNumericaEmCodigoDeBarras(linhaDigitavelFormatadaEmNumerica(linhaDigitavel));
	}

	/**
	 * <p>
	 * Transforma uma linha digitável não formatada em um código de barras.
	 * </p>
	 * <p>
	 * "<strong>ddddddddddddddddddddddddddddddddddddddddddddddd</strong>" <br />
	 * <b>&rArr;</b> <br />
	 * "<strong>dddddddddddddddddddddddddddddddddddddddddddd</strong>"
	 * </p>
	 *
	 * @param linhaDigitavel string no formato FEBRABAN
	 * @return linha digitável contendo somente números
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @see #checkExistsLinhaDigitavel(String)
	 * @see #checkFormatoLinhaDigitavelFormatada(String)
	 */
	public static String linhaDigitavelNumericaEmCodigoDeBarras(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {

		checkFormatoLinhaDigitavelNumerica(linhaDigitavel);
		linhaDigitavel = linhaDigitavel.replace(".", "");

		final StringBuilder codigoDeBarras = new StringBuilder();

		final String l = StringUtils.trim(linhaDigitavel);

		// banco
		codigoDeBarras.append(l.substring(0, 3));
		// moeda
		codigoDeBarras.append(l.substring(3, 4));

		codigoDeBarras.append(l.substring(32, 33));
		codigoDeBarras.append(l.substring(33, 47));
		codigoDeBarras.append(l.substring(4, 9));
		codigoDeBarras.append(l.substring(10, 20));
		codigoDeBarras.append(l.substring(21, 31));

		return codigoDeBarras.toString();
	}

	/**
	 * <p>
	 * Remove formatação e espaços de uma linha digitável no formato FEBRABAN:
	 * </p>
	 * <p>
	 * "<strong>ddddd.ddddd ddddd.dddddd ddddd.dddddd d dddddddddddddd</strong>" <br />
	 * <b>&rArr;</b> <br />
	 * "<strong>ddddddddddddddddddddddddddddddddddddddddddddddd</strong>"
	 * </p>
	 *
	 * @param linhaDigitavel string no formato FEBRABAN
	 * @return linha digitável contendo somente números
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @see #checkExistsLinhaDigitavel(String)
	 * @see #checkFormatoLinhaDigitavelFormatada(String)
	 */
	public static String linhaDigitavelFormatadaEmNumerica(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {

		checkFormatoLinhaDigitavelFormatada(linhaDigitavel);

		return linhaDigitavel.replaceAll(" ", StringUtils.EMPTY).replaceAll("\\.", StringUtils.EMPTY);
	}

	/**
	 * <p>
	 * Transforma linha digitável númerica em FEBRABAN:
	 * </p>
	 * <p>
	 * "<strong>ddddddddddddddddddddddddddddddddddddddddddddddd</strong>" <br />
	 * <b>&rArr;</b> <br />
	 * "<strong>ddddd.ddddd ddddd.dddddd ddddd.dddddd d dddddddddddddd</strong>"
	 * </p>
	 *
	 * @param linhaDigitavel string no formato FEBRABAN
	 * @return linha digitável contendo somente números
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @see #checkExistsLinhaDigitavel(String)
	 * @see #checkFormatoLinhaDigitavelNumerica(String)
	 */
	public static String linhaDigitavelNumericaEmFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {

		checkFormatoLinhaDigitavelNumerica(linhaDigitavel);

		final StringBuilder linhaFormatada = new StringBuilder();

		final String l = StringUtils.trim(linhaDigitavel.replace(".", ""));

		linhaFormatada.append(l.substring(0, 5));
		linhaFormatada.append(".");
		linhaFormatada.append(l.substring(5, 10));
		linhaFormatada.append(" ");
		linhaFormatada.append(l.substring(10, 15));
		linhaFormatada.append(".");
		linhaFormatada.append(l.substring(15, 21));
		linhaFormatada.append(" ");
		linhaFormatada.append(l.substring(21, 26));
		linhaFormatada.append(".");
		linhaFormatada.append(l.substring(26, 32));
		linhaFormatada.append(" ");
		linhaFormatada.append(l.substring(32, 33));
		linhaFormatada.append(" ");
		linhaFormatada.append(l.substring(33));

		return linhaFormatada.toString();
	}

	/**
	 * <p>
	 * Informa se um dado código de barras é válido ou não.
	 * </p>
	 *
	 * @param codigoDeBarras
	 * @return true = valida
	 */
	public static boolean isCodigoDeBarrasValido(String codigoDeBarras) {
		if (StringUtils.isNotBlank(codigoDeBarras)) {
			return codigoDeBarras.trim().matches(CODIGO_DE_BARRAS_REGEX);
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * <p>
	 * Informa se uma dada linha digitável formatada é válida ou não.
	 * </p>
	 *
	 * @param linhaDigitavel
	 * @return true = valida
	 */
	public static boolean isLinhaDigitavelFormatadaValida(String linhaDigitavel) {
		if (StringUtils.isNotBlank(linhaDigitavel)) {
			return linhaDigitavel.trim().matches(LINHA_DIGITAVEL_FORMATADA_REGEX);
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * <p>
	 * Informa se uma dada linha digitável é válida ou não.
	 * </p>
	 *
	 * @param linhaDigitavel
	 * @return true = valida
	 */
	public static boolean isLinhaDigitavelNumericaValida(String linhaDigitavel) {
		if (StringUtils.isNotBlank(linhaDigitavel)) {
			linhaDigitavel = linhaDigitavel.replace(".", "");
			return linhaDigitavel.trim().matches(LINHA_DIGITAVEL_NUMERICA_REGEX);
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * <p>
	 * Verifica se a linha digitável <strong>não é nula</strong>, <strong>não é vazia</strong> e <strong>é
	 * numérica</strong>, obedecendo o seguinte FEBRABAN:
	 * </p>
	 * <p>
	 * "<strong>ddddd.ddddd ddddd.dddddd ddddd.dddddd d dddddddddddddd</strong>"
	 * </p>
	 * <p>
	 * Onde o número de dígitos é igual a 54 e o número de espaços é igual a 4.
	 * </p>
	 * <p>
	 * <ul>
	 * <li>Caso a string seja nula, lança uma <code>NullPointerException</code>;</li>
	 * <li>Caso seja vazia, lança uma <code>IllegalArgumentException</code>;</li>
	 * <li>Caso não esteja no formato especificado, lança uma <code>LinhaDigitavelException</code>.</li>
	 * </ul>
	 * </p>
	 *
	 * @param linhaDigitavel string no formato FEBRABAN
	 * @return linha digitável contendo somente números
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
	 * @see #checkExistsLinhaDigitavel(String)
	 */
	public static void checkFormatoLinhaDigitavelFormatada(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {
		checkExistsLinhaDigitavel(linhaDigitavel);

		String MSG_NAO_FORMATADA = "String formatada [ \"%s\" ] de tamanho [ %d ] está fora do padrão [ \"ddddd.ddddd ddddd.dddddd ddddd.dddddd d dddddddddddddd\" ] tamanho = 54.";
		if (!linhaDigitavel.contains(".")) {
			throw new LinhaDigitavelException(MSG_LINHA_INVALIDA + " " + String.format(MSG_NAO_FORMATADA, linhaDigitavel, linhaDigitavel.length())
				+ " A linha digitável formatada deve conter pontos!");
		}

		if (!linhaDigitavel.trim().contains(" ")) {
			throw new LinhaDigitavelException(MSG_LINHA_INVALIDA + " " + String.format(MSG_NAO_FORMATADA, linhaDigitavel, linhaDigitavel.length())
				+ " A linha digitável formatada deve conter espaços!");
		}

		if (!isLinhaDigitavelFormatadaValida(linhaDigitavel)) {
			throw new LinhaDigitavelException(MSG_LINHA_INVALIDA + " " + String.format(MSG_NAO_FORMATADA, linhaDigitavel, linhaDigitavel.length()));
		}

	}

	/**
	 * <p>
	 * Verifica se a linha digitável <strong>não é nula</strong>, <strong>não é vazia</strong> e <strong>é
	 * numérica</strong>, obedecendo o seguinte formato:
	 * </p>
	 * <p>
	 * "<strong>ddddddddddddddddddddddddddddddddddddddddddddddd</strong>"
	 * </p>
	 * <p>
	 * Onde o número de dígitos é igual a 47.
	 * </p>
	 * <p>
	 * <ul>
	 * <li>Caso a string seja nula, lança uma <code>NullPointerException</code>;</li>
	 * <li>Caso seja vazia, lança uma <code>IllegalArgumentException</code>;</li>
	 * <li>Caso não esteja no formato especificado, lança uma <code>LinhaDigitavelException</code>.</li>
	 * </ul>
	 * </p>
	 *
	 * @param linhaDigitavel
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws LinhaDigitavelException  quando a string não está no formato válido
	 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
	 * @see #checkExistsLinhaDigitavel(String)
	 */
	public static void checkFormatoLinhaDigitavelNumerica(String linhaDigitavel) throws NullPointerException, IllegalArgumentException,
		LinhaDigitavelException {
		checkExistsLinhaDigitavel(linhaDigitavel);
		linhaDigitavel = linhaDigitavel.replace("_", "");
		linhaDigitavel = linhaDigitavel.replace(".", "");
		if (!isLinhaDigitavelNumericaValida(linhaDigitavel)) {
			throw new LinhaDigitavelException(MSG_LINHA_INVALIDA + " A linha digitável deve conter apenas 47 dígitos númericos (0-9)!");
		}

		for (int x = 0; x < 10; x++) {
			if (linhaDigitavel.equals(TesourariaUtil.obterValoresRepetidos(String.valueOf(x), 47))) {
				throw new LinhaDigitavelException("Linha digitável ou código de barras inválido.");
			}
		}
	}

	/**
	 * <p>
	 * Verifica se o código de barras <strong>não é nulo</strong>, <strong>não é vazio</strong> e <strong>é
	 * numérico</strong>, obedecendo o seguinte formato:
	 * </p>
	 * <p>
	 * "<strong>dddddddddddddddddddddddddddddddddddddddddddd</strong>"
	 * </p>
	 * <p>
	 * Onde o número de dígitos é igual a 44.
	 * </p>
	 * <p>
	 * <ul>
	 * <li>Caso a string seja nula, lança uma <code>NullPointerException</code>;</li>
	 * <li>Caso seja vazia, lança uma <code>IllegalArgumentException</code>;</li>
	 * <li>Caso não esteja no formato especificado, lança uma <code>LinhaDigitavelException</code>.</li>
	 * </ul>
	 * </p>
	 *
	 * @param codigoDeBarras
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @throws CodigoDeBarrasException  quando a string não está no formato válido
	 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
	 * @see #checkExistsLinhaDigitavel(String)
	 */
	public static void checkFormatoCodigoDeBarras(String codigoDeBarras) throws NullPointerException, IllegalArgumentException, CodigoDeBarrasException {
		checkExistsCodigoDeBarras(codigoDeBarras);
		codigoDeBarras = codigoDeBarras.replace("_", "");
		if (!isCodigoDeBarrasValido(codigoDeBarras)) {
			throw new CodigoDeBarrasException(MSG_CODIGO_DE_BARRAS + " O código de barras deve conter apenas 44 dígitos númericos (0-9)!");
		}

		for (int x = 0; x < 10; x++) {
			if (codigoDeBarras.equals(TesourariaUtil.obterValoresRepetidos(String.valueOf(x), 47))) {
				String PARA_BOLET_NEC_INFOR_LINHA_DIGIT_OU_O_NUM_BOLETO = "Para boleto é necessário informar a linha digitável ou o número do boleto.";
				throw new CodigoDeBarrasException(PARA_BOLET_NEC_INFOR_LINHA_DIGIT_OU_O_NUM_BOLETO);
			}
		}
	}

	/**
	 * <p>
	 * Verifica se a linha digitável <strong>não é nula</strong> e <strong>não é vazia</strong>. Caso nula lança uma
	 * <code>NullPointerException</code>, caso vazia lança uma <code>IllegalArgumentException</code>.
	 * </p>
	 * <p>
	 * Considera-se vazia se <code>linhaDigitavel.trim().length()</code> == 0.
	 * </p>
	 *
	 * @param linhaDigitavel
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
	 */
	public static void checkExistsLinhaDigitavel(String linhaDigitavel) throws NullPointerException, IllegalArgumentException {
		if (linhaDigitavel == null) {
			throw new IllegalArgumentException(MSG_LINHA_INVALIDA);
		}

		if (StringUtils.isBlank(linhaDigitavel)) {
			throw new IllegalArgumentException(MSG_LINHA_INVALIDA + " " + String.format(MSG_STR_VAZIA, linhaDigitavel, linhaDigitavel.length()));
		}
	}

	/**
	 * <p>
	 * Verifica se código de barras <strong>não é nulo</strong> e <strong>não é vazio</strong>. Caso nula lança uma
	 * <code>NullPointerException</code>, caso vazia lança uma <code>IllegalArgumentException</code>.
	 * </p>
	 * <p>
	 * Considera-se vazia se <code>linhaDigitavel.trim().length()</code> == 0.
	 * </p>
	 *
	 * @param codigoDeBarras
	 * @throws NullPointerException     quando a string é nula
	 * @throws IllegalArgumentException quando a string é vazia
	 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
	 */
	public static void checkExistsCodigoDeBarras(String codigoDeBarras) throws NullPointerException, IllegalArgumentException {
		if (codigoDeBarras == null) {
			throw new IllegalArgumentException(MSG_LINHA_INVALIDA);
		}
		if (StringUtils.isBlank(codigoDeBarras)) {
			throw new IllegalArgumentException(MSG_CODIGO_DE_BARRAS + " " + String.format(MSG_STR_VAZIA, codigoDeBarras, codigoDeBarras.length()));
		}
	}

	/**
	 * Obter a data de vencimento de uma linha digitável.
	 *
	 * @param linhaDigitavel A linha digitável.
	 * @return A data de vencimento da linha digitável, ou {@code null} caso não haja data de vencimento.
	 * @throws ParseException
	 */
	public static Date getDataVencimentoLinhaDigitavel(String linhaDigitavel) throws ParseException {
		int fator = getFatorDeVencimentoLinhaDigitavelEmInteiro(linhaDigitavel);
		return BoletoUtil.getDataVencimento(fator);
	}

	/**
	 * Obter a data de vencimento do código de barras.
	 *
	 * @param codigoBarras O código de barras.
	 * @return A data de vencimento do código de barras, ou {@code null} caso não haja data de vencimento.
	 * @throws ParseException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws CodigoDeBarrasException
	 */
	public static Date getDataVencimentoCodigoBarras(String codigoBarras) throws ParseException, NullPointerException, IllegalArgumentException, CodigoDeBarrasException {
		int fator = getFatorDeVencimentoDoCodigoDeBarrasEmInteiro(codigoBarras);
		return BoletoUtil.getDataVencimento(fator);
	}

	/**
	 * Obter a data de vencimento a partir do fator de vencimento.
	 *
	 * @param fator O fator de vencimento.
	 * @return A data de vencimento.
	 * @throws ParseException
	 */
	private static Date getDataVencimento(int fator) throws ParseException {
		if (fator == 0) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date database = dateFormat.parse(DATA_BASE_FEBRABAN);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(database);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + fator);
		return calendar.getTime();
	}

	/**
	 * <h5 id="LinhaDigitável">Linha Digitável</h5>
	 * <p>
	 * Os dados da linha digitável representam o conteúdo do código de barras, dispostos em outra ordem e acrescidos de
	 * dígitos verificadores nos três primeiros campos.
	 * </p>
	 * <p>
	 * Deve ser utilizada quando há a impossibilidade da captura do código de barras e/ou para pagamentos em terminais
	 * de auto-atendimento, Internet, home/office bank, personal bank, etc.
	 * </p>
	 * <p>
	 * Abaixo, a composição da linha digitável:
	 * </p>
	 * <table class="wiki">
	 * <tr>
	 * <td><strong>Campo</strong></td>
	 * <td><strong>Posição</strong></td>
	 * <td><strong>Tamanho</strong></td>
	 * <td><strong>Conteúdo</strong></td>
	 * </tr>
	 * <tr>
	 * <td>1º</td>
	 * <td>01-03</td>
	 * <td>3</td>
	 * <td>Identificação do banco (posições 1 a 3 do código de barras)</td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td>04-04</td>
	 * <td>1</td>
	 * <td>Código de moeda (9 - Real) (posição 4 do código de barras)</td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td>05-09</td>
	 * <td>5</td>
	 * <td>1ª a 5ª posições do campo livre (posições 20 a 24 do código de barras)</td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td>10-10</td>
	 * <td>1</td>
	 * <td>Dígito verificador do primeiro campo</td>
	 * </tr>
	 * <tr>
	 * <td>2º</td>
	 * <td>11-20</td>
	 * <td>10</td>
	 * <td>6ª a 15ª posições do campo livre (posições 25 a 34 do código de barras)</td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td>21-21</td>
	 * <td>1</td>
	 * <td>Dígito verificador do segundo campo</td>
	 * </tr>
	 * <tr>
	 * <td>3º</td>
	 * <td>22-31</td>
	 * <td>10</td>
	 * <td>16ª a 25ª posições do campo livre (posições 35 a 44 do código de barras)</td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td>32-32</td>
	 * <td>1</td>
	 * <td>Dígito verificador do terceiro campo</td>
	 * </tr>
	 * <tr>
	 * <td>4º</td>
	 * <td>33-33</td>
	 * <td>1</td>
	 * <td>Dígito verificador geral (posição 5 do código de barras)</td>
	 * </tr>
	 * <tr>
	 * <td>5º</td>
	 * <td>34-37</td>
	 * <td>4</td>
	 * <td>Fator de vencimento (posições 6 a 9 do código de barras)</td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td>38-47</td>
	 * <td>10</td>
	 * <td>Valor nominal do título(posições 10 a 19 do código de barras)</td>
	 * </tr>
	 * </table>
	 * <p>
	 * <strong>OBS.:</strong>
	 * </p>
	 * <ol class="loweralpha">
	 * <li>Os dados da linha digitável não se apresentam na mesma ordem do código de barras;</li>
	 * <li>Os dígitos verificadores referentes aos campos 1, 2 e 3 não são representados no código de barras;</li>
	 * <li>Em cada um dos três primeiros campos, após a 5ª posição, deve ser inserido um ponto ".", a fim de facilitar a
	 * visualização da linha digitável;</li>
	 * <li>O ponto não conta como posição na hora de se digitar os números para o pagamento do boleto, ou seja, não deve
	 * ser digitado;</li>
	 * <li>Detalhes sobre o 5º campo da linha digitável:
	 * <ul>
	 * <li>Preenchimento com zeros entre o fator de vencimento e o valor até completar 14 posições. A existência de
	 * "0000" no fator de vencimento da linha digitável do boleto bancário é um indicativo de que o código de barras não
	 * contém fator de vencimento. Isso impede que o banco identifique automaticamente se o boleto está ou não vencido,
	 * logo nesse caso o banco acolhedor / recebedor estará isento das responsabilidades pelo recebimento após o
	 * vencimento;</li>
	 * <li>Quando se tratar de boletos sem discriminação do valor nominal do título no código de barras, a sua
	 * representação deverá ser feita com zeros;</li>
	 * <li>Não deverá conter separação por pontos, vírgulas ou espaços.</li>
	 * </ul>
	 * </li>
	 * </ol>
	 * <p>
	 * Para se calcular o dígito verificador de cada um dos três primeiros campos, deve-se utilizar o módulo 10, da
	 * seguinte maneira:
	 * </p>
	 * <ol>
	 * <li>Multiplique cada dígito de cada campo, iniciando-se da direita para a esquerda, pela seqüência de 2, 1, 2, 1,
	 * 2,...;</li>
	 * <li>Some individualmente os algarismos dos resultados, obtendo-se um total "X";</li>
	 * <li>Divida o valor "X" por 10 (Y = X/10) e determine o resto da divisão (R);</li>
	 * <li>Calcule o DV (Dígito Verificador) através da expressão: DV = 10 – R.</li>
	 * </ol>
	 * <p>
	 * Observação: Caso o resto da divisão (R) seja "0" (zero), ao dígito verificador (DV) deverá ser atribuído o valor
	 * "0" (zero).
	 * </p>
	 *
	 * @return
	 */
	public boolean validarLinhaDigitavel() {
		return Boolean.FALSE;
	}

	// Uso para testes (Daniel).
//    public static void main(String[] args) throws NullPointerException, IllegalArgumentException, LinhaDigitavelException, CodigoDeBarrasException, ParseException {
//        String linha = "00194592705307952234700044034189100000000069800"; //5792
//        String barra = BoletoUtil.linhaDigitavelNumericaEmCodigoDeBarras(linha);
//        System.out.println(linha);
//        System.out.println(barra);
//
//        System.out.println(BoletoUtil.getValorBigDecimalDoTituloDaLinhaDigitavelFormatada(BoletoUtil.linhaDigitavelNumericaEmFormatada(linha)));
//        System.out.println(BoletoUtil.getValorBigDecimalDoTituloDoCodigoDeBarras(barra));
//
//        System.out.println(BoletoUtil.getFatorDeVencimentoDaLinhaDigitavelFormatada(BoletoUtil.linhaDigitavelNumericaEmFormatada(linha)));
//
//        System.out.println(BoletoUtil.getDataVencimentoLinhaDigitavel(linha));
//        System.out.println(BoletoUtil.getDataVencimentoCodigoBarras(barra));
//    }
}

