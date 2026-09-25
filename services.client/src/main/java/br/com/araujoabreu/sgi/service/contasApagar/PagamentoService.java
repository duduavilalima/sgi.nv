package br.com.araujoabreu.sgi.service.contasApagar;

import br.com.araujoabreu.sgi.service.compras.transfer.ParcelaNotaTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.lang.ValidationException;
import com.querydsl.jpa.impl.JPAQuery;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public interface PagamentoService extends CrudService<PagamentoTO> {

	BigDecimal obterValorPagar(Long codigoPagamento, String tipoPagamento);

	BigDecimal obterValorPagar(Long codigoPagamento);

	BigDecimal obterValorLiquido(Long codigoPagamento, String tipoPagamento);

	BigDecimal calcularTotalAcumulado(Long codigoPagamento, String tipoPagamento);

	BigDecimal getTotalOutrosDescontos(Long codigoPagamento);

	BigDecimal tributoMaisOutrosDesc(Long codigoPagamento, String tipoPagamento);

	void retornar(PagamentoTO pagamento, String status, Boolean atualizar) throws ValidationException;

	Long getCodigoNota(Long codigoPagamento);

	String obterNomeTributo(Long codigoPagamento, String tipoPagamento);

	PagamentoTO obterPagamento(Long codigo);

	PagamentoTO obterPagamentoEDadosBancariosPeloCodigoPagamento(Long codigoPagamento);

	PagamentoTO obterPagamentoSimples(Long codigo);

	JPAQuery jpaQueryPagamento();

	PagamentoPesquisaTO getPagamentoPopUp(Long id, String tipo, Long codNota);

	ParcelaNotaTO getParcelaNotaByPagamento(Long codNota);

	Date recalcularDataVencimento(Long codigoNotaTributoAliquota, Long codigoRelacaoBancaria);

	long updateTipoDocumentoPagamentoBoleto(Long codigo);

	long updateTipoDocumentoPagamentoDeposito(Long codigo);

	long updateTipoDocumentoPagamentoDda(Long codigo);

	long updateTipoDocumentoPagamentoGuia(Long codigo);

	long updateTipoDocumentoPagamentoDoc(Long codigo);

	long updateTipoDocumentoPagamentoTed(Long codigo);

	long updateTipoDocumentoPagamentoConcessionaria(Long codigo);

	long updateTipoDocumentoPagamentoOutros(Long codigo);

	long updateDataPagamento(Date dataPagamento, Long codigo);

	long updateDataVencimento(Date dataVencimento, Long codigo);

	long updateCodigoBarras(String codigoBarras, Long codigo);

	long updateLinhaDigitavel(String linhaDigitavel, Long codigo);

	long updateDadosBancarios(Long codigo, Long codigoDadosBancarios);

	long updateStatusAberto(Long codigo);

	long updateStatusAjusteFiscal(Long codigo);

	long updateStatusAnalise(Long codigo);

	long updateStatusReaberto(Long codigo);

	long updateStatusAutorizacao(Long codigo);

	long updateStatusCancelamento(Long codigo);

	long updateStatusCancelado(Long codigo);

	long updateStatusAutorizado(Long codigo);

	long updateStatusPagamento(Long codigo);

	long updateStatusRelacionado(Long codigo);

	long updateStatusFinalizado(Long codigo);

	long updateStatusAprovacao(Long codigo);

	long updateStatusAguardandoAgrupamento(Long codigo);

	long updateStatusAguardandoAgrupado(Long codigo);

	long updateStatusEmHomologacaoFundoFixo(Long codigo);

	long updateStatusAguardandoLiberacaoRn(Long codigo);

	long updateStatusNaoFinalizado(Long codigo);

	JPAQuery<?> findByTributoAcumuladoCodigo(Long tributoAcumuladoCodigo);

	JPAQuery<?> findByItemRelacaoPagamentoCodigo(Long codigo);

	ParcelaNotaTO mockLoteCustos();

}
