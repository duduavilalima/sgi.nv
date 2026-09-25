package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.core.Tuple;

import java.util.Date;
import java.util.List;

public interface NotaFiscalService extends CrudService<NotaFiscalTO> {

	NotaFiscalTO buscarNotaFiscalById(Long id);

	void updateTipoNotaFiscalAvulsa(Long codigo);

	void updateTipoNotaFiscalNormal(Long codigo);

	void updateTipoNotaFiscalFundoFixo(Long codigo);

	void updateTipoNotaFiscalReembolso(Long codigo);

	void updateTipoNotaFiscalTransporte(Long codigo);

	void updateTipoNotaFiscalAdiantamento(Long codigo);

	void updateTipoNotaFiscalDevolucaoPartical(Long codigo);

	void updateTipoNotaFiscalDevolucaoTotal(Long codigo);

	void updateTipoNotaFiscalOcPcFundoFixo(Long codigo);

	void updateTipoNotaFiscalOcPcReembolso(Long codigo);

	void updateTipoNotaFiscalOcPcAdiantamento(Long codigo);

	void updateTipoNotaFiscalFaturamento(Long codigo);

	void updateTipoNotaFiscalFaturamentoExclusaoLogica(Long codigo);

	void cancelamento(Long codigo);

	List<Tuple> listarNFsPorData(Date date);
}
