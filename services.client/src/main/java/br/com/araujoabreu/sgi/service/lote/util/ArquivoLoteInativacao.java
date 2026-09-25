package br.com.araujoabreu.sgi.service.lote.util;

import br.com.araujoabreu.sgi.lang.AdministracaoException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArquivoLoteInativacao {

	private final boolean valido;
	private final String nomeArquivo;
	private final String tipoArquivo;
	private final Path file;
	private final List<Registro> registros;
	private final List<Registro> registrosInvalidos;

	private ArquivoLoteInativacao(
		boolean valido,
		Path arquivo,
		String nomeArquivo,
		String tipoArquivo,
		List<Registro> registros,
		List<Registro> registrosInvalidos) {
		this.valido = valido;
		this.registros = Collections.unmodifiableList(registros);
		this.file = arquivo;
		this.nomeArquivo = nomeArquivo;
		this.tipoArquivo = tipoArquivo;
		this.registrosInvalidos = Collections.unmodifiableList(registrosInvalidos);
	}

	public static ArquivoLoteInativacao abrir(Path arquivoImportacao) throws AdministracaoException {
		return abrir(arquivoImportacao, null, null);
	}

	public static ArquivoLoteInativacao abrir(Path arquivoImportacao, String nomeArquivoExibicao, String tipoArquivo) throws AdministracaoException {
		boolean arquivoValido = true;
		List<Registro> registros = new ArrayList<>();
		List<Registro> registrosInvalidos = new ArrayList<>();

		if (nomeArquivoExibicao == null || nomeArquivoExibicao.isEmpty()) {
			nomeArquivoExibicao = arquivoImportacao.getFileName().toString();
		}

		if (tipoArquivo == null || tipoArquivo.isEmpty()) {
			tipoArquivo = URLConnection.guessContentTypeFromName(arquivoImportacao.getFileName().toString());
			if (tipoArquivo == null || tipoArquivo.isEmpty()) {
				tipoArquivo = "application/octet-stream";
			}
		}

		try {
			BufferedReader in = new BufferedReader(new FileReader(arquivoImportacao.toFile()));
			String linha = "";
			int numeroLinha = 1;

			while ((linha = in.readLine()) != null) {
				if (linha.isEmpty() || linha.matches("\\s+")) continue;

				Registro registro = new Registro(linha, numeroLinha);
				registros.add(registro);

				if (!registro.isValido()) {
					registrosInvalidos.add(registro);
				}

				numeroLinha++;
			}

			in.close();

		} catch (FileNotFoundException e) {
			String mensagem = "Arquivo não encontrado.";
			throw new AdministracaoException(mensagem, e);

		} catch (IOException e) {
			String mensagem = "Erro na leitura do arquivo: " + e.getMessage();
			throw new AdministracaoException(mensagem, e);
		}

		if (arquivoValido && (registros.isEmpty() || !registrosInvalidos.isEmpty())) {
			// NOTE: Arquivos vazios não são válidos.
			arquivoValido = false;
		}

		return new ArquivoLoteInativacao(arquivoValido, arquivoImportacao, nomeArquivoExibicao, tipoArquivo, registros, registrosInvalidos);
	}

	public boolean isValido() {
		return valido;
	}

	public List<TipoErro> getErros() {
		return registrosInvalidos.stream().flatMap(o -> o.getErros().stream()).collect(Collectors.toList());
	}

	public Optional<TipoErro> getPrimeiroErro() {
		return getErros().stream().findFirst();
	}

	public boolean isVazio() {
		return registros.isEmpty();
	}

	public boolean isCpfNaoNumerico() {
		for (Registro registro : registrosInvalidos) {
			if (registro.isCpfNaoNumerico()) return true;
		}
		return false;
	}

	public boolean isNomeVazio() {
		for (Registro registro : registrosInvalidos) {
			if (registro.isNomeVazio()) return true;
		}
		return false;
	}

	public Path getCaminhoArquivo() {
		return file;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public List<Registro> getRegistros() {
		return registros;
	}

	public List<Registro> getRegistrosInvalidos() {
		return registrosInvalidos;
	}

	public static enum TipoErro {
		ARQUIVO_VAZIO,
		FORMATO_LINHA_INVALIDO,
		CPF_NAO_NUMERICO,
		NOME_VAZIO;

		public String getName() {
			return name();
		}
	}

	public static class Registro {
		private final Integer numeroLinha;
		private final String cpf;
		private final String nome;
		private final List<TipoErro> erros;

		private Registro(String linha, Integer numeroLinha) {
			super();
			this.erros = verificarErrosEmLinha(linha);
			this.cpf = erros.isEmpty() ? getCpfLinha(linha) : null;
			this.nome = erros.isEmpty() ? getNomeLinha(linha) : null;
			this.numeroLinha = numeroLinha;
		}

		private static List<TipoErro> verificarErrosEmLinha(String linha) {
			List<TipoErro> erros = new ArrayList<>();

			if (!linha.matches(".{11}[- \\t].{1,30}")) {
				erros.add(TipoErro.FORMATO_LINHA_INVALIDO);

			} else {
				String cpf = getCpfLinha(linha);
				String nome = getNomeLinha(linha);

				if (!cpf.matches("\\d{11}")) {
					erros.add(TipoErro.CPF_NAO_NUMERICO);
				}

				if (nome.isEmpty()) {
					erros.add(TipoErro.NOME_VAZIO);
				}
			}

			return erros;
		}

		private static String getCpfLinha(String linha) {
			return linha.substring(0, 11).trim();
		}

		private static String getNomeLinha(String linha) {
			return linha.substring(12, linha.length()).trim();
		}

		public boolean isValido() {
			return erros.isEmpty();
		}

		public boolean isCpfNaoNumerico() {
			return erros.contains(TipoErro.CPF_NAO_NUMERICO);
		}

		public boolean isNomeVazio() {
			return erros.contains(TipoErro.NOME_VAZIO);
		}

		public String getCpf() {
			return cpf;
		}

		public String getNome() {
			return nome;
		}

		public Integer getNumeroLinha() {
			return numeroLinha;
		}

		public List<TipoErro> getErros() {
			return erros;
		}
	}
}
