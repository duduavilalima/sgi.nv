package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.transfer.MovimentoAutorizadoBoletosTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface MovimentoAutorizadoBoletosService extends CrudService<MovimentoAutorizadoBoletosTO> {
	MovimentoAutorizadoBoletosTO salvarCodigoBarraLinhaDigitavel(MovimentoAutorizadoBoletosTO to);
}
