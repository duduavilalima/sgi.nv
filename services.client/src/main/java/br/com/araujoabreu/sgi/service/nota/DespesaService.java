package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.nota.transfer.DespesaTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.math.BigDecimal;
import java.util.Date;

public interface DespesaService extends CrudService<DespesaTO> {
	void updateValor(Long codigo, BigDecimal valor);

	void updateAtualizador(Long codigo, String atualizador);

	void updateDataAtualizacao(Long codigo, Date dataAtualizacao);
}
