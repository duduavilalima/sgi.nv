package br.com.araujoabreu.sgi.service.faturamento;

import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaCheckDescricaoTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface FaturamentoEtapaService extends CrudService<FaturamentoEtapaTO> {
	List<Long> obterIdsANDCodigoNotaSaidaEtapasDeUmFaturamento(Long codigoFaturamento);

	FaturamentoEtapaTO getFaturamentoEtapaByNotaSaidaId(Long codigo);

	FaturamentoEtapaTO getFaturamentoEtapaByNotaDebitoId(Long codigo);

	FaturamentoEtapaTO getFaturamentoEtapaByCodigo(Long codigo);

	FaturamentoEtapaTO pesquisarFaturamentoEtapaPorCodigoNotaSaidaComCodigoUnidadeCentroFinanceiro(Long codigoNotaSaida);

	FaturamentoEtapaTO obterFaturamentoEtapaParaNotaFiscal(Long codigo);

	FaturamentoEtapaTO obterFaturamentoEtapaParaNotaFiscalSemFaturamento(Long codigo);

	List<FaturamentoEtapaTO> obterFaturamentoEtapaPorNotaSaida(Long codigoNotaSaida);

	List<FaturamentoEtapaTO> obterFaturamentoEtapaPorCodFaturamento(Long codigoFaturamento);

	FaturamentoEtapaTO obterFaturamentoEtapaPorNotaSaida2(Long codigoNotaSaida);

	FaturamentoEtapaTO obterFaturamentoEtapaTelaNotaFiscalComCodigoFaturamento(Long codigoNotaSaida);

	JPAQuery<?> obterFaturamentoEtapaSimplesListaCodigoNotaSaida(Long codigoNotaSaida);

	JPAQuery jpaQueryFaturamentoEtapa();

	JPAQuery<?> obterFaturamentoEtapaMinimoListaCodigoNotaSaida(Long codigoNotaSaida);

	FaturamentoEtapaTO obterProximoNumeroEtapaByFatCod(Long codigoFaturamento);

	void updateSituacaoAFaturar(Long codigo);

	void updateSituacaoFaturada(Long codigo);

	void updateSituacaoRecebida(Long codigo);

	void updateSituacaoCancelada(Long codigo);

	void updateNotaSaida(Long codigo, Long codigoNotaSaida);

	void updateDataAtualizacao(Long codigo, Date date);

	void updateAtualizador(Long codigo, String username);

	void updateUltimoValor(Long codigo, BigDecimal valor);

	void updateValor(Long codigo, BigDecimal valor);

	void updateDataUltAlteracaoValor(Long codigo, Date dtAlteracao);

	void updateCodigoAlteradorValor(Long codigo, Long userCod);

	FaturamentoEtapaCheckDescricaoTO verificarDescricaoEtapaDeFaturamento(FaturamentoEtapaCheckDescricaoTO faturamentoEtapaCheckDescricaoTO);

	JPAQuery<?> montarNotaFiscalFaturamento(FaturamentoTO exampleObj, PageRequest page);

	JPAQuery<?> montarNotaDebitoFaturamento(FaturamentoTO exampleObj, PageRequest page);

	ValueTO etapaAFaturarCheck(Long valueOf);

	List<FaturamentoEtapaTO> obterFaturamentoEtapaPorNotaDebito(Long codigo);

	void updateNotaDebito(Long codigo, Object o);
}
