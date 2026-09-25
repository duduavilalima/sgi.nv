package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.compras.domain.MotivoDescontoDevolucao;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcelaMotivoDescDev;
import br.com.araujoabreu.sgi.compras.domain.QNotaSaidaParcelaMotivoDescDev;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoNotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaMotivoDescDevService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaMotivoDescDevPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaMotivoDescDevTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.motivoDescontoDevolucao.MotivoDescontoDevolucaoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaMotivoDescDevRepository;
import com.querydsl.core.types.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotaSaidaParcelaMotivoDescDevServiceImpl extends BaseSearchService<NotaSaidaParcelaMotivoDescDev, NotaSaidaParcelaMotivoDescDevTO, NotaSaidaParcelaMotivoDescDevPesquisaTO, NotaSaidaParcelaMotivoDescDevRepository> implements NotaSaidaParcelaMotivoDescDevService {

	private final QNotaSaidaParcelaMotivoDescDev qNotaSaidaParcelaMotivoDescDev = QNotaSaidaParcelaMotivoDescDev.notaSaidaParcelaMotivoDescDev;

	@Autowired
	private MotivoDescontoDevolucaoRepository motivoDescontoDevolucaoRepository;

	@Autowired
	private MotivoRetornoDocumentoNotaSaidaParcelaService motivoRetornoDocumentoNotaSaidaParcelaService;

	@Autowired
	public NotaSaidaParcelaMotivoDescDevServiceImpl(NotaSaidaParcelaMotivoDescDevRepository repository) {
		super(repository);
	}

	@Override
	public List<NotaSaidaParcelaMotivoDescDevTO> findNotaSaidaParcelaMotivoDescDev(Long codigoNotaSaidaParcela) {
		List<NotaSaidaParcelaMotivoDescDev> list = select(Projections.fields(NotaSaidaParcelaMotivoDescDev.class,
			qNotaSaidaParcelaMotivoDescDev.codigo,
			qNotaSaidaParcelaMotivoDescDev.codigoMotivoDescontoDevolucao,
			qNotaSaidaParcelaMotivoDescDev.codigoNotaSaidaParcela,
			qNotaSaidaParcelaMotivoDescDev.valor,
			qNotaSaidaParcelaMotivoDescDev.tipo,
			Projections.fields(MotivoDescontoDevolucao.class,
				qNotaSaidaParcelaMotivoDescDev.motivoDescontoDevolucao.codigo,
				qNotaSaidaParcelaMotivoDescDev.motivoDescontoDevolucao.descricao
			).as(qNotaSaidaParcelaMotivoDescDev.motivoDescontoDevolucao)))
			.where(qNotaSaidaParcelaMotivoDescDev.codigoNotaSaidaParcela.eq(codigoNotaSaidaParcela))
			.fetch();
		return map(list);
	}

	@Override
	public void salvarNotaSaidaParcelaMotivoDescDev(NotaSaidaParcelaTO transferObj) {
		selectDelete().where(qNotaSaidaParcelaMotivoDescDev.codigoNotaSaidaParcela.eq(transferObj.getCodigo())).execute();
		for (NotaSaidaParcelaMotivoDescDevTO notaSaidaParcelaMotivoDescDevTO : transferObj.getListaNotaSaidaParcelaMotivoDescDev()) {
			NotaSaidaParcelaMotivoDescDev notaSaidaParcelaMotivoDescDev = new NotaSaidaParcelaMotivoDescDev();
			notaSaidaParcelaMotivoDescDev.setCodigoMotivoDescontoDevolucao(notaSaidaParcelaMotivoDescDevTO.getCodigoMotivoDescontoDevolucao());
			notaSaidaParcelaMotivoDescDev.setCodigoNotaSaidaParcela(notaSaidaParcelaMotivoDescDevTO.getCodigoNotaSaidaParcela());
			notaSaidaParcelaMotivoDescDev.setTipo(notaSaidaParcelaMotivoDescDevTO.getTipo().equalsIgnoreCase("DESCONTO") ? NotaSaidaParcelaMotivoDescDev.Tipo.DESCONTO : NotaSaidaParcelaMotivoDescDev.Tipo.DEVOLUCAO);
			notaSaidaParcelaMotivoDescDev.setValor(notaSaidaParcelaMotivoDescDevTO.getValor());
			notaSaidaParcelaMotivoDescDev.setMotivoDescontoDevolucao(motivoDescontoDevolucaoRepository.findById(notaSaidaParcelaMotivoDescDevTO.getCodigoMotivoDescontoDevolucao()).get());
			getRepository().save(notaSaidaParcelaMotivoDescDev);
		}
	}

	@Override
	public void deletarPorParcela(Long codigoNotaSaidaParcela) {
		motivoRetornoDocumentoNotaSaidaParcelaService.deletarPorParcela(codigoNotaSaidaParcela);
		selectDelete().where(qNotaSaidaParcelaMotivoDescDev.codigoNotaSaidaParcela.eq(codigoNotaSaidaParcela)).execute();
	}
}
