package br.com.fasolti.commons;

import br.com.fasolti.commons.lang.DownloadException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// TODO Organizar
public final class Util {

	@Autowired(required = false)
	private static Logger logger;

	/**
	 * Para utilizar este método deve ser utilizado o caminho completo para o enum, por exemplo:
	 * br.com.araujoabreu.sgi.faturamento.domain.Faturamento$Status
	 * O enum a ser utilizado deve sobrescrever o método toString, pois o mesmo também será
	 * chamado no conversor (EnumConverter) para traduzir o valor do TO para a Entidade.
	 */
	public static String[] getEnumValues(String nomeCompletoEnum) {
		try {
			Class<?> c = Class.forName(nomeCompletoEnum);
			if (c.isEnum()) {
				String[] valores = new String[c.getEnumConstants().length];
				int i = 0;
				for (Object valor : c.getEnumConstants())
					valores[i++] = valor.toString();

				return valores;
			} else
				return null;

		} catch (ClassNotFoundException x) {
			return null;
		}
	}

	/**
	 * Obter o enum referente ao toString do mesmo. Esse método compara usando o equalsIgnoreCase.
	 *
	 * @param enumClass  O classe do enum.
	 * @param enumString O toString do emun ao qual será usado para converter ao enum referente.
	 * @param <T>        O tipo do enum (Class).
	 * @return O enum referente ao toString do mesmo.
	 */
	public static <T extends Enum<?>> Enum<?> getEnumByString(Class<T> enumClass, String enumString) {
		T[] constants = enumClass.getEnumConstants();
		T obj = null;
		for (int i = 0; i < constants.length; i++) {
			obj = constants[i];
			if (obj.toString().equalsIgnoreCase(enumString)) {
				break;
			}
		}
		return obj;
	}


//	public static void download(File file, String contentType, String fileName, final HttpServletRequest request, final HttpServletResponse response) throws ValidationException {
//		try {
//
//			if (file.exists()) {
//				if (Check.isEmpty(contentType)) {
//					contentType = "application/octet-stream";
//				}
//
//				if (Check.isEmpty(fileName)) {
//					fileName = file.getName();
//				}
//
//				InputStream fileInputStream = new FileInputStream(file);
//				OutputStream output = response.getOutputStream();
//
//				response.reset();
//
//				response.setContentType(contentType);
//				response.setContentLength((int) (file.length()));
//
//				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//
//				IOUtils.copyLarge(fileInputStream, output);
//				output.flush();
//			} else {
//				throw new ValidationException("Arquivo não encontrado");
//			}
//
//		} catch (Exception e) {
//			throw new ValidationException(e.getCause());
//		}
//	}
//
//	public static void download(File file, String contentType, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
//		download(file, contentType, null, request, response);
//	}
//
//	public static void download(File file, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
//		download(file, null, null, request, response);
//	}

	/**
	 * Separa string em uma lista.
	 *
	 * @param string    A string a ser separada.
	 * @param separator O separador.
	 * @return List com a string separada.
	 */
	public static List<String> stringTokenizer(String string, String separator) {
		List<String> strings = new ArrayList<String>();

		StringTokenizer tokens = new StringTokenizer(string, separator);
		int value = tokens.countTokens();
		if (value > 0) {
			for (int x = 0; x < value; x++) {
				String token = tokens.nextToken();
				strings.add(token);
			}
		}

		return strings;
	}

	/**
	 * Separa a string pelo separado ";".
	 *
	 * @param string A string a ser separada.
	 * @return Lista de strings.
	 */
	public static List<String> stringTokenizer(String string) {
		return stringTokenizer(string, Constants.SEMICOLON);
	}

	/**
	 * Data agora.
	 *
	 * @return A data de "agora".
	 */
	public static Date dateNow() {
		return GregorianCalendar.getInstance().getTime();
	}

