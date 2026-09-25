package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.Date;

public interface NotaService extends CrudService<NotaTO> {
	void updateStatusByLoteInterfaceNotaEmRecebimentoFaturamento(Long codigoLoteInterfaceNota);

	void updateDataReversaoCancelamento(Long codigo, Date dataReversaoCancelamento);

	void updateResponsavelReversaoCancelamento(Long codigo, Long codigoPessoaFisica);
}
