package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.lang.ValidationException;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface NotaSaidaParcelaService extends CrudService<NotaSaidaParcelaTO> {
	List<NotaSaidaParcelaTO> getListaParcelaByNotaSaidaId(Long codigo);

	NotaSaidaParcelaTO findByCodigo(Long codigo);

	JPAQuery jpaQueryNotaSaidaParcel();

	JPAQuery<?> obterNotaSaidaParcelaBasica(Long codigo);

	JPAQuery<?> findNotaSaidaByCodigoNotaSaidaParcela(Long codigo);

	JPAQuery<?> findNotaSaidaByCodigoNotaSaida(Long codigoNotaSaida);

	JPAQuery<?> findNotaSaidaByCodigoNotaSaidaNotFinalizada(Long codigoNotaSaida);

	JPAQuery<?> obterNotaSaidaParcelaApenasComCodigo(Long codigo);

	NotaSaidaParcelaTO obterNotaSaidaParcelaBasicaCodigoOrdem(Long codigo);

	JPAQuery<?> obterJpaQueryNotaSaidaParcelaBasicaCodigoSituacao(Long codigo);

	void updateAtualizadorAndData(Long codigo);

	void updateSituacaoEmRecebimento(Long codigo);

	void updateSituacaoAberta(Long codigo);

	void updateSituacaoFinalizada(Long codigo);

	void updateSituacaoCancelada(Long codigo);

	void cancelamento(Long codigo);

	@Transactional()
	void atualizarBaixaRetencaoRemovidaValor(long codigoNotaSaidaParcela, BigDecimal valorBaixa);

	@Transactional()
	void atualizarBaixaRetencaoValor(long codigoNotaSaidaParcela, BigDecimal valorBaixa, BigDecimal valorJuros);

	ValueTO verificarLoteLoteContabilDiario(long codigoNotaSaidaParcela) throws ValidationException;
}
