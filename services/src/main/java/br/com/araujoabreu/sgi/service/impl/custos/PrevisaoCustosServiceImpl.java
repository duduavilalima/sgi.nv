package br.com.araujoabreu.sgi.service.impl.custos;

import br.com.araujoabreu.sgi.custos.PrevisaoCustos;
import br.com.araujoabreu.sgi.service.custos.PrevisaoCustosService;
import br.com.araujoabreu.sgi.service.custos.transfer.PrevisaoCustosPesquisaTO;
import br.com.araujoabreu.sgi.service.custos.transfer.PrevisaoCustosTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.custos.PrevisaoCustosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrevisaoCustosServiceImpl extends BaseSearchService<PrevisaoCustos, PrevisaoCustosTO, PrevisaoCustosPesquisaTO, PrevisaoCustosRepository> implements PrevisaoCustosService {

	@Autowired
	public PrevisaoCustosServiceImpl(PrevisaoCustosRepository repository) {
		super(repository);
	}

}
