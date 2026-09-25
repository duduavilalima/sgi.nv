package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcelaRetencao;
import br.com.araujoabreu.sgi.compras.domain.QNotaSaidaParcelaRetencao;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaRetencaoService;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaRetencaoPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaRetencaoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRetencaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NotaSaidaParcelaRetencaoServiceImpl extends BaseSearchService<NotaSaidaParcelaRetencao, NotaSaidaParcelaRetencaoTO, NotaSaidaParcelaRetencaoPesquisaTO, NotaSaidaParcelaRetencaoRepository> implements NotaSaidaParcelaRetencaoService {

	private static final QNotaSaidaParcelaRetencao qNotaSaidaParcelaRetencao = QNotaSaidaParcelaRetencao.notaSaidaParcelaRetencao;

	@Autowired
	private NotaSaidaParcelaService notaSaidaParcelaService;

	@Autowired
	private NotaSaidaParcelaRetencaoRepository notaSaidaParcelaRetencaoRepository;

	@Autowired
	public NotaSaidaParcelaRetencaoServiceImpl(NotaSaidaParcelaRetencaoRepository repository) {
		super(repository);
	}

	@Override
	public void salvarNovaNotaSaidaParcelaRetencaoACESSORIA(Long codigoNotaSaidaParcela, BigDecimal valorRetido) {
		salvarParcelaRetencao(NotaSaidaParcelaRetencao.TipoRetencao.ACESSORIA, codigoNotaSaidaParcela, valorRetido);
	}

	@Override
	public void salvarNovaNotaSaidaParcelaRetencaoCONTRATUAL(Long codigoNotaSaidaParcela, BigDecimal valorRetido) {
		salvarParcelaRetencao(NotaSaidaParcelaRetencao.TipoRetencao.CONTRATUAL, codigoNotaSaidaParcela, valorRetido);
	}

	private void salvarParcelaRetencao(NotaSaidaParcelaRetencao.TipoRetencao tipo, Long codigoNotaSaidaParcela, BigDecimal valorRetido) {
		NotaSaidaParcela notaSaidaParcela = (NotaSaidaParcela) notaSaidaParcelaService.obterNotaSaidaParcelaApenasComCodigo(codigoNotaSaidaParcela).fetchOne();
		NotaSaidaParcelaRetencao valorRetencaoAcessoria = new NotaSaidaParcelaRetencao();
		valorRetencaoAcessoria.setParcela(notaSaidaParcela);
		valorRetencaoAcessoria.setTipo(tipo);
		valorRetencaoAcessoria.setValorAReter(valorRetido);
		valorRetencaoAcessoria.setValorRetido(valorRetido);
		notaSaidaParcelaRetencaoRepository.save(valorRetencaoAcessoria);
	}

	@Override
	public void updateValorNotaSaidaParcelaRetencao(Long codigo, BigDecimal valor) {
		selectUpdate()
			.set(qNotaSaidaParcelaRetencao.valorRetido, valor)
			.set(qNotaSaidaParcelaRetencao.valorAReter, valor)
			.where(qNotaSaidaParcelaRetencao.codigo.eq(codigo))
			.execute();
	}

	@Override
	public long deleteNotaSaidaParcelaRetencaoDaParcela(Long codigo) {
		return selectDelete().where(qNotaSaidaParcelaRetencao.parcela.codigo.eq(codigo)).execute();
	}
}
