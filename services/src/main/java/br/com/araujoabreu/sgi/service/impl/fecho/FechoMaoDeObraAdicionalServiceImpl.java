package br.com.araujoabreu.sgi.service.impl.fecho;

import br.com.araujoabreu.sgi.comercial.domain.CategoriaMaoDeObraAdicional;
import br.com.araujoabreu.sgi.comercial.domain.FechoMaoDeObraAdicional;
import br.com.araujoabreu.sgi.comercial.domain.QCategoriaMaoDeObraAdicional;
import br.com.araujoabreu.sgi.comercial.domain.QFechoMaoDeObraAdicional;
import br.com.araujoabreu.sgi.service.fecho.FechoMaoDeObraAdicionalService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraAdicionalPesquisaTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraAdicionalTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.fecho.FechoMaoDeObraAdicionalRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FechoMaoDeObraAdicionalServiceImpl extends BaseSearchService<FechoMaoDeObraAdicional, FechoMaoDeObraAdicionalTO, FechoMaoDeObraAdicionalPesquisaTO, FechoMaoDeObraAdicionalRepository> implements FechoMaoDeObraAdicionalService {

	private static final QFechoMaoDeObraAdicional qFechoMaoDeObraAdicional = QFechoMaoDeObraAdicional.fechoMaoDeObraAdicional;

	@Autowired
	public FechoMaoDeObraAdicionalServiceImpl(FechoMaoDeObraAdicionalRepository repository) {
		super(repository);
	}

	@Override
	public List<FechoMaoDeObraAdicionalTO> adicionalByCodMaoDeObra(String id) throws Exception {
		try {
			return buscarListaAdicionalByCodMaoDeObra(Long.parseLong(id));
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	@Override
	public List<FechoMaoDeObraAdicionalTO> buscarListaAdicionalByCodMaoDeObra(Long id) {
		QCategoriaMaoDeObraAdicional qCategoriaMaoDeObraAdicional = QCategoriaMaoDeObraAdicional.categoriaMaoDeObraAdicional;

		QBean qBean = Projections.bean(
			FechoMaoDeObraAdicional.class,
			qFechoMaoDeObraAdicional.codigo,
			qFechoMaoDeObraAdicional.valorUnitario,
			Projections.bean(CategoriaMaoDeObraAdicional.class, qCategoriaMaoDeObraAdicional.codigo, qCategoriaMaoDeObraAdicional.nome).as("categoriaMaoDeObraAdicional")
		);

		JPAQuery<FechoMaoDeObraAdicional> jpaQueryAdicional = select();

		jpaQueryAdicional
			.select(qBean)
			.from(qFechoMaoDeObraAdicional).join(qFechoMaoDeObraAdicional.categoriaMaoDeObraAdicional, qCategoriaMaoDeObraAdicional)
			.where(qFechoMaoDeObraAdicional.fechoMaoDeObra.codigo.eq(id));

		List<FechoMaoDeObraAdicional> lista = jpaQueryAdicional.fetch();
		List<FechoMaoDeObraAdicionalTO> listaTO = new ArrayList<>();

		for (FechoMaoDeObraAdicional adicional : lista) {
			FechoMaoDeObraAdicionalTO to = getMapper().map(adicional, FechoMaoDeObraAdicionalTO.class);
			listaTO.add(to);
		}

		return listaTO;
	}
}