	/**
	 * Retorna uma nova data com os parametros informados.
	 *
	 * @param field Campos do Calendar.
	 * @param value Valor a ser modificado da data.
	 * @return A data modificada.
	 */
	public static Date dateAdd(int field, int value) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(field, value);
		return calendar.getTime();
	}

	/**
	 * Retorna uma nova data com os parametros informados.
	 *
	 * @param date  A data a ser modificada.
	 * @param field Campos do Calendar.
	 * @param value Valor a ser modificado da data.
	 * @return A data modificada.
	 */
	public static Date dateAdd(Date date, int field, int value) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, value);
		return calendar.getTime();
	}

	/**
	 * Data atual, modificada com o ano -1.
	 *
	 * @return A data atual com o ano passado.
	 */
	public static Date dateYesterday() {
		return dateAdd(Calendar.DATE, -1);
	}

	/**
	 * Data atual em string.
	 *
	 * @return A data atual em dd/MM/yyyy HH:mm:ss.
	 */
	public static String dateStringNow() {
		return new SimpleDateFormat(Constants.DATE_FORMAT_FULL).format(dateNow());
	}

	/**
	 * Converte a data em string.
	 *
	 * @param date A data a ser transformada para string.
	 * @return A data em dd/MM/yyyy HH:mm:ss.
	 */
	public static String format(Date date) {
		return new SimpleDateFormat(Constants.DATE_FORMAT_FULL).format(date);
	}

	/**
	 * Converte o nome de uma classe em nome de variável padrão, "descapitalizando" a primeira letra
	 */
	public static String varName(Class clazz) {
		String className = clazz.getSimpleName();
		char c[] = className.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	}

	/**
	 * Envia o arquivo como anexo (download) na resposta HTTP.
	 */
	public static void download(File arquivo, String contentType, HttpServletRequest request,
		HttpServletResponse response) throws DownloadException {
		if (arquivo == null || !arquivo.isFile()) {
			throw new DownloadException("Arquivo não encontrado para download");
		}
		try (InputStream in = new FileInputStream(arquivo)) {
			String nome = URLEncoder.encode(arquivo.getName(), "UTF-8").replace("+", "%20");
			response.setContentType(contentType);
			response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + nome);
			response.setContentLengthLong(arquivo.length());
			OutputStream out = response.getOutputStream();
			byte[] buffer = new byte[8192];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			out.flush();
		} catch (IOException e) {
			throw new DownloadException("Falha ao enviar o arquivo " + arquivo.getName(), e);
		}
	}

	/**
	 * Compacta os arquivos em um ZIP no caminho indicado por <code>destino</code>.
	 */
	public static File compactarArquivos(List<FileBean> arquivos, FileBean destino) throws IOException {
		File zip = new File(destino.getCaminhoCompleto());
		try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip))) {
			byte[] buffer = new byte[8192];
			for (FileBean bean : arquivos) {
				File arquivo = new File(bean.getCaminhoCompleto());
				out.putNextEntry(new ZipEntry(bean.getNomeCompleto()));
				try (InputStream in = new FileInputStream(arquivo)) {
					int read;
					while ((read = in.read(buffer)) != -1) {
						out.write(buffer, 0, read);
					}
				}
				out.closeEntry();
			}
		}
		return zip;
	}

	/**
	 * Compara duas datas considerando apenas o dia (ignora a hora).
	 *
	 * @return negativo se <code>a</code> for anterior a <code>b</code>, zero se forem o mesmo dia, positivo caso contrário.
	 */
	public static int comparaDatas(Date a, Date b) {
		return toLocalDate(a).compareTo(toLocalDate(b));
	}

	private static java.time.LocalDate toLocalDate(Date date) {
		return new java.sql.Date(date.getTime()).toLocalDate();
	}

	/**
	 * Formata o número com zeros à esquerda até o tamanho informado.
	 */
	public static String formataNumero(int numero, int tamanho) {
		return String.format("%0" + tamanho + "d", numero);
	}

	public static String formataNumero(Integer numero, int tamanho) {
		return formataNumero(numero.intValue(), tamanho);
	}

	public static String converteDateEmString(Date data, String formato) {
		return new SimpleDateFormat(formato).format(data);
	}

	/**
	 * Valor monetário no formato do local "pt_BR".
	 */
	public static String formatarValor(BigDecimal valor) {
		return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor.doubleValue());
	}
}
