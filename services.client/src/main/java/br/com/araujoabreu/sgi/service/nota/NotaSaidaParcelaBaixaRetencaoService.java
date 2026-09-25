package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaBaixaRetencaoTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.lang.ValidationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotaSaidaParcelaBaixaRetencaoService extends CrudService<NotaSaidaParcelaBaixaRetencaoTO> {
	List<NotaSaidaParcelaBaixaRetencaoTO> obterBaixasRetencaoDaNotaSaidaParcela(long codigNotaSaidaParcela);

	@Transactional
	void removerBaixaRetencao(long codigoNotaSaidaBaixaRetencao) throws ValidationException;

	ValueTO verificarLoteBaixas(long codigoBaixa);
}
