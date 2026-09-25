package br.com.araujoabreu.sgi.service.impl.parametro;

import br.com.araujoabreu.sgi.service.parametro.ParametroService;
import br.com.araujoabreu.sgi.service.parametro.transfer.ParametroTO;
import br.com.fasolti.corporativo.configuration.domain.Parametro;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParametroServiceImpl implements ParametroService {

	@Autowired
	private ParametroRepository parametroRepository;

	@Override
	public ParametroTO buscaPorExemplo(ParametroTO exampleObj) {
		Parametro entity = parametroRepository.findByChave(exampleObj.getChave());

		ParametroTO parametroTO = new ParametroTO();
		parametroTO.setChave(entity.getChave());
		parametroTO.setValor(entity.getValor());

		return parametroTO;
	}
}
