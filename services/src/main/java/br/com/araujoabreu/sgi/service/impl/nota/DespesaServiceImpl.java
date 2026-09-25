package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.compras.domain.Despesa;
import br.com.araujoabreu.sgi.compras.domain.QDespesa;
import br.com.araujoabreu.sgi.service.nota.DespesaService;
import br.com.araujoabreu.sgi.service.nota.transfer.DespesaPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.DespesaTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.despesa.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class DespesaServiceImpl extends BaseSearchService<Despesa, DespesaTO, DespesaPesquisaTO, DespesaRepository> implements DespesaService {

	private static final QDespesa qDespesa = QDespesa.despesa;

	@Autowired
	public DespesaServiceImpl(DespesaRepository repository) {
		super(repository);
	}

	@Override
	@Transactional
	public void updateValor(Long codigo, BigDecimal valor) {
		selectUpdate().where(qDespesa.codigo.eq(codigo)).set(qDespesa.valor, valor).execute();
	}

	@Override
	@Transactional
	public void updateAtualizador(Long codigo, String atualizador) {
		selectUpdate().where(qDespesa.codigo.eq(codigo)).set(qDespesa.atualizador, atualizador).execute();
	}

	@Override
	@Transactional
	public void updateDataAtualizacao(Long codigo, Date dataAtualizacao) {
		selectUpdate().where(qDespesa.codigo.eq(codigo)).set(qDespesa.dataAtualizacao, dataAtualizacao).execute();
	}
}
