package br.com.fasolti.sgi.service;

import br.com.fasolti.corporativo.configuration.domain.Parametro;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import br.com.fasolti.sgi.service.util.ConfiguracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("configuracaoService")
public class ConfiguracaoServiceImpl implements ConfiguracaoService {

	@Autowired
	private ParametroRepository parametroRepository;

	@Override
	public Optional<String> getAny(String key) {
		String valor = System.getProperty(key);
		if (valor != null) {
			return Optional.of(valor);
		}
		return parametroRepository.findById(key).map(Parametro::getValor);
	}
}
