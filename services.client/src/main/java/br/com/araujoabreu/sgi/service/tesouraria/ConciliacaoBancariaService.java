package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoBancariaRelacaoBancariaPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoBancariaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.DadosConciliacaoBancariaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RelacaoBancariaPagamentosVO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface ConciliacaoBancariaService extends CrudService<ConciliacaoBancariaTO> {
	List<ConciliacaoBancariaTO> validarUnicidade(ConciliacaoBancariaTO object);

	List<RelacaoBancariaPagamentosVO> pesquisarRelacaoBancaria(ConciliacaoBancariaRelacaoBancariaPesquisaTO filtro) throws Exception;

	ConciliacaoBancariaTO salvarDados(DadosConciliacaoBancariaTO dados) throws Exception;
}
