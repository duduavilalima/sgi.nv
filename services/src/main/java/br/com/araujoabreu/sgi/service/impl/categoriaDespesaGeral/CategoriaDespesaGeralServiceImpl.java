package br.com.araujoabreu.sgi.service.impl.categoriaDespesaGeral;

import br.com.araujoabreu.sgi.comercial.domain.CategoriaDespesaGeral;
import br.com.araujoabreu.sgi.comercial.domain.QCategoriaDespesaGeral;
import br.com.araujoabreu.sgi.service.categoriaDespesaGeral.CategoriaDespesaGeralService;
import br.com.araujoabreu.sgi.service.categoriaDespesaGeral.transfer.CategoriaDespesaGeralPesquisaTO;
import br.com.araujoabreu.sgi.service.categoriaDespesaGeral.transfer.CategoriaDespesaGeralTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.categoriaDespesaGeral.CategoriaDespesaGeralRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CategoriaDespesaGeralServiceImpl extends BaseSearchService<CategoriaDespesaGeral, CategoriaDespesaGeralTO, CategoriaDespesaGeralPesquisaTO, CategoriaDespesaGeralRepository> implements CategoriaDespesaGeralService {

	private static final QCategoriaDespesaGeral qCategoriaDespesaGeral = QCategoriaDespesaGeral.categoriaDespesaGeral;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	public CategoriaDespesaGeralServiceImpl(CategoriaDespesaGeralRepository repository) {
		super(repository);
	}

	@Override
	public List<CategoriaDespesaGeralTO> buscarTodasCategorias() {

		List<CategoriaDespesaGeral> listaCategoria = new ArrayList<>();

		try {
			QBean qBean = Projections.bean(
				CategoriaDespesaGeral.class,
				qCategoriaDespesaGeral.codigo,
				qCategoriaDespesaGeral.nome);

			JPAQuery<CategoriaDespesaGeral> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qCategoriaDespesaGeral)
				.orderBy(qCategoriaDespesaGeral.nome.asc());

			listaCategoria = jpaQuery.fetch();
			return map(listaCategoria);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void doBeforeSave(CategoriaDespesaGeral entity, CategoriaDespesaGeralTO object) throws ServiceException {
		entity.setDataCriacao(new Date());
		entity.setCriador(securityService.getUsername());
	}
}
