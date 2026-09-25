package br.com.araujoabreu.sgi.service.motivo_desconto_devolucao;

import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer.MotivoDescontoDevolucaoContaContabilTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface MotivoDescontoDevolucaoContaContabilService extends CrudService<MotivoDescontoDevolucaoContaContabilTO> {

	List<MotivoDescontoDevolucaoContaContabilTO> listarDescontoPorMatrizNF(Long codigoNota);

	List<MotivoDescontoDevolucaoContaContabilTO> listarDevolucaoPorMatrizNF(Long codigoNota);
}
