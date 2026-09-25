package br.com.araujoabreu.sgi.service.util;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro.EscopoCompra;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro.Garantias;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro.SituacaoCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro.TipoCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.administracao.domain.Contrato.*;
import br.com.araujoabreu.sgi.administracao.domain.Diretoria.TipoDiretoria;
import br.com.araujoabreu.sgi.administracao.domain.Feriado.Meses;
import br.com.araujoabreu.sgi.administracao.domain.Mensagem.TipoMensagem;
import br.com.araujoabreu.sgi.administracao.domain.NaturezaFornecimento.TipoFornecimento;
import br.com.araujoabreu.sgi.administracao.enums.TipoDominio;
import br.com.araujoabreu.sgi.compras.domain.*;
import br.com.araujoabreu.sgi.compras.domain.MeioContato.Tipo;
import br.com.araujoabreu.sgi.compras.domain.Nota.StatusNota;
import br.com.araujoabreu.sgi.compras.domain.Nota.TipoDocumentoNota;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal.NaturezaNotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal.TipoNotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.RelacaoNotaFiscal.StatusRelacaoNF;
import br.com.araujoabreu.sgi.compras.enums.TipoFrete;
import br.com.araujoabreu.sgi.compras.enums.TipoFreteOC;
import br.com.araujoabreu.sgi.compras.enums.TipoSeguro;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil.ClassificacaoContaContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil.TipoContaContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil.TipoEspecial;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface.TipoLoteInterface;
import br.com.araujoabreu.sgi.contabilidade.domain.PessoaContaContabil.TipoRelacionamento;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.Status;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.TipoPagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo.Periodo;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaDesconto;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.Tributo;
import br.com.araujoabreu.sgi.enumerators.EscopoReajuste;
import br.com.araujoabreu.sgi.faturamento.domain.Faturamento.TipoFaturamento;
import br.com.araujoabreu.sgi.fundoFixo.domain.Demonstrativo;
import br.com.araujoabreu.sgi.fundoFixo.domain.ItemDemonstrativo;
import br.com.araujoabreu.sgi.fundoFixo.enums.TipoConta;
import br.com.araujoabreu.sgi.inrf.domain.Processo.StatusProcesso;
import br.com.araujoabreu.sgi.service.contasApagar.PagamentoService;
import br.com.araujoabreu.sgi.tesouraria.domain.Lancamento.StatusLancamento;
import br.com.araujoabreu.sgi.tesouraria.domain.RelacaoBancaria.MeioPagamento;
import br.com.araujoabreu.sgi.tesouraria.domain.RelacaoBancaria.StatusRelacaoBancaria;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.enums.ESituacao;
import br.com.fasolti.corporativo.security.domain.TipoAcesso;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Transient;
import javax.swing.text.MaskFormatter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// FIXME: Provisório... colocar no framework
public class GlobalBean {

	@Autowired
	private PagamentoService pagamentoService;

	public static final String LOCALHOST_EPSON_FX_890 = "\\\\localhost\\EPSON FX-890";

	final public static String CURRENCY_PATTERN = "###,###,##0.00";

	final public static String CURRENCY_PATTERN_3_DECIMAL = "###,###,##0.000";

	final public static String CURRENCY_PATTERN_4_DECIMAL = "###,###,##0.0000";

	final public static String DECIMAL_PATTERN = "###,###,##0.00";

	final public static String DATE_PATTERN = "dd/MM/yyyy";

	final public static String DATE_PATTERN_YEAR_SHORT = "dd/MM/yy";

	final public static String DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

	final public static String DATETIME_MEDIUM_PATTERN = "dd/MM/yyyy HH:mm";

	final public static String CONTA_CONTABIL_COMPLETA_PATTERN = "[0-9]{1}[.][0-9]{1}[.][0-9]{1}[.][0-9]{2}[.][0-9]{2}[.][0-9]{3}[.][0-9]{4}";

	final public static String NAO_INFORMADO_LABEL = "Não Informado";

	final public static Integer POPUP_MAX_RESULTS = 10;

	final public static Integer CODIGO_NOTA_ELETRONICA = 30;

	final public static Integer MAX_CURRENCY_SIZE = 11;

	final public static String DATA_CHEQUE_COPIA = "dd 'de' MMMM 'de' yyyy";
	final public static String DATA_CHEQUE_DIA = "dd";
	final public static String DATA_CHEQUE_MES = "MMMM";
	final public static String DATA_CHEQUE_ANO = "yyyy";

	public static final String DATA_FILE_INTERFACE_BANCARIA = "yyyy-MM-dd-HH-mm-ss";

	public static final String CONTABILIDADE_INTERFACES_ANEXO_DIRETORIO = "contabilidade.interface.anexo.diretorio";
	public static final String CONTABILIDADE_INTERFACES_ANEXO_DIRETORIO_DEFAULT = "/var/lib/sgi/interfaces/anexo";

	public static final String CONTASAPAGAR_INTERFACE_FLUXOCAIXA_PRAZO = "contasApagar.interface.fluxoCaixa.prazo";
	/**
	 * Pesos para o cálculo do CPF.
	 */
	public static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
	/**
	 * Pesos para o cálculo do CNPJ.
	 */
	public static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
	public static final String VAZIO = "";
	public static final String ESPACO_VAZIO = " ";
	public static final String _E_ = " e ";
	public static final String ZERO_EX = "zero";
	public static final byte ZERO = 0;

//	@In
//	private PropertyService propertyService;
	public static final byte CEM = 100;
	public static final String PONTO = ".";
	public static final String VIRGULA = ",";
	public static final String BARRA = "/";
	public static final String TRACO = "-";
	// Cheque
	public static final byte SALTOS_MAX_CHEQUE = 24;
	public static final byte TAMANHO_MAX_EXTENSO_CHEQUE = 54;
	public static final byte TAMANHO_MAX_COPIA_CHEQUE = 56;
	public static final byte TAMANHO_MES_DA_DATA = 14;
	public static final String SALTO = "\n";
	public static final String MASCARA_CNPJ = "__.___.___/____-__";
	private static final Locale LOCAL_BR = new Locale("pt", "BR");
	private static final NumberFormat fmtNumber = NumberFormat.getInstance(LOCAL_BR);
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

	public static StatusPagamentoPesquisa getStatusPagamento(Status statusPagamento) {
		for (StatusPagamentoPesquisa st : StatusPagamentoPesquisa.values()) {
			if (st.getStatus().equals(statusPagamento))
				return st;
		}
		return null;
	}

	public static String removerAcentuacao(String texto) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[^\\p{ASCII}]", "");

