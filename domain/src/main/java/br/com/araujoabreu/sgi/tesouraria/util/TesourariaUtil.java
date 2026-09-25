package br.com.araujoabreu.sgi.tesouraria.util;

import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacao;
import br.com.araujoabreu.sgi.tesouraria.domain.RelacaoBancaria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class TesourariaUtil {

	private static final Log LOGGER = LogFactory.getLog(TesourariaUtil.class);

	private static TesourariaUtil instance;

	private TesourariaUtil() {
	}

	public static TesourariaUtil getInstance() {
		if (TesourariaUtil.instance == null) {
			TesourariaUtil.instance = new TesourariaUtil();
		}

		return TesourariaUtil.instance;
	}

	/**
	 * Obter repetição do valor informado.
	 *
	 * @param valor     Valor a ser repedido.
	 * @param repeticao Quantidade de repetição.
	 * @return O valor repedido.
	 */
	public static String obterValoresRepetidos(String valor, int repeticao) {
		String result = "";
		for (int x = 0; x < repeticao; x++) {
			result += valor;
		}
		return result;
	}

	/**
	 * Gera arquivo de saída para a VAN de todos os {@link ItemRelacao} da {@link RelacaoBancaria}.
	 *
	 * @param text O texto a ser inserido no arquivo.
	 * @param path O caminho onde será gravado o arquivo.
	 * @throws IOException
	 */
	public void gerarArquivoSaida(final String fileName, final String text, final String path) throws IOException {
		File filePath = new File(path);

		if (!filePath.exists()) {
			if (filePath.mkdirs()) {
				LOGGER.info("Diretórios criados com sucesso.");
			} else {
				LOGGER.error("Não foi possível criar diretórios para os arquivos.");
				System.err.println("Não foi possível criar diretórios para os arquivos.");
				throw new IOException("Erro ao criar diretórios.");
			}
		}

		File fileSaida = new File(path, fileName);

//		FileWriter writer = null;
//
//		try {
//			writer = new FileWriter(fileSaida);
//		} catch (IOException e) {
//			LOGGER.error("Erro ao criar arquivo.", e);
//			e.printStackTrace();
//			throw new IOException("Erro ao criar arquivo.", e);
//		}

		PrintWriter printWriter = new PrintWriter(fileSaida, "ISO-8859-1");
		printWriter.print(text);
		printWriter.close();
	}

	/**
	 * Responsável por mover arquivo.<br/>
	 *
	 * @param file    O arquivo a ser movido.
	 * @param destino O destino do arquivo.
	 * @param delete  Deletar o arquivo após move-lo.
	 * @return O arquivo com o novo caminho.
	 */
	public File moverArquivo(File file, String destino, boolean delete) {
		InputStream inStream = null;
		OutputStream outStream = null;

		// Usar separador do sistema nativo.
		destino = destino.replace("/", System.getProperty("file.separator"));
		destino = destino.replace("\\", System.getProperty("file.separator"));

		File bfile = new File(destino + System.getProperty("file.separator") + file.getName());

		try {

			inStream = new FileInputStream(file);
			outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;

			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}

			inStream.close();
			outStream.close();

			if (delete) {
				if (!file.delete()) {
					LOGGER.error("Não foi possível deletar o arquivo");
				}
			}

		} catch (IOException e) {
			LOGGER.error(e);
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
		}

		return bfile;
	}

	/**
	 * Retorna a extensão de um arquivo.
	 *
	 * @param file O arquivo para obtenção da extensão.
	 * @return A extensão do arquivos. Caso o não seja possível
	 * obter a extensão, será retornado uma string vazia.
	 */
	public String obterExtensaoArquivo(File file) {
		String extension = "";

		int i = file.getName().lastIndexOf('.');

		if (i > 0) {
			extension = file.getName().substring(i + 1);
		}

		return extension;
	}

}
