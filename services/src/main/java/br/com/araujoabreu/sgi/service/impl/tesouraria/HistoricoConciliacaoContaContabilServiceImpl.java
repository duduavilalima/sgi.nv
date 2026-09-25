package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.HistoricoConciliacaoContaContabilService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoContaContabilPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoContaContabilTO;
import br.com.araujoabreu.sgi.tesouraria.domain.HistoricoConciliacaoContaContabil;
import br.com.araujoabreu.sgi.tesouraria.domain.QHistoricoConciliacaoContaContabil;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.tesouraria.HistoricoConciliacaoContaContabilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
public class HistoricoConciliacaoContaContabilServiceImpl extends BaseSearchService<HistoricoConciliacaoContaContabil, HistoricoConciliacaoContaContabilTO, HistoricoConciliacaoContaContabilPesquisaTO, HistoricoConciliacaoContaContabilRepository> implements HistoricoConciliacaoContaContabilService {

	@SuppressWarnings("unused")
	private static final QHistoricoConciliacaoContaContabil qHistoricoConciliacaoContaContabil = QHistoricoConciliacaoContaContabil.historicoConciliacaoContaContabil;

	@Autowired
	public HistoricoConciliacaoContaContabilServiceImpl(HistoricoConciliacaoContaContabilRepository repository) {
		super(repository);
	}

	@Override
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/remover")
	@Transactional
	public void remover(@PathVariable Long id) {
		HistoricoConciliacaoContaContabil contaContabil = getRepository().findById(id).orElse(null);
		getRepository().delete(contaContabil);
	}
}