		return texto;
	}

	public static String removerCaracteresNaoAlfanumericos(String texto) {
		texto = texto.replaceAll("[^a-z A-Z 1-9 ]", "");
		return texto;
	}

	public static String removerAcentuacaoENaoAlfanumericos(String texto) {
		texto = removerAcentuacao(texto);
		texto = removerCaracteresNaoAlfanumericos(texto);

		return texto;
	}

	public static String formatarCNPJComPonto(String documento) {
		return String.format("%s.%s.%s/%s-%s", documento.substring(0, 2), documento.substring(2, 5), documento.substring(5, 8), documento.substring(8, 12), documento.substring(12, 14));
	}

	public static String formatarCPFComPonto(String documento) {
		return String.format("%s.%s.%s-%s", documento.substring(0, 3), documento.substring(3, 6), documento.substring(6, 9), documento.substring(9, 11));
	}

	public static String formatBigDecimal(final BigDecimal value) {
		fmtNumber.setMinimumFractionDigits(value.scale());
		fmtNumber.setMaximumFractionDigits(value.scale());
		return fmtNumber.format(value);
	}

	public static Meses getMesToEnum(Integer valor) {
		valor--;
		for (Meses mesCompare : Meses.values()) {
			if (mesCompare.ordinal() == valor) {
				return mesCompare;
			}
		}
		return null;
	}

	public static Integer getMaxCurrencyValue() {
		return MAX_CURRENCY_SIZE;
	}

	/**
	 * Truncate a string to maximum length;
	 *
	 * @param string the string
	 * @param length the length
	 * @return a truncated string or original string if it is short enough
	 */
	public static String truncate(final String string, final int length) {
		if (length >= string.length()) {
			return string;
		} else {
			return string.substring(0, length);
		}
	}

	/**
	 * Método utilizado para a formatação de datas nas pesquisas utilizadas para a geração de relatórios.
	 *
	 * @param data A data a ser formatada.
	 * @return A data formatada em String.
	 */
	public static String formatarDataPesquisa(final Date data) {
		return formatarData(data, "yyyy-MM-dd");
	}

	/**
	 * Método utilizado para formatar da data informada pelo parâmetro.
	 *
	 * @param data   A data a ser formatada.
	 * @param format O formato da data.
	 * @return A data formatada.
	 */
	public static String formatarData(final Date data, final String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, LOCAL_BR);

		return formatter.format(data);
	}

	public static Long getDiferencaEntreDatas(final Date data01, final Date data02) {
		if (data01 != null && data02 != null) {
			long diferencaDias = 0;
			final int DIVISOR_DIFERENCA_DIAS = 1000 * 60 * 60 * 24;
			Calendar calendar01 = Calendar.getInstance();
			calendar01.setTime(data01);
			Calendar calendar02 = Calendar.getInstance();
			calendar02.setTime(data02);
			if (calendar01.after(calendar02)) {
				diferencaDias = (calendar01.getTimeInMillis() - calendar02.getTimeInMillis()) / DIVISOR_DIFERENCA_DIAS;
			} else {
				diferencaDias = (calendar02.getTimeInMillis() - calendar01.getTimeInMillis()) / DIVISOR_DIFERENCA_DIAS;
			}
			return diferencaDias;
		}
		return null;
	}

	public static PessoaJuridica converterPessoaJuridica(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof PessoaJuridica) {
			return ((PessoaJuridica) obj);
		}
		return null;
	}

	public static String getInscricalEstadualFormatada(Pessoa credor) {
		if (credor == null)
			return null;
		if (credor instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) credor;
			if (pessoaJuridica.getInscricaoEstadual() == null) {
				return null;
			} else if (pessoaJuridica.getInscricaoEstadual().trim().equals("")) {
				return null;
			} else {
				return pessoaJuridica.getInscricaoEstadual() + "/" + pessoaJuridica.getUfInscricaoEstadual();
			}
		}
		return "";
	}

	public static String getInscricalEstadualFormatada(br.com.araujoabreu.sgi.administracao.domain.Unidade unidade) {
		if (unidade == null) {
			return null;
		} else if (unidade.getInscricaoEstadual() == null) {
			return null;
		} else if (unidade.getInscricaoEstadual().trim().equals("")) {
			return null;
		} else {
			return unidade.getInscricaoEstadual() + "/" + unidade.getUfInscricaoEstadual();
		}
	}

	public static boolean isGerente(final Colaborador colaborador) {
		if (colaborador.getGerencia() != null && colaborador.getGerencia() instanceof Gerencia) {
			Gerencia gerencia = colaborador.getGerencia();
			if (gerencia != null && gerencia.getGerente() != null && gerencia.getGerente().getCodigo() != null
				&& gerencia.getGerente().getCodigo().equals(colaborador.getCodigo())) {
				return true;
			}
		}
		return false;
	}

	public static BigDecimal getValorLiquido(NotaFiscal notaFiscal, BigDecimal totalItens) {
		return totalItens.subtract((getTotalTributosNotaFiscal(notaFiscal)).add(getTotalOutrosDescontos(notaFiscal)));
	}

	public static BigDecimal getTotalItens(NotaFiscal notaFiscal, BigDecimal totalNFAvulsa, List<Recebimento> recebimentos) {
		if (notaFiscal != null && notaFiscal.getTipoNotaFiscal() != null) {
			if (!isNotaFiscalOC(notaFiscal))
				return totalNFAvulsa;
			else if (notaFiscal.getRecebimentos() != null && !notaFiscal.getRecebimentos().isEmpty())
				return getValorRecebimentos(recebimentos);
		}
		return new BigDecimal("0");
	}

	public static BigDecimal getValorRecebimentos(List<br.com.araujoabreu.sgi.compras.domain.Recebimento> recebimentos) {
		BigDecimal valor = BigDecimal.ZERO;
		// Devemos somar o valor de cada pedido (somatório dos itens do pedido)
		for (Recebimento recebimento : recebimentos) {
			valor = valor.add(recebimento.getTotal());
		}
		// O valor é arredondado em duas casas pra evitar diferenças de
		// precisão.
		return valor.setScale(4, RoundingMode.HALF_EVEN);
	}

	public static BigDecimal getValorItensNotaFiscal(Set<ItemNotaFiscal> itens) {
		BigDecimal valor = BigDecimal.ZERO;

		for (ItemNotaFiscal itemNotaFiscal : itens) {
			valor = valor.add(itemNotaFiscal.getValorTotal());
		}
		// O valor é arredondado em duas casas pra evitar diferenças de
		// precisão.
		return valor.setScale(4, RoundingMode.HALF_EVEN);
	}

	public static BigDecimal getTotalOutrosDescontos(NotaFiscal notaFiscal) {
		BigDecimal totalOutrosDescontos = new BigDecimal("0");
		if (notaFiscal != null && notaFiscal.getListaNotaDesconto() != null && !notaFiscal.getListaNotaDesconto().isEmpty()) {
			for (NotaDesconto nd : notaFiscal.getListaNotaDesconto()) {
				if (nd.getCodigo() != null && nd.getValor() != null) {
					totalOutrosDescontos = totalOutrosDescontos.add(nd.getValor());
				}
			}
		}
		return totalOutrosDescontos;
	}

	public static BigDecimal getTotalTributosNotaFiscal(NotaFiscal notaFiscal) {
		BigDecimal totalTributos = new BigDecimal("0");
		if (notaFiscal != null && notaFiscal.getListaNotaTributoAliquota() != null && !notaFiscal.getListaNotaTributoAliquota().isEmpty()) {
			for (NotaTributoAliquota nta : notaFiscal.getListaNotaTributoAliquota()) {
				if (nta.getPagamento() != null && nta.getPagamento().getCodigo() != null && nta.getPagamento().getValor() != null) {
					totalTributos = totalTributos.add(nta.getPagamento().getValor());
				}
			}
		}
		return totalTributos;
	}

	/* NAO SERÁ MAIS UTILIZADO ESTE CALCULO PARA CALCULAR O TOTAL DE TRIBUTOS
	  SERÁ SOMENTE A SOMA DOS TRIBUTOS DE listaNotaTributoAliquita. SGI-741*/
	public static BigDecimal getTotalTributos(NotaFiscal notaFiscal) {
		BigDecimal total = new BigDecimal("0");
		if (notaFiscal != null) {
			BigDecimal baseCalculo = notaFiscal.getValor();
			if (baseCalculo != null) {
				if (notaFiscal.getTipoFornecimento().equals(TipoFornecimento.CONJUGADO)) {
					baseCalculo = new BigDecimal(0);
					for (ItemNotaFiscal inf : notaFiscal.getItems()) {
						if (inf.getItemCompra().getNaturezaFornecimento().getTipo().equals(TipoFornecimento.SERVICOS)) {
							baseCalculo = baseCalculo.add(inf.getQuantidade().multiply(inf.getPreco()));
						}
					}
				}

				BigDecimal totalTributos = getTotalPercentualTributos(notaFiscal);
				if (!totalTributos.equals(BigDecimal.ZERO)) {
					total = baseCalculo.multiply(totalTributos.divide(new BigDecimal("100")));
				}
			}
		}
		return total;
	}

	public static BigDecimal getTotalPercentualTributos(NotaFiscal notaFiscal) {
		BigDecimal total = new BigDecimal(0);
		if (notaFiscal != null && notaFiscal.getListaNotaTributoAliquota() != null && !notaFiscal.getListaNotaTributoAliquota().isEmpty()) {
			for (NotaTributoAliquota nta : notaFiscal.getListaNotaTributoAliquota()) {
				if (nta != null) {
					if (nta.getPercentual() != null)
						total = total.add(nta.getPercentual());
					else if (nta.getAliquota() != null && nta.getAliquota().getPercentual() != null)
						total = total.add(nta.getAliquota().getPercentual());
				}
			}
		}

		total = total.setScale(2, RoundingMode.HALF_UP);
		return total;

	}

	public static boolean hasMesesDiferentes(Date data1, Date data2) {
		if (data1 != null && data2 != null) {
			Calendar calendarPgto = Calendar.getInstance();
			Calendar calendarVenc = Calendar.getInstance();
			calendarPgto.setTime(data1);
			calendarVenc.setTime(data2);
			int mesPgto = calendarPgto.get(Calendar.MONTH);
			int mesVenc = calendarVenc.get(Calendar.MONTH);

//			if (data1.getTime() - data2.getTime() < 0)
//				return false;
			if (mesPgto != mesVenc)
				return true;
			else if ((mesPgto == mesVenc) && (calendarPgto.get(Calendar.YEAR) != calendarVenc.get(Calendar.YEAR)))
				return true;

			return false;

		}
		return false;
	}

	/**
	 * Validar se a data passada pelo parâmetro está com o período
	 * de 30 dias futuros ou passados a partir da data atual (sysdate).
	 *
	 * @param dataPag A data para validação.
	 * @return <code>true</code> data está na faixa de 30 dias.
	 * <code>false</code> data está com faixa de mais de 30 dias.
	 */
	public static boolean validarDataPeriodoTrintaDias(final Date dataPag) {
		Calendar dt = GregorianCalendar.getInstance();

		if ((dt.getTime().getTime() - dataPag.getTime()) < -2592000000L && (dt.getTime().getTime() - dataPag.getTime()) > 2678400000L) {
			return false;
		}

		return true;
	}

	/**
	 * Retorna o valor por extenso.
	 *
	 * @param vlr O valor a ser "traduzido"
	 * @return O valor informado por extenso.
	 */
	public static String valorPorExtenso(final String vlr) {
		return valorPorExtenso(new BigDecimal(vlr.replace(VIRGULA, PONTO)));
	}

	/**
	 * Retorna o valor por extenso.
	 *
	 * @param vlr O valor do tipo {@link BigDecimal};
	 * @return O valor informado por extenso.
	 */
	public static String valorPorExtenso(final BigDecimal vlr) {
		return valorPorExtenso(vlr.doubleValue());
	}

	/**
	 * Retorna o valor por extenso.
	 *
	 * @param vlr O valor do tipo double;
	 * @return O valor informado por extenso.
	 */
	public static String valorPorExtenso(final double vlr) {
		if (vlr == ZERO) {
			return (ZERO_EX);
		}

		long inteiro = (long) Math.abs(vlr); // parte inteira do valor
		double resto = vlr - inteiro; // parte fracionária do valor

		String vlrS = String.valueOf(inteiro);
		if (vlrS.length() > 15) {
			return ("Erro: valor superior a 999 trilhões.");
		}

		String s = VAZIO, saux, vlrP;

		String centavos = String.valueOf((int) Math.round(resto * 100));

		String[] unidade = {VAZIO, "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove",
			"dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete",
			"dezoito", "dezenove"};

		String[] centena = {VAZIO, "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos",
			"setecentos", "oitocentos", "novecentos"};

		String[] dezena = {VAZIO, VAZIO, "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"};

		String[] qualificaS = {VAZIO, "mil", "milhão", "bilhão", "trilhão"};

		String[] qualificaP = {VAZIO, "mil", "milhões", "bilhões", "trilhões"};

		// definindo o extenso da parte inteira do valor
		int n, unid, dez, cent, tam, i = ZERO;
		boolean umReal = false, tem = false;
		while (!vlrS.equals(ZERO_EX)) {
			tam = vlrS.length();
			// retira do valor a 1a. parte, 2a. parte, por exemplo, para 123456789:
			// 1a. parte = 789 (centena)
			// 2a. parte = 456 (mil)
			// 3a. parte = 123 (milhões)
			if (tam > 3) {
				vlrP = vlrS.substring(tam - 3, tam);
				vlrS = vlrS.substring(0, tam - 3);
			} else { // última parte do valor
				vlrP = vlrS;
				vlrS = ZERO_EX;
			}
			if (!vlrP.equals("000")) {
				saux = VAZIO;
				if (vlrP.equals("100")) {
					saux = "cem";
				} else {
					n = Integer.parseInt(vlrP, 10); // para n = 371, tem-se:
					cent = n / CEM; // cent = 3 (centena trezentos)
					dez = (n % CEM) / 10; // dez = 7 (dezena setenta)
					unid = (n % CEM) % 10; // unid = 1 (unidade um)
					if (cent != ZERO) {
						saux = centena[cent];
					}
					if ((n % CEM) <= 19) {
						if (saux.length() != ZERO) {
							saux = saux + _E_ + unidade[n % CEM];
						} else {
							saux = unidade[n % CEM];
						}
					} else {
						if (saux.length() != ZERO) {
							saux = saux + _E_ + dezena[dez];
						} else {
							saux = dezena[dez];
						}
						if (unid != ZERO) {
							if (saux.length() != ZERO) {
								saux = saux + _E_ + unidade[unid];
							} else {
								saux = unidade[unid];
							}
						}
					}
				}
				if (vlrP.equals("1") || vlrP.equals("001")) {
					if (i == ZERO) {
						umReal = true;
					} else {
						saux = saux + " " + qualificaS[i];
					}
				} else if (i != ZERO) {
					saux = saux + " " + qualificaP[i];
				}
				if (s.length() != ZERO) {
					s = saux + ", " + s;
				} else {
					s = saux;
				}
			}
			if (((i == ZERO) || (i == 1)) && s.length() != ZERO) {
				tem = true; // tem centena ou mil no valor
			}
			i = i + 1; // próximo qualificador: 1- mil, 2- milhão, 3- bilhão, ...
		}

		if (s.length() != ZERO) {
			if (umReal) {
				s = s + " real";
			} else if (tem) {
				s = s + " reais";
			} else {
				s = s + " de reais";
			}
		}

		// definindo o extenso dos centavos do valor
		if (!centavos.equals("0")) { // valor com centavos
			if (s.length() != ZERO) {
				s = s + _E_;
			}
			if (centavos.equals("1")) {
				s = s + "um centavo";
			} else {
				n = Integer.parseInt(centavos, 10);
				if (n <= 19) {
					s = s + unidade[n];
				} else { // para n = 37, tem-se:
					unid = n % 10; // unid = 37 % 10 = 7 (unidade sete)
					dez = n / 10; // dez = 37 / 10 = 3 (dezena trinta)
					s = s + dezena[dez];
					if (unid != ZERO) {
						s = s + _E_ + unidade[unid];
					}
				}
				s = s + " centavos";
			}
		}
		return (s);
	}

	/**
	 * Obter o valor formatado. Formato para o local "pt_BR".
	 *
	 * @param valor O valor a ser formatado.
	 * @return O valor formatado em Currency.
	 */
	public static String formatarValor(final BigDecimal valor) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		return formatter.format(valor.doubleValue());
	}

	/**
	 * Formar números inteiros.
	 *
	 * @param valor O valor {@link Integer} a ser formatado.
	 * @return O valor formatado.
	 */
	public static String formatarInteiro(final Object valor) {
		if (valor != null) {
			NumberFormat numberFormat = NumberFormat.getIntegerInstance();
			return numberFormat.format(valor);
		}
		return "0";
	}

	public static String formatDecimal(final BigDecimal valor) {
		DecimalFormat decimalFormat = new DecimalFormat(CURRENCY_PATTERN);
		return valor == null ? "" : decimalFormat.format(valor.doubleValue());
	}

	public static br.com.araujoabreu.sgi.administracao.domain.TipoNaturezaDespesa getTipoNaturezaDespesaByNota(Demonstrativo demonstrativo, Nota nota) {
		if (demonstrativo != null && nota != null && nota.getCodigo() != null) {
			for (ItemDemonstrativo item : demonstrativo.getItens()) {
				if (item.getNotaFiscal() != null && Objects.equals(item.getNotaFiscal().getCodigo(), nota.getCodigo())) {
					return item.getNaturezaDespesa().getTipoNaturezaDespesa();
				}
			}
		}
		return null;
	}

	public static NaturezaDespesa getNaturezaDespesaByNota(Demonstrativo demonstrativo, Nota nota) {
		if (demonstrativo != null && nota != null && nota.getCodigo() != null) {
			for (ItemDemonstrativo item : demonstrativo.getItens()) {
				if (item.getNotaFiscal() != null && Objects.equals(item.getNotaFiscal().getCodigo(), nota.getCodigo())) {
					return item.getNaturezaDespesa();
				}
			}
		}
		return null;
	}

	public static boolean isTributoSelected(Tributo tributo, Set<Tributo> tributos) {
		if (tributos != null)
			return tributos.contains(tributo);
		return false;
	}

	/**
	 * Divide a string passada com um salto simples (\n).<br>
	 *
	 * @param value       A string a ser dividida.
	 * @param maxCaracter O máximo de caracteres para dividir.
	 * @return A string dividida.
	 */
	public static String dividirString(final String value, final int maxCaracter) {
		StringBuilder builder = new StringBuilder();
		if (value.length() <= maxCaracter) {
			builder.append(value);
		} else {
			if (value.charAt(maxCaracter - 1) == ' ') {
				builder.append(value.substring(0, maxCaracter - 1));
				builder.append(GlobalBean.SALTO);
				builder.append(value.substring((maxCaracter - 1), value.length()));
			} else {
				int aux = 0;
				for (int x = (maxCaracter - 1); x != 0; x--) {
					if (value.charAt(x) == ' ') {
						aux = x;
						break;
					}
				}
				builder.append(value.substring(0, aux++));
				builder.append(GlobalBean.SALTO);
				builder.append(value.substring(aux, value.length()));
			}
		}
		return builder.toString();
	}

	/**
	 * Validar se os objetos {@link Pessoa} tem o mesmo documento.
	 *
	 * @param valueA O objeto {@link Pessoa} para verificação de documento com outro objeto {@link Pessoa}.
	 * @param valueB O objeto {@link Pessoa} para verificação de documento com outro objeto {@link Pessoa}.
	 * @return <code>true</code> se os objetos estiverem o mesmo documento.
	 * <code>false</code> caso os objetos tenham documentos diferentes.
	 */
	public static boolean mesmoDocumento(final Pessoa valueA, final Pessoa valueB) {
		if (valueA == null || valueB == null) {
			return false;
		}

		if (valueA.getDocumento() == null || valueB.getDocumento() == null) {
			return false;
		}

		if (valueA.getDocumento().equalsIgnoreCase(valueB.getDocumento())) {
			return true;
		}

		return false;
	}

	public static String getNomeFantasia(Pessoa pessoa) {
		if (pessoa != null) {
			if (pessoa.getTipoPessoa().equals(Pessoa.TipoPessoa.PESSOA_JURIDICA)) {
				/*
				 * Feito o procedimento abaixo, para evitar lançar exceção
				 * quando há erro no banco ao trazer objeto pessoa jurídica
				 * e não for possível fazer o cast.
				 */
				if (pessoa instanceof PessoaJuridica) {
					return pessoa.getNomeFantasia();
				} else {
					return pessoa.getNome();
				}
			} else {
				return pessoa.getNome();
			}
		} else {
			return "";
		}
	}

	public static String getNomeFantasia(Unidade unidade) {
		if (unidade != null) {
			if (unidade.getTipoPessoa().equals(Pessoa.TipoPessoa.PESSOA_JURIDICA)) {
				return unidade.getNomeFantasia();
			}
			if (unidade.getTipoPessoa().equals(Pessoa.TipoPessoa.UNIDADE)) {
				return unidade.getNomeFantasia();
			} else {
				return unidade.getNome();
			}
		}
		return "";
	}

	public static String formatCep(String cep) {
		if (cep != null && !cep.isEmpty()) {
			if (!cep.contains("-")) {
				cep = cep.substring(0, 5) + "-" + cep.substring(5, 7);
			}
		}
		return cep;
	}

	/**
	 * Obter XML de um serviço informando a URL.
	 *
	 * @param url A URL do serviço.
	 * @return O XML (String).
	 * @throws IOException
	 */
	public static String obterXMLServico(String url) throws IOException {
		URL u = new URL(url);
		URLConnection con = u.openConnection();
		con.setConnectTimeout(2000);
		con.setReadTimeout(5000);
		InputStream in = con.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		StringBuilder sb = new StringBuilder();

		String line;

		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}

	public static boolean isReachableByPing(String host) {
		try {
			String cmd = "";

			if (System.getProperty("os.name").startsWith("Windows")) {
				// For Windows
				cmd = "ping -n 1 " + host;
			} else {
				// For Linux and OSX
				cmd = "ping -c 1 " + host;
			}

			Process myProcess = Runtime.getRuntime().exec(cmd);
			myProcess.waitFor();

			if (myProcess.exitValue() == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Converte uma string com conteúdo XML para o objeto especificado.
	 *
	 * @param $class A classe a ser convertida.
	 * @param xml    O XML a ser convertido em Objeto.
	 * @return O objeto convertido do XML informado.
	 * @throws JAXBException
	 */
	public static Object converterXMLParaObjecto(Class<?> $class, String xml) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance($class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StringReader reader = new StringReader(xml);

		return unmarshaller.unmarshal(reader);
	}

	/**
	 * Converter Objeto em XML.
	 *
	 * @param obj O objeto a ser convertido.
	 * @return O String XML.
	 * @throws JAXBException
	 */
	public static String converterObjetoParaXML(Object obj) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(obj.getClass());

		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		StringWriter sw = new StringWriter();

		marshaller.marshal(obj, sw);

		return sw.toString();
	}

	public static void salvarArquivo(File file, String newFileName, String path) throws IOException {
		try {
			InputStream in = new FileInputStream(file);

			if (newFileName == null || newFileName.isEmpty()) {
				newFileName = file.getName();
			}

			if (!path.substring(path.length() - 1, path.length()).equals("/") && !path.substring(path.length() - 1, path.length()).equals("\\")) {
				path += System.getProperty("file.separator");
			}

			OutputStream out = new FileOutputStream(new File(path + newFileName));

			int read = ZERO;

			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, ZERO, read);
			}

			in.close();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("Arquivo [" + newFileName + "] não encontrado.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Erro na gravação do arquivo '" + newFileName + "'.", e);
		}
	}

	public static void gerarArquivoSaida(final String fileName, final String text, final String path) throws IOException {
		File filePath = new File(path);

		if (!filePath.exists()) {
			if (filePath.mkdirs()) {
				System.out.println("Diretórios criados com sucesso.");
			} else {
				System.err.println("Não foi possível criar diretórios para os arquivos.");
				throw new IOException("Erro ao criar diretórios.");
			}
		}

		File fileSaida = new File(path, fileName);

		PrintWriter printWriter = new PrintWriter(fileSaida, "UTF-8");
		printWriter.print(text);
		printWriter.close();
	}

	public static boolean removerArquivoAnexo(Anexo anexo, String path) {
		if (!path.substring(path.length() - 1, path.length()).equals("/") && !path.substring(path.length() - 1, path.length()).equals("\\")) {
			path += System.getProperty("file.separator");
		}
		File file = new File(path + anexo.getNomeCompleto());
		return file.delete();
	}

	/**
	 * Obter o ano de uma data informada. Exemplo: 'yyyy'
	 *
	 * @param date A data para obter o ano.
	 * @return O ano em 'yyyy'.
	 */
	public static String getAnoDaData(Date date) {
		return formatarData(date, "yyyy");
	}

	/**
	 * Obter o mês de uma data informada.
	 *
	 * @param date A data para obter o mês.
	 * @return O mês em 'MM'.
	 */
	public static String getMesDaData(Date date) {
		return formatarData(date, "MM");
	}

	public static double obterTamanhoArquivoEmKilobytes(File file) {
		double bytes = file.length();
		return (bytes / 1024);
	}

	public static double obterTamanhoArquivoEmMegabytes(File file) {
		return (obterTamanhoArquivoEmKilobytes(file) / 1024);
	}

	public static String adicionarZerosAoNumero(String numero, int qtdZeros) {
		String value = numero.toString();
		String zeros = "";
		for (int x = 0; x < (qtdZeros - value.length()); x++) {
			zeros += "0";
		}
		return zeros + value;
	}

	public static String adicionarZerosAoNumero(Long numero, int qtdZeros) {
		return adicionarZerosAoNumero(numero.toString(), qtdZeros);
	}

	/**
	 * Realizar o split por espaço.
	 *
	 * @param value O valor a ser "splitado".
	 * @return Lista com os valores "splitados".
	 */
	public static List<String> splitBySpace(String value) {
		List<String> values = new ArrayList<>();

		if (value == null || value.isEmpty()) {
			return values;
		}

		StringTokenizer st = new StringTokenizer(value);

		while (st.hasMoreElements()) {
			values.add((String) st.nextElement());
		}

		return values;
	}

	/**
	 * Realizar o split pelo valor informado..
	 *
	 * @param str   O valor a ser "splitado".
	 * @param value "Splitar" por esse valor informado.
	 * @return Lista com os valores "splitados".
	 */
	public static List<String> splitByValue(String str, String value) {
		List<String> values = new ArrayList<>();

		if (str == null || str.isEmpty()) {
			return values;
		}

		StringTokenizer st = new StringTokenizer(str, value);

		while (st.hasMoreElements()) {
			values.add((String) st.nextElement());
		}

		return values;
	}

	/**
	 * Formata o valor informado.<br/>
	 * <table>
	 * <tr>
	 * <td>#</td><td>Any valid number, uses Character.isDigit</td>
	 * </tr>
	 * <tr>
	 * <td>'</td><td>Escape character, used to escape any of the special formatting characters</td>
	 * </tr>
	 * <tr>
	 * <td>U</td><td>Any character (Character.isLetter). All lowercase letters are mapped to upper case</td>
	 * </tr>
	 * <tr>
	 * <td>L</td><td>Any character (Character.isLetter). All upper case letters are mapped to lower case</td>
	 * </tr>
	 * <tr>
	 * <td>A</td><td>Any character or number (Character.isLetter or Character.isDigit)</td>
	 * </tr>
	 * <tr>
	 * <td>?</td><td>Any character (Character.isLetter)</td>
	 * </tr>
	 * <tr>
	 * <td>*</td><td>Anything</td>
	 * </tr>
	 * <tr>
	 * <td>H</td><td>Any hex character (0-9, a-f or A-F)</td>
	 * </tr>
	 * </table>
	 * <br/><br/>
	 * <a href='http://docs.oracle.com/javase/7/docs/api/javax/swing/text/MaskFormatter.html'>Doc - Class MaskFormatter</a>
	 *
	 * @param value O valor (String) a ser formatado.
	 * @param mask  A mascara de formatação.
	 * @return O valor formatado.
	 * @throws ParseException
	 */
	public static String formatarString(String value, String mask) {
		MaskFormatter formatter;
		try {
			formatter = new MaskFormatter(mask);
			formatter.setValueContainsLiteralCharacters(false);
			return formatter.valueToString(value);
		} catch (ParseException e) {
			e.printStackTrace();
			return "#error!";
		}
	}

	/**
	 * Retorna a diferença em dias.
	 *
	 * @throws ParseException
	 */
	public static int diferencaEntreDatas(String data1, String data2) throws ParseException {
		GregorianCalendar ini = new GregorianCalendar();
		GregorianCalendar fim = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		ini.setTime(sdf.parse(data1));
		fim.setTime(sdf.parse(data2));
		return diferencaEntreDatas(ini.getTime(), fim.getTime());
	}

	/**
	 * Retorna a diferença em dias.
	 *
	 */
	public static int diferencaEntreDatas(Date data1, Date data2) {
		GregorianCalendar ini = new GregorianCalendar();
		GregorianCalendar fim = new GregorianCalendar();
		ini.setTime(data1);
		fim.setTime(data2);
		long dt1 = ini.getTimeInMillis();
		long dt2 = fim.getTimeInMillis();
		return (int) (((dt2 - dt1) / 86400000));
	}

	/**
	 * Obtem a diferença de duas datas em meses com arredondamente.
	 * </br>
	 * Exemplo 1: Se a data inicial for 01/09/2015 e a data final for 15/09/2015 o retorno será 0 (zero).
	 * </br>
	 * Exemplo 2: Se a data inicial for 01/09/2015 e a data final for 16/09/2015 o retorno será 1 (um).
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int obterAdiferencaEmMeses(Date startDate, Date endDate) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);

		double dt = ((endDate.getTime() - startDate.getTime()) + 3600000);
		double meses = (dt / 86400000L) / 30;
		BigDecimal value = new BigDecimal(meses).setScale(0, RoundingMode.HALF_EVEN);

		return Integer.valueOf(value.intValue());
	}

	/**
	 * Obtem a diferença de duas datas em meses sem arredondamente.
	 * </br>
	 * Exemplo 1: Se a data inicial for 01/09/2015 e a data final for 15/09/2015 o retorno será 0 (zero).
	 * </br>
	 * Exemplo 2: Se a data inicial for 01/09/2015 e a data final for 16/09/2015 o retorno será 0 (zero).
	 * </br>
	 * Exemplo 3: Se a data inicial for 01/09/2015 e a data final for 30/09/2015 o retorno será 0 (zero).
	 * </br>
	 * Exemplo 4: Se a data inicial for 01/09/2015 e a data final for 01/10/2015 o retorno será 1 (um).
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int obterAdiferencaEmMesesSemArredondamento(Date startDate, Date endDate) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);

		double dt = ((endDate.getTime() - startDate.getTime()) + 3600000);
		double meses = (dt / 86400000L) / 30;
		BigDecimal value = new BigDecimal(meses);

		return Integer.valueOf(value.intValue());
	}

	public static int obterAdiferencaEmDias(Date startDate, Date endDate) {
		GregorianCalendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		startDate.getTime();

		GregorianCalendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);
		endDate.getTime();

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		return diffMonth == 0 ? 1 : diffMonth;
	}

	public static int obterAdiferencaEmAnos(Date startDate, Date endDate) {
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		return endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
	}

	public static int obterAdiferencaEmAnos(Calendar startDate, Calendar endDate) {
		return obterAdiferencaEmAnos(startDate.getTime(), endDate.getTime());
	}

	/**
	 * Retorna lista com anos entre dataInicio e dataFim.
	 *
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static List<String> obterListaDeAnos(Date dataInicio, Date dataFim) {

		Calendar calendarInicio = Calendar.getInstance();
		calendarInicio.setTime(dataInicio);

		Calendar calendarFim = Calendar.getInstance();
		calendarFim.setTime(dataFim);

		int diferencaAnos = GlobalBean.obterAdiferencaEmAnos(calendarInicio, calendarFim);

		List<String> listaAno = new ArrayList<>();

		for (int i = 0; i <= diferencaAnos; i++) {
			int ano = calendarInicio.get(Calendar.YEAR) + i;
			listaAno.add(String.valueOf(ano));
		}

		return listaAno;
	}

	public static String formatFractionDigits(int digits, String valor) {
		if (!valor.contains(",")) {
			valor = valor.replace(".", ",");
		}
		if (!valor.contains(",")) {
			valor = valor + ",00";
		} else if (valor.substring(valor.indexOf(",") + 1).length() == 0) {
			valor = valor + "00";
		} else if (valor.substring(valor.indexOf(",") + 1).length() == 1) {
			valor = valor + "0";
		}
		return valor.substring(0, valor.indexOf(",") + digits + 1);
	}

	public static String formatFractionDigits(int digits, BigDecimal valor) {
		return formatFractionDigits(digits, valor.toString());
	}

	public static boolean validarMensagemSms(String message) throws Exception {
		if (message == null || message.isEmpty()) {
			throw new Exception("Mensagem tem mais de 150 caracteres.");
		} else if (message.length() >= 150) {
			throw new Exception("Mensagem tem mais de 150 caracteres.");
		} else {
			return true;
		}
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

	public static SituacaoCentroFinanceiro getSituacaoCF(int ordinal) {
		for (SituacaoCentroFinanceiro situacao : SituacaoCentroFinanceiro.values()) {
			if (situacao.ordinal() == ordinal)
				return situacao;
		}
		return null;
	}

	public static boolean isTipoNotaPrestacaoConta(NotaFiscal notaFiscal) {
		if (notaFiscal.getTipoNotaFiscal() != null) {
			if (notaFiscal.getTipoNotaFiscal().equals(TipoNotaFiscal.FUNDO_FIXO)
				|| notaFiscal.getTipoNotaFiscal().equals(TipoNotaFiscal.REEMBOLSO)
				|| notaFiscal.getTipoNotaFiscal().equals(TipoNotaFiscal.ADIANTAMENTO)

				|| notaFiscal.getTipoNotaFiscal().equals(TipoNotaFiscal.OC_PC_FUNDO_FIXO)
				|| notaFiscal.getTipoNotaFiscal().equals(TipoNotaFiscal.OC_PC_REEMBOLSO)
				|| notaFiscal.getTipoNotaFiscal().equals(TipoNotaFiscal.OC_PC_ADIANTAMENTO)

			) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNotaFiscalOC(NotaFiscal notaFiscal) {
		if (notaFiscal.getTipoNotaFiscal() != null) {
			switch (notaFiscal.getTipoNotaFiscal()) {
				case NORMAL:
				case OC_PC_FUNDO_FIXO:
				case OC_PC_ADIANTAMENTO:
				case OC_PC_REEMBOLSO:
					return true;
				default:
					break;
			}
		}
		return false;
	}

	/**
	 * Apartir de uma data, obter o mês anterior.
	 *
	 * @param data A data para obter o mês anterior.
	 * @return Mês anterior e ano (caso data seja em janeiro, mês e ano anterior)
	 */
	public static String obterMesAnterior(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		String mes = (calendar.get(Calendar.MONTH) > 9) ? "" + calendar.get(Calendar.MONTH) : "0" + calendar.get(Calendar.MONTH);
		if (mes.equals("00")) {
			return "12/" + (calendar.get(Calendar.YEAR) - 1);
		}
		return mes + "/" + calendar.get(Calendar.YEAR);
	}

	public static List<String> obterReferenciaMesAnoAte(Date dataInicio, Date dataFinal, boolean ignorarMesAtual) {
		List<String> meses = new ArrayList<>();
		List<Calendar> datas = getListaReferencia(dataInicio, dataFinal, ignorarMesAtual);
		if (datas != null && !datas.isEmpty()) {
			for (Calendar calendar : datas) {
				Meses mes = Meses.getMes(calendar.get(Calendar.MONTH));
				String mesAno = mes.getNome() + "/" + calendar.get(Calendar.YEAR);
				meses.add(mesAno);
			}
		}
		return meses;
	}

	public static List<Calendar> getListaReferencia(Date dataInicio, Date dataFinal, boolean ignorarMesAtual) {
		List<Calendar> datas = new ArrayList<>();
		Calendar calendarIni = Calendar.getInstance();
		calendarIni.setTime(dataInicio);
		Calendar calendarFim = Calendar.getInstance();
		calendarFim.setTime(dataFinal);

		int mesAtual = calendarIni.get(Calendar.MONTH);
		int anoAtual = calendarIni.get(Calendar.YEAR);
		int mesFinal = calendarFim.get(Calendar.MONTH);
		int anoFinal = calendarFim.get(Calendar.YEAR);
		while (calendarIni.before(calendarFim) || calendarIni.equals(calendarFim)) {
			if (ignorarMesAtual && (mesAtual == mesFinal && anoFinal == anoAtual))
				break;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(calendarIni.getTime());
			datas.add(calendar);
			calendarIni.add(Calendar.MONTH, 1);
			mesAtual = calendarIni.get(Calendar.MONTH);
			anoAtual = calendarIni.get(Calendar.YEAR);
		}
		return datas;
	}

	public static TipoEspecial getTipoEspecial(TipoRelacionamento tipoRelacionamento) {
		if (tipoRelacionamento != null) {
			if (tipoRelacionamento.equals(TipoRelacionamento.FUNDO_FIXO)) {
				return TipoEspecial.FUNDO_FIXO;
			}
			if (tipoRelacionamento.equals(TipoRelacionamento.ADIANTAMENTO)) {
				return TipoEspecial.ADIANTAMENTO;
			}
			if (tipoRelacionamento.equals(TipoRelacionamento.REEMBOLSO)) {
				return TipoEspecial.REEMBOLSO;
			}
			if (tipoRelacionamento.equals(TipoRelacionamento.EMPRESTIMO)) {
				return TipoEspecial.EMPRESTIMO;
			}
		}
		return null;
	}

	public static String removerTexto(String numero) {
		if (numero != null && numero.length() > 0) {
			numero = numero.replaceAll("[\\//a-zA-Z.-]", "");
			return numero;
		}
		return numero;
	}

	/**
	 * Obter a data de início e fim do mês de uma data.
	 *
	 * @param date A data para obter o início e o fim.
	 * @return Array com a data inicial [0] e final [1].
	 */
	public static Date[] obterDataInicalEFimDeUmaDataDuranteMes(Date date) {
		Date[] datas = new Date[2];
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		// Primeiro dia do mês.
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		datas[0] = calendar.getTime();
		// Último dia do mês.
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		datas[1] = calendar.getTime();

		return datas;
	}

	public static Date converterStringParaData (String dataString) {
		try {
			Date data = new SimpleDateFormat("dd/MM/yyyy").parse(dataString);
			return  data;
		}catch (ParseException e){
			return null;
		}
	}

	public static BigDecimal calcularPercentual(BigDecimal valor, BigDecimal percentual) {
		BigDecimal total = new BigDecimal("0");
		total = (valor.multiply(percentual)).divide(new BigDecimal("100"), RoundingMode.HALF_EVEN);
		return total;
	}

	public static boolean validarEmail(String email) {
		if (email != null && !email.isEmpty()) {
			String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(email);
			return matcher.matches();
		}
		return false;
	}

	/**
	 * Responsável por calcular o dígito na validação do CPF ou CNPJ.
	 *
	 * @param doc  O número de CPF ou CNPJ para obter o dígito.
	 * @param peso Os pesos utilizado para o cálculo.
	 * @return O cálculo do dígito.
	 */
	public int calcularDigito(final String doc, final int[] peso) {
		int soma = 0;
		for (int indice = doc.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(doc.substring(indice, indice + 1));
			soma += digito * peso[peso.length - doc.length() + indice];
		}
		soma = 11 - soma % 11;

		return soma > 9 ? 0 : soma;
	}

	/**
	 * Remover os caracteres ".", "-" e "/" do documento.
	 *
	 * @param doc O valor CNPJ ou CPF.
	 * @return O documento sem os caracteres padrões de documentos.
	 */
	public String removerCaracteres(final String doc) {
		if (!StringUtils.isEmpty(doc))
			return doc.replace(PONTO, VAZIO).replace(TRACO, VAZIO).replace(BARRA, VAZIO).replace("_", VAZIO);
		else
			return null;
	}

	/**
	 * Validar se o documento é nulo ou não e caso o mesmo tenho caracteres padrões
	 * de CNPJ e CPF, os caracteres serão removidos.
	 *
	 * @param doc O CNPJ ou CPF.
	 * @return O documento com apenas os números.
	 */
	private String validar(final String doc) {
		if (doc == null) {
			throw new IllegalArgumentException("Tipo não suportado.");
		} else if ((doc.contains(PONTO)) || (doc.contains(TRACO)) || (doc.contains(BARRA))) {
			return removerCaracteres(doc);
		}
		return doc;
	}

	public boolean isValidCPF(final String doc) {
		String cpf = validar(doc);
		if (cpf.length() != 11) {
			return false;
		}
		int digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
		int digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);

		return cpf.equals(cpf.substring(0, 9) + Integer.toString(digito1) + Integer.toString(digito2));
	}

	public boolean isValidCNPJ(final String doc) {
		String cnpj = validar(doc);
		if (cnpj.length() != 14) {
			return false;
		}
		int digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
		int digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);

		return cnpj.equals(cnpj.substring(0, 12) + Integer.toString(digito1) + Integer.toString(digito2));
	}

	public String pagingStatus(final boolean paginated, final Integer first, final Integer max, final Integer results, final Long total,
							   final String label) {
		if (results > 0) {
			if (paginated) {
				return MessageFormat.format("{0} 'a' {1} 'de' {2} {3}", parseFirstResult(first), parseLastResult(first, results), total, label);
			} else {
				return MessageFormat.format("{0} {1}", results, label);
			}
		}
		return "";
	}

	private int parseFirstResult(final Integer first) {
		return ((first != null) ? first : 0) + 1;
	}

	private int parseLastResult(final Integer first, final Integer results) {
		return (parseFirstResult(first) + results) - 1;
	}

	public String getDecimalPattern() {
		return DECIMAL_PATTERN;
	}

	public String getCurrencyPattern() {
		return CURRENCY_PATTERN;
	}

	public String getCurrencyPattern3Decimal() {
		return CURRENCY_PATTERN_3_DECIMAL;
	}

	public String getCurrencyPattern4Decimal() {
		return CURRENCY_PATTERN_4_DECIMAL;
	}

	/**
	 * Retorna o formato que será usado para apresentar a diferença entre duas
	 * propostas (a melhor proposta e uma outra qualquer).
	 *
	 * @return o formato para a diferença
	 */
	public String getNegativeFormat() {
		return "(+" + GlobalBean.CURRENCY_PATTERN + ")";
	}

	/**
	 * Identico ao método acima, mas utilizado na tela de NF, onde o conceito de diferença utilizado é diferente.
	 * No método acima queremos indicar que existe uma diferença entre duas proposta de fornecedores. Na NF é
	 * utilizado para indicar o quanto falta para completar os valores do parcelamento.
	 *
	 * @return o formato para a diferença
	 */
	public String getNegativeFormatNF() {
		return "(" + GlobalBean.CURRENCY_PATTERN + ")";
	}

	public String getDatePattern() {
		return DATE_PATTERN;
	}

	public String getDateTimePattern() {
		return DATETIME_PATTERN;
	}

	public String getDateTimeMediumPattern() {
		return DATETIME_MEDIUM_PATTERN;
	}

	public Boolean[] getBooleans() {
		return new Boolean[]{Boolean.TRUE, Boolean.FALSE};
	}

	/**
	 * Retorna os possívels valores que definem a necessidade de uma requisição de compra.
	 *
	 * @return
	 */
	public EnumSelectContratoServicoExtra[] getOpcoesSelectContratoServicoExtra() {
		EnumSelectContratoServicoExtra[] enums = new EnumSelectContratoServicoExtra[2];
		enums[0] = EnumSelectContratoServicoExtra.CONTRATO;
		enums[1] = EnumSelectContratoServicoExtra.SERVICO_EXTRA;
		return enums;
	}

	/**
	 * Retorna os possívels valores que definem a necessidade de uma nota fiscal.
	 *
	 * @return
	 */
	public EnumSelectContratoServicoExtra[] getOpcoesSelectContratoServicoExtraNotaFiscal() {
		EnumSelectContratoServicoExtra[] enums = new EnumSelectContratoServicoExtra[3];
		enums[0] = EnumSelectContratoServicoExtra.CLIENTE;
		enums[1] = EnumSelectContratoServicoExtra.CONTRATO;
		enums[2] = EnumSelectContratoServicoExtra.SERVICO_EXTRA;
		return enums;
	}

	public ESTADOS[] getEstados() {
		return ESTADOS.values();
	}

	public ESTADOSENDERECO[] getEstadosEndereco() {
		return ESTADOSENDERECO.values();

	}

	public PessoaJuridica.Mei[] getMeiStatus() {
		return new PessoaJuridica.Mei[]{PessoaJuridica.Mei.SIM, PessoaJuridica.Mei.NAO};
	}

	public Status[] getStatus() {
		Status[] status = new Status[12];
		status[0] = Status.ABERTO;
		status[1] = Status.REABERTO;
		status[2] = Status.APROVACAO;
		status[3] = Status.AJUSTE_FISCAL;
		status[4] = Status.ANALISE;
		status[5] = Status.AUTORIZACAO;
		status[6] = Status.AUTORIZADO;
		status[7] = Status.PAGAMENTO;
		status[8] = Status.RELACIONADO;
		status[9] = Status.FINALIZADO;
		status[10] = Status.CANCELAMENTO;
		status[11] = Status.CANCELADO;
		return status;
	}

	public Status getStatusFinalizado() {
		return Status.FINALIZADO;
	}

	public Garantias[] getGarantias() {
		return Garantias.values();
	}

	public TipoFornecimento[] getTiposRequisicao() {
		return TipoFornecimento.values();
	}

	public NaturezaNotaFiscal[] getNaturezasPagamento() {
		return NaturezaNotaFiscal.values();
	}

	public PessoaJuridica.CanalComunicacao[] getCanaisComunicacao() {
		return PessoaJuridica.CanalComunicacao.values();
	}

	public TipoCentroFinanceiro[] getTiposCentroFinanceiro() {
		return TipoCentroFinanceiro.values();
	}

	public Tipo[] getTiposMeioContato() {
		return Tipo.values();
	}

	public TipoNotaFiscal[] getNaturezaNotaSaida() {
		return new TipoNotaFiscal[]{
			TipoNotaFiscal.DEVOLUCAO_PARCIAL,
			TipoNotaFiscal.DEVOLUCAO_TOTAL
		};
	}

	public StatusNota[] getSituacaoNotaSaida() {
		return new StatusNota[]{
			StatusNota.ABERTA,
			StatusNota.CANCELADA,
			StatusNota.FINALIZADO};
	}

	public Tipo[] getTiposMeioContatoOrdemCompra() {
		return new Tipo[]{Tipo.ARAUJOABREU, Tipo.OUTRO};
	}

	public TipoSeguro[] getTiposSeguro() {
		return new TipoSeguro[]{
			TipoSeguro.DESTINATARIO,
			TipoSeguro.EMITENTE
		};
	}

	public TipoFrete[] getTiposFrete() {
		return new TipoFrete[]{
			TipoFrete.DESTINATARIO,
			TipoFrete.EMITENTE
		};
	}

	public TipoFreteOC[] getTiposFreteOC() {
		return new TipoFreteOC[]{
			TipoFreteOC.DESTINATARIO,
			TipoFreteOC.EMITENTE
		};
	}

	public TipoFrete[] getTiposFreteCompleto() {
		return new TipoFrete[]{
			TipoFrete.DESTINATARIO,
			TipoFrete.EMITENTE,
			TipoFrete.TERCEIROS,
			TipoFrete.SEM_FRETE
		};
	}

	public br.com.araujoabreu.sgi.compras.domain.OrdemCompra.Status[] getStatusOrdemCompra() {
		return br.com.araujoabreu.sgi.compras.domain.OrdemCompra.Status.values();
	}

	public br.com.araujoabreu.sgi.compras.domain.OrdemCompra.Status[] getStatusOrdemCompraRecebimento() {
		return new br.com.araujoabreu.sgi.compras.domain.OrdemCompra.Status[]{
			br.com.araujoabreu.sgi.compras.domain.OrdemCompra.Status.RECEBIMENTO,
			br.com.araujoabreu.sgi.compras.domain.OrdemCompra.Status.RECEBIMENTO_INICIADO
		};
	}

	public StatusRelacaoNF[] getStatusRelacaoNF() {
		StatusRelacaoNF[] values = new StatusRelacaoNF[StatusRelacaoNF.values().length];
		values[0] = StatusRelacaoNF.ABERTA;
		values[1] = StatusRelacaoNF.ENVIADA;
		values[2] = StatusRelacaoNF.RECEBIDA_TOTAL_PARCIAL;
		values[3] = StatusRelacaoNF.RECEBIDA_TOTAL;
		values[4] = StatusRelacaoNF.RECEBIDA_PARCIAL;
		values[5] = StatusRelacaoNF.CANCELADA;
		return values;
	}

	public StatusNota[] getStatusNota() {

		StatusNota[] values = new StatusNota[11];
		values[0] = StatusNota.ABERTA;
		values[1] = StatusNota.REABERTA;
		values[2] = StatusNota.EM_APROVACAO_NOVA_NF;
		values[3] = StatusNota.EM_APROVACAO_RETORNO_FINANCEIRO;
		values[4] = StatusNota.EM_APROVACAO_RETORNO_AJUSTES;
		values[5] = StatusNota.EM_RELACAO_NOTAS;
		values[6] = StatusNota.RECEBIDA_FINANCEIRO;
		values[7] = StatusNota.EM_AJUSTE_FISCAL;
		values[8] = StatusNota.APROVADA;
		values[9] = StatusNota.CANCELADA;
		values[10] = StatusNota.EM_APROVACAO_FUNDO_FIXO;

		return values;
	}

	public StatusNota[] getStatusNotaPagamento() {

		StatusNota[] values = new StatusNota[5];
		values[0] = StatusNota.ABERTA;
		values[1] = StatusNota.REABERTA;
		values[2] = StatusNota.RECEBIDA_FINANCEIRO;
		values[3] = StatusNota.EM_AJUSTE_FISCAL;
		values[4] = StatusNota.CANCELADA;
		return values;
	}

	public Periodo[] getPeriodos() {
		return Periodo.values();
	}

	public StatusLancamento[] getStatusLancamento() {
		return StatusLancamento.values();
	}

	public TipoNotaFiscal[] getTiposNotaFiscalFF() {
		TipoNotaFiscal[] tipos = new TipoNotaFiscal[3];
		tipos[0] = TipoNotaFiscal.FUNDO_FIXO;
		tipos[1] = TipoNotaFiscal.REEMBOLSO;
		tipos[2] = TipoNotaFiscal.ADIANTAMENTO;
		return tipos;
	}

	public TipoNotaFiscal[] getTiposNotaFiscal() {
		TipoNotaFiscal[] tipos = new TipoNotaFiscal[9];
		tipos[0] = TipoNotaFiscal.AVULSA;
		tipos[1] = TipoNotaFiscal.NORMAL;
		tipos[2] = TipoNotaFiscal.FUNDO_FIXO;
		tipos[3] = TipoNotaFiscal.REEMBOLSO;
		tipos[4] = TipoNotaFiscal.ADIANTAMENTO;
		tipos[5] = TipoNotaFiscal.OC_PC_FUNDO_FIXO;
		tipos[6] = TipoNotaFiscal.OC_PC_REEMBOLSO;
		tipos[7] = TipoNotaFiscal.OC_PC_ADIANTAMENTO;
		tipos[8] = TipoNotaFiscal.TRANSPORTE;
		return tipos;
	}

	public TipoContrato[] getTiposContratos() {
		return TipoContrato.values();
	}

	public SituacaoContrato[] getSituacoesContratos() {
		SituacaoContrato[] values = new SituacaoContrato[7];
		values[0] = SituacaoContrato.EM_ELABORACAO;
		values[1] = SituacaoContrato.EM_ANALISE;
		values[2] = SituacaoContrato.ATIVO;
		values[3] = SituacaoContrato.PARALISADO;
		values[4] = SituacaoContrato.ATIVO_PARALISADO;
		values[5] = SituacaoContrato.ENCERRADO;
		values[6] = SituacaoContrato.CANCELADO;
		return values;
	}

	public SituacaoContrato[] getSituacoesContratosFornecedores() {
		SituacaoContrato[] values = new SituacaoContrato[6];
		values[0] = SituacaoContrato.EM_ELABORACAO;
		values[1] = SituacaoContrato.ATIVO;
		values[2] = SituacaoContrato.PARALISADO;
		values[3] = SituacaoContrato.ATIVO_PARALISADO;
		values[4] = SituacaoContrato.ENCERRADO;
		values[5] = SituacaoContrato.CANCELADO;
		return values;
	}

	public TipoDiretoria[] getTiposDiretorias() {
		return TipoDiretoria.values();
	}

	public EstagioComercial[] getEstagiosComerciais() {
		return EstagioComercial.values();
	}

	public PeriodoReajuste[] getPeriodoReajustes() {
		return PeriodoReajuste.values();
	}

	public Prorrogacao[] getProrrogacoes() {
		return Prorrogacao.values();
	}

	public TipoNotaFiscal[] getTiposNotaFiscalAvulsa() {
		List<TipoNotaFiscal> tipos = new ArrayList<>(Arrays.asList(getTiposNotaFiscal()));
		tipos.remove(TipoNotaFiscal.NORMAL);
		return tipos.toArray(new TipoNotaFiscal[0]);
	}

	public br.com.araujoabreu.sgi.administracao.domain.Avaliacao.StatusAvaliacao[] getStatusAvaliacao() {
		return br.com.araujoabreu.sgi.administracao.domain.Avaliacao.StatusAvaliacao.values();
	}

	public Demonstrativo.Status[] getStatusDemonstrativo() {
		return Demonstrativo.Status.values();
	}

	public TipoFaturamento[] getTiposFaturamento() {
		return TipoFaturamento.values();
	}

	/**
	 * Retorna os enumerators de StatusRelacaoBancaria.
	 *
	 * @return Os enumerators StatusRelacaoBancaria.
	 */
	public StatusRelacaoBancaria[] getStatusRelacaoBancaria() {
		return new StatusRelacaoBancaria[]{
			StatusRelacaoBancaria.ABERTA,
			StatusRelacaoBancaria.A_PROCESSAR,
			StatusRelacaoBancaria.EM_PROCESSAMENTO,
			StatusRelacaoBancaria.AGUARDANDO_RETORNO,
			StatusRelacaoBancaria.PROCESSANDO_RETORNO,
			StatusRelacaoBancaria.FINALIZADA,
			StatusRelacaoBancaria.CANCELADA
		};
	}

	/**
	 * Retorna o Enum "CHEQUE" de meio de pagamento.
	 *
	 * @return O Enum "CHEQUE" de meio de pagamento.
	 */
	public MeioPagamento getMeioPagamento_Cheque() {
		return MeioPagamento.CHEQUE;
	}

	public br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface.SituacaoLoteInterface[] getOpcoesSelectStatusLoteContabil() {
		br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface.SituacaoLoteInterface[] values = new br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface.SituacaoLoteInterface[5];
		values[0] = br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface.SituacaoLoteInterface.CRIADO;
		values[1] = br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface.SituacaoLoteInterface.EMGERACAO;
		values[2] = br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface.SituacaoLoteInterface.GERADO;
		values[3] = br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface.SituacaoLoteInterface.IMPORTADO;
		values[4] = br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface.SituacaoLoteInterface.PROCESSADOCOMERROS;
		return values;
	}

	public TipoLoteInterface[] getOpcoesSelectTiposLoteContabil() {
		TipoLoteInterface[] values = new TipoLoteInterface[5];
		values[0] = TipoLoteInterface.Custos;
		values[1] = TipoLoteInterface.Novos_fornecedores;
		values[2] = TipoLoteInterface.Provisionamento;
		values[3] = TipoLoteInterface.Tesouraria;
		values[4] = TipoLoteInterface.NF_Devolucao;
		return values;
	}

	public EntityStatus[] getEntityStatus() {
		return EntityStatus.values();
	}

	public TipoDominio[] getDominios() {
		return TipoDominio.values();
	}

	public EntityStatus getEntityStatusEnabled() {
		return EntityStatus.ENABLED;
	}

	public EntityStatus getEntityStatusDisabled() {
		return EntityStatus.DISABLED;
	}

	public EscopoReajuste[] getEscopoReajuste() {
		return EscopoReajuste.values();
	}

	public Pessoa.Situacao[] getSituacoesPessoa() {
		Pessoa.Situacao[] situacao = new Pessoa.Situacao[2];
		situacao[0] = Pessoa.Situacao.ENABLED;
		situacao[1] = Pessoa.Situacao.DISABLED;
		return situacao;
	}

	public br.com.araujoabreu.sgi.enumerators.Situacao[] getSituacoesDefault() {
		br.com.araujoabreu.sgi.enumerators.Situacao[] situacao = new br.com.araujoabreu.sgi.enumerators.Situacao[2];
		situacao[0] = br.com.araujoabreu.sgi.enumerators.Situacao.ATIVO;
		situacao[1] = br.com.araujoabreu.sgi.enumerators.Situacao.INATIVO;
		return situacao;
	}

	public ESituacao[] getSituacoesOutroDesconto() {
		ESituacao[] situacao = new ESituacao[2];
		situacao[0] = ESituacao.ATIVO;
		situacao[1] = ESituacao.INATIVO;
		return situacao;
	}

	public Boolean[] getHomologado() {
		Boolean[] homologado = new Boolean[2];
		homologado[0] = Boolean.TRUE;
		homologado[1] = Boolean.FALSE;
		return homologado;
	}

	public Boolean[] getIsantoIcms() {
		Boolean[] isantoIcms = new Boolean[2];
		isantoIcms[0] = Boolean.TRUE;
		isantoIcms[1] = Boolean.FALSE;
		return isantoIcms;
	}

	public EscopoCompra[] getEscopoCompra() {
		return EscopoCompra.values();
	}

	public TipoCentroFinanceiro getCentroLucro() {
		return TipoCentroFinanceiro.CENTRO_LUCRO;
	}

	public TipoCentroFinanceiro getCentroCusto() {
		return TipoCentroFinanceiro.CENTRO_CUSTO;
	}

	public TipoPagamento getTipoNotaPagamento() {
		return TipoPagamento.NOTA_PAGAMENTO;
	}

	public TipoPagamento[] getTiposPagamento() {
		return TipoPagamento.values();
	}

	public String getNotaFiscalNormal() {
		return TipoNotaFiscal.NORMAL.toString();
	}

	public String getNotaFiscalAvulsa() {
		return TipoNotaFiscal.AVULSA.toString();
	}

	public TipoPagamento getTipoNotaFiscal() {
		return TipoPagamento.NOTA_FISCAL;
	}

	public TipoPagamento getTipoTributo() {
		return TipoPagamento.TRIBUTO;
	}

	public Pessoa.TipoPessoa getTipoPessoaFisica() {
		return Pessoa.TipoPessoa.PESSOA_FISICA;
	}

	public Pessoa.TipoPessoa getTipoPessoaJuridica() {
		return Pessoa.TipoPessoa.PESSOA_JURIDICA;
	}

	public Pessoa.TipoPessoa getTipoPessoaSimples() {
		return Pessoa.TipoPessoa.PESSOA_SIMPLES;
	}

	public Pessoa.TipoPessoa getTipoFavorecido() {
		return Pessoa.TipoPessoa.FAVORECIDO;
	}

	public Pessoa.TipoPessoa getTipoUnidade() {
		return Pessoa.TipoPessoa.UNIDADE;
	}

	public String getNaoInformadoLabel() {
		return NAO_INFORMADO_LABEL;
	}

	public Integer getPopupMaxResults() {
		return POPUP_MAX_RESULTS;
	}

	public Integer getCodigoNotaEletronica() {
		return CODIGO_NOTA_ELETRONICA;
	}

	public MeioPagamento[] getMeiosPagamento() {
		return MeioPagamento.values();
	}

	public MeioPagamento[] getMeiosPagamentoPesquisa() {
		return new MeioPagamento[]{MeioPagamento.INTERNET_BANKING, MeioPagamento.CHEQUE, MeioPagamento.BANKLINE_MANUAL};
	}

	public Date getCurrentDate() {
		return new Date();
	}

	public String getDataPorExtenso(Date data) {
		DateFormat dfmt = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy");
		return dfmt.format(data);
	}

	public MeioPagamento getTipoCheque() {
		return MeioPagamento.CHEQUE;
	}

	public Integer getDefaultFirstResult() {
		return 0;
	}

	public String formatDate(final Date date) {
		return DATE_FORMAT.format(date);
	}

	public StatusProcesso[] getStatusProcesso() {
		return StatusProcesso.values();
	}

	public Meses[] getMeses() {
		return Meses.values();
	}

	public ClassificacaoContaContabil[] getClassificacoesContaContabil() {
		return ClassificacaoContaContabil.values();
	}

	public TipoContaContabil[] getTiposContaContabil() {
		return TipoContaContabil.values();
	}

	private ESTADOS stringToEnumUf(final String valor) {
		for (ESTADOS estado : ESTADOS.values()) {
			if (valor.equals(estado.toString())) {
				return estado;
			}
		}
		return null;
	}

	public String formatarInscricao(final String ie, final String uf, final String im) {
		if (ie != null && !ie.trim().equals("")) {
			if (uf != null) {
				return ie + " / " + uf;
			} else {
				return ie + " / " + uf;
			}
		} else if (im != null) {
			return im;
		}

		return null;
	}

	public TipoDocumentoNota[] getTiposDocumentosNota() {
		return TipoDocumentoNota.values();
	}

	/**
	 * Retorna array de tipos de documento para pagamento, não de {@link Nota}.
	 *
	 * @return Array de tipos de documento para pagamento, não de {@link Nota}.
	 * @see Pagamento.TipoDocumentoPagamento
	 */
	public Pagamento.TipoDocumentoPagamento[] getTipoDocumentoPagamento() {
		return Pagamento.TipoDocumentoPagamento.values();
	}

	public String getFormatoIE(final String valor) {
		if (valor != null && !valor.isEmpty()) {
			ESTADOS estado = stringToEnumUf(valor);
			if (estado != null) {
				switch (estado) {
					case AL:
					case AM:
					case AP:
					case CE:
					case GO:
					case MA:
					case MS:
					case PA:
					case PI:
					case RN:
					case TO:
					case PB:
						return "'99.999.999-9'";

					case MT:
					case SE:

					case RR:
						return "'99999999-9'";

					case PE:
						return "'9999999-99'";

					case ES:
						return "'999.999.99-9'";

					case RJ:
						return "'99.999.999'";

					case AC:
					case DF:
						return "'99.999.999/999-99'";

					case SC:
					case BA:
						return "'999.999.999'";

					case MG:
						return "'999.999.999/9999'";
					case SP:
						return "'999.999.999.999'";
					case PR:
						return "'999.99999-99'";
					case RS:
						return "'999/9999999'";
					case RO:
						return "'9999999999999-9'";

					default:
						return "";
				}
			}
		}

		return "";
	}

	public String getCodigoUfNFe(NotaSaidaVO notaSaidaVO) {
		String codigoUF = notaSaidaVO.getNotaSaida().getEmitente().getEndereco().getUf();
		if (codigoUF != null && !codigoUF.isEmpty()) {
			ESTADOS estado = stringToEnumUf(codigoUF);
			switch (estado) {
				case AC:
					return "12";
				case AL:
					return "27";
				case AM:
					return "16";
				case AP:
					return "13";
				case BA:
				case RR:
					return "29";
				case CE:
					return "23";
				case DF:
					return "53";
				case ES:
					return "32";
				case GO:
					return "52";
				case MA:
					return "21";
				case MG:
					return "31";
				case MS:
					return "50";
				case MT:
					return "51";
				case PA:
					return "15";
				case PB:
					return "26";
				case PI:
					return "22";
				case PR:
					return "41";
				case RJ:
					return "33";
				case RN:
					return "24";
				case RO:
					return "11";
				case RS:
					return "43";
				case SE:
					return "28";
				case SP:
					return "35";
				case TO:
					return "17";

				default:
					return "  ";
			}
		}
		return codigoUF;

	}

	public String getCurrentDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
		return dateFormat.format(new Date());
	}

	public Colaborador pessoaToColaborador(Pessoa pessoa) {
		if (pessoa != null) {
			return ((Colaborador) pessoa);
		}
		return null;
	}

	public TipoMensagem[] getTiposMensagens() {
		return TipoMensagem.values();
	}

	/**
	 * Tipos de prestação de contas (fundo fixo).
	 *
	 * @return Array de tipos de conta (fundo fixo).
	 */
	public TipoConta[] getTiposConta() {
		return new TipoConta[]{TipoConta.FUNDO_FIXO, TipoConta.REEMBOLSO, TipoConta.ADIANTAMENTO};
	}

	public TipoAcesso[] getTiposAcesso() {
		return new TipoAcesso[]{TipoAcesso.INTERNO, TipoAcesso.FORNECEDOR};
	}

	public String abreviarNomes(String nome, int limite, int posicoes) {
		if (nome != null && !nome.isEmpty()) {
			if (nome.length() > limite) {
				return nome.substring(0, posicoes) + "...";
			} else {
				return nome;
			}
		}
		return "";
	}

	public String abreviarNomeSemReticencias(String nome, int limite, int posicoes) {
		if (nome != null && !nome.isEmpty()) {
			if (nome.length() > limite) {
				return nome.substring(0, posicoes);
			} else {
				return nome;
			}
		}
		return "";
	}

	public boolean isEmpty(String str) {
		if (str == null)
			return true;

		return StringUtils.isEmpty(str);

	}

	public Pessoa.Notificacao[] getNotificacoes() {
		Pessoa.Notificacao[] values = new Pessoa.Notificacao[2];
		values[0] = Pessoa.Notificacao.NENHUM;
		values[1] = Pessoa.Notificacao.SMS;
		return values;
	}

	public String getCidadeEstadoISS(List<NotaTributoAliquota> lista) {
		NotaTributoAliquota ntaISS = null;
		if (lista != null && !lista.isEmpty()) {
			for (NotaTributoAliquota nta : lista) {
				if (nta.getAliquota() != null && nta.getAliquota().getCategoriaTributo() != null
					&& nta.getAliquota().getCategoriaTributo().getTributo() != null
					&& nta.getAliquota().getCategoriaTributo().getTributo().getNome().trim().equalsIgnoreCase("ISS")) {
					ntaISS = nta;
					break;
				}
			}
		}

		if (ntaISS != null && ntaISS.getAliquota().getCategoriaTributo() != null && ntaISS.getAliquota().getCategoriaTributo().getCidade() != null) {
			return ntaISS.getAliquota().getCategoriaTributo().getCidade().getCidadeEstado();
		}
		return "";
	}

	@Transient
	public String getCidadeEstadoCredorNota(Nota nota) {
		if (nota.getCredor() != null && nota.getCredor().getEndereco() != null && nota.getCredor().getEndereco().getCidadeUf() != null) {
			return nota.getCredor().getEndereco().getCidadeUf();
		}
		return "";
	}

	@Transient
	public boolean isNotaCarioca(List<NotaTributoAliquota> lista) {
		NotaTributoAliquota ntaISS = null;
		if (lista != null && !lista.isEmpty()) {
			for (NotaTributoAliquota nta : lista) {
				if (nta.getAliquota() != null && nta.getAliquota().getCategoriaTributo() != null
					&& nta.getAliquota().getCategoriaTributo().getTributo() != null
					&& nta.getAliquota().getCategoriaTributo().getTributo().getNome().trim().equalsIgnoreCase("ISS")) {
					ntaISS = nta;
					break;
				}
			}
		}

		if (ntaISS != null && ntaISS.getValidaPrefeitura() != null && ntaISS.getValidaPrefeitura())
			return true;
		return false;
	}

	public static enum ESTADOS {
		AC, AL, AM, AP, BA, CE, DF, ES, GO, MA, MG, MS, MT, PA, PB, PE, PI, PR, RJ, RN, RO, RR, RS, SC, SE, SP, TO
	}

	public static enum ESTADOSENDERECO {
		AC("AC", "Acre"),
		AL("AL", "Alagoas"),
		AM("AM", "Amazonas"),
		AP("AP", "Amapá"),
		BA("BA", "Bahia"),
		CE("CE", "Ceará"),
		DF("DF", "Distrito Federal"),
		ES("ES", "Espírito Santo"),
		GO("GO", "Goiás"),
		MA("MA", "Maranhão"),
		MG("MG", "Minas Gerais"),
		MS("MS", "Mato Grosso do Sul"),
		MT("MT", "Mato Grosso"),
		PA("PA", "Pará"),
		PB("PB", "Paraíba"),
		PE("PE", "Pernambuco"),
		PI("PI", "Piauí"),
		PR("PR", "Paraná"),
		RJ("RJ", "Rio de Janeiro"),
		RN("RN", "Rio Grande do Norte"),
		RO("RO", "Rondônia"),
		RR("RR", "Roraima"),
		RS("RS", "Rio Grande do Sul"),
		SC("SC", "Santa Catarina"),
		SE("SE", "Sergipe"),
		SP("SP", "São Paulo"),
		TO("TO", "Tocantins");

		private final String sigla;
		private final String nome;

		ESTADOSENDERECO(String sigla, String nome) {
			this.nome = nome;
			this.sigla = sigla;
		}

		public String getNome() {
			return nome;
		}

		public String getSigla() {
			return sigla;
		}
	}

	public enum StatusPagamentoPesquisa {
		ABERTO("Nota Fiscal cadastrada e não enviada para aprovação", Status.ABERTO),
		REABERTO("Nota Fiscal retornada para ajustes", Status.REABERTO),
		APROVACAO("Aguardando aprovação", Status.APROVACAO),
		AGUARDANDO_LIBERACAO_RN("Aguardando liberação da RN", Status.AGUARDANDO_LIBERACAO_RN),
		AJUSTE_FISCAL("Em análise pelo Controle Fiscal", Status.AJUSTE_FISCAL),
		ANALISE("Em análise pelo Contas a Pagar", Status.ANALISE),
		AUTORIZACAO("Aguardando autorização da Diretoria", Status.AUTORIZACAO),
		AUTORIZADO("Aguardando liberação para envio a Tesouraria", Status.AUTORIZADO),
		PAGAMENTO("Liberado para a Tesouraria", Status.PAGAMENTO),
		RELACIONADO("Em relação bancária para envio ao banco", Status.RELACIONADO),
		FINALIZADO("Pago (após retorno do banco)", Status.FINALIZADO),
		CANCELAMENTO("Em cancelamento pelo Diretor Financeiro", Status.CANCELAMENTO),
		CANCELADO("Cancelado pelo Diretor Financeiro", Status.CANCELADO),
		EM_HOMOLOGACAO_FUNDO_FIXO("Aguardando homologação (Fundo Fixo)", Status.EM_HOMOLOGACAO_FUNDO_FIXO);

		private final String descricao;
		private final Status status;
		StatusPagamentoPesquisa(String nome, Status status) {
			this.descricao = nome;
			this.status = status;
		}

		public String getDescricao() {
			return descricao;
		}

		public Status getStatus() {
			return status;
		}

	}

	public static enum EnumSelectContratoServicoExtra {
		CONTRATO, SERVICO_EXTRA, CLIENTE
	}

}
