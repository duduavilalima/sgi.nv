package br.com.araujoabreu.sgi.service.impl.categoriaMaoDeObra;

import br.com.araujoabreu.sgi.comercial.domain.CategoriaMaoDeObra;
import br.com.araujoabreu.sgi.comercial.domain.QCategoriaMaoDeObra;
import br.com.araujoabreu.sgi.service.categoriaMaoDeObra.CategoriaMaoDeObraService;
import br.com.araujoabreu.sgi.service.categoriaMaoDeObra.transfer.CategoriaMaoDeObraPesquisaTO;
import br.com.araujoabreu.sgi.service.categoriaMaoDeObra.transfer.CategoriaMaoDeObraTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.categoriaMaoDeObra.CategoriaMaoDeObraRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CategoriaMaoDeObraServiceImpl extends BaseSearchService<CategoriaMaoDeObra, CategoriaMaoDeObraTO, CategoriaMaoDeObraPesquisaTO, CategoriaMaoDeObraRepository> implements CategoriaMaoDeObraService {

	private static final QCategoriaMaoDeObra qCategoriaMaoDeObra = QCategoriaMaoDeObra.categoriaMaoDeObra;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	public CategoriaMaoDeObraServiceImpl(CategoriaMaoDeObraRepository repository) {
		super(repository);
	}

	@Override
	public List<CategoriaMaoDeObraTO> buscarTodasCategorias() {

		List<CategoriaMaoDeObra> listaCategoria = new ArrayList<>();

		try {
			QBean qBean = Projections.bean(
				CategoriaMaoDeObra.class,
				qCategoriaMaoDeObra.codigo,
				qCategoriaMaoDeObra.nome);

			JPAQuery<CategoriaMaoDeObra> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qCategoriaMaoDeObra)
				.orderBy(qCategoriaMaoDeObra.nome.asc());

			listaCategoria = jpaQuery.fetch();
			return map(listaCategoria);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void doBeforeSave(CategoriaMaoDeObra entity, CategoriaMaoDeObraTO object) throws ServiceException {
		entity.setDataCriacao(new Date());
		entity.setCriador(securityService.getUsername());
	}
}
