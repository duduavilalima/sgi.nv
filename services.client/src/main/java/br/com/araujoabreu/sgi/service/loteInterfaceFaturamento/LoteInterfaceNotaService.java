package br.com.araujoabreu.sgi.service.loteInterfaceFaturamento;

import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceNotaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface LoteInterfaceNotaService extends CrudService<LoteInterfaceNotaTO> {
	List<LoteInterfaceNotaTO> findNotaByCodigoLote(Long codigo);

	JPAQuery findNotaByCodigoLoteReturnJPAQuery(Long codigo);

	List<LoteInterfaceNotaTO> findListaByCodNota(Long codigo);

	List<LoteInterfaceNotaTO> findByCodNotaTipo(Long codigo, Long codTipo);

	void atualizarNfEmRecebimentoFaturamento(NotaFiscalTO nfTO);

	void reprocessarNfEmRecebimentoFaturamento(NotaFiscalTO nfTO);

}
