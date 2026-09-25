package br.com.araujoabreu.sgi.service.motivo_desconto_devolucao;

import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer.MotivoDescontoDevolucaoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface MotivoDescontoDevolucaoService extends CrudService<MotivoDescontoDevolucaoTO> {

	List<MotivoDescontoDevolucaoTO> listarDescontoPorMatrizNF(Long codigoNota);

	List<MotivoDescontoDevolucaoTO> listarDevolucaoPorMatrizNF(Long codigoNota);

}
