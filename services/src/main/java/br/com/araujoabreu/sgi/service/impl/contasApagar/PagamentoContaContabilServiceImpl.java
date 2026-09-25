package br.com.araujoabreu.sgi.service.impl.contasApagar;

import br.com.araujoabreu.sgi.contasApagar.domain.PagamentoContaContabil;
import br.com.araujoabreu.sgi.service.contasApagar.PagamentoContaContabilService;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoContaContabilPesquisaTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoContaContabilTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoContaContabilRepository;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagamentoContaContabilServiceImpl extends BaseSearchService<PagamentoContaContabil, PagamentoContaContabilTO, PagamentoContaContabilPesquisaTO, PagamentoContaContabilRepository> implements PagamentoContaContabilService {

	@Autowired
	public PagamentoContaContabilServiceImpl(PagamentoContaContabilRepository repository) {
		super(repository);
	}

	@Override
	public JPAQuery jpaQueryPagamentoContaContabil() {
		return select();
	}
}
