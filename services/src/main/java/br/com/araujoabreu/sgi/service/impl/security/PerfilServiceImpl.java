package br.com.araujoabreu.sgi.service.impl.security;

import br.com.araujoabreu.sgi.service.security.PerfilService;
import br.com.araujoabreu.sgi.service.security.transfer.PerfilPesquisaTO;
import br.com.araujoabreu.sgi.service.security.transfer.PerfilTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Perfil;
import br.com.fasolti.sgi.repository.security.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerfilServiceImpl extends BaseSearchService<Perfil, PerfilTO, PerfilPesquisaTO, PerfilRepository> implements PerfilService {

	@Autowired
	public PerfilServiceImpl(PerfilRepository repository) {
		super(repository);
	}

	@Override
	public Iterable<PerfilTO> listarPerfisAtivos() {
		Iterable<Perfil> perfis = getRepository().listarAtivos();
		List<PerfilTO> perfisTO = new ArrayList<>();
		for (Perfil perfil : perfis) {
			perfisTO.add(map(perfil));
		}
		return perfisTO;
	}
}
