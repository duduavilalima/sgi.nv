package br.com.araujoabreu.sgi.service.impl.funcionalidade;

import br.com.araujoabreu.sgi.service.funcionalidade.FuncionalidadeService;
import br.com.araujoabreu.sgi.service.funcionalidade.transfer.FuncionalidadePesquisaTO;
import br.com.araujoabreu.sgi.service.funcionalidade.transfer.FuncionalidadeTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Funcionalidade;
import br.com.fasolti.sgi.repository.menu.FuncionalidadeRepository;
import org.springframework.stereotype.Service;

@Service
public class FuncionalidadeServiceImpl extends BaseSearchService<Funcionalidade, FuncionalidadeTO, FuncionalidadePesquisaTO, FuncionalidadeRepository> implements FuncionalidadeService {

	public FuncionalidadeServiceImpl(FuncionalidadeRepository repository) {
		super(repository);
	}
}
